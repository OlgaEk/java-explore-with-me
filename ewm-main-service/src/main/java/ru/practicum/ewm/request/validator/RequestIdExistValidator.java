package ru.practicum.ewm.request.validator;

import lombok.RequiredArgsConstructor;
import ru.practicum.ewm.exception.NoEntityException;
import ru.practicum.ewm.request.repository.RequestRepository;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.ConstraintViolation;

@RequiredArgsConstructor
public class RequestIdExistValidator implements ConstraintValidator<RequestIdExist,Long> {
    private final RequestRepository repository;

    @Override
    public boolean isValid(Long requestId, ConstraintValidatorContext cxt){
        if(requestId == null) return false;
        if(repository.findById(requestId).isEmpty())
            throw new NoEntityException("Request with id =" + requestId + " was not found");
        return true;
    }
}
