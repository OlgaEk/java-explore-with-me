package ru.practicum.ewm.event.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewm.category.validator.CategoryIdNullOrExist;
import ru.practicum.ewm.event.validator.EventDateInFuture;
import ru.practicum.ewm.event.validator.EventIdExist;

import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateEventRequest {
    @Size(min = 20, max = 2000)
    String annotation;
    @CategoryIdNullOrExist
    Long category;
    @Size(min = 20, max = 7000)
    String description;
    @EventDateInFuture
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime eventDate;
    @EventIdExist
    Long eventId;
    Boolean paid;
    @PositiveOrZero
    Integer participantLimit;
    @Size(min = 3, max = 120)
    String title;
}
