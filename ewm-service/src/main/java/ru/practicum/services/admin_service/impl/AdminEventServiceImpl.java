package ru.practicum.services.admin_service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ViewStatsDto;
import ru.practicum.dto.event_dto.EventFullDto;
import ru.practicum.dto.event_dto.UpdateEventRequest;
import ru.practicum.models.event.Event;
import ru.practicum.models.event.EventState;
import ru.practicum.repositories.event.EventRepository;
import ru.practicum.services.admin_service.api.AdminEventService;
import ru.practicum.utility.*;
import ru.practicum.utility.mapper.EventMapper;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminEventServiceImpl implements AdminEventService {
    EventRepository eventRepository;
    StatsManager statsManager;
    EventPatcher eventPatcher;

    @Transactional(readOnly = true)
    @Override
    public List<EventFullDto> getEventFullDtoList(Set<Long> users,
                                                  Set<String> states,
                                                  Set<Long> categories,
                                                  String rangeStart,
                                                  String rangeEnd,
                                                  Integer from,
                                                  Integer size) {
        List<Event> events = getEventList(users, states, categories, rangeStart, rangeEnd, from, size);
        Set<Long> eventIds = events.stream().map(Event::getId).collect(Collectors.toSet());
        List<ViewStatsDto> viewStatsList = statsManager.getViewStats(eventIds);
        return events.stream()
                .map(event -> {
                    EventFullDto eventFullDto = EventMapper.toEventFullDto(event);
                    eventFullDto.setViews(statsManager.getViewsCount(eventFullDto.getId(), viewStatsList));
                    return eventFullDto;
                })
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public EventFullDto patchEventById(Long eventId, UpdateEventRequest updateEventRequest) {
        return eventPatcher.setAndPatch(updateEventRequest, eventRepository.getEventById(eventId), Duration.ofHours(1), true);
    }

    private List<Event> getEventList(Set<Long> users,
                                     Set<String> states,
                                     Set<Long> categories,
                                     String rangeStart,
                                     String rangeEnd,
                                     Integer from,
                                     Integer size) {
        LocalDateTime start = DateTimeParser.toLocalDateTime(rangeStart);
        LocalDateTime end = DateTimeParser.toLocalDateTime(rangeEnd);
        Pageable pageable = PageableBuilder.getPageable(from, size);
        return eventRepository.findEvents(users, getEventStateSet(states), categories, start, end, pageable).getContent();
    }

    @SuppressWarnings("all")
    private Set<EventState> getEventStateSet(Set<String> stringStates) {
        if (stringStates != null && !stringStates.isEmpty()) {
            return stringStates.stream()
                    .map(EventEnumValidator::validateState)
                    .collect(Collectors.toSet());
        } else {
            return null;
        }
    }
}
