package ru.practicum.ewm.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.validator.EventIdExist;
import ru.practicum.ewm.request.model.dto.ParticipationRequestDto;
import ru.practicum.ewm.request.service.RequestService;
import ru.practicum.ewm.request.validator.RequestIdExist;
import ru.practicum.ewm.user.validator.UserIdExist;

import java.util.List;

@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/users/{userId}/requests")
public class RequestPrivateController {
    private final RequestService service;


    @PostMapping
    public ParticipationRequestDto createRequest(@PathVariable @UserIdExist Long userId,
                                                 @RequestParam @EventIdExist Long eventId){
        log.info("Try to create request to event with id ={} by user with id = {} ",eventId, userId);
        return service.create(userId, eventId);
    }

    @GetMapping
    public List<ParticipationRequestDto> getRequest(@PathVariable @UserIdExist Long userId){
        log.info("Try to get all request made by user with id = {}", userId);
        return service.getByUser(userId);
    }

    @PatchMapping("{requestId}/cancel")
    public ParticipationRequestDto cancelRequest(@PathVariable @UserIdExist Long userId,
                                                 @PathVariable @RequestIdExist Long requestId){
        log.info("Try to cancel request with id = {} by user with id ={}", requestId, userId);
        return service.cancel(userId, requestId);
    }
}
