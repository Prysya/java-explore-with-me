package ru.practicum.main_service.compilation.model;

import lombok.*;
import ru.practicum.main_service.event.model.Event;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Builder
@AllArgsConstructor
@Setter
@Getter
@Table(name = "compilations")
@NoArgsConstructor
@ToString
public class Compilation implements Serializable {
    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinTable(
        name = "compilation_events",
        joinColumns = @JoinColumn(name = "event_id"),
        inverseJoinColumns = @JoinColumn(name = "compilation_id")
    )
    @ToString.Exclude
    private Set<Event> events;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String title;
    @Column
    private Boolean pinned;
}
