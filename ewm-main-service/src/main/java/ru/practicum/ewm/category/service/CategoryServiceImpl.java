package ru.practicum.ewm.category.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.category.model.dto.CategoryInput;
import ru.practicum.ewm.category.model.dto.CategoryUpdateInput;
import ru.practicum.ewm.category.model.mapper.CategoryMapper;
import ru.practicum.ewm.category.repository.CategoryRepository;
import ru.practicum.ewm.exception.NoEntityException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository repository;
    private final CategoryMapper mapper;

    public Category create(CategoryInput categoryInput) {
        return repository.save(mapper.inputToCategory(categoryInput));
    }

    public Category update(CategoryUpdateInput category) {
        return repository.save(mapper.updateInputToCategory(category));
    }

    public void delete(Long catId) {
        //Поиск событий привязанных к категории
        Category category = repository.findById(catId)
                .orElseThrow(() -> new NoEntityException("Category with id =" + catId + " was not found"));
        repository.delete(category);
    }

    public List<Category> getAll(Pageable pageable) {
        return repository.findAll(pageable).getContent();
    }

    public Category getById(Long catId) {
        return repository.findById(catId)
                .orElseThrow(() -> new NoEntityException("Category with id =" + catId + " was not found"));
    }


}
