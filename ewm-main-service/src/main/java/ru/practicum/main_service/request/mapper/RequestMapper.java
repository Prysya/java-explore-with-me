package ru.practicum.main_service.request.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.main_service.request.dto.ParticipationRequestDto;
import ru.practicum.main_service.request.model.Request;

@UtilityClass
public class RequestMapper {
    public static ParticipationRequestDto toDto(Request request) {
        return ParticipationRequestDto.builder()
            .id(request.getId())
            .created(request.getCreated())
            .requester(request.getRequester().getId())
            .status(request.getStatus())
            .event(request.getEvent().getId())
            .build();
    }
}
