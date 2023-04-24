package ru.practicum.main_service.location.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.main_service.location.model.Location;

import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    @Query("select l from Location l where l.lat = :lat and l.lon = :lon")
    Optional<Location> findByLocation(@Param("lat") Float lat, @Param("lon") Float lon);


}