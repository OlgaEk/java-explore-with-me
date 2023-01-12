package ru.practicum.ewm.compilation.model.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewm.event.model.dto.EventShortDto;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompilationDto {
    private List<EventShortDto> events;
    private Long id;
    private Boolean pinned;
    private String title;
}
