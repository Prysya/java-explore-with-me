package ru.practicum.main_service.event.service;

import org.springframework.data.domain.PageRequest;
import ru.practicum.main_service.event.constant.EventSort;
import ru.practicum.main_service.event.constant.EventState;
import ru.practicum.main_service.event.constant.EventStateAdminAction;
import ru.practicum.main_service.event.constant.EventStateUserAction;
import ru.practicum.main_service.event.dto.EventFullDto;
import ru.practicum.main_service.event.dto.EventShortDto;
import ru.practicum.main_service.event.dto.NewEventDto;
import ru.practicum.main_service.event.dto.UpdateEventRequest;

import java.time.LocalDateTime;
import java.util.List;

public interface EventService {
    List<EventShortDto> getUserEvents(Long userId, PageRequest pageRequest);

    EventFullDto createEvent(Long userId, NewEventDto newEventDto);

    EventFullDto getUserEventById(Long userId, Long eventId);

    EventFullDto updateUserEvent(
        Long userId, Long eventId, UpdateEventRequest<EventStateUserAction> updateEventRequest
    );

    List<EventFullDto> getEventsByAdmin(
        List<Long> users, List<EventState> states, List<Long> categories, LocalDateTime rangeStart,
        LocalDateTime rangeEnd,
        PageRequest pageRequest
    );

    EventFullDto updateEventByAdmin(Long eventId, UpdateEventRequest<EventStateAdminAction> updateEventRequest);

    List<EventShortDto> getPublicEvents(
        String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart, LocalDateTime rangeEnd,
        Boolean onlyAvailable, EventSort sort, Integer from, Integer size
    );

    EventFullDto getPublicEventById(Long id);
}
