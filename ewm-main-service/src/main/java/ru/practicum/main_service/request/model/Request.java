package ru.practicum.main_service.request.model;

import lombok.*;
import ru.practicum.main_service.event.model.Event;
import ru.practicum.main_service.request.constant.RequestStatus;
import ru.practicum.main_service.user.model.User;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Builder
@AllArgsConstructor
@Setter
@Getter
@Table(name = "requests")
@NoArgsConstructor
@ToString
public class Request implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    @ManyToOne
    @JoinColumn(name = "requester_id")
    private User requester;

    @Column
    @Enumerated(EnumType.STRING)
    private RequestStatus status;

    @Column
    private LocalDateTime created;
}
