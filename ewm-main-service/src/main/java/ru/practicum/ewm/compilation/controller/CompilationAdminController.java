package ru.practicum.ewm.compilation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.compilation.model.dto.CompilationDto;
import ru.practicum.ewm.compilation.model.dto.NewCompilationDto;
import ru.practicum.ewm.compilation.service.CompilationService;
import ru.practicum.ewm.compilation.validator.CompIdExist;
import ru.practicum.ewm.event.validator.EventIdExist;

@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/compilations")
public class CompilationAdminController {
    private final CompilationService service;

    @PostMapping
    public CompilationDto createCompilation(@Validated @RequestBody NewCompilationDto compDto) {
        log.info("Try to create new compilation with title {}", compDto.getTitle());
        return service.create(compDto);
    }

    @DeleteMapping("/{compId}")
    public void deleteCompilation(@PathVariable @CompIdExist Long compId) {
        log.info("Try to delete compilation with id ={}", compId);
        service.delete(compId);
    }

    @PatchMapping("/{compId}/events/{eventId}")
    public void addEventToCompilation(@PathVariable @CompIdExist Long compId,
                                      @PathVariable @EventIdExist Long eventId) {
        log.info("Try to add event with id={} to compilation with id ={}", eventId, compId);
        service.addEvent(compId, eventId);
    }

    @DeleteMapping("/{compId}/events/{eventId}")
    public void deleteEventFromCompilation(@PathVariable @CompIdExist Long compId,
                                           @PathVariable @EventIdExist Long eventId){
        log.info("Try to delete event with id = {}, from compilation with id = {}", eventId,compId);
        service.deleteEvent(compId, eventId);
    }

    @PatchMapping("/{compId}/pin")
    public void pinnedCompilation (@PathVariable @CompIdExist Long compId){
        log.info("Try to pin compilation with id = {}", compId);
        service.pin(compId,true);

    }

    @DeleteMapping ("/{compId}/pin")
    public void unPinnedCompilation (@PathVariable @CompIdExist Long compId){
        log.info("Try to unpin compilation with id = {}", compId);
        service.pin(compId,false);
    }

}
