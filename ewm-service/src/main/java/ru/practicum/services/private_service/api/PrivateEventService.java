package ru.practicum.services.private_service.api;

import ru.practicum.dto.event_dto.*;
import ru.practicum.dto.participation_request_dto.EventRequestStatusUpdateRequest;
import ru.practicum.dto.participation_request_dto.EventRequestStatusUpdateResult;
import ru.practicum.dto.participation_request_dto.ParticipationRequestDto;

import java.util.List;

public interface PrivateEventService {

    List<EventShortDto> getUserEvents(Long userId, Integer from, Integer size);

    EventFullDto postNewEvent(Long userId, EventDto event);

    EventFullDto getUserEventById(Long userId, Long eventId);

    EventFullDto patchEventByInitiator(Long userId,
                                       Long eventId,
                                       UpdateEventRequest event);

    List<ParticipationRequestDto> getEventRequests(Long userId, Long eventId);

    EventRequestStatusUpdateResult patchEventRequests(Long userId,
                                                      Long eventId,
                                                      EventRequestStatusUpdateRequest updateRequest);
}