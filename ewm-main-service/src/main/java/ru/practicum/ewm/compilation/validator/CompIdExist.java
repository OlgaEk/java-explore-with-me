package ru.practicum.ewm.compilation.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CompIdExistValidator.class)
@Target({ElementType.PARAMETER, ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CompIdExist {
    String message() default "Compilation id is null";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
