package ru.practicum.ewm.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.model.dto.EventFullDto;
import ru.practicum.ewm.event.model.dto.EventShortDto;
import ru.practicum.ewm.event.model.dto.NewEventDto;
import ru.practicum.ewm.event.model.dto.UpdateEventRequest;
import ru.practicum.ewm.event.service.EventService;
import ru.practicum.ewm.event.validator.EventIdExist;
import ru.practicum.ewm.user.validator.UserIdExist;

import java.util.List;

@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/users/{userId}/events")
public class EventPrivateController {
    private final EventService service;

    @PostMapping
    public EventFullDto createEvent(@PathVariable @UserIdExist Long userId,
                                    @Validated @RequestBody NewEventDto eventDto) {
        log.info("Try to create new event with annotation: {} by user with id={}", eventDto.getAnnotation(), userId);
        return service.create(eventDto, userId);
    }

    @GetMapping
    public List<EventShortDto> getAllEventsByUser(@PathVariable @UserIdExist Long userId,
                                                  @RequestParam(name = "from", defaultValue = "0") int from,
                                                  @RequestParam(name = "size", defaultValue = "10") int size) {
        log.info("Try to get events created by user with id = {}. And return a {}, from {}.", userId, size, from);
        PageRequest pageRequest = PageRequest.of(from / size, size);
        return service.getAllByUser(userId, pageRequest);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getEventByUser(@PathVariable @UserIdExist Long userId,
                                       @PathVariable @EventIdExist Long eventId) {
        log.info("Try to get event with id = {} by user with id = {}", eventId, userId);
        return service.getByUser(userId, eventId);
    }

    @PatchMapping
    public EventFullDto updateEventByUser(@PathVariable @UserIdExist Long userId,
                                          @Validated @RequestBody UpdateEventRequest eventDto) {
        log.info("Try to update event with id ={} by user with id = {}", eventDto.getEventId(), userId);
        return service.updateByUser(userId, eventDto);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto cancelEventByUser(@PathVariable @UserIdExist Long userId,
                                          @PathVariable @EventIdExist Long eventId) {
        log.info("Try to cancel event with id={} by user with id = {}", eventId, userId);
        return service.cancelByUser(userId, eventId);
    }
}
