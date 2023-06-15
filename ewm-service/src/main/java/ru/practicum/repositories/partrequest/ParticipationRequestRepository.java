package ru.practicum.repositories.partrequest;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.models.partrequest.ParticipationRequest;
import ru.practicum.models.partrequest.ParticipationRequestStatus;

import java.util.List;
import java.util.Optional;

public interface ParticipationRequestRepository extends JpaRepository<ParticipationRequest, Long> {

    Long countByEventIdAndStatus(Long eventId, ParticipationRequestStatus participationRequestStatus);

    Optional<ParticipationRequest> findByIdAndRequesterId(Long requestId, Long requesterId);

    List<ParticipationRequest> findAllByEventId(Long eventId);

    List<ParticipationRequest> findAllByRequesterId(Long requesterId);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE ParticipationRequest pr " +
            "SET pr.status = ?3 " +
            "WHERE pr.event.id = ?1 " +
            "AND pr.status = ru.practicum.models.partrequest.ParticipationRequestStatus.PENDING " +
            "AND pr.id IN ?2")
    void patch(Long eventId, List<Long> requestIds, ParticipationRequestStatus status);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE ParticipationRequest pr " +
            "SET pr.status = ru.practicum.models.partrequest.ParticipationRequestStatus.CANCELED " +
            "WHERE pr.id = ?1 " +
            "AND pr.requester.id = ?2")
    void cancel(Long requestId, Long requesterId);
}
