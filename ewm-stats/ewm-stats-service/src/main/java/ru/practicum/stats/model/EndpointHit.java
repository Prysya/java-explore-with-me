package ru.practicum.stats.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Builder
@AllArgsConstructor
@Setter
@Getter
@Table(name = "stats")
@NoArgsConstructor
@ToString
public class EndpointHit {
    /**
     * Идентификатор записи
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Идентификатор сервиса для которого записывается информация
     */
    @Column
    private String app;

    /**
     * URI для которого был осуществлен запрос
     */
    @Column
    private String uri;

    /**
     * IP-адрес пользователя, осуществившего запрос
     */
    @Column
    private String ip;

    /**
     * Дата и время, когда был совершен запрос к эндпоинту
     */
    @Column
    private LocalDateTime timestamp;
}
