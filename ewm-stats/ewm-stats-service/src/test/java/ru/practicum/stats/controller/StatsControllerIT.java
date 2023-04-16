package ru.practicum.stats.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.stats.service.StatsService;
import ru.practicum.stats_dto.EndpointHitRequestDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StatsController.class)
@ExtendWith(MockitoExtension.class)
class StatsControllerIT {
    private static final String STATS_URL = "/stats";
    private static final String HIT_URL = "/hit";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private StatsService statsService;

    private EndpointHitRequestDto validEndpointHit;

    @BeforeEach
    public void beforeEach() {
        String str = "2023-04-14 12:30:20";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        validEndpointHit = EndpointHitRequestDto.builder()
            .app("app")
            .ip("ip")
            .uri("uri")
            .timestamp(
                LocalDateTime.parse(str, formatter)
            )
            .build();
    }

    @Test
    @SneakyThrows
    void createEndpointHit_whenEndpointHitIsValid_thenStatusOk() {
        mockMvc
            .perform(
                post(HIT_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(validEndpointHit))
            )
            .andExpect(status().isCreated());

        verify(statsService).createEndpointHit(validEndpointHit);
    }

    @Test
    @SneakyThrows
    void createEndpointHit_whenEndpointHitIsNotValid_thenStatus() {
        mockMvc
            .perform(
                post(HIT_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(EndpointHitRequestDto.builder().build()))
            )
            .andExpect(status().isBadRequest());

        verify(statsService, never()).createEndpointHit(validEndpointHit);
    }

    @Test
    @SneakyThrows
    void getEndpointHits_whenStartAndEndIsPresentedAndFalseIsDefault_thenReturnedStatusOk() {
        mockMvc
            .perform(
                get(STATS_URL)
                    .param("start", "2022-02-02 22:22:22")
                    .param("end", "2022-02-02 22:22:22")
            )
            .andExpect(status().isOk());

        verify(statsService).getEndpointHits(any(LocalDateTime.class),
            any(LocalDateTime.class), anyList(), eq(false)
        );
    }

    @Test
    @SneakyThrows
    void getEndpointHits_whenStartIsNotPresented_thenReturnedBadRequest() {
        mockMvc
            .perform(
                get(STATS_URL)
                    .param("end", "2022-02-02 22:22:22")
            )
            .andExpect(status().isBadRequest());

        verify(statsService, never()).getEndpointHits(any(LocalDateTime.class),
            any(LocalDateTime.class), anyList(), anyBoolean()
        );
    }

    @Test
    @SneakyThrows
    void getEndpointHits_whenEndIsNotPresented_thenReturnedBadRequest() {
        mockMvc
            .perform(
                get(STATS_URL)
                    .param("start", "2022-02-02 22:22:22")
            )
            .andExpect(status().isBadRequest());

        verify(statsService, never()).getEndpointHits(any(LocalDateTime.class),
            any(LocalDateTime.class), anyList(), anyBoolean()
        );
    }
}