package ru.practicum.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ViewStats {
    String app;
    String uri;
    Long hits;

    public ViewStats() {
    }

    public ViewStats(String app, String uri, Long hits) {
        this.app = app;
        this.uri = uri;
        this.hits = hits;
    }
}
