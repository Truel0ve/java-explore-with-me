package ru.practicum.utility.validator;

import lombok.experimental.UtilityClass;
import ru.practicum.exceptions.ValidationException;
import ru.practicum.models.event.Event;

@UtilityClass
public class EventInitiatorValidator {

    public void validateInitiator(Long userId, Event event) {
        if (!isInitiator(userId, event)) {
            throw new ValidationException(
                    "The user id=" + userId + " is not the initiator of the specified event id=" + event.getId(),
                    new IllegalArgumentException());
        }
    }

    public void validateNotInitiator(Long userId, Event event) {
        if (isInitiator(userId, event)) {
            throw new ValidationException(
                    "The specified user id=" + userId + " is the initiator of the event id=" + event.getId(),
                    new IllegalArgumentException());
        }
    }

    private boolean isInitiator(Long userId, Event event) {
        return event.getInitiator().getId().equals(userId);
    }
}
