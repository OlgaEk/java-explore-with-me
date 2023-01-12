package ru.practicum.ewm.compilation.model.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewCompilationDto {
    private List<Long> events;
    private Boolean pinned;
    @NotBlank
    @Size(min = 3, max = 120)
    private String title;
}
