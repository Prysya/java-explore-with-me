package ru.practicum.main_service.rating.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Builder
@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
@ToString
@Table(name = "ratings")
@IdClass(RatingCompositeKey.class)
public class Rating implements Serializable {
    @Id
    @Column(name = "user_id")
    private Long userId;

    @Id
    @Column(name = "event_id")
    private Long eventId;

    @Column(name = "is_positive")
    private Boolean isPositive;

    @Column(name = "initiator_id")
    private long initiatorId;
}
