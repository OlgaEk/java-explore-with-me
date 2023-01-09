package ru.practicum.ewm.compilation.validator;

import lombok.RequiredArgsConstructor;
import ru.practicum.ewm.compilation.repository.CompilationRepository;
import ru.practicum.ewm.exception.NoEntityException;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@RequiredArgsConstructor
public class CompIdExistValidator implements ConstraintValidator<CompIdExist,Long> {
    private final CompilationRepository repository;

    @Override
    public boolean isValid(Long compId, ConstraintValidatorContext cxt){
        if(compId == null) return false;
        if(repository.findById(compId).isEmpty())
            throw new NoEntityException("Event with id = "+ compId + " is not found");
        return true;
    }

}
