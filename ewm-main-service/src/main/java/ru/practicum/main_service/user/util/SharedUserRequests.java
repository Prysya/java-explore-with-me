package ru.practicum.main_service.user.util;


import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.main_service.user.exception.UserNotFoundException;
import ru.practicum.main_service.user.model.User;
import ru.practicum.main_service.user.repository.UserRepository;

@Slf4j
@UtilityClass
public class SharedUserRequests {
    public static User checkAndReturnUser(UserRepository userRepository, Long userId) {
        log.debug("SharedUserRequests check user. User id: {}", userId);

        return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
    }
}
