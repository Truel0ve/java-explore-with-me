package ru.practicum.repositories.event;

import ru.practicum.models.event.Event;

public interface EventRepositoryCustom {
    Event getEventById(Long eventId);
}
