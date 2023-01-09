package ru.practicum.ewm.request.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.ewm.category.model.mapper.CategoryMapper;
import ru.practicum.ewm.category.service.CategoryService;
import ru.practicum.ewm.event.model.mapper.EventMapper;
import ru.practicum.ewm.request.model.Participation;
import ru.practicum.ewm.request.model.dto.ParticipationRequestDto;
import ru.practicum.ewm.user.model.mapper.UserMapper;

import java.util.List;

@Mapper (componentModel = "spring", uses = {UserMapper.class, EventMapper.class})
public interface RequestMapper {
    @Mapping(target="event", source = "event.id")
    @Mapping(target = "requester", source = "requester.id")
    ParticipationRequestDto requestToDto(Participation request);

    List<ParticipationRequestDto> requestToDto(List<Participation>request);

}
