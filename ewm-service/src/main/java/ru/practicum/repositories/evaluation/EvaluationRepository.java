package ru.practicum.repositories.evaluation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.models.evaluations.Evaluation;
import ru.practicum.models.evaluations.EvaluationId;

import java.util.List;
import java.util.Set;

public interface EvaluationRepository extends JpaRepository<Evaluation, EvaluationId>, EvaluationRepositoryCustom {

    @Query("SELECT e " +
            "FROM Evaluation e " +
            "WHERE e.event.initiator.id IN ?1 OR ?1 IS NULL")
    List<Evaluation> findAllByInitiatorIds(Set<Long> initiatorIds);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Evaluation e " +
            "SET e.isLike = ?3 " +
            "WHERE e.user.id = ?1 " +
            "AND e.event.id = ?2")
    void put(Long userId, Long eventId, boolean isLike);

    @Modifying(clearAutomatically = true)
    void deleteByUserIdAndEventId(Long userId, Long eventId);
}