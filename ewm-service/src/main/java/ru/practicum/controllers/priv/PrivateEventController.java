package ru.practicum.controllers.priv;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.evaluation.EvaluationDto;
import ru.practicum.dto.event.*;
import ru.practicum.dto.partrequest.EventRequestStatusUpdateRequest;
import ru.practicum.dto.partrequest.EventRequestStatusUpdateResult;
import ru.practicum.dto.partrequest.ParticipationRequestDto;
import ru.practicum.services.priv.api.PrivateEventService;

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
                                             @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                             @Positive @RequestParam(defaultValue = "10") Integer size,
                                             @RequestParam(required = false) Boolean asc) {
        return privateEventService.getUserEvents(userId, from, size, asc);
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

    @PostMapping(value = "/{eventId}/evaluation")
    @ResponseStatus(value = HttpStatus.CREATED)
    public EvaluationDto postEvaluation(@Positive @NotNull @PathVariable Long userId,
                                        @Positive @NotNull @PathVariable Long eventId,
                                        @RequestParam(defaultValue = "true") Boolean isLike) {
        return privateEventService.postEvaluation(userId, eventId, isLike);
    }

    @PutMapping(value = "/{eventId}/evaluation")
    @ResponseStatus(value = HttpStatus.OK)
    public EvaluationDto putEvaluation(@Positive @NotNull @PathVariable Long userId,
                              @Positive @NotNull @PathVariable Long eventId,
                              @RequestParam @NotNull Boolean isLike) {
        return privateEventService.putEvaluation(userId, eventId, isLike);
    }

    @DeleteMapping(value = "/{eventId}/evaluation")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteEvaluation(@Positive @NotNull @PathVariable Long userId,
                                 @Positive @NotNull @PathVariable Long eventId) {
        privateEventService.deleteEvaluation(userId, eventId);
    }
}
