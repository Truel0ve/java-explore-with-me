package ru.practicum.services.priv.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.partrequest.ParticipationRequestDto;
import ru.practicum.exceptions.ArgumentNotFoundException;
import ru.practicum.exceptions.ValidationException;
import ru.practicum.exceptions.WrongStateArgumentException;
import ru.practicum.models.event.Event;
import ru.practicum.models.event.EventState;
import ru.practicum.models.partrequest.ParticipationRequest;
import ru.practicum.models.partrequest.ParticipationRequestStatus;
import ru.practicum.models.user.User;
import ru.practicum.repositories.event.EventRepository;
import ru.practicum.repositories.partrequest.ParticipationRequestRepository;
import ru.practicum.repositories.user.UserRepository;
import ru.practicum.services.priv.api.PrivateRequestService;
import ru.practicum.utility.mapper.ParticipationRequestMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PrivateRequestServiceImpl implements PrivateRequestService {
    UserRepository userRepository;
    ParticipationRequestRepository requestRepository;
    EventRepository eventRepository;

    @Transactional(readOnly = true)
    @Override
    public List<ParticipationRequestDto> getUserRequestsForEvents(Long userId) {
        return requestRepository.findAllByRequesterId(userId)
                .stream()
                .map(ParticipationRequestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public ParticipationRequestDto postNewRequestForEvent(Long userId, Long eventId) {
        User user = userRepository.getUserById(userId);
        Event event = eventRepository.getEventById(eventId);
        validateUserIsNotInitiator(userId, event);
        validateRepeatedRequest(userId, event);
        validateEventIsPublished(event);
        validateParticipantLimit(event);
        ParticipationRequest request = getNewParticipationRequest(user, event);
        if (event.getParticipantLimit() == 0 || Boolean.FALSE.equals(event.getRequestModeration())) {
            request.setStatus(ParticipationRequestStatus.CONFIRMED);
        }
        return ParticipationRequestMapper.toParticipationRequestDto(requestRepository.save(request));
    }

    @Transactional
    @Override
    public ParticipationRequestDto cancelRequestForEvent(Long userId, Long requestId) {
        ParticipationRequest request = requestRepository.findByIdAndRequesterId(requestId, userId).orElseThrow(
                () -> new ArgumentNotFoundException("The specified participation request id=" + requestId +
                        " of user id=" + userId + " does not exist", new NullPointerException()));
        if (request.getStatus().equals(ParticipationRequestStatus.CANCELED)) {
            throw new WrongStateArgumentException("The specified participation request id=" + requestId +
                    " of user id=" + userId + " has already been canceled", new IllegalArgumentException());
        } else {
            requestRepository.cancel(requestId, userId);
        }
        return ParticipationRequestMapper.toParticipationRequestDto(requestRepository.getReferenceById(requestId));
    }

    private void validateUserIsNotInitiator(Long userId, Event event) {
        if (event.getInitiator().getId().equals(userId)) {
            throw new ValidationException("The specified user id=" + userId +
                    " is the initiator of the event id=" + event.getId(),
                    new IllegalArgumentException());
        }
    }

    private void validateRepeatedRequest(Long userId, Event event) {
        event.getRequests()
                .stream()
                .filter(request -> request.getRequester().getId().equals(userId))
                .findAny()
                .ifPresent(request -> {
                    throw new ValidationException("The specified user id=" + userId +
                " has already created participation request for event id" + event.getId(),
                new IllegalArgumentException());
                });
    }

    private void validateEventIsPublished(Event event) {
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new ValidationException("Event has not published", new IllegalArgumentException());
        }
    }

    private void validateParticipantLimit(Event event) {
        Long participantLimit = event.getParticipantLimit();
        if (participantLimit > 0) {
            Long confirmedRequests = requestRepository.countByEventIdAndStatus(event.getId(), ParticipationRequestStatus.CONFIRMED);
            if (participantLimit.equals(confirmedRequests)) {
                throw new ValidationException("Participant limit has already reached the maximum value: " + participantLimit,
                        new IllegalArgumentException());
            }
        }
    }

    private ParticipationRequest getNewParticipationRequest(User user, Event event) {
        ParticipationRequest request = new ParticipationRequest();
        request.setEvent(event);
        request.setRequester(user);
        request.setStatus(ParticipationRequestStatus.PENDING);
        return request;
    }
}
