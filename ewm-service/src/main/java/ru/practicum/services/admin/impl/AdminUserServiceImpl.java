package ru.practicum.services.admin.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.user.UserDto;
import ru.practicum.models.evaluations.Evaluation;
import ru.practicum.models.user.User;
import ru.practicum.repositories.evaluation.EvaluationRepository;
import ru.practicum.repositories.user.UserRepository;
import ru.practicum.services.admin.api.AdminUserService;
import ru.practicum.utility.RateCalculator;
import ru.practicum.utility.PageableBuilder;
import ru.practicum.utility.RateSorter;
import ru.practicum.utility.mapper.UserMapper;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminUserServiceImpl implements AdminUserService {
    private final UserRepository userRepository;
    private final EvaluationRepository evaluationRepository;

    @Transactional(readOnly = true)
    @Override
    public List<UserDto> getUsers(Set<Long> users, Integer from, Integer size, Boolean asc) {
        Pageable pageable = PageableBuilder.getPageable(from, size);
        List<UserDto> userDtoList = userRepository.getUsers(users, pageable)
                .stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
        List<Evaluation> evaluations = evaluationRepository.findAllByInitiatorIds(users);
        setUserRates(userDtoList, evaluations);
        return RateSorter.getSortedUsers(userDtoList, asc);
    }

    @Transactional
    @Override
    public UserDto postNewUser(UserDto user) {
        return UserMapper.toUserDto(userRepository.save(UserMapper.toUser(user)));
    }

    @Transactional
    @Override
    public void deleteUser(Long userId) {
        User user = userRepository.getUserById(userId);
        userRepository.deleteById(user.getId());
    }

    private void setUserRates(List<UserDto> userDtoList, List<Evaluation> evaluations) {
        for (UserDto userDto : userDtoList) {
            Set<Evaluation> userEventEvaluations = getUserEventEvaluations(userDto.getId(), evaluations);
            userDto.setRating(RateCalculator.calculateEvaluation(userEventEvaluations));
        }
    }

    private Set<Evaluation> getUserEventEvaluations(Long userId, List<Evaluation> evaluations) {
        if (evaluations != null && !evaluations.isEmpty()) {
            return evaluations
                    .stream()
                    .filter(evaluation -> evaluation.getEvent().getInitiator().getId().equals(userId))
                    .collect(Collectors.toSet());
        } else {
            return Collections.emptySet();
        }
    }
}
