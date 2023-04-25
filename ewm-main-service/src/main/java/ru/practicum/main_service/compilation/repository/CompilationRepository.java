package ru.practicum.main_service.compilation.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.main_service.compilation.model.Compilation;

import java.util.List;

@Repository
public interface CompilationRepository extends JpaRepository<Compilation, Long> {
    @EntityGraph(value = Compilation.WITH_EVENTS_GRAPH, type = EntityGraph.EntityGraphType.LOAD)
    List<Compilation> findByPinned(Boolean pinned, Pageable pageable);
}