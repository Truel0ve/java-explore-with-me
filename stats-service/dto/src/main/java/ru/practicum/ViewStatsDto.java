package ru.practicum;

import lombok.Data;

@Data
public class ViewStatsDto {
    String app;
    String uri;
    Long hits;
}
