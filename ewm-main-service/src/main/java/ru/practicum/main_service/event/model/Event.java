package ru.practicum.main_service.event.model;

import lombok.*;
import org.hibernate.annotations.WhereJoinTable;
import ru.practicum.main_service.category.model.Category;
import ru.practicum.main_service.event.constant.EventState;
import ru.practicum.main_service.location.model.Location;
import ru.practicum.main_service.user.model.User;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Builder
@AllArgsConstructor
@Setter
@Getter
@Table(name = "events")
@NoArgsConstructor
@ToString
@NamedEntityGraph(
    name = Event.WITH_EVENT_DATA_GRAPH,
    attributeNodes = {
        @NamedAttributeNode("participants"),
    }
)
public class Event implements Serializable {
    public static final String WITH_EVENT_DATA_GRAPH = "graph.Event";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 2000)
    private String annotation;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(name = "created_on", nullable = false)
    private LocalDateTime createdOn;

    @Column(nullable = false, length = 7000)
    private String description;

    @Column(name = "event_date", nullable = false)
    private LocalDateTime eventDate;

    @ManyToOne
    @JoinColumn(name = "initiator_id", nullable = false)
    private User initiator;

    @ManyToOne
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    @Column(nullable = false)
    private Boolean paid;

    @Column(name = "participant_limit", nullable = false)
    private Integer participantLimit;

    @WhereJoinTable(clause = "status='CONFIRMED'")
    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinTable(name = "requests",
        joinColumns = @JoinColumn(name = "event_id"),
        inverseJoinColumns = @JoinColumn(name = "requester_id"))
    @ToString.Exclude
    private Set<User> participants = new HashSet<>();

    @Column(name = "published_on")
    private LocalDateTime publishedOn;

    @Column(name = "request_moderation", nullable = false)
    private Boolean requestModeration;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EventState state;

    @Column(nullable = false, length = 120)
    private String title;
}
