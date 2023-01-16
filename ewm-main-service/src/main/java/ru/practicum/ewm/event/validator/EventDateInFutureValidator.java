package ru.practicum.ewm.event.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class EventDateInFutureValidator implements ConstraintValidator<EventDateInFuture, LocalDateTime> {
    @Override
    public boolean isValid(LocalDateTime eventDate, ConstraintValidatorContext cxt) {
        return eventDate.isAfter(LocalDateTime.now().plusHours(2));
    }
}
