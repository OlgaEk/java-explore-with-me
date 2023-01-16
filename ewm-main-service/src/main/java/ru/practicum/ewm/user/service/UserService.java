package ru.practicum.ewm.user.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.model.dto.NewUserRequest;

import java.util.List;

public interface UserService {
    User create(NewUserRequest user);

    List<User> get(List<Long> ids, Pageable pageable);

    void delete(Long id);
}
