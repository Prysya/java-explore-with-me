package ru.practicum.main_service.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main_service.event.constant.EventState;
import ru.practicum.main_service.event.model.Event;
import ru.practicum.main_service.event.repository.EventRepository;
import ru.practicum.main_service.request.constant.NewRequestStatus;
import ru.practicum.main_service.request.constant.RequestStatus;
import ru.practicum.main_service.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.main_service.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.main_service.request.dto.ParticipationRequestDto;
import ru.practicum.main_service.request.exception.*;
import ru.practicum.main_service.request.mapper.RequestMapper;
import ru.practicum.main_service.request.model.Request;
import ru.practicum.main_service.request.repository.RequestRepository;
import ru.practicum.main_service.user.model.User;
import ru.practicum.main_service.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.main_service.event.util.SharedEventRequests.checkAndReturnEvent;
import static ru.practicum.main_service.user.util.SharedUserRequests.checkAndReturnUser;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Override
    public List<ParticipationRequestDto> getUserRequests(Long userId) {
        checkAndReturnUser(userRepository, userId);

        List<Request> requests = requestRepository.findByRequesterId(userId);

        log.debug("RequestService get user requests. User id: {}, requests: {}", userId, requests);

        return requests.stream().map(RequestMapper::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ParticipationRequestDto createUserRequest(Long userId, Long eventId) {
        log.debug("RequestService create user request. User id: {}, eventId: {}", userId, eventId);

        Event event = checkAndReturnEvent(eventRepository, eventId);
        User user = checkAndReturnUser(userRepository, userId);


        if (Boolean.TRUE.equals(requestRepository.existsByEventIdAndRequesterId(event.getId(), userId))) {
            throw new RequestAlreadyCreatedConflictException();
        }

        if (event.getInitiator().getId().equals(userId)) {
            throw new InitiatorConflictException();
        }

        if (event.getState() != EventState.PUBLISHED) {
            throw new UnpublishedEventConflictException();
        }

        if (event.getParticipants().size() >= event.getParticipantLimit()) {
            throw new ParticipantLimitConflictException();
        }

        return RequestMapper.toDto(requestRepository.save(Request.builder()
            .requester(user)
            .event(event)
            .created(LocalDateTime.now())
            .status(Boolean.TRUE.equals(event.getRequestModeration()) ? RequestStatus.PENDING : RequestStatus.CONFIRMED)
            .build()));
    }

    @Override
    @Transactional
    public ParticipationRequestDto cancelUserRequest(Long userId, Long requestId) {
        log.debug("RequestService cancel user request. UserId: {}, requestId: {}", userId, requestId);

        User user = checkAndReturnUser(userRepository, userId);
        Request request = checkAndReturnRequest(requestId);

        if (!user.getId().equals(request.getRequester().getId())) {
            throw new RequestUserConflictException();
        }

        log.debug("RequestService request: {} canceled by user: {}", request, user);

        request.setStatus(RequestStatus.CANCELED);

        return RequestMapper.toDto(request);
    }

    @Override
    public List<ParticipationRequestDto> getUserEventRequests(Long userId, Long eventId) {
        List<Request> requests = requestRepository.findByEventInitiatorIdAndEventId(userId, eventId);

        log.debug(
            "RequestService get user event requests. UserId: {}, eventId: {}, requests: {}", userId, eventId,
            requests
        );

        return requests.stream().map(RequestMapper::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EventRequestStatusUpdateResult updateUserEventRequest(
        Long userId, Long eventId, EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest
    ) {

        List<ParticipationRequestDto> confirmedRequests = new ArrayList<>();
        List<ParticipationRequestDto> rejectedRequests = new ArrayList<>();

        Event event = checkAndReturnEvent(eventRepository, eventId);

        if (event.getParticipants().size() >= event.getParticipantLimit()) {
            throw new ParticipantLimitConflictException();
        }

        if (event.getState() != EventState.PUBLISHED) {
            throw new UnpublishedEventConflictException();
        }

        List<Request> requests = requestRepository.findAllById(eventRequestStatusUpdateRequest.getRequestIds());

        requests.forEach(request -> {
            if (Boolean.FALSE.equals(event.getRequestModeration()) || event.getParticipantLimit() == 0) {
                return;
            }

            if (eventRequestStatusUpdateRequest.getStatus() == NewRequestStatus.REJECTED) {
                request.setStatus(RequestStatus.REJECTED);
                rejectedRequests.add(RequestMapper.toDto(request));
            }

            if (eventRequestStatusUpdateRequest.getStatus() == NewRequestStatus.CONFIRMED) {
                request.setStatus(RequestStatus.CONFIRMED);
                confirmedRequests.add(RequestMapper.toDto(request));
            }

        });

        return EventRequestStatusUpdateResult.builder().confirmedRequests(confirmedRequests)
            .rejectedRequests(rejectedRequests).build();
    }

    private Request checkAndReturnRequest(Long requestId) {
        log.debug("RequestService check request. Request id: {}", requestId);

        return requestRepository.findById(requestId).orElseThrow(() ->
            new RequestNotFoundException(requestId));
    }
}
