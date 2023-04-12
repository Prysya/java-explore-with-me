package ru.practicum.stats.service;


import ru.practicum.stats.model.EndpointHit;
import ru.practicum.stats_dto.EndpointHitRequestDto;
import ru.practicum.stats_dto.EndpointHitResponseDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {
    void createEndpointHit(EndpointHitRequestDto endpointHitRequestDto);

    List<EndpointHitResponseDto> getEndpointHits(
        LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique
    );

    List<EndpointHit> debug();
}
