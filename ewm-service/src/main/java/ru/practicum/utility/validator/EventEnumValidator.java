package ru.practicum.utility.validator;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.EnumUtils;
import ru.practicum.dto.event.UpdateEventRequest;
import ru.practicum.exceptions.ValidationException;
import ru.practicum.exceptions.WrongStateArgumentException;
import ru.practicum.models.event.Event;
import ru.practicum.models.event.EventSort;
import ru.practicum.models.event.EventState;
import ru.practicum.models.event.EventStateAction;

import java.time.LocalDateTime;

@UtilityClass
public class EventEnumValidator {

    public EventState validateAndSetEventState(UpdateEventRequest updateEventRequest, Event event, boolean isAdmin) {
        String stateAction = updateEventRequest.getStateAction();
        if (stateAction == null) {
            return event.getState();
        }
        validateStateAction(stateAction);
        EventStateAction eventStateAction = EventStateAction.valueOf(stateAction);
        if (isAdmin) {
            return setEventStateByAdmin(eventStateAction, event);
        } else {
            return setEventStateByUser(eventStateAction, event);
        }
    }

    private EventState setEventStateByAdmin(EventStateAction eventStateAction, Event event) {
        switch (eventStateAction) {
            case PUBLISH_EVENT:
                if (event.getState().equals(EventState.PENDING)) {
                    event.setPublishedOn(LocalDateTime.now());
                    return EventState.PUBLISHED;
                } else {
                    throw new ValidationException(
                            "The event id=" + event.getId() + " cannot be changed, event state is not pending",
                            new IllegalArgumentException());
                }
            case REJECT_EVENT:
                if (!event.getState().equals(EventState.PUBLISHED)) {
                    return EventState.CANCELED;
                } else {
                    throw new ValidationException(
                            "The event id=" + event.getId() + " has already been published and cannot be changed",
                            new IllegalArgumentException());
                }
            default:
                throw new ValidationException(
                        "The event state action " + eventStateAction + " not available for admin",
                        new IllegalArgumentException());
        }
    }

    private EventState setEventStateByUser(EventStateAction eventStateAction, Event event) {
        switch (eventStateAction) {
            case SEND_TO_REVIEW:
                if (!event.getState().equals(EventState.PUBLISHED)) {
                    return EventState.PENDING;
                } else {
                    throw new ValidationException(
                            "The event id=" + event.getId() + " has already been published and cannot be changed",
                            new IllegalArgumentException());
                }
            case CANCEL_REVIEW:
                if (!event.getState().equals(EventState.PUBLISHED)) {
                    return EventState.CANCELED;
                } else {
                    throw new ValidationException(
                            "The event id=" + event.getId() + " has already been published and cannot be changed",
                            new IllegalArgumentException());
                }
            default:
                throw new ValidationException(
                        "The event state action " + eventStateAction + " not available for users",
                        new IllegalArgumentException());
        }
    }

    public EventState validateState(String state) {
        if (!EnumUtils.isValidEnum(EventState.class, state)) {
            throw new WrongStateArgumentException(
                    "The event state " + state + " does not exist",
                    new IllegalArgumentException());
        } else {
            return EventState.valueOf(state);
        }
    }

    public EventSort validateEventSort(String sort) {
        if (!EnumUtils.isValidEnum(EventSort.class, sort)) {
            throw new WrongStateArgumentException(
                    "The event sort " + sort + " does not exist",
                    new IllegalArgumentException());
        } else {
            return EventSort.valueOf(sort);
        }
    }

    private void validateStateAction(String stateAction) {
        if (!EnumUtils.isValidEnum(EventStateAction.class, stateAction)) {
            throw new WrongStateArgumentException(
                    "The event state action " + stateAction + " does not exist",
                    new IllegalArgumentException());
        }
    }
}
