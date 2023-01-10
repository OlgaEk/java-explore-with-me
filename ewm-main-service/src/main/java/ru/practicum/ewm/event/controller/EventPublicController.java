package ru.practicum.ewm.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.model.dto.EventFullDto;
import ru.practicum.ewm.event.model.dto.EventShortDto;
import ru.practicum.ewm.event.service.EventService;
import ru.practicum.ewm.event.validator.EventIdExist;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/events")
public class EventPublicController {
    private final EventService service;

    @GetMapping
    public List<EventShortDto> searchEventsByUser(@RequestParam(name = "text", required = false) String text,
                                                  @RequestParam(name = "categories", required = false) List<Long> categories,
                                                  @RequestParam(name = "paid", required = false) Boolean paid,
                                                  @RequestParam(name = "rangeStart", required = false)
                                                  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                                  @RequestParam(name = "rangeEnd", required = false)
                                                  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                                  @RequestParam(name = "onlyAvailable", defaultValue = "false") Boolean onlyAvailable,
                                                  @RequestParam(name = "sort", required = false) SortEvents sort,
                                                  @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero int from,
                                                  @RequestParam(name = "size", defaultValue = "10") @Positive int size,
                                                  HttpServletRequest request) {
        log.info("Try to find events by user, searched text is {}, categories in {}, paid is {}, start after {} " +
                        "and before {}. And return {} (only available is {}) events from {}, sorted by {}." +
                        "Request ip={},uri={}",
                text, categories, paid, rangeStart, rangeEnd, size, onlyAvailable, from, sort, request.getRemoteAddr(),
                request.getRequestURI());
        PageRequest pageRequest = PageRequest.of(from / size, size, Sort.by("eventDate").descending());
        return service.searchByUser(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, pageRequest, request);
    }

    @GetMapping("/{id}")
    public EventFullDto getEventByPublic(@PathVariable @EventIdExist Long id,
                                         HttpServletRequest request) {
        log.info("Try to get event with id = {} by public endpoint.Request ip={},uri={}", id, request.getRemoteAddr(),
                request.getRequestURI());
        return service.getByPublic(id, request);
    }

}
