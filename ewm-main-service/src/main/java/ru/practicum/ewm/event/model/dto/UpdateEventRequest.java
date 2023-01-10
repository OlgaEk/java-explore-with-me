package ru.practicum.ewm.event.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import ru.practicum.ewm.category.validator.CategoryIdNullOrExist;
import ru.practicum.ewm.event.validator.EventDateInFuture;
import ru.practicum.ewm.event.validator.EventIdExist;

import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
public class UpdateEventRequest {
    @Size(min = 20, max = 2000)
    private String annotation;
    @CategoryIdNullOrExist
    private Long category;
    @Size(min = 20, max = 7000)
    private String description;
    @EventDateInFuture
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    @EventIdExist
    private Long eventId;
    private Boolean paid;
    @PositiveOrZero
    private Integer participantLimit;
    @Size(min = 3, max = 120)
    private String title;
}
