package ru.practicum.ewm.compilation.model.mapper;

import org.mapstruct.Mapper;
import ru.practicum.ewm.compilation.model.Compilation;
import ru.practicum.ewm.compilation.model.dto.CompilationDto;
import ru.practicum.ewm.compilation.model.dto.NewCompilationDto;
import ru.practicum.ewm.event.service.EventService;

import java.util.List;

@Mapper(componentModel = "spring", uses = {EventService.class})
public interface CompilationMapper {

    Compilation dtoToCompilation(NewCompilationDto compilationDto);

    CompilationDto compilationToDto(Compilation compilation);

    List<CompilationDto> compilationToDto(List<Compilation> compilations);


}
