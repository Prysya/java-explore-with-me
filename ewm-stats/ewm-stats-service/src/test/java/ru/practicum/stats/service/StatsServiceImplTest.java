package ru.practicum.stats.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.stats.model.EndpointHit;
import ru.practicum.stats.repository.StatsRepository;
import ru.practicum.stats_dto.EndpointHitRequestDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class StatsServiceImplTest {
    private final EndpointHitRequestDto endpointHitRequestDto = EndpointHitRequestDto.builder().build();
    @Mock
    private StatsRepository statsRepository;
    @InjectMocks
    private StatsServiceImpl statsService;

    @Test
    void createEndpointHit_whenInvoked_thenSaveToRepository() {
        statsService.createEndpointHit(endpointHitRequestDto);

        verify(statsRepository).save(any(EndpointHit.class));
    }

    @Test
    void getEndpointHits_whenUrisIsEmpty_thenFindAllByDateBetweenInvoked() {
        statsService.getEndpointHits(LocalDateTime.now(), LocalDateTime.now(), List.of(), false);

        verify(statsRepository).findAllByDateBetween(any(LocalDateTime.class), any(LocalDateTime.class));
    }

    @Test
    void getEndpointHits_whenUrisIsNotEmpty_thenFindAllByDateBetweenIpInInvoked() {
        statsService.getEndpointHits(LocalDateTime.now(), LocalDateTime.now(), List.of("uri"), false);

        verify(statsRepository).findAllByDateBetweenIpIn(any(LocalDateTime.class), any(LocalDateTime.class), anyList());
    }

    @Test
    void getEndpointHits_whenUrisIsNotEmptyAndUniqueIsTrue_thenFindAllByDateBetweenUniqueIpInInvoked() {
        statsService.getEndpointHits(LocalDateTime.now(), LocalDateTime.now(), List.of("uri"), true);

        verify(statsRepository).findAllByDateBetweenUniqueIpIn(
            any(LocalDateTime.class), any(LocalDateTime.class), anyList());
    }
}