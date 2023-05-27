package ru.practicum.service;

import ru.practicum.EndpointHitDto;
import ru.practicum.ViewStatsDto;

import java.util.List;

public interface StatService {

    void postEndpointHit(EndpointHitDto endpointHitDto);

    List<ViewStatsDto> getViewStats(String start,
                                    String end,
                                    String[] uris,
                                    boolean unique);
}
