package ru.practicum.main_service.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main_service.category.mapper.CategoryMapper;
import ru.practicum.main_service.category.model.Category;
import ru.practicum.main_service.category.repository.CategoryRepository;
import ru.practicum.main_service.event.constant.EventSort;
import ru.practicum.main_service.event.constant.EventState;
import ru.practicum.main_service.event.constant.EventStateAdminAction;
import ru.practicum.main_service.event.constant.EventStateUserAction;
import ru.practicum.main_service.event.dto.EventFullDto;
import ru.practicum.main_service.event.dto.EventShortDto;
import ru.practicum.main_service.event.dto.NewEventDto;
import ru.practicum.main_service.event.dto.UpdateEventRequest;
import ru.practicum.main_service.event.exception.*;
import ru.practicum.main_service.event.mapper.EventMapper;
import ru.practicum.main_service.event.model.Event;
import ru.practicum.main_service.event.repository.EventRepository;
import ru.practicum.main_service.location.mapper.LocationMapper;
import ru.practicum.main_service.location.model.Location;
import ru.practicum.main_service.location.repository.LocationRepository;
import ru.practicum.main_service.user.mapper.UserMapper;
import ru.practicum.main_service.user.model.User;
import ru.practicum.main_service.user.repository.UserRepository;
import ru.practicum.stats_client.StatsClient;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.practicum.main_service.category.util.SharedCategoryRequests.checkAndReturnCategory;
import static ru.practicum.main_service.location.util.SharedLocationRequests.findOrCreateLocation;
import static ru.practicum.main_service.user.util.SharedUserRequests.checkAndReturnUser;
import static ru.practicum.main_service.util.StatsClientHelper.getViews;
import static ru.practicum.main_service.util.StatsClientHelper.makePublicEndpointHit;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private static final String EVENTS_PREFIX = "/events/";

    private final LocationRepository locationRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final StatsClient statsClient;


    @Override
    public EventFullDto getPublicEventById(Long eventId, HttpServletRequest request) {
        log.debug("Event Service. Get public events by eventId: {}", eventId);

        makePublicEndpointHit(statsClient, request);

        return parseToFullDtoWithMappers(
            eventRepository.findByIdAndPublished(eventId).orElseThrow(() -> new EventNotFoundException(eventId)));
    }


    @Override
    public List<EventFullDto> getEventsByAdmin(
        List<Long> users, List<EventState> states, List<Long> categories, LocalDateTime rangeStart,
        LocalDateTime rangeEnd, PageRequest pageRequest
    ) {
        log.debug("Event Service. Get events by admin. Users: {}, states: {}, categories: {}, rangeStart: {}," +
            "rangeEnd: {}, pageRequest: {}", users, states, categories, rangeStart, rangeEnd, pageRequest);

        return eventRepository.getEventsForAdmin(users, states, categories, rangeStart, rangeEnd, pageRequest).stream()
            .map(this::parseToFullDtoWithMappers).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EventFullDto updateEventByAdmin(Long eventId, UpdateEventRequest<EventStateAdminAction> updateEventRequest) {
        log.debug("Event Service. Update event by admin. EventId: {}, eventRequest: {}", eventId, updateEventRequest);

        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException(eventId));


        checkEventState(event.getState());
        updateEvent(updateEventRequest, event);

        /* Обновление состояния */
        Optional.ofNullable(updateEventRequest.getStateAction()).ifPresent(state -> {
            switch (state) {
                case PUBLISH_EVENT:
                    if (event.getState() != EventState.PENDING) {
                        throw new EventStatusConflictException();
                    }

                    event.setState(EventState.PUBLISHED);
                    event.setPublishedOn(LocalDateTime.now());
                    break;

                case REJECT_EVENT:
                    event.setState(EventState.CANCELED);
                    break;
                default:
                    throw new EventStateConflictException(state);
            }
        });


        log.debug("Event Service. Updated event by admin: {}", event);

        return parseToFullDtoWithMappers(event);
    }

    @Override
    public List<EventShortDto> getUserEvents(Long userId, PageRequest pageRequest) {
        checkAndReturnUser(userRepository, userId);

        List<Event> events = eventRepository.getByInitiatorIdOrderByEventDateDesc(userId, pageRequest);

        log.debug(
            "Event Service. Get user events. User id: {}, page request: {}, events: {}", userId, pageRequest, events);

        return events.stream().map(this::parseToShortDtoWithMappers).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EventFullDto createEvent(Long userId, NewEventDto newEventDto) {
        log.debug("Event Service. Create event. UserId: {}, NewEventDto: {}", userId, newEventDto);

        checkEventDate(newEventDto.getEventDate());

        User user = checkAndReturnUser(userRepository, userId);
        Location location = findOrCreateLocation(locationRepository, newEventDto.getLocation());
        Category category = checkAndReturnCategory(categoryRepository, newEventDto.getCategory());

        log.debug("Event Service. Create event. Found category: {}", category);

        Event event = EventMapper.toEntity(newEventDto, category, location, user);

        return parseToFullDtoWithMappers(eventRepository.save(event));
    }

    @Override
    public EventFullDto getUserEventById(Long userId, Long eventId) {
        log.debug("Event Service. Get user event By Id. UserId: {}, eventId: {}", userId, eventId);

        return parseToFullDtoWithMappers(eventRepository.findByInitiatorIdAndId(userId, eventId)
            .orElseThrow(() -> new EventNotFoundException(eventId)));
    }

    @Override
    @Transactional
    public EventFullDto updateUserEvent(
        Long userId, Long eventId, UpdateEventRequest<EventStateUserAction> updateEventRequest
    ) {
        log.debug("Event Service. Update user event. UserId: {}, eventId: {}, UpdateEventUserRequest: {}", userId,
            eventId, updateEventRequest
        );

        Event event = eventRepository.findByInitiatorIdAndId(userId, eventId)
            .orElseThrow(() -> new EventNotFoundException(eventId));

        checkEventInitiator(userId, event.getInitiator());
        checkEventState(event.getState());
        updateEvent(updateEventRequest, event);

        /* Обновление состояния */
        Optional.ofNullable(updateEventRequest.getStateAction()).ifPresent(state -> {
            switch (state) {
                case CANCEL_REVIEW:
                    event.setState(EventState.CANCELED);
                    break;
                case SEND_TO_REVIEW:
                    event.setState(EventState.PENDING);
                    event.setPublishedOn(LocalDateTime.now());
                    break;
                default:
                    throw new EventStateConflictException(state);
            }
        });

        log.debug("Event Service. Updated event by user: {}", event);

        return parseToFullDtoWithMappers(event);
    }

    @Override
    public List<EventShortDto> getPublicEvents(
        String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart, LocalDateTime rangeEnd,
        Boolean onlyAvailable, EventSort sort, Integer from, Integer size, HttpServletRequest request
    ) {
        log.debug("Event Service. Get public events. Text: {}, categories: {}, paid: {}, rangeStart: {}," +
                "rangeEnd: {}, onlyAvailable: {}, sort: {}, from: {}, size: {}",
            text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size
        );

        makePublicEndpointHit(statsClient, request);

        PageRequest pageRequest;
        List<Event> events;

        if (sort == EventSort.EVENT_DATE) {
            pageRequest = PageRequest.of(from, size, Sort.by("eventDate"));
        } else {
            pageRequest = PageRequest.of(from, size);
        }

        if (Boolean.TRUE.equals(onlyAvailable)) {
            events =
                eventRepository.getAvailableEventsForUser(text, categories, paid, rangeStart, rangeEnd, pageRequest);
        } else {
            events = eventRepository.getEventsForUser(text, categories, paid, rangeStart, rangeEnd, pageRequest);
        }

        if (sort == EventSort.VIEWS) {
            sortEventsByViews(events);
        }

        return events.stream().map(this::parseToShortDtoWithMappers).collect(Collectors.toList());
    }

    /* Helpers */


    /**
     * Сортировка событий по количеству просмотров
     *
     * @param events список событий
     */
    private void sortEventsByViews(List<Event> events) {
        Map<String, Long> stats = getEventStats(events);

        events.sort((a, b) -> stats.getOrDefault(EVENTS_PREFIX + b.getId(), 0L)
            .compareTo(stats.getOrDefault(EVENTS_PREFIX + a.getId(), 0L)));
    }

    /**
     * Получение просмотров с сервиса статистики
     *
     * @param events список событий
     * @return {@link Map} где ключ это url, а значение количество просмотров
     */
    private Map<String, Long> getEventStats(List<Event> events) {
        List<String> uris = events
            .stream()
            .map(event -> EVENTS_PREFIX + event.getId())
            .collect(Collectors.toList());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String start = LocalDateTime.now().minusYears(10).format(formatter);
        String end = LocalDateTime.now().plusYears(10).format(formatter);

        return getViews(statsClient, start, end, uris, false);
    }


    /**
     * Проверка на то что событие еще не опубликовано
     *
     * @param state текущее состояние события
     * @throws EventDateConflictException если событие опубликовано
     */
    private void checkEventState(EventState state) {
        if (state == EventState.PUBLISHED) {
            throw new EventStatusConflictException();
        }
    }

    /**
     * Парсинг сущности к укороченному варианту данных события
     *
     * @param event {@link Event}
     * @return {@link EventShortDto}
     */
    private EventShortDto parseToShortDtoWithMappers(Event event) {
        return EventMapper.toShortDto(event, CategoryMapper.toDto(event.getCategory()),
            UserMapper.toShortDto(event.getInitiator())
        );
    }

    /**
     * Парсинг сущности к полному варианту данных события
     *
     * @param event {@link Event}
     * @return {@link EventFullDto}
     */
    private EventFullDto parseToFullDtoWithMappers(Event event) {
        return EventMapper.toDto(eventRepository.save(event), CategoryMapper.toDto(event.getCategory()),
            UserMapper.toShortDto(event.getInitiator()), LocationMapper.toDto(event.getLocation())
        );
    }

    /**
     * Проверка на то что пользователь является инициатором осбытия
     *
     * @param userId    идентификатор пользователя
     * @param initiator инициатор события
     * @throws EventInitiatorConflictException если пользователь не инициатор события
     */
    private void checkEventInitiator(Long userId, User initiator) {
        if (!Objects.equals(userId, initiator.getId())) {
            throw new EventInitiatorConflictException();
        }
    }

    /**
     * Проверка на то что дата и время на которые намечено событие не может быть раньше,
     * чем через два часа от текущего момента
     *
     * @param eventDate дата события
     * @throws EventDateConflictException если не прошла валидация по дате события
     */
    private void checkEventDate(LocalDateTime eventDate) {
        if (LocalDateTime.now().plusHours(2).isAfter(eventDate)) {
            throw new EventDateConflictException(eventDate);
        }
    }

    /**
     * Вынес одинаковые для пользователя и админа поля для обновления
     *
     * @param updateEventRequest новые значения для события
     * @param event              событие для записи значений
     */
    private void updateEvent(UpdateEventRequest<?> updateEventRequest, Event event) {
        /* Обновление аннотации */
        Optional.ofNullable(updateEventRequest.getAnnotation()).ifPresent(event::setAnnotation);
        /* Обновление категории */
        Optional.ofNullable(updateEventRequest.getCategory())
            .ifPresent(categoryId -> event.setCategory(checkAndReturnCategory(categoryRepository, categoryId)));
        /* Обновление описания */
        Optional.ofNullable(updateEventRequest.getDescription()).ifPresent(event::setDescription);
        /* Обновление даты события */
        Optional.ofNullable(updateEventRequest.getEventDate()).ifPresent(eventDate -> {
            checkEventDate(eventDate);
            event.setEventDate(eventDate);
        });
        /* Обновление локации */
        Optional.ofNullable(updateEventRequest.getLocation())
            .ifPresent(locationDto -> event.setLocation(findOrCreateLocation(locationRepository, locationDto)));
        /* Обновление платности мероприятия */
        Optional.ofNullable(updateEventRequest.getPaid()).ifPresent(event::setPaid);
        /* Обновление лимита посетителей */
        Optional.ofNullable(updateEventRequest.getParticipantLimit()).ifPresent(event::setParticipantLimit);
        /* Обновление статуса модерации */
        Optional.ofNullable(updateEventRequest.getRequestModeration()).ifPresent(event::setRequestModeration);
        /* Обновление заголовка */
        Optional.ofNullable(updateEventRequest.getTitle()).ifPresent(event::setTitle);
    }
}
