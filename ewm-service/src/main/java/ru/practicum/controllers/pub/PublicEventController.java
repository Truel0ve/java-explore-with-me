package ru.practicum.controllers.pub;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.services.pub.api.PublicEventService;
import ru.practicum.utility.StatsManager;
import ru.practicum.utility.mapper.EndpointMapper;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.*;
import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/events")
@Validated
public class PublicEventController {
    private final PublicEventService publicEventService;
    private final StatsManager statsManager;

    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    public List<EventShortDto> getEvents(@RequestParam(required = false) @Size(min = 1, max = 7000) String text,
                                         @RequestParam(required = false) Set<@Positive Long> categories,
                                         @RequestParam(required = false) Boolean paid,
                                         @RequestParam(required = false)
                                         @Pattern(regexp = "^[0-9]{4}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1]) (2[0-3]|[01][0-9]):[0-5][0-9]:[0-5][0-9]$")
                                         String rangeStart,
                                         @RequestParam(required = false)
                                         @Pattern(regexp = "^[0-9]{4}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1]) (2[0-3]|[01][0-9]):[0-5][0-9]:[0-5][0-9]$")
                                         String rangeEnd,
                                         @RequestParam(defaultValue = "false", required = false) Boolean onlyAvailable,
                                         @RequestParam(defaultValue = "EVENT_DATE", required = false) String sort,
                                         @RequestParam(defaultValue = "0", required = false) @PositiveOrZero Integer from,
                                         @RequestParam(defaultValue = "10", required = false) @Positive Integer size,
                                         HttpServletRequest request) {
        List<EventShortDto> events = publicEventService.getEvents(text, categories, paid, rangeStart, rangeEnd,
                onlyAvailable, sort, from, size);
        statsManager.postHit(EndpointMapper.toEndpointHitDto(request));
        return events;
    }

    @GetMapping(value = "/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public EventFullDto getEventById(@Positive @NotNull @PathVariable Long id,
                                     HttpServletRequest request) {
        EventFullDto event = publicEventService.getEventById(id);
        statsManager.postHit(EndpointMapper.toEndpointHitDto(request));
        return event;
    }
}
