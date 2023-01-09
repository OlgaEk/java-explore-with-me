package ru.practicum.ewm.category.model.mapper;

import org.mapstruct.Mapper;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.category.model.dto.CategoryDto;
import ru.practicum.ewm.category.model.dto.CategoryInput;
import ru.practicum.ewm.category.model.dto.CategoryUpdateInput;

@Mapper
public interface CategoryMapper {
    Category inputToCategory (CategoryInput input);

    Category updateInputToCategory(CategoryUpdateInput updateInput);
}
