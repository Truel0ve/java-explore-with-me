package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.EndpointHitDto;
import ru.practicum.ViewStatsDto;
import ru.practicum.exceptions.WrongStateArgumentException;
import ru.practicum.model.ViewStats;
import ru.practicum.repository.StatRepository;
import ru.practicum.utility.EndpointHitMapper;
import ru.practicum.utility.TimestampParser;
import ru.practicum.utility.ViewStatsMapper;

import java.sql.Timestamp;
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
        Timestamp timestampStart = TimestampParser.toTimestamp(start);
        Timestamp timestampEnd = TimestampParser.toTimestamp(end);
        validateStartAndEnd(timestampStart, timestampEnd);
        if (unique) {
            return mapViewStatsList(statRepository.getViewStatsWithUrisAndIp(timestampStart, timestampEnd, getUrisList(uris)));
        } else {
            return mapViewStatsList(statRepository.getViewStatsWithUris(timestampStart, timestampEnd, getUrisList(uris)));
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

    private void validateStartAndEnd(Timestamp start, Timestamp end) {
        if (start.after(end)) {
            throw new WrongStateArgumentException("The start date cannot be after end date");
        }
    }
}
