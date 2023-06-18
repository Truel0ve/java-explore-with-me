package ru.practicum.utility;

import lombok.experimental.UtilityClass;
import ru.practicum.models.evaluations.Evaluation;

import java.util.Set;

@UtilityClass
public class RateCalculator {

    public Long calculateEvaluation(Set<Evaluation> evaluations) {
        if (evaluations != null && !evaluations.isEmpty()) {
            long likes = evaluations
                    .stream()
                    .filter(evaluation -> evaluation.getIsLike().equals(Boolean.TRUE))
                    .count();
            return likes - (evaluations.size() - likes);
        } else {
            return 0L;
        }
    }
}