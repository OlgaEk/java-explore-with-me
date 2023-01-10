package ru.practicum.ewm.request.service;

import ru.practicum.ewm.request.model.dto.ParticipationRequestDto;

import java.util.List;

public interface RequestService {
    ParticipationRequestDto create(Long userId, Long eventId);

    List<ParticipationRequestDto> getByUser(Long userId);

    ParticipationRequestDto cancel(Long userId, Long requestId);

    List<ParticipationRequestDto> getForUsersEvent(Long userId, Long eventId);

    ParticipationRequestDto confirm(Long userId, Long eventId, Long reqId);

    ParticipationRequestDto reject(Long userId, Long eventId, Long reqId);
}
