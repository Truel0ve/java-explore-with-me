package ru.practicum.services.priv.api;

import ru.practicum.dto.event.*;
import ru.practicum.dto.partrequest.EventRequestStatusUpdateRequest;
import ru.practicum.dto.partrequest.EventRequestStatusUpdateResult;
import ru.practicum.dto.partrequest.ParticipationRequestDto;

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