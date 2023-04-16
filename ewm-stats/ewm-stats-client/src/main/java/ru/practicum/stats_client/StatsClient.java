package ru.practicum.stats_client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.stats_dto.EndpointHitRequestDto;

@Service
public class StatsClient extends BaseClient {
    private static final String API_PREFIX = "/hit";

    @Autowired
    public StatsClient(@Value("${stats-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
            builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build()
        );
    }


    public ResponseEntity<Object> createEndpointHit(EndpointHitRequestDto endpointHitRequestDto) {
        return post("", endpointHitRequestDto);
    }
}
