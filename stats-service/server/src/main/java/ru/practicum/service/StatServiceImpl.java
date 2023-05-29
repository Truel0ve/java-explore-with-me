package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.EndpointHitDto;
import ru.practicum.ViewStatsDto;
import ru.practicum.model.ViewStats;
import ru.practicum.repository.StatRepository;
import ru.practicum.utility.EndpointHitMapper;
import ru.practicum.utility.TimestampParser;
import ru.practicum.utility.ViewStatsMapper;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StatServiceImpl implements StatService {
    private final StatRepository statRepository;

    @Transactional
    @Override
    public void postEndpointHit(EndpointHitDto endpointHitDto) {
        statRepository.save(EndpointHitMapper.toEndpointHit(endpointHitDto));
    }

    @Override
    public List<ViewStatsDto> getViewStats(String start, String end, String[] uris, boolean unique) {
        if (unique) {
            return mapViewStatsList(statRepository.getViewStatsWithUrisAndIp(
                    TimestampParser.toTimestamp(start),
                    TimestampParser.toTimestamp(end),
                    getUrisList(uris)));
        } else {
            return mapViewStatsList(statRepository.getViewStatsWithUris(
                    TimestampParser.toTimestamp(start),
                    TimestampParser.toTimestamp(end),
                    getUrisList(uris)));
        }
    }

    private List<ViewStatsDto> mapViewStatsList(List<ViewStats> viewStats) {
        return viewStats.stream()
                .map(ViewStatsMapper::toDto)
                .collect(Collectors.toList());
    }

    @SuppressWarnings("all")
    private List<String> getUrisList(String[] uris) {
        if (uris == null || uris.length == 0) {
            return null;
        } else {
            return Arrays.asList(uris);
        }
    }
}
