package ru.practicum.main_service.user.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.main_service.rating.util.RatingCalculator;
import ru.practicum.main_service.user.dto.NewUserRequestDto;
import ru.practicum.main_service.user.dto.UserDto;
import ru.practicum.main_service.user.dto.UserShortDto;
import ru.practicum.main_service.user.model.User;

import java.util.Optional;

@UtilityClass
public class UserMapper {
    public static User toEntity(NewUserRequestDto newUserRequestDto) {
        return User.builder()
            .email(newUserRequestDto.getEmail())
            .name(newUserRequestDto.getName())
            .build();
    }

    public static UserShortDto toShortDto(User user) {
        return UserShortDto.builder()
            .id(user.getId())
            .name(user.getName())
            .build();
    }

    public static UserDto toDto(User user) {
        return UserDto.builder()
            .id(user.getId())
            .email(user.getEmail())
            .name(user.getName())
            .rating(
                Optional.ofNullable(user.getRatings())
                    .map(RatingCalculator::calculateRating).orElse(0)
            )
            .build();
    }
}
