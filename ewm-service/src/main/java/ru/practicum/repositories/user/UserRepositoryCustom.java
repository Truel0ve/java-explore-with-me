package ru.practicum.repositories.user;

import ru.practicum.models.user.User;

public interface UserRepositoryCustom {
    User getUserById(Long userId);
}
