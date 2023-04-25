package ru.practicum.main_service.compilation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main_service.category.mapper.CategoryMapper;
import ru.practicum.main_service.compilation.dto.CompilationDto;
import ru.practicum.main_service.compilation.dto.NewCompilationDto;
import ru.practicum.main_service.compilation.dto.UpdateCompilationRequestDto;
import ru.practicum.main_service.compilation.exception.CompilationNotFoundException;
import ru.practicum.main_service.compilation.mapper.CompilationMapper;
import ru.practicum.main_service.compilation.model.Compilation;
import ru.practicum.main_service.compilation.repository.CompilationRepository;
import ru.practicum.main_service.event.dto.EventShortDto;
import ru.practicum.main_service.event.mapper.EventMapper;
import ru.practicum.main_service.event.model.Event;
import ru.practicum.main_service.event.repository.EventRepository;
import ru.practicum.main_service.exception.ConflictException;
import ru.practicum.main_service.user.mapper.UserMapper;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CompilationsServiceImpl implements CompilationsService {
    private final EventRepository eventRepository;
    private final CompilationRepository compilationRepository;

    @Override
    public List<CompilationDto> getCompilations(Boolean pinned, PageRequest pageRequest) {
        List<Compilation> compilations = compilationRepository.findByPinned(pinned, pageRequest);

        log.debug(
            "Get compilations. Pinned: {}, page request: {}, compilations: {}", pinned, pageRequest, compilations);

        return compilations.stream().map(compilation ->
                CompilationMapper.toDto(
                    compilation,
                    parseEventsToDto(compilation.getEvents())
                ))
            .collect(Collectors.toList());
    }

    @Override
    public CompilationDto getCompilationById(Long compilationId) {
        Compilation compilation = checkAndReturnCompilation(compilationId);

        return CompilationMapper.toDto(compilation, parseEventsToDto(compilation.getEvents()));
    }

    @Override
    @Transactional
    public CompilationDto createCompilation(NewCompilationDto newCompilationDto) {
        if (Objects.isNull(newCompilationDto)) {
            throw new ConflictException("Request body is empty");
        }

        Set<Event> events = eventRepository.getByIdIn(newCompilationDto.getEvents());
        Compilation compilation = CompilationMapper.toEntity(newCompilationDto, events);

        log.debug(
            "Create compilation. NewCompilationDto: {}, compilation: {}, events: {}", newCompilationDto, compilation,
            events
        );

        return CompilationMapper.toDto(compilationRepository.save(compilation), parseEventsToDto(events));
    }

    @Override
    @Transactional
    public void deleteCompilation(Long compilationId) {
        log.debug("Delete compilation. Compilation id: {}", compilationId);

        compilationRepository.delete(checkAndReturnCompilation(compilationId));
    }

    @Override
    @Transactional
    public CompilationDto updateCompilation(
        Long compilationId, UpdateCompilationRequestDto updateCompilation
    ) {
        log.debug(
            "Update compilation. Compilation id: {}, updateCompilationRequestDto: {}", compilationId,
            updateCompilation
        );

        Compilation compilation = checkAndReturnCompilation(compilationId);

        Optional.ofNullable(updateCompilation.getTitle()).ifPresent(compilation::setTitle);
        Optional.ofNullable(updateCompilation.getPinned()).ifPresent(compilation::setPinned);
        Optional.ofNullable(updateCompilation.getEvents())
            .ifPresent(eventIds -> compilation.setEvents(eventRepository.getByIdIn(eventIds)));

        log.debug("Update compilation. Updated compilation: {}", compilation);

        return CompilationMapper.toDto(compilationRepository.save(compilation), null);
    }

    private Compilation checkAndReturnCompilation(Long compilationId) {
        log.debug("Compilation find by id. Compilation id: {}", compilationId);

        return compilationRepository.findById(compilationId)
            .orElseThrow(() -> new CompilationNotFoundException(compilationId));
    }

    private List<EventShortDto> parseEventsToDto(Set<Event> events) {
        return events.stream().map(event ->
            EventMapper.toShortDto(
                event,
                CategoryMapper.toDto(event.getCategory()),
                UserMapper.toShortDto(event.getInitiator())
            )).collect(Collectors.toList());
    }
}
