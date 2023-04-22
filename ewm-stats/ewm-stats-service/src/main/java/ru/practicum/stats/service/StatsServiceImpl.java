package ru.practicum.stats.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.stats.mapper.EndpointHitMapper;
import ru.practicum.stats.model.Stat;
import ru.practicum.stats.repository.StatsRepository;
import ru.practicum.stats_dto.EndpointHitRequestDto;
import ru.practicum.stats_dto.EndpointHitResponseDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StatsServiceImpl implements StatsService {
    private final StatsRepository statsRepository;


    /**
     * Создание точки в статистике
     *
     * @param endpointHitRequestDto тело запроса
     */
    @Override
    @Transactional
    public void createEndpointHit(EndpointHitRequestDto endpointHitRequestDto) {
        statsRepository.save(EndpointHitMapper.toEntity(endpointHitRequestDto));
    }

    /**
     * Получение статистики по посещениям
     *
     * @param start  - дата и время начала диапазона за который нужно выгрузить статистику
     * @param end    - дата и время конца диапазона за который нужно выгрузить статистику
     * @param uris   - список uri для которых нужно выгрузить статистику
     * @param unique - нужно ли учитывать только уникальные ip
     * @return список статистики по посещениям
     */
    @Override
    public List<EndpointHitResponseDto> getEndpointHits(
        LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique
    ) {
        List<Stat> stats;

        if (Boolean.TRUE.equals(unique)) {
            stats = statsRepository.findAllByDateBetweenUniqueIpIn(start, end, uris);
        } else {
            stats = statsRepository.findAllByDateBetweenIpIn(start, end, uris);
        }

        return stats.stream().map(EndpointHitMapper::toDto).collect(Collectors.toList());
    }
}
