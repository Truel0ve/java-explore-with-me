package ru.practicum.utility;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import ru.practicum.ViewStatsDto;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.UpdateEventRequest;
import ru.practicum.models.event.Event;
import ru.practicum.repositories.category.CategoryRepository;
import ru.practicum.repositories.event.EventRepository;
import ru.practicum.utility.mapper.EventMapper;

import java.time.Duration;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EventPatcher {
    EventRepository eventRepository;
    CategoryRepository categoryRepository;
    StatsManager statsManager;

    public EventFullDto setAndPatch(UpdateEventRequest updateEventRequest, Event event, Duration duration, boolean isAdmin) {
        Event settedEvent = setEventFields(updateEventRequest, event, duration, isAdmin);
        eventRepository.patch(
                settedEvent.getInitiator().getId(),
                settedEvent.getId(),
                settedEvent.getTitle(),
                settedEvent.getAnnotation(),
                settedEvent.getCategory(),
                settedEvent.getDescription(),
                settedEvent.getEventDate(),
                settedEvent.getPublishedOn(),
                settedEvent.getLocation(),
                settedEvent.getParticipantLimit(),
                settedEvent.getPaid(),
                settedEvent.getRequestModeration(),
                settedEvent.getState());
        EventFullDto eventFullDto = EventMapper.toEventFullDto(eventRepository.getEventById(settedEvent.getId()));
        List<ViewStatsDto> viewStatsDtoList = statsManager.getViewStats(Set.of(settedEvent.getId()));
        eventFullDto.setViews(statsManager.getViewsCount(settedEvent.getId(), viewStatsDtoList));
        return eventFullDto;
    }

    private Event setEventFields(UpdateEventRequest updateEventRequest, Event event, Duration duration, boolean isAdmin) {
        if (updateEventRequest.getTitle() != null && !updateEventRequest.getTitle().isBlank()) {
            event.setTitle(updateEventRequest.getTitle());
        }
        if (updateEventRequest.getAnnotation() != null && !updateEventRequest.getAnnotation().isBlank()) {
            event.setAnnotation(updateEventRequest.getAnnotation());
        }
        if (updateEventRequest.getDescription() != null && !updateEventRequest.getDescription().isBlank()) {
            event.setDescription(updateEventRequest.getDescription());
        }
        if (updateEventRequest.getCategory() != null) {
            event.setCategory(categoryRepository.getCategoryById(updateEventRequest.getCategory()));
        }
        if (updateEventRequest.getEventDate() != null) {
            event.setEventDate(EventDateValidator.validateEventDate(
                    updateEventRequest.getEventDate(), duration));
        }
        if (updateEventRequest.getLocation() != null) {
            event.setLocation(updateEventRequest.getLocation());
        }
        if (updateEventRequest.getPaid() != null) {
            event.setPaid(updateEventRequest.getPaid());
        }
        if (updateEventRequest.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEventRequest.getParticipantLimit());
        }
        if (updateEventRequest.getRequestModeration() != null) {
            event.setRequestModeration(updateEventRequest.getRequestModeration());
        }
        event.setState(EventEnumValidator.validateAndSetEventState(updateEventRequest, event, isAdmin));
        return event;
    }

}
