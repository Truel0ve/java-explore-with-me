package ru.practicum.services.pub.api;

import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.EventShortDto;

import java.util.List;
import java.util.Set;

public interface PublicEventService {

    List<EventShortDto> getEvents(String text,
                                  Set<Long> categories,
                                  Boolean paid,
                                  String rangeStart,
                                  String rangeEnd,
                                  Boolean onlyAvailable,
                                  String sort,
                                  Integer from,
                                  Integer size);

    EventFullDto getEventById(Long id);
}