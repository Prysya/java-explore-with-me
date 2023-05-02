package ru.practicum.main_service.rating.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.main_service.rating.model.Rating;
import ru.practicum.main_service.rating.model.RatingCompositeKey;

@Repository
public interface RatingRepository extends JpaRepository<Rating, RatingCompositeKey> {
    void deleteByUserIdAndEventId(Long userId, Long eventId);
}