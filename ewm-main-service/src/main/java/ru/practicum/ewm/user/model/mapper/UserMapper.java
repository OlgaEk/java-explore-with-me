package ru.practicum.ewm.user.model.mapper;

import org.mapstruct.Mapper;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.model.dto.NewUserRequest;

@Mapper
public interface UserMapper {
    User userInputToUser(NewUserRequest newUserRequest);
}
