package ru.practicum.repositories.location;

import org.springframework.context.annotation.Lazy;
import ru.practicum.exceptions.ArgumentNotFoundException;
import ru.practicum.models.location.Location;

@SuppressWarnings("all")
public class LocationRepositoryImpl implements LocationRepositoryCustom {
    private final LocationRepository locationRepository;

    public LocationRepositoryImpl(@Lazy LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    @Override
    public Location getLocation(Number lat, Number lon) {
        return locationRepository.findByLatAndLon(lat, lon).orElseThrow(
                () -> new ArgumentNotFoundException("The specified location was not found",
                        new NullPointerException()));
    }
}
