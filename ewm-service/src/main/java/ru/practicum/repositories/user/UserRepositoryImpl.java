package ru.practicum.repositories.user;

import org.springframework.context.annotation.Lazy;
import ru.practicum.exceptions.ArgumentNotFoundException;
import ru.practicum.models.user.User;

@SuppressWarnings("all")
public class UserRepositoryImpl implements UserRepositoryCustom {
    private final UserRepository userRepository;

    public UserRepositoryImpl(@Lazy UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new ArgumentNotFoundException("The specified user id=" + userId + " does not exist",
                        new NullPointerException()));
    }
}
