package ru.practicum.main_service.rating.util;

import lombok.experimental.UtilityClass;
import ru.practicum.main_service.rating.model.Rating;

import java.util.Set;

@UtilityClass
public class RatingCalculator {
    public static Integer calculateRating(Set<Rating> ratings) {
        return ratings.stream()
            .reduce(
                0,
                (acc, rating) -> Boolean.TRUE.equals(rating.getIsPositive()) ? acc + 1 : acc - 1,
                Integer::sum
            );
    }
}
