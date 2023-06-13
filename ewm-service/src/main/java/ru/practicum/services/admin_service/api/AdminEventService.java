package ru.practicum.services.admin_service.api;

import ru.practicum.dto.event_dto.EventFullDto;
import ru.practicum.dto.event_dto.UpdateEventRequest;

import java.util.List;
import java.util.Set;

public interface AdminEventService {

    List<EventFullDto> getEventFullDtoList(Set<Long> users,
                                           Set<String> states,
                                           Set<Long> categories,
                                           String rangeStart,
                                           String rangeEnd,
                                           Integer from,
                                           Integer size);

    EventFullDto patchEventById(Long eventId, UpdateEventRequest category);
}
