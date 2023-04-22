package ru.practicum.main_service.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main_service.event.constant.EventState;
import ru.practicum.main_service.event.constant.EventStateAdminAction;
import ru.practicum.main_service.event.dto.EventFullDto;
import ru.practicum.main_service.event.dto.UpdateEventRequest;
import ru.practicum.main_service.event.service.EventService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/admin/events")
@RequiredArgsConstructor
public class AdminEventController {

    private final EventService eventService;

    @GetMapping
    public List<EventFullDto> getEventsByAdmin(
        @RequestParam(required = false) List<Long> users,
        @RequestParam(required = false) List<EventState> states,
        @RequestParam(required = false) List<Long> categories,
        @RequestParam(required = false, defaultValue = "#{T(java.time.LocalDateTime).now()}")
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
        @RequestParam(required = false, defaultValue = "#{T(java.time.LocalDateTime).now().plusYears(50)}")
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
        @RequestParam(defaultValue = "0", required = false) Integer from,
        @RequestParam(defaultValue = "10", required = false) Integer size
    ) {
        return eventService.getEventsByAdmin(
            users, states, categories, rangeStart, rangeEnd, PageRequest.of(from, size));
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEventByAdmin(
        @PathVariable Long eventId,
        @RequestBody UpdateEventRequest<EventStateAdminAction> updateEventRequest
    ) {
        return eventService.updateEventByAdmin(eventId, updateEventRequest);
    }

}
