package ru.practicum.ewm.event.model.mapper;

import org.mapstruct.*;
import ru.practicum.ewm.category.model.mapper.CategoryMapper;
import ru.practicum.ewm.category.service.CategoryService;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.dto.*;
import ru.practicum.ewm.user.model.mapper.UserMapper;

import java.util.List;

@Mapper (componentModel = "spring", uses = {CategoryMapper.class, CategoryService.class, UserMapper.class})
public interface EventMapper {
    @Mapping(target = "category", source = "category")
    @Mapping(target = "lat", source = "location.lat")
    @Mapping(target = "lon", source = "location.lon")
    Event fullDtoToEvent(NewEventDto eventDto);

    List<EventShortDto> eventToShortDto(List<Event> events);
    List<EventFullDto> eventToFullDto(List<Event> events);

    @Mapping(target = "location.lat",source = "lat")
    @Mapping(target = "location.lon", source = "lon")
    EventFullDto eventToFullDto (Event event);

    //https://mapstruct.org/documentation/stable/reference/html/#mapping-result-for-null-properties
    //https://mapstruct.org/documentation/stable/reference/html/#updating-bean-instances

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEventFromDto(UpdateEventRequest dto, @MappingTarget Event event);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEventFromDto(AdminUpdateEventRequest dto, @MappingTarget Event event);

}
