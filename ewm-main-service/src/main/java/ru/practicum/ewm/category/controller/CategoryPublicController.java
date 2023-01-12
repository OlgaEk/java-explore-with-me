package ru.practicum.ewm.category.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.category.service.CategoryService;
import ru.practicum.ewm.category.validator.CategoryIdExist;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoryPublicController {
    private final CategoryService service;

    @GetMapping
    List<Category> getCategories(@RequestParam(name = "from", defaultValue = "0") @PositiveOrZero int from,
                                 @RequestParam(name = "size", defaultValue = "10") @Positive int size) {
        log.info("Try to get {} categories from {}", size, from);
        PageRequest pageRequest = PageRequest.of(from / size, size);
        return service.getAll(pageRequest);
    }

    @GetMapping("/{catId}")
    Category getCategory(@PathVariable @CategoryIdExist Long catId) {
        log.info("Try to get category by id = {}", catId);
        return service.getById(catId);

    }

}
