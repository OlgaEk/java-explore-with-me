package ru.practicum.ewm.event.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventState;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByInitiatorId(Long userId, Pageable pageable);


    @Query("select e from Event e " +
            "where (:users is null or e.initiator.id in :users) " +
            "and (:states is null or e.state in :states) " +
            "and (:categories is null or e.category.id in :categories) " +
            "and (cast(:start as date) is null or e.eventDate >= :start) " +
            "and (cast(:ends as date) is null or e.eventDate <= :ends) ")
    List<Event> searchEvents(@Param("users") List<Long> ids, @Param("states") List<EventState> states,
                             @Param("categories") List<Long> ids1, @Param("start") LocalDateTime eventDate,
                             @Param("ends") LocalDateTime eventDate1, Pageable pageable);

    @Query("select e from Event e " +
            "where (:text is null or upper(e.annotation) like upper(concat('%',:text,'%'))) " +
            "and (e.state = :state) " +
            "and (:categories is null or e.category.id in :categories) " +
            "and (:paid is null or e.paid = :paid) " +
            "and ( e.eventDate >= :start) " +
            "and (cast(:ends as date) is null or e.eventDate <= :ends) ")
    List<Event> searchEventsForUser(@Param("state") EventState state, @Param("text") String text,
                                    @Param("categories") List<Long> categories,
                                    @Param("paid") Boolean paid, @Param("start") LocalDateTime eventDate,
                                    @Param("ends") LocalDateTime eventDate1, Pageable pageable);


}
