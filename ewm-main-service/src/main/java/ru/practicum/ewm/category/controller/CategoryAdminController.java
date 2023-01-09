package ru.practicum.ewm.category.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.category.model.dto.CategoryInput;
import ru.practicum.ewm.category.model.dto.CategoryUpdateInput;
import ru.practicum.ewm.category.service.CategoryService;
import ru.practicum.ewm.category.validator.CategoryIdExist;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/categories")
public class CategoryAdminController {
    private final CategoryService service;

    @PostMapping
    public Category createCategory(@Validated @RequestBody CategoryInput categoryInput){
        log.info("Try to create new category with name= {}", categoryInput.getName());
        return service.create(categoryInput);
    }

    @PatchMapping
    public Category updateCategory (@Validated @RequestBody CategoryUpdateInput categoryUpdateInput){
        log.info("Try to update category with id={}, and new name = {}",
                categoryUpdateInput.getId(),categoryUpdateInput.getName());
        return service.update(categoryUpdateInput);

    }

    @DeleteMapping("/{catId}")
    public void deleteCategory (@PathVariable @CategoryIdExist Long catId){
        log.info("Try to delete category with id = {}", catId);
        service.delete(catId);
    }

}
