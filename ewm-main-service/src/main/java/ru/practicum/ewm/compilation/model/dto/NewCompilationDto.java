package ru.practicum.ewm.compilation.model.dto;

import lombok.Data;
import ru.practicum.ewm.event.model.Event;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class NewCompilationDto {
    List<Long> events;
    Boolean pinned;
    @NotNull
    @Size(min = 3,max = 120)
    String title;
}