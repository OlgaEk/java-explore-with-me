package ru.practicum.ewm.subscription.service;

import ru.practicum.ewm.event.model.dto.EventShortDto;
import ru.practicum.ewm.subscription.model.SubStatus;

import java.util.List;

public interface SubService {

    void create(Long userId, Long friendId);

    void setStatus(Long userId, Long friendId, SubStatus status);

    String status(Long userId, Long friendId);

    List<EventShortDto> getFriendEvent(Long userId);

}
