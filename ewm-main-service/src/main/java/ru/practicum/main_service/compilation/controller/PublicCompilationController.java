package ru.practicum.main_service.compilation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main_service.compilation.dto.CompilationDto;
import ru.practicum.main_service.compilation.service.CompilationsService;
import ru.practicum.stats_client.StatsClient;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

import static ru.practicum.main_service.util.EndpointHitCreator.makePublicEndpointHit;

@Validated
@RestController
@RequestMapping("/compilations")
@RequiredArgsConstructor
public class PublicCompilationController {
    private final CompilationsService compilationsService;
    private final StatsClient statsClient;

    @GetMapping
    public List<CompilationDto> getCompilations(
        @RequestParam(required = false, defaultValue = "false") Boolean pinned,
        @RequestParam(defaultValue = "0", required = false) @PositiveOrZero Integer from,
        @RequestParam(defaultValue = "10", required = false) @Positive Integer size,
        HttpServletRequest request
    ) {
        makePublicEndpointHit(statsClient, request);

        return compilationsService.getCompilations(pinned, PageRequest.of(from, size));
    }

    @GetMapping("/{id}")
    public CompilationDto getCompilationById(
        @PathVariable Long id, HttpServletRequest request
    ) {
        makePublicEndpointHit(statsClient, request);

        return compilationsService.getCompilationById(id);
    }

}
