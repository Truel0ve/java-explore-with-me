package ru.practicum.models.evaluations;


import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.io.Serializable;

@Embeddable
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class EvaluationId implements Serializable {
    Long userId;
    Long eventId;

    public EvaluationId() {

    }

    public EvaluationId(Long userId, Long eventId) {
        this.userId = userId;
        this.eventId = eventId;
    }
}