package ru.practicum.ewm.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.model.dto.EndpointHit;
import ru.practicum.ewm.model.dto.ViewStats;
import ru.practicum.ewm.model.mapper.StatsMapper;
import ru.practicum.ewm.repository.StatsRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {
    private final StatsRepository repository;
    private final StatsMapper mapper;

    public void create(EndpointHit hitDto) {
        repository.save(mapper.dtoToStats(hitDto));
    }

    public List<ViewStats> get(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        if (unique) return repository.searchHitUnique(uris, start, end);
        return repository.searchHit(uris, start, end);
    }
}
