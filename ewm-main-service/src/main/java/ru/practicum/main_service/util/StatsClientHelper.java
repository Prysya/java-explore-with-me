package ru.practicum.main_service.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.experimental.UtilityClass;
import org.springframework.http.ResponseEntity;
import ru.practicum.stats_client.StatsClient;
import ru.practicum.stats_dto.EndpointHitRequestDto;
import ru.practicum.stats_dto.EndpointHitResponseDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@UtilityClass
public class StatsClientHelper {
    private static final Gson gson = new Gson();

    public static void makePublicEndpointHit(StatsClient statsClient, HttpServletRequest request) {
        statsClient.createEndpointHit(EndpointHitRequestDto.builder()
            .app("ewm-main-service")
            .timestamp(LocalDateTime.now())
            .uri(request.getRequestURI())
            .ip(request.getRemoteAddr())
            .build());
    }

    public static Map<String, Long> getViews(
        StatsClient statsClient, String start, String end, List<String> uris, Boolean unique
    ) {
        ResponseEntity<Object> responseEntity = statsClient.getEndpointStats(start, end, uris, unique);


        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            List<EndpointHitResponseDto> stats =
                gson.fromJson(
                    gson.toJson(responseEntity.getBody()),
                    new TypeToken<List<EndpointHitResponseDto>>() {
                    }.getType()
                );

            return stats.stream().collect(Collectors.toMap(
                EndpointHitResponseDto::getUri,
                EndpointHitResponseDto::getHits
            ));
        }

        return new HashMap<>();
    }
}
