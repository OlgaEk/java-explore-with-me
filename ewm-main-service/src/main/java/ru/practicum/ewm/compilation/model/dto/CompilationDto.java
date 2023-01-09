package ru.practicum.ewm.compilation.model.dto;

import lombok.Data;
import ru.practicum.ewm.event.model.dto.EventShortDto;

import java.util.List;

@Data
public class CompilationDto {
    List<EventShortDto> events;
    Long id;
    Boolean pinned;
    String title;
}
