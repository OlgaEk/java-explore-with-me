package ru.practicum.ewm.category.model.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewm.category.validator.CategoryIdExist;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryUpdateInput {
    @CategoryIdExist
    Long id;

    @NotBlank
    @Size(max = 512)
    String name;

}
