package ru.practicum.main_service.location.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.main_service.location.dto.LocationDto;
import ru.practicum.main_service.location.mapper.LocationMapper;
import ru.practicum.main_service.location.model.Location;
import ru.practicum.main_service.location.repository.LocationRepository;

@Slf4j
@UtilityClass
public class SharedLocationRequests {
    public static Location findOrCreateLocation(LocationRepository locationRepository, LocationDto locationDto) {
        log.debug("SharedLocationRequests find or create location. Location: {}", locationDto);

        Float lat = locationDto.getLat();
        Float lon = locationDto.getLon();

        return locationRepository.findByLocation(lat, lon)
            .orElse(locationRepository.save(LocationMapper.toEntity(locationDto)));
    }
}
