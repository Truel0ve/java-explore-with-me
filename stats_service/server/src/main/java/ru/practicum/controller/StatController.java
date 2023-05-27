package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.EndpointHitDto;
import ru.practicum.service.StatService;
import ru.practicum.ViewStatsDto;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class StatController {
    private final StatService statService;

    @PostMapping(value = "/hit")
    @ResponseStatus(value = HttpStatus.CREATED)
    public void postEndpointHit(@Valid @RequestBody EndpointHitDto endpointHitDto) {
        statService.postEndpointHit(endpointHitDto);
    }

    @GetMapping(value = "/stats")
    @ResponseStatus(value = HttpStatus.OK)
    public List<ViewStatsDto> getStats(@RequestParam String start,
                                       @RequestParam String end,
                                       @RequestParam(required = false) String[] uris,
                                       @RequestParam(required = false, defaultValue = "false") boolean unique) {
        return statService.getViewStats(start, end, uris, unique);
    }
}
