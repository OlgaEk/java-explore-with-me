package ru.practicum.ewm.compilation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.compilation.model.Compilation;
import ru.practicum.ewm.compilation.model.dto.CompilationDto;
import ru.practicum.ewm.compilation.model.dto.NewCompilationDto;
import ru.practicum.ewm.compilation.model.mapper.CompilationMapper;
import ru.practicum.ewm.compilation.repository.CompilationRepository;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.exception.ForbiddenException;
import ru.practicum.ewm.exception.NoEntityException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final CompilationMapper mapper;
    private final EventRepository eventRepository;

    public CompilationDto create(NewCompilationDto compDto) {
        return mapper.compilationToDto(compilationRepository.save(mapper.dtoToCompilation(compDto)));
    }

    public void delete(Long compId) {
        Compilation comp = compilationRepository.findById(compId)
                .orElseThrow(() -> new NoEntityException("Event with id = " + compId + " is not found"));
        compilationRepository.delete(comp);
    }

    public List<CompilationDto> get(Boolean pinned, Pageable pageable) {
        return mapper.compilationToDto(compilationRepository.findAllByPinned(pinned, pageable));
    }

    public CompilationDto getById(Long compId) {
        Compilation comp = compilationRepository.findById(compId)
                .orElseThrow(() -> new NoEntityException("Event with id = " + compId + " is not found"));
        return mapper.compilationToDto(comp);
    }

    public void addEvent(Long compId, Long eventId) {
        Compilation comp = compilationRepository.findById(compId)
                .orElseThrow(() -> new NoEntityException("Event with id = " + compId + " is not found"));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NoEntityException("Event with id = " + eventId + " was not found"));
        List<Event> events = comp.getEvents();
        if (events.contains(event))
            throw new ForbiddenException("Event with id = " + eventId + " already in compilation with id = " + compId);
        events.add(event);
        comp.setEvents(events);
        compilationRepository.save(comp);
    }

    public void deleteEvent(Long compId, Long eventId) {
        Compilation comp = compilationRepository.findById(compId)
                .orElseThrow(() -> new NoEntityException("Event with id = " + compId + " is not found"));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NoEntityException("Event with id = " + eventId + " was not found"));
        List<Event> events = comp.getEvents();
        if (!events.contains(event))
            throw new ForbiddenException("Event with id = " + eventId + " not in compilation with id = " + compId);
        events.remove(event);
        comp.setEvents(events);
        compilationRepository.save(comp);
    }

    public void pin(Long compId, Boolean pinned) {
        Compilation comp = compilationRepository.findById(compId)
                .orElseThrow(() -> new NoEntityException("Event with id = " + compId + " is not found"));
        comp.setPinned(pinned);
        compilationRepository.save(comp);
    }


}
