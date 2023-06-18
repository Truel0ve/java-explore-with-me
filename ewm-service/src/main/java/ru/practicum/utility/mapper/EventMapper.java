package ru.practicum.utility.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.dto.event.EventDto;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.models.event.Event;
import ru.practicum.models.partrequest.ParticipationRequest;
import ru.practicum.models.partrequest.ParticipationRequestStatus;
import ru.practicum.utility.RateCalculator;
import ru.practicum.utility.validator.EventDateValidator;
import ru.practicum.utility.DateTimeParser;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Set;

@UtilityClass
public class EventMapper {

    public EventFullDto toEventFullDto(Event event) {
        EventFullDto eventFullDto = new EventFullDto();
        eventFullDto.setId(event.getId());
        eventFullDto.setTitle(event.getTitle());
        eventFullDto.setAnnotation(event.getAnnotation());
        eventFullDto.setCategory(CategoryMapper.toCategoryDto(event.getCategory()));
        eventFullDto.setDescription(event.getDescription());
        eventFullDto.setEventDate(DateTimeParser.toString(event.getEventDate()));
        eventFullDto.setCreatedOn(DateTimeParser.toString(event.getCreatedOn()));
        eventFullDto.setPublishedOn(setPublishedOn(event.getPublishedOn()));
        eventFullDto.setLocation(event.getLocation());
        eventFullDto.setPaid(event.getPaid());
        eventFullDto.setRequestModeration(event.getRequestModeration());
        eventFullDto.setParticipantLimit(event.getParticipantLimit());
        eventFullDto.setInitiator(UserMapper.toUserShortDto(event.getInitiator()));
        eventFullDto.setState(event.getState().toString());
        eventFullDto.setConfirmedRequests(getConfirmedRequestsCount(event.getRequests()));
        eventFullDto.setRating(RateCalculator.calculateEvaluation(event.getEvaluations()));
        return eventFullDto;
    }

    public EventShortDto toEventShortDto(Event event) {
        EventShortDto eventShortDto = new EventShortDto();
        eventShortDto.setId(event.getId());
        eventShortDto.setTitle(event.getTitle());
        eventShortDto.setAnnotation(event.getAnnotation());
        eventShortDto.setCategory(CategoryMapper.toCategoryDto(event.getCategory()));
        eventShortDto.setEventDate(DateTimeParser.toString(event.getEventDate()));
        eventShortDto.setPaid(event.getPaid());
        eventShortDto.setConfirmedRequests(getConfirmedRequestsCount(event.getRequests()));
        eventShortDto.setInitiator(UserMapper.toUserShortDto(event.getInitiator()));
        eventShortDto.setRating(RateCalculator.calculateEvaluation(event.getEvaluations()));
        return eventShortDto;
    }

    public Event toEvent(EventDto eventDto) {
        Event event = new Event();
        event.setTitle(eventDto.getTitle());
        event.setAnnotation(eventDto.getAnnotation());
        event.setDescription(eventDto.getDescription());
        event.setEventDate(EventDateValidator.validateEventDate(eventDto.getEventDate(), Duration.ofHours(2)));
        event.setLocation(eventDto.getLocation());
        if (eventDto.getPaid() == null) {
            event.setPaid(false);
        } else {
            event.setPaid(eventDto.getPaid());
        }
        if (eventDto.getRequestModeration() == null) {
            event.setRequestModeration(true);
        } else {
            event.setRequestModeration(eventDto.getRequestModeration());
        }
        if (eventDto.getParticipantLimit() == null) {
            event.setParticipantLimit(0L);
        } else {
            event.setParticipantLimit(eventDto.getParticipantLimit());
        }
        return event;
    }

    private Long getConfirmedRequestsCount(Set<ParticipationRequest> requests) {
        if (requests == null || requests.isEmpty()) {
            return 0L;
        } else {
            return requests.stream()
                    .filter(request -> request.getStatus().equals(ParticipationRequestStatus.CONFIRMED))
                    .count();
        }
    }

    private String setPublishedOn(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        } else {
            return DateTimeParser.toString(localDateTime);
        }
    }
}
