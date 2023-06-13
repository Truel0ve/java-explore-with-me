package ru.practicum.controllers.private_controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.event_dto.*;
import ru.practicum.dto.participation_request_dto.EventRequestStatusUpdateRequest;
import ru.practicum.dto.participation_request_dto.EventRequestStatusUpdateResult;
import ru.practicum.dto.participation_request_dto.ParticipationRequestDto;
import ru.practicum.services.private_service.api.PrivateEventService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users/{userId}/events")
@Validated
public class PrivateEventController {
    private final PrivateEventService privateEventService;

    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    public List<EventShortDto> getUserEvents(@Positive @NotNull @PathVariable Long userId,
                                             @PositiveOrZero @RequestParam(defaultValue = "0", required = false) Integer from,
                                             @Positive @RequestParam(defaultValue = "10", required = false) Integer size) {
        return privateEventService.getUserEvents(userId, from, size);
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public EventFullDto postNewEvent(@Positive @NotNull @PathVariable Long userId,
                                     @Valid @RequestBody EventDto event) {
        return privateEventService.postNewEvent(userId, event);
    }

    @GetMapping(value = "/{eventId}")
    @ResponseStatus(value = HttpStatus.OK)
    public EventFullDto getUserEventById(@Positive @NotNull @PathVariable Long userId,
                                         @Positive @NotNull @PathVariable Long eventId) {
        return privateEventService.getUserEventById(userId, eventId);
    }

    @PatchMapping(value = "/{eventId}")
    @ResponseStatus(value = HttpStatus.OK)
    public EventFullDto patchUserEvent(@Positive @NotNull @PathVariable Long userId,
                                       @Positive @NotNull @PathVariable Long eventId,
                                       @Valid @RequestBody UpdateEventRequest event) {
        return privateEventService.patchEventByInitiator(userId, eventId, event);
    }

    @GetMapping(value = "/{eventId}/requests")
    @ResponseStatus(value = HttpStatus.OK)
    public List<ParticipationRequestDto> getEventRequests(@Positive @NotNull @PathVariable Long userId,
                                                          @Positive @NotNull @PathVariable Long eventId) {
        return privateEventService.getEventRequests(userId, eventId);
    }

    @PatchMapping(value = "/{eventId}/requests")
    @ResponseStatus(value = HttpStatus.OK)
    public EventRequestStatusUpdateResult patchEventRequests(@Positive @NotNull @PathVariable Long userId,
                                                             @Positive @NotNull @PathVariable Long eventId,
                                                             @RequestBody EventRequestStatusUpdateRequest updateRequest) {
        return privateEventService.patchEventRequests(userId, eventId, updateRequest);
    }
}
