package ru.practicum.ewm.category.model.dto;

import lombok.Data;
import ru.practicum.ewm.category.validator.CategoryIdExist;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CategoryDto {
    private Long id;
    private String name;
}
