package ru.practicum.ewm.subscription.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.model.dto.EventShortDto;
import ru.practicum.ewm.subscription.model.SubStatus;
import ru.practicum.ewm.subscription.service.SubService;
import ru.practicum.ewm.user.validator.UserIdExist;

import java.util.List;

@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/users/{userId}/sub")
public class SubscriptionPublicController {
    private final SubService service;


    @PostMapping("/{friendId}")
    public void createSub(@PathVariable @UserIdExist Long userId,
                          @PathVariable @UserIdExist Long friendId) {
        log.info("Try to make by user with id ={} a friend to user with id ={}", userId, friendId);
        service.create(userId, friendId);
    }

    @PatchMapping("/{friendId}")
    public void confirmSub(@PathVariable @UserIdExist Long userId,
                           @PathVariable @UserIdExist Long friendId) {
        log.info("Try to confirm a friendship from user with id = {} to friend with id ={}", userId, friendId);
        service.setStatus(userId, friendId, SubStatus.CONFIRMED);
    }

    @DeleteMapping("/{friendId}")
    public void rejectSub(@PathVariable @UserIdExist Long userId,
                          @PathVariable @UserIdExist Long friendId) {
        log.info("Try to reject a friendship from user with id = {} to friend with id ={}", userId, friendId);
        service.setStatus(userId, friendId, SubStatus.REJECTED);
    }


    @GetMapping("/{friendId}")
    public String statusSub(@PathVariable @UserIdExist Long userId,
                            @PathVariable @UserIdExist Long friendId) {
        log.info("Try to get status a friendship from user wit id = {} to friend with id= {}", userId, friendId);
        return service.status(userId, friendId);
    }

    @GetMapping("/events")
    public List<EventShortDto> getFriendEvent(@PathVariable @UserIdExist Long userId) {
        log.info("Try to get events in with friends take part. User with id = {} ", userId);
        return service.getFriendEvent(userId);
    }


}
