package ru.practicum.stats.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.stats.service.StatsService;
import ru.practicum.stats_dto.EndpointHitRequestDto;
import ru.practicum.stats_dto.EndpointHitResponseDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StatsControllerTest {

    @InjectMocks
    private StatsController statsController;

    @Mock
    private StatsService statsService;

    private final EndpointHitResponseDto endpointHitResponseDto = EndpointHitResponseDto.builder().build();
    private final EndpointHitRequestDto endpointHitRequestDto = EndpointHitRequestDto.builder().build();


    @Test
    void createEndpointHit_whenInvoked_thenReturnedWithoutError() {
        statsController.createEndpointHit(endpointHitRequestDto);

        verify(statsService).createEndpointHit(endpointHitRequestDto);
    }

    @Test
    void getEndpointHits_whenInvoked_thenReturnedListOfEndpointHitResponseDto() {
        when(statsService.getEndpointHits(
            any(LocalDateTime.class),
            any(LocalDateTime.class),
            anyList(),
            anyBoolean()
            )
        )
            .thenReturn(List.of(endpointHitResponseDto));

        List<EndpointHitResponseDto> endpointHits =
            statsController.getEndpointHits(LocalDateTime.now(), LocalDateTime.now(), new ArrayList<>(), false);

        assertEquals(List.of(endpointHitResponseDto), endpointHits);
    }
}