package ru.practicum.ewm.subscription.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.dto.EventShortDto;
import ru.practicum.ewm.event.model.mapper.EventMapper;
import ru.practicum.ewm.exception.ForbiddenException;
import ru.practicum.ewm.exception.NoEntityException;
import ru.practicum.ewm.request.model.RequestStatus;
import ru.practicum.ewm.request.repository.RequestRepository;
import ru.practicum.ewm.subscription.model.SubStatus;
import ru.practicum.ewm.subscription.model.Subscription;
import ru.practicum.ewm.subscription.repository.SubRepository;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SubServiceImpl implements SubService {
    private final UserRepository userRepository;
    private final SubRepository subRepository;
    private final RequestRepository requestRepository;

    private final EventMapper eventMapper;

    @Transactional
    public void create(Long userId, Long friendId) {
        if (userId.equals(friendId))
            throw new ForbiddenException("Not allowed to user be a friend of himself");
        if (subRepository.findByUserIdAndFriendId(userId, friendId).isPresent())
            throw new ForbiddenException("Request to friendship from user with id = " + userId + "to friend" +
                    " with id=" + friendId + "already exist");
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoEntityException("User with id = " + userId + " was not found"));
        User friend = userRepository.findById(friendId)
                .orElseThrow(() -> new NoEntityException("User with id = " + userId + " was not found"));
        Subscription sub = new Subscription();
        sub.setUser(user);
        sub.setFriend(friend);
        sub.setStatus(SubStatus.PENDING);
        subRepository.save(sub);
    }

    @Transactional
    public void setStatus(Long userId, Long friendId, SubStatus status) {
        Subscription sub = subRepository.findByUserIdAndFriendId(friendId, userId)
                .orElseThrow(() -> new ForbiddenException("Request to friendship from user with id = " + userId +
                        "to friend with id=" + friendId + " not exist"));
        sub.setStatus(status);
        subRepository.save(sub);
    }


    public String status(Long userId, Long friendId) {
        Subscription sub = subRepository.findByUserIdAndFriendId(userId, friendId)
                .orElseThrow(() -> new ForbiddenException("User with id =" + userId + "is not have request " +
                        "to friendship to user with id=" + friendId));
        return sub.getStatus().toString();
    }

    public List<EventShortDto> getFriendEvent(Long userId) {
        List<Event> events = requestRepository.findAllEventsByUserIDIdsAndStatus(RequestStatus.CONFIRMED, userId,
                SubStatus.CONFIRMED);
        return eventMapper.eventToShortDto(events);


    }


}
