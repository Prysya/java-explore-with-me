package ru.practicum.stats.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Stat {
    private String app;
    private String uri;
    private Long hits;

    public Stat(String app, String uri, Long hits) {
        this.app = app;
        this.uri = uri;
        this.hits = hits;
    }
}
