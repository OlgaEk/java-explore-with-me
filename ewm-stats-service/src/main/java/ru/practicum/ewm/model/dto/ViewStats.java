package ru.practicum.ewm.model.dto;

import lombok.Data;

@Data
public class ViewStats {
    private String app;
    private String uri;
    private Long hits;
}
