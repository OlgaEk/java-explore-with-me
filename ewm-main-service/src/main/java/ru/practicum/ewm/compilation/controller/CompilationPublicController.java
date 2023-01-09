package ru.practicum.ewm.compilation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.compilation.model.dto.CompilationDto;
import ru.practicum.ewm.compilation.service.CompilationService;
import ru.practicum.ewm.compilation.validator.CompIdExist;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("compilations")
public class CompilationPublicController {
    private final CompilationService service;

    @GetMapping
    public List<CompilationDto> getCompilations (@RequestParam(name="pinned",defaultValue = "true") Boolean pinned,
                                          @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero int from,
                                          @RequestParam(name = "size", defaultValue = "10") @Positive int size){
        log.info("Try to get all compilations (pinned = {}). And return {} compilations from {}",pinned,size,from);
        PageRequest pageRequest = PageRequest.of(from / size, size);
        return service.get(pinned,pageRequest);
    }

    @GetMapping("/{compId}")
    public CompilationDto getComplicationById (@PathVariable @CompIdExist Long compId){
        log.info("Try to get compilation by id ={}",compId);
        return service.getById(compId);
    }

}
