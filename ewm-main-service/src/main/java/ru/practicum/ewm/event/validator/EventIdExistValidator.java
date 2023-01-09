package ru.practicum.ewm.event.validator;

import lombok.RequiredArgsConstructor;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.exception.NoEntityException;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@RequiredArgsConstructor
public class EventIdExistValidator implements ConstraintValidator<EventIdExist,Long> {
    private final EventRepository repository;

    @Override
    public boolean isValid(Long eventId, ConstraintValidatorContext cxt){
        if(eventId == null) return false;
        if(repository.findById(eventId).isEmpty())
            throw new NoEntityException("Event with id = "+ eventId + " is not found");
        return true;
    }
}
