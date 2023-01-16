package ru.practicum.ewm.compilation.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.ewm.compilation.model.dto.CompilationDto;
import ru.practicum.ewm.compilation.model.dto.NewCompilationDto;

import java.util.List;

public interface CompilationService {
    CompilationDto create(NewCompilationDto compDto);

    void delete(Long compId);

    List<CompilationDto> get(Boolean pinned, Pageable pageable);

    CompilationDto getById(Long compId);

    void addEvent(Long compId, Long eventId);

    void deleteEvent(Long compId, Long eventId);

    void pin(Long compId, Boolean pinned);
}
