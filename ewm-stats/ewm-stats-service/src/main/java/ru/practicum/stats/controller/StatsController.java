package ru.practicum.stats.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.stats.model.EndpointHit;
import ru.practicum.stats_dto.EndpointHitRequestDto;
import ru.practicum.stats_dto.EndpointHitResponseDto;
import ru.practicum.stats.service.StatsService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
@Validated
public class StatsController {
    private final StatsService service;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public void createEndpointHit(
        @RequestBody EndpointHitRequestDto endpointHitRequestDto
    ) {
        service.createEndpointHit(endpointHitRequestDto);
    }

    @GetMapping("/stats")
    public List<EndpointHitResponseDto> getEndpointHits(
        @RequestParam
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        @Valid
        LocalDateTime start,
        @RequestParam
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        @Valid
        LocalDateTime end,
        @RequestParam(required = false, defaultValue = "")
        List<String> uris,
        @RequestParam(required = false, defaultValue = "false")
        Boolean unique
    ) {
        return service.getEndpointHits(
            start, end, uris, unique
        );
    }

    @GetMapping
    public List<EndpointHit> getDebug() {
        return service.debug();
    }

}
