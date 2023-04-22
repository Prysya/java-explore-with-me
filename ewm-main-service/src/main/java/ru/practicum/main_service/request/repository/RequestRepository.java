package ru.practicum.main_service.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.main_service.request.model.Request;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findByRequesterId(Long id);

    Boolean existsByEventIdAndRequesterId(Long eventId, Long requesterId);

    List<Request> findByEventInitiatorIdAndEventId(Long initiatorId, Long eventId);

}