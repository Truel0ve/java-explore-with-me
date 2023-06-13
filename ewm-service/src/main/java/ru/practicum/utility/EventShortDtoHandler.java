package ru.practicum.utility;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.practicum.ViewStatsDto;
import ru.practicum.dto.event_dto.EventShortDto;
import ru.practicum.repositories.event.EventRepository;
import ru.practicum.utility.mapper.EventMapper;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class EventShortDtoHandler {
    @Lazy
    EventRepository eventRepository;
    @Lazy
    StatsManager statsManager;

    public List<EventShortDto> getEventShortList(Set<Long> eventIds) {
        if (eventIds != null) {
            List<ViewStatsDto> viewStatsList = statsManager.getViewStats(eventIds);
            return eventRepository.findAllById(eventIds)
                    .stream()
                    .map(event -> {
                        EventShortDto eventShortDto = EventMapper.toEventShortDto(event);
                        eventShortDto.setViews(statsManager.getViewsCount(eventShortDto.getId(), viewStatsList));
                        return eventShortDto;
                    })
                    .collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }
}
