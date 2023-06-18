package ru.practicum.repositories.evaluation;

import ru.practicum.models.evaluations.Evaluation;

public interface EvaluationRepositoryCustom {
    Evaluation getEvaluationByUserIdAndEventId(Long userId, Long eventId);
}
