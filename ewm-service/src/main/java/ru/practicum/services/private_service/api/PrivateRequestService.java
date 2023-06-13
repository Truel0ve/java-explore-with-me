package ru.practicum.services.private_service.api;

import ru.practicum.dto.participation_request_dto.ParticipationRequestDto;

import java.util.List;

public interface PrivateRequestService {

    List<ParticipationRequestDto> getUserRequestsForEvents(Long userId);

    ParticipationRequestDto postNewRequestForEvent(Long userId, Long eventId);

    ParticipationRequestDto cancelRequestForEvent(Long userId, Long requestId);
}