package ru.practicum.utility;

import lombok.experimental.UtilityClass;
import ru.practicum.ViewStatsDto;
import ru.practicum.model.ViewStats;

@UtilityClass
public class ViewStatsMapper {

    public ViewStatsDto toDto(ViewStats viewStats) {
        ViewStatsDto dto = new ViewStatsDto();
        dto.setApp(viewStats.getApp());
        dto.setUri(viewStats.getUri());
        dto.setHits(viewStats.getHits());
        return dto;
    }
}
