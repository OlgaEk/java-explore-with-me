package ru.practicum.ewm.category.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CategoryIdNullOrExistValidator.class)
@Target({ElementType.PARAMETER, ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CategoryIdNullOrExist {
    String message() default "Category id is not exist";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
