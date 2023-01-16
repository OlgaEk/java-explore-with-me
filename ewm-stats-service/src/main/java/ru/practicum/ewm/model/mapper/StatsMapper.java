package ru.practicum.ewm.model.mapper;

import org.mapstruct.Mapper;
import ru.practicum.ewm.model.Stats;
import ru.practicum.ewm.model.dto.EndpointHit;

@Mapper
public interface StatsMapper {
    Stats dtoToStats(EndpointHit hitDto);
}
