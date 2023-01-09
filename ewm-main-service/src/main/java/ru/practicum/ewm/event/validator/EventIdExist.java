package ru.practicum.ewm.event.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EventIdExistValidator.class)
@Target({ElementType.PARAMETER, ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EventIdExist {
    String message() default "Event id is null";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
