package ru.practicum.ewm.event.model.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Location {
    private Float lat;
    private Float lon;
}
