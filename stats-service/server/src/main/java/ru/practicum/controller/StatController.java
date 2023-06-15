package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.EndpointHitDto;
import ru.practicum.service.StatService;
import ru.practicum.ViewStatsDto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
public class StatController {
    private final StatService statService;

    @PostMapping(value = "/hit")
    @ResponseStatus(value = HttpStatus.CREATED)
    public void postEndpointHit(@Valid @RequestBody EndpointHitDto endpointHitDto) {
        statService.postEndpointHit(endpointHitDto);
    }

    @GetMapping(value = "/stats")
    @ResponseStatus(value = HttpStatus.OK)
    public List<ViewStatsDto> getStats(@RequestParam
                                       @Pattern(regexp = "^[0-9]{4}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1]) (2[0-3]|[01][0-9]):[0-5][0-9]:[0-5][0-9]$")
                                       @NotNull
                                       String start,
                                       @RequestParam
                                       @Pattern(regexp = "^[0-9]{4}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1]) (2[0-3]|[01][0-9]):[0-5][0-9]:[0-5][0-9]$")
                                       @NotNull
                                       String end,
                                       @RequestParam(required = false) String[] uris,
                                       @RequestParam(required = false, defaultValue = "false") boolean unique) {
        return statService.getViewStats(start, end, uris, unique);
    }
}
