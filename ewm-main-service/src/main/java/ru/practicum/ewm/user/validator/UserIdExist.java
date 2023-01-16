package ru.practicum.ewm.user.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UserIdExistValidator.class)
@Target({ElementType.PARAMETER, ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UserIdExist {
    String message() default "User id is null";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
