package ru.practicum.services.priv.api;

import ru.practicum.dto.partrequest.ParticipationRequestDto;

import java.util.List;

public interface PrivateRequestService {

    List<ParticipationRequestDto> getUserRequestsForEvents(Long userId);

    ParticipationRequestDto postNewRequestForEvent(Long userId, Long eventId);

    ParticipationRequestDto cancelRequestForEvent(Long userId, Long requestId);
}