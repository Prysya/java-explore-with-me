package ru.practicum.main_service.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main_service.event.constant.EventSort;
import ru.practicum.main_service.event.dto.EventFullDto;
import ru.practicum.main_service.event.dto.EventShortDto;
import ru.practicum.main_service.event.service.EventService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class PublicEventController {
    private final EventService eventService;

    @GetMapping
    public List<EventShortDto> getPublicEvents(
        @RequestParam(required = false) String text,
        @RequestParam(required = false) List<Long> categories,
        @RequestParam(required = false) Boolean paid,
        @RequestParam(required = false, defaultValue = "#{T(java.time.LocalDateTime).now()}")
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
        @RequestParam(required = false, defaultValue = "#{T(java.time.LocalDateTime).now().plusYears(50)}")
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
        @RequestParam(required = false, defaultValue = "false") Boolean onlyAvailable,
        @RequestParam(required = false) EventSort sort,
        @RequestParam(defaultValue = "0", required = false) Integer from,
        @RequestParam(defaultValue = "10", required = false) Integer size,
        HttpServletRequest request
    ) {

        return eventService.getPublicEvents(
            text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size, request);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getPublicEventById(
        @PathVariable Long eventId, HttpServletRequest request
    ) {
        return eventService.getPublicEventById(eventId, request);
    }
}
