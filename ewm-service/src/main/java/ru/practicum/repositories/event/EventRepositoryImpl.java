package ru.practicum.repositories.event;

import org.springframework.context.annotation.Lazy;
import ru.practicum.exceptions.ArgumentNotFoundException;
import ru.practicum.models.event.Event;

public class EventRepositoryImpl implements EventRepositoryCustom {
    private final EventRepository eventRepository;

    public EventRepositoryImpl(@Lazy EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public Event getEventById(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(
                () -> new ArgumentNotFoundException("The specified event id=" + eventId + " does not exist",
                        new NullPointerException()));
    }

}
