package ru.practicum.models.event;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import ru.practicum.models.compilation.Compilation;
import ru.practicum.models.evaluations.Evaluation;
import ru.practicum.models.location.Location;
import ru.practicum.models.category.Category;
import ru.practicum.models.partrequest.ParticipationRequest;
import ru.practicum.models.user.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "events", schema = "public")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    Long id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "initiator", nullable = false)
    User initiator;

    @NotNull
    @Column(name = "title")
    String title;

    @NotNull
    @Column(name = "annotation")
    String annotation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category", nullable = false)
    Category category;

    @NotNull
    @Column(name = "description")
    String description;

    @NotNull
    @Column(name = "event_date")
    LocalDateTime eventDate;

    @NotNull
    @Column(name = "created_on")
    LocalDateTime createdOn;

    @Column(name = "published_on")
    LocalDateTime publishedOn;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "location")
    Location location;

    @NotNull
    @Column(name = "participant_limit", columnDefinition = "bigint default 0")
    Long participantLimit;

    @NotNull
    @Column(name = "paid", columnDefinition = "boolean default false")
    Boolean paid;

    @NotNull
    @Column(name = "request_moderation", columnDefinition = "boolean default true")
    Boolean requestModeration;

    @Enumerated(EnumType.STRING)
    EventState state;

    @OneToMany(targetEntity = ParticipationRequest.class,
            mappedBy = "event",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    Set<ParticipationRequest> requests = new HashSet<>();

    @OneToMany(targetEntity = Evaluation.class,
            mappedBy = "event",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    Set<Evaluation> evaluations = new HashSet<>();

    @ManyToMany(targetEntity = Compilation.class,
            mappedBy = "events",
            fetch = FetchType.LAZY)
    Set<Compilation> compilations = new HashSet<>();
}
