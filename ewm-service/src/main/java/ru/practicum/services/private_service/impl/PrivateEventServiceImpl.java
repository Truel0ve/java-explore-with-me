package ru.practicum.services.private_service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ViewStatsDto;
import ru.practicum.dto.event_dto.EventDto;
import ru.practicum.dto.event_dto.EventFullDto;
import ru.practicum.dto.event_dto.EventShortDto;
import ru.practicum.dto.event_dto.UpdateEventRequest;
import ru.practicum.dto.participation_request_dto.EventRequestStatusUpdateRequest;
import ru.practicum.dto.participation_request_dto.EventRequestStatusUpdateResult;
import ru.practicum.dto.participation_request_dto.ParticipationRequestDto;
import ru.practicum.exceptions.ArgumentNotFoundException;
import ru.practicum.exceptions.ValidationException;
import ru.practicum.models.event.Event;
import ru.practicum.models.event.EventState;
import ru.practicum.models.location.Location;
import ru.practicum.models.participation_request.ParticipationRequestStatus;
import ru.practicum.repositories.category.CategoryRepository;
import ru.practicum.repositories.event.EventRepository;
import ru.practicum.repositories.location.LocationRepository;
import ru.practicum.repositories.participation_request.ParticipationRequestRepository;
import ru.practicum.repositories.user.UserRepository;
import ru.practicum.services.private_service.api.PrivateEventService;
import ru.practicum.utility.*;
import ru.practicum.utility.mapper.EventMapper;
import ru.practicum.utility.mapper.ParticipationRequestMapper;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PrivateEventServiceImpl implements PrivateEventService {
    EventRepository eventRepository;
    ParticipationRequestRepository requestRepository;
    UserRepository userRepository;
    CategoryRepository categoryRepository;
    LocationRepository locationRepository;
    StatsManager statsManager;
    EventPatcher eventPatcher;

    @Override
    public List<EventShortDto> getUserEvents(Long userId, Integer from, Integer size) {
        Set<Long> userEventIds = new HashSet<>();
        List<EventShortDto> events = eventRepository.findEventsByInitiatorId(userId, PageableBuilder.getPageable(from, size))
                .stream()
                .map(event -> {
                    EventShortDto eventShortDto = EventMapper.toEventShortDto(event);
                    userEventIds.add(eventShortDto.getId());
                    return eventShortDto;
                })
                .collect(Collectors.toList());
        List<ViewStatsDto> viewStatsList = statsManager.getViewStats(userEventIds);
        return events.stream()
                .peek(eventShortDto -> eventShortDto.setViews(statsManager.getViewsCount(eventShortDto.getId(), viewStatsList)))
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public EventFullDto postNewEvent(Long userId, EventDto eventDto) {
        EventFullDto eventFullDto = EventMapper.toEventFullDto(setAndSaveNewEvent(userId, eventDto));
        eventFullDto.setConfirmedRequests(0L);
        eventFullDto.setViews(0L);
        return eventFullDto;
    }

    @Override
    public EventFullDto getUserEventById(Long userId, Long eventId) {
        Event event = eventRepository.getEventById(eventId);
        validateEventInitiator(userId, event);
        EventFullDto eventFullDto = EventMapper.toEventFullDto(event);
        eventFullDto.setConfirmedRequests(
                requestRepository.countByEventIdAndStatus(eventId, ParticipationRequestStatus.CONFIRMED));
        eventFullDto.setViews(statsManager.getViewsCount(eventId, statsManager.getViewStats(Set.of(eventId))));
        return eventFullDto;
    }

    @Transactional
    public EventFullDto patchEventByInitiator(Long userId, Long eventId, UpdateEventRequest updateEventRequest) {
        Event event = eventRepository.getEventById(eventId);
        validateEventInitiator(userId, event);
        validateEventStatus(event);
        return eventPatcher.setAndPatch(updateEventRequest, event, Duration.ofHours(1), false);
    }

    @Override
    public List<ParticipationRequestDto> getEventRequests(Long userId, Long eventId) {
        validateEventInitiator(userId, eventRepository.getEventById(eventId));
        return requestRepository.findAllByEventId(eventId)
                .stream()
                .map(ParticipationRequestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public EventRequestStatusUpdateResult patchEventRequests(Long userId,
                                                             Long eventId,
                                                             EventRequestStatusUpdateRequest updateRequest) {
        patchRequests(userId, eventId, updateRequest);
        return getPatchedRequests(eventId);
    }

    private void patchRequests(Long userId,
                               Long eventId,
                               EventRequestStatusUpdateRequest updateRequest) {
        Event event = eventRepository.getEventById(eventId);
        validateEventInitiator(userId, event);
        ParticipationRequestStatus newStatus = ParticipationRequestStatus.valueOf(updateRequest.getStatus());
        if (newStatus.equals(ParticipationRequestStatus.REJECTED)) {
            validateConfirmedRequests(updateRequest.getRequestIds());
            requestRepository.patch(eventId, updateRequest.getRequestIds(), ParticipationRequestStatus.REJECTED);
        } else {
            EventFullDto eventFullDto = EventMapper.toEventFullDto(event);
            Long participantLimit = event.getParticipantLimit();
            if (participantLimit == 0) {
                requestRepository.patch(eventId, updateRequest.getRequestIds(), newStatus);
            } else {
                Long confirmedRequests = eventFullDto.getConfirmedRequests();
                if (confirmedRequests < participantLimit) {
                    List<Long> idsForConfirm = updateRequest.getRequestIds()
                            .stream()
                            .filter(id -> confirmedRequests + 1 <= participantLimit)
                            .collect(Collectors.toList());
                    List<Long> idsForReject = updateRequest.getRequestIds()
                            .stream()
                            .filter(id -> !idsForConfirm.contains(id))
                            .collect(Collectors.toList());
                    requestRepository.patch(eventId, idsForConfirm, ParticipationRequestStatus.CONFIRMED);
                    if (!idsForReject.isEmpty()) {
                        requestRepository.patch(eventId, idsForReject, ParticipationRequestStatus.REJECTED);
                    }
                } else {
                    throw new ValidationException(
                            "Entry limit for the event id=" + event.getId() + " has been reached",
                            new IllegalArgumentException());
                }
            }
        }
    }

    private Event setAndSaveNewEvent(Long userId, EventDto eventDto) {
        Event event = EventMapper.toEvent(eventDto);
        event.setInitiator(userRepository.getUserById(userId));
        event.setCategory(categoryRepository.getCategoryById(eventDto.getCategory()));
        event.setLocation(setNewLocation(event.getLocation()));
        event.setCreatedOn(LocalDateTime.now());
        event.setState(EventState.PENDING);
        return eventRepository.save(event);
    }

    private EventRequestStatusUpdateResult getPatchedRequests(Long eventId) {
        List<ParticipationRequestDto> allEventRequests = requestRepository.findAllByEventId(eventId)
                .stream()
                .map(ParticipationRequestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
        List<ParticipationRequestDto> confirmedRequests = allEventRequests
                .stream()
                .filter(request -> request.getStatus().equals(ParticipationRequestStatus.CONFIRMED.toString()))
                .collect(Collectors.toList());
        List<ParticipationRequestDto> rejectedRequests = allEventRequests
                .stream()
                .filter(request -> request.getStatus().equals(ParticipationRequestStatus.REJECTED.toString()))
                .collect(Collectors.toList());
        return new EventRequestStatusUpdateResult(confirmedRequests, rejectedRequests);
    }

    private Location setNewLocation(Location location) {
        try {
            return locationRepository.getLocation(location.getLat(), location.getLon());
        } catch (ArgumentNotFoundException e) {
            return locationRepository.save(location);
        }
    }

    private void validateEventInitiator(Long userId, Event event) {
        if (!event.getInitiator().getId().equals(userId)) {
            throw new ValidationException(
                    "The user id=" + userId + " is not the initiator of the specified event id=" + event.getId(),
                    new IllegalArgumentException());
        }
    }

    private void validateEventStatus(Event event) {
        if (event.getState().equals(EventState.PUBLISHED)) {
            throw new ValidationException(
                    "The event id=" + event.getId() + " is already published and cannot be changed",
                    new IllegalArgumentException());
        }
    }

    private void validateConfirmedRequests(List<Long> requestIds) {
        requestRepository.findAllById(requestIds).stream()
                        .filter(request -> request.getStatus().equals(ParticipationRequestStatus.CONFIRMED))
                        .findAny()
                        .ifPresent(request -> {
                            throw new ValidationException(
                                    "Unable to change confirmed participation request",
                                    new IllegalArgumentException());
                        });
    }
}
