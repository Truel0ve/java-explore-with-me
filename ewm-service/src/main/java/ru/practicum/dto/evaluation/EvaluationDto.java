package ru.practicum.dto.evaluation;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.models.evaluations.EvaluationValue;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EvaluationDto {
    String userName;
    String eventTitle;
    EvaluationValue evaluation;
}
