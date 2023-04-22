package ru.practicum.main_service.util;

import lombok.experimental.UtilityClass;
import ru.practicum.stats_client.StatsClient;
import ru.practicum.stats_dto.EndpointHitRequestDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@UtilityClass
public class EndpointHitCreator {
    public static void makePublicEndpointHit(StatsClient statsClient, HttpServletRequest request) {
        Thread thread = new Thread(() -> statsClient.createEndpointHit(EndpointHitRequestDto.builder()
            .app("ewm-main-service")
            .timestamp(LocalDateTime.now())
            .uri(request.getRemoteAddr())
            .ip(request.getRequestURI())
            .build()));

        thread.start();
    }
}
