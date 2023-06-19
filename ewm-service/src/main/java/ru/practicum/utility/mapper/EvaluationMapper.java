package ru.practicum.utility.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.dto.evaluation.EvaluationDto;
import ru.practicum.models.evaluations.Evaluation;
import ru.practicum.models.evaluations.EvaluationValue;

@UtilityClass
public class EvaluationMapper {

    public EvaluationDto toEvaluationDto(Evaluation evaluation) {
        EvaluationDto evaluationDto = new EvaluationDto();
        evaluationDto.setUserName(evaluation.getUser().getName());
        evaluationDto.setEventTitle(evaluation.getEvent().getTitle());
        if (Boolean.TRUE.equals(evaluation.getIsLike())) {
            evaluationDto.setEvaluation(EvaluationValue.LIKE);
        } else {
            evaluationDto.setEvaluation(EvaluationValue.DISLIKE);
        }
        return evaluationDto;
    }
}
