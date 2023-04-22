package ru.practicum.main_service.user.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.main_service.user.model.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("select u from User as u where ((:users) is null or u.id in :users)")
    List<User> getAllOrIdInUsers(@Param("users") List<Long> users, Pageable pageable);
}