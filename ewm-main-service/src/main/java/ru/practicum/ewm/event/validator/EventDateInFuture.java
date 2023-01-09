package ru.practicum.ewm.event.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EventDateInFutureValidator.class)
@Target({ElementType.PARAMETER, ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EventDateInFuture {
    String message() default "Event date is not in future";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
