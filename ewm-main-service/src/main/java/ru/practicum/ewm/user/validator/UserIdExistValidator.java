package ru.practicum.ewm.user.validator;

import lombok.RequiredArgsConstructor;
import ru.practicum.ewm.exception.NoEntityException;
import ru.practicum.ewm.user.repository.UserRepository;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@RequiredArgsConstructor
public class UserIdExistValidator implements ConstraintValidator<UserIdExist, Long> {
    private final UserRepository repository;

    @Override
    public boolean isValid(Long userId, ConstraintValidatorContext cxt) {
        if (userId == null) return false;
        if (repository.findById(userId).isEmpty())
            throw new NoEntityException("User with id =" + userId + " was not found");
        return true;
    }
}
