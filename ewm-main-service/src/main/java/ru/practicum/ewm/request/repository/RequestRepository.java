package ru.practicum.ewm.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.request.model.Participation;
import ru.practicum.ewm.request.model.RequestStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface RequestRepository extends JpaRepository<Participation, Long> {
    Optional<Participation> findByEventIdAndRequesterId(Long eventId, Long userId);

    List<Participation> findAllByRequesterId(Long userId);

    List<Participation> findAllByEventId(Long eventId);

    @Transactional
    @Modifying
    @Query("update Participation p set p.status = :status where p.event.id = :eventId and p.status = :statusNew")
    int updateAllByEventId(@Param("status") RequestStatus status,
                           @Param("eventId") Long eventId,
                           @Param("statusNew") RequestStatus statusNew);
}
