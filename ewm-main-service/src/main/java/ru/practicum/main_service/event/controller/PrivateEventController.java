package ru.practicum.main_service.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main_service.event.constant.EventStateUserAction;
import ru.practicum.main_service.event.dto.EventFullDto;
import ru.practicum.main_service.event.dto.EventShortDto;
import ru.practicum.main_service.event.dto.NewEventDto;
import ru.practicum.main_service.event.dto.UpdateEventRequest;
import ru.practicum.main_service.event.service.EventService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Validated
@RestController
@RequestMapping("/users/{userId}/events")
@RequiredArgsConstructor
public class PrivateEventController {

    private final EventService eventService;

    @GetMapping
    public List<EventShortDto> getUserEvents(
        @PathVariable Long userId,
        @RequestParam(defaultValue = "0", required = false) @PositiveOrZero Integer from,
        @RequestParam(defaultValue = "10", required = false) @Positive Integer size
    ) {
        return eventService.getUserEvents(userId, PageRequest.of(from, size));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto createEvent(
        @PathVariable Long userId,
        @RequestBody @Validated NewEventDto newEventDto
    ) {
        return eventService.createEvent(userId, newEventDto);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getUserEventById(
        @PathVariable Long userId,
        @PathVariable Long eventId
    ) {
        return eventService.getUserEventById(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateUserEvent(
        @PathVariable Long userId,
        @PathVariable Long eventId,
        @RequestBody UpdateEventRequest<EventStateUserAction> updateEventRequest
    ) {
        return eventService.updateUserEvent(userId, eventId, updateEventRequest);
    }

}