package ru.practicum.main_service.location.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Builder
@AllArgsConstructor
@Setter
@Getter
@Table(name = "locations")
@NoArgsConstructor
@ToString
public class Location implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Float lat;

    @Column(nullable = false, unique = true)
    private Float lon;
}