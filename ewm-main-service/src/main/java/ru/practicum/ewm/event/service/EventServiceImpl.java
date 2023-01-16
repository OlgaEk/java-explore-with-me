package ru.practicum.ewm.event.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.event.controller.SortEvents;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.event.model.dto.*;
import ru.practicum.ewm.event.model.mapper.EventMapper;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.exception.ForbiddenException;
import ru.practicum.ewm.exception.NoEntityException;
import ru.practicum.ewm.stat.StatsClient;
import ru.practicum.ewm.stat.model.EndpointHit;
import ru.practicum.ewm.stat.model.ViewStats;
import ru.practicum.ewm.user.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.practicum.ewm.constant.Constant.DEFAULT_DATE_FORMAT;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final EventMapper mapper;

    private final StatsClient statsClient;

    @Transactional
    public EventFullDto create(NewEventDto eventDto, Long userId) {
        Event event = mapper.fullDtoToEvent(eventDto);
        event.setState(EventState.PENDING);
        event.setInitiator(userRepository.findById(userId)
                .orElseThrow(() -> new NoEntityException("User with id = " + userId + " was not found")));
        event.setConfirmedRequests(0);
        return mapper.eventToFullDto(eventRepository.save(event));
    }

    public List<EventShortDto> getAllByUser(Long userId, Pageable pageable) {
        return mapper.eventToShortDto(eventRepository.findByInitiatorId(userId, pageable));
    }

    public EventFullDto getByUser(Long userId, Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NoEntityException("Event with id = " + eventId + " was not found"));
        if (!event.getInitiator().getId().equals(userId))
            throw new ForbiddenException("User with id = " + userId + " not allowed to view event with id =" + eventId);
        setViews(List.of(event));
        return mapper.eventToFullDto(event);
    }

    @Transactional
    public EventFullDto updateByUser(Long userId, UpdateEventRequest eventDto) {
        Event event = eventRepository.findById(eventDto.getEventId())
                .orElseThrow(() -> new NoEntityException("Event with id = " + eventDto.getEventId() + " was not found"));
        if (!event.getInitiator().getId().equals(userId))
            throw new ForbiddenException("User with id = " + userId
                    + " not allowed to view event with id =" + eventDto.getEventId());
        switch (event.getState()) {
            case CANCELED:
                event.setState(EventState.PENDING);
                break;
            case PUBLISHED:
                throw new ForbiddenException("Not allowed to update a published event with id = "
                        + eventDto.getEventId());
        }
        mapper.updateEventFromDto(eventDto, event);
        return mapper.eventToFullDto(eventRepository.save(event));
    }

    @Transactional
    public EventFullDto cancelByUser(Long userId, Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NoEntityException("Event with id = " + eventId + " was not found"));
        if (!event.getInitiator().getId().equals(userId))
            throw new ForbiddenException("User with id = " + userId
                    + " not allowed to view event with id =" + eventId);
        if (!event.getState().equals(EventState.PENDING))
            throw new ForbiddenException("Not allowed to cancel event with id =" + eventId + " not in pending state");
        event.setState(EventState.CANCELED);
        return mapper.eventToFullDto(eventRepository.save(event));
    }

    public List<EventFullDto> getByAdmin(List<Long> users, List<EventState> states,
                                         List<Long> categories, LocalDateTime rangeStart,
                                         LocalDateTime rangeEnd, Pageable pageable) {
        return mapper.eventToFullDto(eventRepository.searchEvents(users, states, categories, rangeStart, rangeEnd, pageable));

    }

    @Transactional
    public EventFullDto updateByAdmin(Long eventId, AdminUpdateEventRequest eventDto) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NoEntityException("Event with id = " + eventId + " was not found"));
        mapper.updateEventFromDto(eventDto, event);
        return mapper.eventToFullDto(eventRepository.save(event));
    }

    @Transactional
    public EventFullDto publish(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NoEntityException("Event with id = " + eventId + " was not found"));
        if (!event.getState().equals(EventState.PENDING))
            throw new ForbiddenException("Not allowed to publish event with id =" + eventId + " not in pending state");
        if (event.getEventDate().isBefore(LocalDateTime.now().plusHours(1)))
            throw new ForbiddenException("Not allowed to publish event with id =" + eventId
                    + " which start less then in а hour");
        event.setState(EventState.PUBLISHED);
        event.setPublishedOn(LocalDateTime.now());
        return mapper.eventToFullDto(eventRepository.save(event));
    }

    @Transactional
    public EventFullDto reject(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NoEntityException("Event with id = " + eventId + " was not found"));
        if (event.getState().equals(EventState.PUBLISHED))
            throw new ForbiddenException("Not allowed to reject published event with id =" + eventId);
        event.setState(EventState.CANCELED);
        return mapper.eventToFullDto(eventRepository.save(event));
    }

    public List<EventShortDto> searchByUser(String text, List<Long> categories, Boolean paid,
                                            LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                            Boolean onlyAvailable, SortEvents sort,
                                            Pageable pageable, HttpServletRequest request) {
        rangeStart = rangeStart != null ? rangeStart : LocalDateTime.now();
        List<Event> events = eventRepository.searchEventsForUser(EventState.PUBLISHED, text, categories,
                paid, rangeStart, rangeEnd, pageable);
        if (onlyAvailable)
            events = events.stream()
                    .filter(e -> e.getParticipantLimit() == 0 || e.getConfirmedRequests() < e.getParticipantLimit())
                    .collect(Collectors.toList());
        hitToStats(request);
        setViews(events);
        if (sort != null && sort.equals(SortEvents.VIEWS))
            events = events.stream().sorted(Comparator.comparing(Event::getViews)).collect(Collectors.toList());
        return mapper.eventToShortDto(events);
    }

    public EventFullDto getByPublic(Long eventId, HttpServletRequest request) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NoEntityException("Event with id = " + eventId + " was not found"));
        if (!event.getState().equals(EventState.PUBLISHED))
            throw new ForbiddenException("Not allowed to view not published event with id =" + eventId);
        hitToStats(request);
        setViews(List.of(event));
        return mapper.eventToFullDto(event);
    }

    public Event getById(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new NoEntityException("Event with id = " + eventId + " was not found"));
    }

    private void hitToStats(HttpServletRequest request) {
        EndpointHit endpointHit = new EndpointHit();
        endpointHit.setIp(request.getRemoteAddr());
        endpointHit.setUri(request.getRequestURI());
        endpointHit.setApp("ewm-main-service");
        endpointHit.setTimestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT)));
        statsClient.hit(endpointHit);
    }

    private void setViews(List<Event> events) {
        String start = LocalDateTime.of(1900, 1, 1, 1, 1, 1)
                .format(DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT));
        String end = LocalDateTime.now().format(DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT));
        Map<Long, Event> eventMap = new HashMap<>();
        events.forEach(e -> eventMap.put(e.getId(), e));
        List<String> uris = events.stream()
                .map(Event::getId)
                .map(id -> "/events/" + id)
                .collect(Collectors.toList());
        ResponseEntity<Object> response = statsClient.get(start, end, uris, false);

        //Parse ResponseEntity<Object> to List<ViewStats>
        GsonBuilder gsonBuilder = new GsonBuilder().serializeNulls().setPrettyPrinting();
        Gson gson = gsonBuilder.create();
        String responseJson = gson.toJson(response.getBody());
        List<ViewStats> views = gson.fromJson(responseJson,
                new TypeToken<List<ViewStats>>() {
                }.getType());

        for (ViewStats view : views) {
            Long id = Long.parseLong(view.getUri().split("/")[2]);
            Long hits = view.getHits() == null ? 0L : view.getHits();
            eventMap.get(id).setViews(hits);
        }
    }

}
