package ru.practicum.ewm.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.model.dto.NewUserRequest;
import ru.practicum.ewm.user.service.UserService;
import ru.practicum.ewm.user.validator.UserIdExist;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping(path = "/admin/users")
@RequiredArgsConstructor
public class UserAdminController {
    private final UserService service;

    @PostMapping
    public User createUser(@Validated @RequestBody NewUserRequest user) {
        log.info("Try to create user. User name:{}", user.getName());
        return service.create(user);
    }

    @GetMapping
    public List<User> getUsers(@RequestParam(name = "ids", required = false) List<Long> ids,
                               @RequestParam(name = "from", defaultValue = "0") int from,
                               @RequestParam(name = "size", defaultValue = "10") int size) {
        log.info("Try to get users. Users ids : {}. And return a {} users from {}.", ids, size, from);
        PageRequest pageRequest = PageRequest.of(from / size, size);
        return service.get(ids, pageRequest);

    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable @UserIdExist Long userId) {
        log.info("Try to delete user with id = {}", userId);
        service.delete(userId);

    }


}
