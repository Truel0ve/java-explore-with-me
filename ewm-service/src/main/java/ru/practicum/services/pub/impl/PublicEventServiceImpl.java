package ru.practicum.services.pub.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ViewStatsDto;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.exceptions.ArgumentNotFoundException;
import ru.practicum.models.event.Event;
import ru.practicum.models.event.EventSort;
import ru.practicum.repositories.event.EventRepository;
import ru.practicum.services.pub.api.PublicEventService;
import ru.practicum.utility.EventEnumValidator;
import ru.practicum.utility.PageableBuilder;
import ru.practicum.utility.StatsManager;
import ru.practicum.utility.DateTimeParser;
import ru.practicum.utility.mapper.EventMapper;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PublicEventServiceImpl implements PublicEventService {
    EventRepository eventRepository;
    StatsManager statsManager;

    @Override
    public List<EventShortDto> getEvents(String text,
                                 Set<Long> categories,
                                 Boolean paid,
                                 String rangeStart,
                                 String rangeEnd,
                                 Boolean onlyAvailable,
                                 String sort,
                                 Integer from,
                                 Integer size) {
        List<Event> events = eventRepository.findAllForPublic(text, categories, paid,
                DateTimeParser.toLocalDateTime(rangeStart), DateTimeParser.toLocalDateTime(rangeEnd));
        List<EventShortDto> eventShortDtoList = getEventShortDtoList(events, onlyAvailable);
        Page<EventShortDto> page = getEventPage(
                sortEvents(eventShortDtoList, EventEnumValidator.validateEventSort(sort)),
                from,
                size);
        return page.getContent();
    }

    @Override
    public EventFullDto getEventById(Long eventId) {
        Event event = eventRepository.findByIdForPublic(eventId).orElseThrow(
                () -> new ArgumentNotFoundException("The specified event id=" + eventId + " does not exist", new NullPointerException()));
        EventFullDto eventFullDto = EventMapper.toEventFullDto(event);
        eventFullDto.setViews(statsManager.getViewsCount(eventId, statsManager.getViewStats(Set.of(eventId))));
        return eventFullDto;
    }

    private List<EventShortDto> getEventShortDtoList(List<Event> events, Boolean onlyAvailable) {
        Set<Long> eventIds = events.stream()
                .map(Event::getId)
                .collect(Collectors.toSet());
        List<ViewStatsDto> viewStatsList = statsManager.getViewStats(eventIds);
        return events.stream()
                .map(event -> {
                    EventShortDto eventShortDto = EventMapper.toEventShortDto(event);
                    if (Boolean.TRUE.equals(onlyAvailable)) {
                        return getAvailable(event, eventShortDto);
                    } else {
                        return eventShortDto;
                    }
                })
                .filter(Objects::nonNull)
                .peek(eventShortDto -> eventShortDto.setViews(statsManager.getViewsCount(eventShortDto.getId(), viewStatsList)))
                .collect(Collectors.toList());
    }

    private List<EventShortDto> sortEvents(List<EventShortDto> events, EventSort sort) {
        switch (sort) {
            case EVENT_DATE:
                return events.stream()
                        .sorted(Comparator.comparing(EventShortDto::getEventDate))
                        .collect(Collectors.toList());
            case VIEWS:
                return events.stream()
                        .sorted(Comparator.comparing(EventShortDto::getViews))
                        .collect(Collectors.toList());
            default:
                throw new ArgumentNotFoundException("The specified sort " + sort + " is not supported",
                        new NullPointerException());
        }
    }

    private EventShortDto getAvailable(Event event, EventShortDto eventShortDto) {
        if (event.getParticipantLimit() == 0 || event.getParticipantLimit() < eventShortDto.getConfirmedRequests()) {
            return eventShortDto;
        } else {
            return null;
        }
    }

    private Page<EventShortDto> getEventPage(List<EventShortDto> eventShortDtoList, Integer from, Integer size) {
        Pageable pageable = PageableBuilder.getPageable(from, size);
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), eventShortDtoList.size());
        return new PageImpl<>(eventShortDtoList.subList(start, end), pageable, eventShortDtoList.size());
    }
}
