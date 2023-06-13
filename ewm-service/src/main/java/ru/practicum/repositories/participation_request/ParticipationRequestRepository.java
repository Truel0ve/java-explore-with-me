package ru.practicum.repositories.participation_request;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.models.participation_request.ParticipationRequest;
import ru.practicum.models.participation_request.ParticipationRequestStatus;

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
            "AND pr.status = ru.practicum.models.participation_request.ParticipationRequestStatus.PENDING " +
            "AND pr.id IN ?2")
    void patch(Long eventId, List<Long> requestIds, ParticipationRequestStatus status);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE ParticipationRequest pr " +
            "SET pr.status = ru.practicum.models.participation_request.ParticipationRequestStatus.CANCELED " +
            "WHERE pr.id = ?1 " +
            "AND pr.requester.id = ?2")
    void cancel(Long requestId, Long requesterId);
}
