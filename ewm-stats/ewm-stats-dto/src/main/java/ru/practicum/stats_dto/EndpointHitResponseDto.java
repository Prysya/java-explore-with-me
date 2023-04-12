package ru.practicum.stats_dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.io.Serializable;

@Value
@Builder
@AllArgsConstructor
public class EndpointHitResponseDto implements Serializable {
    String app;
    String uri;
    Long hits;
}
