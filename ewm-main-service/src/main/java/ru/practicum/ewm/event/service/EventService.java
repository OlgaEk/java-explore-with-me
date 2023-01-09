package ru.practicum.ewm.event.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.event.controller.SortEvents;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.event.model.dto.*;

import java.time.LocalDateTime;
import java.util.List;


public interface EventService {
    EventFullDto create (NewEventDto eventDto, Long userId);
    List<EventShortDto> getAllByUser(Long userId, Pageable pageable);
    EventFullDto getByUser(Long userId, Long eventId);
    EventFullDto updateByUser(Long userId, UpdateEventRequest eventDto);
    EventFullDto cancelByUser(Long userId, Long eventId);
    List<EventFullDto> getByAdmin(List<Long> users, List<EventState> states,
                                  List<Long> categories, LocalDateTime rangeStart,
                                  LocalDateTime rangeEnd, Pageable pageable);

    EventFullDto updateByAdmin (Long eventId, AdminUpdateEventRequest eventDto);
    EventFullDto publish (Long eventId);
    EventFullDto reject (Long eventId);
    List<EventShortDto> searchByUser (String text, List<Long> categories, Boolean paid,
                                   LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                   Boolean onlyAvailable, SortEvents sort, Pageable pageable);
    EventFullDto getByPublic(Long eventId);
    Event getById(Long eventId);

}
