package ru.practicum.stats_client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.stats_dto.EndpointHitRequestDto;

import java.util.List;
import java.util.Map;

@Service
public class StatsClient extends BaseClient {
    @Autowired
    public StatsClient(@Value("${stats-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
            builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build()
        );
    }


    public ResponseEntity<Object> createEndpointHit(EndpointHitRequestDto endpointHitRequestDto) {
        return post("/hit", endpointHitRequestDto);
    }

    public ResponseEntity<Object> getEndpointStats(String start, String end, List<String> uris, Boolean unique) {
        Map<String, Object> parameters = Map.of(
            "uris", String.join(",", uris),
            "unique", unique,
            "start", start,
            "end", end
        );

        return get("/stats?start={start}&end={end}&uris={uris}&unique={unique}", parameters);
    }
}