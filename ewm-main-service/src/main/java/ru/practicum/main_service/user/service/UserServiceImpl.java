package ru.practicum.main_service.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main_service.user.dto.NewUserRequestDto;
import ru.practicum.main_service.user.dto.UserDto;
import ru.practicum.main_service.user.exception.UserNotFoundException;
import ru.practicum.main_service.user.mapper.UserMapper;
import ru.practicum.main_service.user.model.User;
import ru.practicum.main_service.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public List<UserDto> getUsers(List<Long> ids, PageRequest pageRequest) {
        List<User> users = userRepository.getAllOrIdInUsers(ids, pageRequest);

        log.debug("Get users. Ids: {}, PageRequest: {}, Users: {}", ids, pageRequest, users);

        return users.stream().map(UserMapper::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserDto createUser(NewUserRequestDto newUserRequestDto) {
        User user = UserMapper.toEntity(newUserRequestDto);

        log.debug("Create user. Dto: {}, Entity: {}", newUserRequestDto, user);

        return UserMapper.toDto(userRepository.save(user));
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        log.debug("Delete user. User id: {}", userId);

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException(userId));

        userRepository.delete(user);
    }
}
