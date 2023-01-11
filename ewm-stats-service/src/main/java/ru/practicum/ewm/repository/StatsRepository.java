package ru.practicum.ewm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.ewm.model.Stats;
import ru.practicum.ewm.model.dto.ViewStats;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface StatsRepository extends JpaRepository<Stats, Long> {

    //Если не указывать тип dto в query запросе то выдает ошибку, что не может определить, что такое ViewStats
    @Query("select new ru.practicum.ewm.model.dto.ViewStats (s.app, s.uri, count(s.ip)) from Stats s " +
            "where s.timestamp >= :start and s.timestamp < :end " +
            "and ((:uris) is null or s.uri in (:uris)) " +
            "group by s.uri, s.app")
    List<ViewStats> searchHit(@Param("uris") Collection<String> uris, @Param("start") LocalDateTime timestamp,
                              @Param("end") LocalDateTime timestamp1);

    @Query("select new ru.practicum.ewm.model.dto.ViewStats (s.app, s.uri, count(distinct s.ip)) from Stats s " +
            "where s.timestamp >= :start and s.timestamp < :end " +
            "and ((:uris) is null or s.uri in (:uris)) " +
            "group by s.uri, s.app")
    List<ViewStats> searchHitUnique(@Param("uris") Collection<String> uris, @Param("start") LocalDateTime timestamp,
                                    @Param("end") LocalDateTime timestamp1);
}
