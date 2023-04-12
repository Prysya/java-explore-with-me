package ru.practicum.stats.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.stats.model.EndpointHit;
import ru.practicum.stats.model.Stat;
import ru.practicum.stats_dto.EndpointHitRequestDto;
import ru.practicum.stats_dto.EndpointHitResponseDto;

@UtilityClass
public class EndpointHitMapper {
    public static EndpointHitResponseDto toDto(Stat stat) {
        return EndpointHitResponseDto.builder()
            .hits(stat.getHits())
            .uri(stat.getUri())
            .app(stat.getApp())
            .build();
    }

    public static EndpointHit toEntity(EndpointHitRequestDto endpointHitRequestDto) {
        return EndpointHit.builder()
            .ip(endpointHitRequestDto.getIp())
            .timestamp(endpointHitRequestDto.getTimestamp())
            .uri(endpointHitRequestDto.getUri())
            .app(endpointHitRequestDto.getApp())
            .build();
    }
}
