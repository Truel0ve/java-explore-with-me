package ru.practicum.models.evaluations;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import ru.practicum.models.event.Event;
import ru.practicum.models.user.User;

import javax.persistence.*;

@Entity
@Table(name = "evaluations", schema = "public")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class Evaluation {
    @EmbeddedId
    EvaluationId id;

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    User user;

    @MapsId("eventId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    Event event;

    @Column(name = "evaluation")
    Boolean isLike;
}