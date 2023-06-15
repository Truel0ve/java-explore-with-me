package ru.practicum.repositories.location;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.models.location.Location;

import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Long>, LocationRepositoryCustom {

    Optional<Location> findByLatAndLon(Number lat, Number lon);
}
