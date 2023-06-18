package ru.practicum.repositories.evaluation;

import org.springframework.context.annotation.Lazy;
import ru.practicum.exceptions.ArgumentNotFoundException;
import ru.practicum.models.evaluations.Evaluation;
import ru.practicum.models.evaluations.EvaluationId;

public class EvaluationRepositoryImpl implements EvaluationRepositoryCustom {
    private final EvaluationRepository evaluationRepository;

    public EvaluationRepositoryImpl(@Lazy EvaluationRepository evaluationRepository) {
        this.evaluationRepository = evaluationRepository;
    }

    @Override
    public Evaluation getEvaluationByUserIdAndEventId(Long userId, Long eventId) {
        return evaluationRepository.findById(new EvaluationId(userId, eventId)).orElseThrow(
                () -> new ArgumentNotFoundException("The user id=" + userId + " has not rated the event id=" + eventId,
                        new NullPointerException()));
    }
}
