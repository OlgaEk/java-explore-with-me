package ru.practicum.ewm.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.exception.ForbiddenException;
import ru.practicum.ewm.exception.NoEntityException;
import ru.practicum.ewm.request.model.Participation;
import ru.practicum.ewm.request.model.RequestStatus;
import ru.practicum.ewm.request.model.dto.ParticipationRequestDto;
import ru.practicum.ewm.request.model.mapper.RequestMapper;
import ru.practicum.ewm.request.repository.RequestRepository;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final RequestMapper mapper;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;


    public ParticipationRequestDto create(Long userId, Long eventId) {
        if (requestRepository.findByEventIdAndRequesterId(eventId, userId).isPresent())
            throw new ForbiddenException("User with id = " + userId
                    + " already make a request to event with id =" + eventId);
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NoEntityException("Event with id = " + eventId + " was not found"));
        if (event.getInitiator().getId().equals(userId))
            throw new ForbiddenException("User with id = " + userId
                    + " not allowed to make a request to his event with id =" + eventId);
        if (!event.getState().equals(EventState.PUBLISHED))
            throw new ForbiddenException("Not allowed to make a request to not published event with id =" + eventId);
        if (event.getParticipantLimit() != 0)
            if (event.getConfirmedRequests() >= event.getParticipantLimit())
                throw new ForbiddenException("Event with id =" + eventId + "is reached requests limit.");
        Participation request = newRequest(eventId, userId);
        if (!event.getRequestModeration()) confirmRequestToEvent(request);
        //??если для события лимит заявок равен 0 или отключена пре-модерация заявок, то подтверждение заявок не требуется
        if (event.getParticipantLimit() == 0) confirmRequestToEvent(request);
        return mapper.requestToDto(requestRepository.save(request));
    }

    public List<ParticipationRequestDto> getByUser(Long userId) {
        return mapper.requestToDto(requestRepository.findAllByRequesterId(userId));
    }

    public ParticipationRequestDto cancel(Long userId, Long requestId) {
        Participation request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NoEntityException("Request with id = " + requestId + " was not found"));
        if (!request.getRequester().getId().equals(userId))
            throw new ForbiddenException("User with id = " + userId +
                    " is not allowed to cancel request with id = " + requestId);
        if (request.getStatus().equals(RequestStatus.CONFIRMED)) cancelConfirmedRequestToEvent(request);
        else request.setStatus(RequestStatus.CANCELED);
        return mapper.requestToDto(requestRepository.save(request));
    }

    public List<ParticipationRequestDto> getForUsersEvent(Long userId, Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NoEntityException("Event with id = " + eventId + " was not found"));
        if (!event.getInitiator().getId().equals(userId))
            throw new ForbiddenException("User with id = " + userId
                    + " not allowed to see request for event with id =" + eventId);
        return mapper.requestToDto(requestRepository.findAllByEventId(eventId));
    }

    public ParticipationRequestDto confirm(Long userId, Long eventId, Long reqId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NoEntityException("Event with id = " + eventId + " was not found"));
        if (!event.getInitiator().getId().equals(userId))
            throw new ForbiddenException("User with id = " + userId
                    + " not allowed to confirm request for event with id =" + eventId);
        Participation request = requestRepository.findById(reqId)
                .orElseThrow(() -> new NoEntityException("Request with id = " + reqId + " was not found"));
        if (!request.getEvent().getId().equals(eventId))
            throw new ForbiddenException("Request with id=" + reqId + "not for event with id=" + eventId);
        if (request.getStatus().equals(RequestStatus.CONFIRMED))
            throw new ForbiddenException("Request with id = " + reqId + "already confirmed");
        if (!request.getStatus().equals(RequestStatus.PENDING))
            throw new ForbiddenException("Request with id = " + reqId + "not pending to confirm");
        if (event.getConfirmedRequests() >= event.getParticipantLimit())
            throw new ForbiddenException("Reached limit to confirmed request to event with id=" + eventId);
        confirmRequestToEvent(request);
        if (event.getConfirmedRequests() >= event.getParticipantLimit())
            requestRepository.updateAllByEventId(RequestStatus.PENDING, eventId, RequestStatus.REJECTED);
        return mapper.requestToDto(requestRepository.save(request));
    }

    public ParticipationRequestDto reject(Long userId, Long eventId, Long reqId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NoEntityException("Event with id = " + eventId + " was not found"));
        if (!event.getInitiator().getId().equals(userId))
            throw new ForbiddenException("User with id = " + userId
                    + " not allowed to reject request for event with id =" + eventId);
        Participation request = requestRepository.findById(reqId)
                .orElseThrow(() -> new NoEntityException("Request with id = " + reqId + " was not found"));
        if (!request.getEvent().getId().equals(eventId))
            throw new ForbiddenException("Request with id=" + reqId + "not for event with id=" + eventId);
        if (request.getStatus().equals(RequestStatus.CONFIRMED)) cancelConfirmedRequestToEvent(request);
        request.setStatus(RequestStatus.REJECTED);
        return mapper.requestToDto(requestRepository.save(request));
    }

    private Participation newRequest(Long eventId, Long userId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NoEntityException("Event with id = " + eventId + " was not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoEntityException("User with id = " + userId + " was not found"));
        Participation request = new Participation();
        request.setEvent(event);
        request.setRequester(user);
        request.setCreated(LocalDateTime.now());
        request.setStatus(RequestStatus.PENDING);
        return request;
    }

    private void confirmRequestToEvent(Participation request) {
        Event event = eventRepository.findById(request.getEvent().getId())
                .orElseThrow(() -> new NoEntityException("Event with id = " + request.getEvent().getId() +
                        " was not found"));
        if (event.getParticipantLimit() != 0)
            if (event.getConfirmedRequests() >= event.getParticipantLimit())
                throw new ForbiddenException("Event with id =" + request.getEvent().getId() +
                        "is reached requests limit.");
        event.setConfirmedRequests(event.getConfirmedRequests() + 1);
        eventRepository.save(event);
        request.setStatus(RequestStatus.CONFIRMED);
    }

    private void cancelConfirmedRequestToEvent(Participation request) {
        Event event = eventRepository.findById(request.getEvent().getId())
                .orElseThrow(() -> new NoEntityException("Event with id = " + request.getEvent().getId() +
                        " was not found"));
        if (event.getConfirmedRequests() != 0) event.setConfirmedRequests(event.getConfirmedRequests() - 1);
        eventRepository.save(event);
        request.setStatus(RequestStatus.CANCELED);
    }

}
