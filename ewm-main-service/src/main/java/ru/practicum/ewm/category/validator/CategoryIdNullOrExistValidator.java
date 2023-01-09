package ru.practicum.ewm.category.validator;

import lombok.RequiredArgsConstructor;
import ru.practicum.ewm.category.repository.CategoryRepository;
import ru.practicum.ewm.exception.NoEntityException;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@RequiredArgsConstructor
public class CategoryIdNullOrExistValidator implements ConstraintValidator<CategoryIdNullOrExist,Long> {
    private final CategoryRepository repository;
    @Override
    public boolean isValid(Long id, ConstraintValidatorContext cxt){
        if(id == null) return true;
        if(repository.findById(id).isEmpty())
            throw new NoEntityException("Category with id =" + id + " was not found");
        return true;
    }
}
