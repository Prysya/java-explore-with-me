package ru.practicum.main_service.event.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.main_service.category.dto.CategoryDto;
import ru.practicum.main_service.category.model.Category;
import ru.practicum.main_service.event.constant.EventState;
import ru.practicum.main_service.event.dto.EventFullDto;
import ru.practicum.main_service.event.dto.EventShortDto;
import ru.practicum.main_service.event.dto.NewEventDto;
import ru.practicum.main_service.event.model.Event;
import ru.practicum.main_service.location.dto.LocationDto;
import ru.practicum.main_service.location.model.Location;
import ru.practicum.main_service.user.dto.UserShortDto;
import ru.practicum.main_service.user.model.User;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

@UtilityClass
public class EventMapper {
    public static Event toEntity(NewEventDto newEventDto, Category category, Location location, User initiator) {
        return Event.builder()
            .annotation(newEventDto.getAnnotation())
            .category(category)
            .description(newEventDto.getDescription())
            .eventDate(newEventDto.getEventDate())
            .location(location)
            .paid(newEventDto.getPaid())
            .participantLimit(newEventDto.getParticipantLimit())
            .requestModeration(newEventDto.getRequestModeration())
            .title(newEventDto.getTitle())
            .createdOn(LocalDateTime.now())
            .initiator(initiator)
            .state(EventState.PENDING)
            .build();
    }

    public static EventShortDto toShortDto(Event event, CategoryDto category, UserShortDto initiator) {
        return EventShortDto.builder()
            .id(event.getId())
            .annotation(event.getAnnotation())
            .category(category)
            .confirmedRequests(Optional.ofNullable(event.getParticipants()).orElse(Set.of()).size())
            .eventDate(event.getEventDate())
            .initiator(initiator)
            .paid(event.getPaid())
            .title(event.getTitle())
            .build();
    }

    public static EventFullDto toDto(Event event, CategoryDto category, UserShortDto initiator, LocationDto location) {
        return EventFullDto.builder()
            .id(event.getId())
            .annotation(event.getAnnotation())
            .category(category)
            .confirmedRequests(Optional.ofNullable(event.getParticipants()).orElse(Set.of()).size())
            .createdOn(event.getCreatedOn())
            .description(event.getDescription())
            .eventDate(event.getEventDate())
            .initiator(initiator)
            .location(location)
            .paid(event.getPaid())
            .participantLimit(event.getParticipantLimit())
            .publishedOn(event.getPublishedOn())
            .requestModeration(event.getRequestModeration())
            .state(event.getState())
            .title(event.getTitle())
            .build();
    }
}
