package ru.practicum.repositories.location;

import ru.practicum.models.location.Location;

public interface LocationRepositoryCustom {
    Location getLocation(Number lat, Number lon);
}
