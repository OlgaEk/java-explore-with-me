package ru.practicum.ewm.service;

import ru.practicum.ewm.model.dto.EndpointHit;
import ru.practicum.ewm.model.dto.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {
    void create(EndpointHit hitDto);

    List<ViewStats> get(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);
}
