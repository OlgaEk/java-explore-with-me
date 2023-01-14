package ru.practicum.ewm.subscription.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.subscription.model.SubStatus;
import ru.practicum.ewm.subscription.model.Subscription;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubRepository extends JpaRepository<Subscription, Long> {


    Optional<Subscription> findByUserIdAndFriendId(Long userId, Long friendId);

    List<Subscription> findAllByUserIdAndStatus(Long userId, SubStatus status);

    @Query("select s.friend.id from Subscription s where s.user.id = :id and s.status = :status")
    List<Long> findAllUserIdByUserIdAndStatus(@Param("id") Long id, @Param("status") SubStatus status);


}
