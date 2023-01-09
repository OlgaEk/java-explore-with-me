package ru.practicum.ewm.event.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.ewm.event.controller.SortEvents;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.event.model.dto.*;
import ru.practicum.ewm.event.model.mapper.EventMapper;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.exception.ForbiddenException;
import ru.practicum.ewm.exception.NoEntityException;
import ru.practicum.ewm.user.repository.UserRepository;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.xml.stream.Location;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private  final EventMapper mapper;

    public EventFullDto create (NewEventDto eventDto, Long userId){
        Event event = mapper.fullDtoToEvent(eventDto);
        event.setState(EventState.PENDING);
        event.setInitiator(userRepository.findById(userId)
                .orElseThrow(()->new NoEntityException("User with id = " + userId + " was not found")));
        event.setConfirmedRequests(0);
        return mapper.eventToFullDto(eventRepository.save(event));
    }

    public List<EventShortDto> getAllByUser(Long userId, Pageable pageable){
        return mapper.eventToShortDto(eventRepository.findByInitiatorId(userId,pageable));
    }

    public EventFullDto getByUser(Long userId, Long eventId){
        Event event = eventRepository.findById(eventId)
                .orElseThrow(()->new NoEntityException("Event with id = "+eventId+" was not found"));
        if(!event.getInitiator().getId().equals(userId))
            throw new ForbiddenException("User with id = "+userId+" not allowed to view event with id ="+eventId);
        return mapper.eventToFullDto(event);
    }

    public EventFullDto updateByUser(Long userId, UpdateEventRequest eventDto){
        Event event = eventRepository.findById(eventDto.getEventId())
                .orElseThrow(()->new NoEntityException("Event with id = "+eventDto.getEventId()+" was not found"));
        if(!event.getInitiator().getId().equals(userId))
            throw new ForbiddenException("User with id = "+userId
                    +" not allowed to view event with id ="+eventDto.getEventId());
        switch (event.getState()){
            case CANCELED:
                event.setState(EventState.PENDING);
                break;
            case PUBLISHED:
                throw new ForbiddenException("Not allowed to update a published event with id = "
                        +eventDto.getEventId());
        }
        mapper.updateEventFromDto(eventDto,event);
        return mapper.eventToFullDto(eventRepository.save(event));
    }

    public EventFullDto cancelByUser(Long userId, Long eventId){
        Event event = eventRepository.findById(eventId)
                .orElseThrow(()->new NoEntityException("Event with id = "+eventId+" was not found"));
        if(!event.getInitiator().getId().equals(userId))
            throw new ForbiddenException("User with id = "+userId
                    +" not allowed to view event with id ="+eventId);
        if(!event.getState().equals(EventState.PENDING))
            throw new ForbiddenException("Not allowed to cancel event with id ="+eventId+" not in pending state");
        event.setState(EventState.CANCELED);
        return mapper.eventToFullDto(eventRepository.save(event));
    }

    public List<EventFullDto> getByAdmin(List<Long> users, List<EventState> states,
                      List<Long> categories, LocalDateTime rangeStart,
                      LocalDateTime rangeEnd, Pageable pageable){
        return mapper.eventToFullDto(eventRepository.searchEvents(users,states,categories,rangeStart,rangeEnd,pageable));

    }

    public EventFullDto updateByAdmin (Long eventId, AdminUpdateEventRequest eventDto){
        Event event = eventRepository.findById(eventId)
                .orElseThrow(()->new NoEntityException("Event with id = "+eventId+" was not found"));
        mapper.updateEventFromDto(eventDto,event);
        return mapper.eventToFullDto(eventRepository.save(event));
    }

    public EventFullDto publish (Long eventId){
        Event event = eventRepository.findById(eventId)
                .orElseThrow(()->new NoEntityException("Event with id = "+eventId+" was not found"));
        if(!event.getState().equals(EventState.PENDING))
            throw new ForbiddenException("Not allowed to publish event with id ="+eventId+" not in pending state");
        if(event.getEventDate().isBefore(LocalDateTime.now().plusHours(1)))
            throw new ForbiddenException("Not allowed to publish event with id ="+eventId
                    +" which start less then in а hour");
        event.setState(EventState.PUBLISHED);
        event.setPublishedOn(LocalDateTime.now());
        return mapper.eventToFullDto(eventRepository.save(event));
    }

    public EventFullDto reject (Long eventId){
        Event event = eventRepository.findById(eventId)
                .orElseThrow(()->new NoEntityException("Event with id = "+eventId+" was not found"));
        if(event.getState().equals(EventState.PUBLISHED))
            throw new ForbiddenException("Not allowed to reject published event with id ="+eventId);
        event.setState(EventState.CANCELED);
        return mapper.eventToFullDto(eventRepository.save(event));
    }

    public List<EventShortDto> searchByUser (String text, List<Long> categories, Boolean paid,
                                     LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                     Boolean onlyAvailable, SortEvents sort, Pageable pageable){
        rangeStart = rangeStart !=null ? rangeStart : LocalDateTime.now();
        List<Event> events = eventRepository.searchEventsForUser(EventState.PUBLISHED,text,categories,
                paid,rangeStart,rangeEnd,pageable);

        return mapper.eventToShortDto(events);
    }

    public EventFullDto getByPublic(Long eventId){
        Event event = eventRepository.findById(eventId)
                .orElseThrow(()->new NoEntityException("Event with id = "+eventId+" was not found"));
        if(!event.getState().equals(EventState.PUBLISHED))
            throw new ForbiddenException("Not allowed to view not published event with id ="+eventId);

        return mapper.eventToFullDto(event);
    }

    public Event getById(Long eventId){
        return eventRepository.findById(eventId)
                .orElseThrow(()->new NoEntityException("Event with id = "+eventId+" was not found"));
    }

}
