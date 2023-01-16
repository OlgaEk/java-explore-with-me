package ru.practicum.ewm.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.model.dto.NewUserRequest;
import ru.practicum.ewm.user.model.mapper.UserMapper;
import ru.practicum.ewm.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final UserMapper mapper;

    @Transactional
    public User create(NewUserRequest newUserRequest) {
        return repository.save(mapper.userInputToUser(newUserRequest));
    }

    public List<User> get(List<Long> ids, Pageable pageable) {
        return ids == null ? repository.findAll(pageable).getContent() : repository.findAllByIdIn(ids, pageable);
    }

    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }

}
