package ru.practicum.main_service.compilation.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.main_service.compilation.dto.CompilationDto;
import ru.practicum.main_service.compilation.dto.NewCompilationDto;
import ru.practicum.main_service.compilation.model.Compilation;
import ru.practicum.main_service.event.dto.EventShortDto;
import ru.practicum.main_service.event.model.Event;

import java.util.List;
import java.util.Set;

@UtilityClass
public class CompilationMapper {
    public static Compilation toEntity(NewCompilationDto newCompilationDto, Set<Event> events) {
        return Compilation.builder()
            .events(events)
            .title(newCompilationDto.getTitle())
            .pinned(newCompilationDto.getPinned())
            .build();
    }

    public static CompilationDto toDto(Compilation compilation, List<EventShortDto> events) {
        return CompilationDto.builder()
            .id(compilation.getId())
            .pinned(compilation.getPinned())
            .title(compilation.getTitle())
            .events(events)
            .build();
    }
}
