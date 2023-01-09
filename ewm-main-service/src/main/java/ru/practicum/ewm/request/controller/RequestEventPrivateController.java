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
@RequestMapping("/users/{userId}/events/{eventId}/requests")
public class RequestEventPrivateController {
    private final RequestService service;

    @GetMapping
    public List<ParticipationRequestDto> getRequestsForUsersEvent(@PathVariable @UserIdExist Long userId,
                                                                  @PathVariable @EventIdExist Long eventId) {
        log.info("Try to get request from user with id = {} to event with id = {}", userId, eventId);
        return service.getForUsersEvent(userId, eventId);
    }

    @PatchMapping("/{reqId}/confirm")
    public ParticipationRequestDto confirmEvent(@PathVariable @UserIdExist Long userId,
                                           @PathVariable @EventIdExist Long eventId,
                                           @PathVariable @RequestIdExist Long reqId) {
        log.info("Try to confirm request with id={} in event with id={}, by user with id = {}", reqId, eventId, userId);
        return service.confirm(userId, eventId, reqId);
    }
    @PatchMapping("/{reqId}/reject")
    public ParticipationRequestDto rejectEvent(@PathVariable @UserIdExist Long userId,
                                          @PathVariable @EventIdExist Long eventId,
                                          @PathVariable @RequestIdExist Long reqId){
        log.info("Try to reject request with id={} in event with id={}, by user with id = {}", reqId, eventId, userId);
        return service.reject(userId, eventId, reqId);
    }

}
