package ru.practicum.utility;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.EndpointHitDto;
import ru.practicum.StatClient;
import ru.practicum.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class StatsManager {
    private final StatClient statClient = new StatClient("http://stats-service:9090");

    public void postHit(EndpointHitDto endpointHitDto) {
        statClient.postEndpointHit(endpointHitDto);
    }

    public Long getViewsCount(Long eventId, List<ViewStatsDto> viewStatsList) {
        if (viewStatsList != null && !viewStatsList.isEmpty()) {
            ViewStatsDto viewStats = viewStatsList.stream()
                    .filter(viewStatsDto -> isEventHasViewStats(eventId, viewStatsDto))
                    .findFirst().orElse(null);
            if (viewStats != null) {
                return viewStats.getHits();
            } else {
                return 0L;
            }
        } else {
            return 0L;
        }
    }

    public List<ViewStatsDto> getViewStats(Set<Long> eventIds) {
        return statClient.getViewStats(
                "2023-01-01 00:00:00",
                DateTimeParser.toString(LocalDateTime.now()),
                getUris(eventIds),
                true).getBody();
    }

    private String[] getUris(Set<Long> eventIds) {
        List<String> urisList = new ArrayList<>(eventIds.size());
        String baseUrl = "/events/";
        for (Long eventId : eventIds) {
            urisList.add(baseUrl + eventId);
        }
        return urisList.toArray(new String[0]);
    }

    private boolean isEventHasViewStats(Long eventId, ViewStatsDto viewStats) {
        String[] segments = viewStats.getUri().split("/");
        String idStr = segments[segments.length - 1];
        Long id = Long.parseLong(idStr);
        return eventId.equals(id);
    }
}
