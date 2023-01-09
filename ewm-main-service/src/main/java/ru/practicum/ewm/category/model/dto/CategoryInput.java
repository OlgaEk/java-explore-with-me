package ru.practicum.ewm.category.model.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CategoryInput {
    @NotBlank
    @NotNull
    private String name;
}
