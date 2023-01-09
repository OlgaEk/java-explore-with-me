package ru.practicum.ewm.category.validator;

import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.user.validator.UserIdExistValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CategoryIdExistValidator.class)
@Target({ElementType.PARAMETER, ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CategoryIdExist {
    String message() default "Category id is null";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
