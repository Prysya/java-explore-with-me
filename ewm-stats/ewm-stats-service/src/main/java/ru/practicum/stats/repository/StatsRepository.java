package ru.practicum.stats.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.stats.model.EndpointHit;
import ru.practicum.stats.model.Stat;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatsRepository extends JpaRepository<EndpointHit, Long> {
    @Query("select new ru.practicum.stats.model.Stat(e.app, e.uri, count(e.ip)) " +
        "from EndpointHit e " +
        "where e.timestamp between :start and :end " +
        "and ((:uris) is null or e.uri in :uris) " +
        "group by e.app, e.uri " +
        "order by count(e.ip) desc"
    )
    List<Stat> findAllByDateBetweenIpIn(
        @Param("start") LocalDateTime start,
        @Param("end") LocalDateTime end,
        @Param("uris") List<String> uris
    );

    @Query("select new ru.practicum.stats.model.Stat(e.app, e.uri, count(distinct e.ip)) " +
        "from EndpointHit e " +
        "where e.timestamp between :start and :end " +
        "and e.uri in :uris " +
        "group by e.app, e.uri " +
        "order by count(e.ip) desc"
    )
    List<Stat> findAllByDateBetweenUniqueIpIn(
        @Param("start") LocalDateTime start,
        @Param("end") LocalDateTime end,
        @Param("uris") List<String> uris
    );
}