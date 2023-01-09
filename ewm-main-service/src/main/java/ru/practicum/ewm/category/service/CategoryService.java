package ru.practicum.ewm.category.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.category.model.dto.CategoryInput;
import ru.practicum.ewm.category.model.dto.CategoryUpdateInput;

import java.util.List;

public interface CategoryService {
    Category create (CategoryInput categoryInput);
    Category update (CategoryUpdateInput category);
    void delete(Long catId);
    List<Category> getAll(Pageable pageable);
    Category getById(Long catId);


}
