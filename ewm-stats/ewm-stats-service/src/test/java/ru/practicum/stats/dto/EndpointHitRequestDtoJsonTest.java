package ru.practicum.stats.dto;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.stats_dto.EndpointHitRequestDto;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class EndpointHitRequestDtoJsonTest {
    @Autowired
    private JacksonTester<EndpointHitRequestDto> json;

    @Test
    @SneakyThrows
    void testEndpointHitRequestDto() {
        EndpointHitRequestDto endpointHitRequestDto = EndpointHitRequestDto.builder()
            .timestamp(LocalDateTime.now())
            .uri("uri")
            .ip("ip")
            .app("app")
            .build();

        JsonContent<EndpointHitRequestDto> result = json.write(endpointHitRequestDto);

        assertThat(result).extractingJsonPathStringValue("$.timestamp")
            .matches("^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}$");
        assertThat(result).extractingJsonPathStringValue("$.uri")
                .isEqualTo(endpointHitRequestDto.getUri());
        assertThat(result).extractingJsonPathStringValue("$.ip")
            .isEqualTo(endpointHitRequestDto.getIp());
        assertThat(result).extractingJsonPathStringValue("$.app")
            .isEqualTo(endpointHitRequestDto.getApp());
    }
}
