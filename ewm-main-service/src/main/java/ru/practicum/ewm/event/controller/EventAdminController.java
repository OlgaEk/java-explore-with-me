package ru.practicum.ewm.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.event.model.dto.AdminUpdateEventRequest;
import ru.practicum.ewm.event.model.dto.EventFullDto;
import ru.practicum.ewm.event.service.EventService;
import ru.practicum.ewm.event.validator.EventIdExist;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/admin/events")
public class EventAdminController {
    private final EventService service;

    @GetMapping
    public List<EventFullDto> getEventByAdmin(@RequestParam(name = "users", required = false) List<Long> users,
                                              @RequestParam(name = "states", required = false) List<EventState> states,
                                              @RequestParam(name = "categories", required = false) List<Long> categories,
                                              @RequestParam(name = "rangeStart", required = false)
                                              @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                              @RequestParam(name = "rangeEnd", required = false)
                                              @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                              @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero int from,
                                              @RequestParam(name = "size", defaultValue = "10") @Positive int size) {
        if (users != null && users.size() == 1 && users.get(0) == 0) users = null;
        if (categories != null && categories.size() == 1 && categories.get(0) == 0) categories = null;
        log.info("Try to find events by admin, created by users with id in {}, states in {}, categories in {}, " +
                        "start after {} and before {}. And return {} events from {}.",
                users, states, categories, rangeStart, rangeEnd, size, from);
        PageRequest pageRequest = PageRequest.of(from / size, size);
        return service.getByAdmin(users, states, categories, rangeStart, rangeEnd, pageRequest);
    }

    @PutMapping("/{eventId}")
    public EventFullDto updateEventByAdmin(@PathVariable @EventIdExist Long eventId,
                                           @RequestBody AdminUpdateEventRequest eventDto) {
        log.info("Try to update event with id = {} by admin", eventId);
        return service.updateByAdmin(eventId, eventDto);
    }

    @PatchMapping("/{eventId}/publish")
    public EventFullDto publishEvent(@PathVariable @EventIdExist Long eventId) {
        log.info("Try to publish event with id = {}", eventId);
        return service.publish(eventId);
    }

    @PatchMapping("{eventId}/reject")
    public EventFullDto rejectEvent(@PathVariable @EventIdExist Long eventId) {
        log.info("Try to reject event with id = {}", eventId);
        return service.reject(eventId);
    }
}
