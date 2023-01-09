package ru.practicum.ewm.event.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import ru.practicum.ewm.category.model.dto.CategoryDto;
import ru.practicum.ewm.category.validator.CategoryIdExist;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.event.validator.EventDateInFuture;
import ru.practicum.ewm.user.model.dto.UserShortDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
public class NewEventDto {
    @NotNull
    @NotBlank
    @Size(min =20, max =2000 )
    private String annotation;
    @CategoryIdExist
    private Long category;
    @NotNull
    @NotBlank
    @Size(min = 20,max = 7000)
    private String description;
    @EventDateInFuture
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    @NotNull
    private Location location;
    private boolean paid;
    @PositiveOrZero
    private Integer participantLimit;
    private Boolean requestModeration;
    @NotNull
    @NotBlank
    @Size(min = 3,max = 120)
    private String title;
}
