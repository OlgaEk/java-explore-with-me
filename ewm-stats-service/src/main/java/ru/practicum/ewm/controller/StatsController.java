package ru.practicum.ewm.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.model.dto.EndpointHit;
import ru.practicum.ewm.model.dto.ViewStats;
import ru.practicum.ewm.service.StatsService;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class StatsController {
    private final StatsService service;

    @PostMapping("/hit")
    public void createStats(@RequestBody @Validated EndpointHit hitDto) {
        log.info("Try to write statistic app = {}, uri ={}, ip = {},time = {}",
                hitDto.getApp(), hitDto.getUri(), hitDto.getIp(), hitDto.getTimestamp());
        service.create(hitDto);

    }

    @GetMapping("/stats")
    public List<ViewStats> getStats(@RequestParam(name = "start")
                                    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                                    @RequestParam(name = "end")
                                    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                                    @RequestParam(name = "uris", required = false) List<String> uris,
                                    @RequestParam(name = "unique", defaultValue = "false") Boolean unique) {
        log.info("try to get statistic (unique = {}) from {}, to {}, by uris = {}.", unique, start, end, uris);
        return service.get(start, end, uris, unique);
    }
}
