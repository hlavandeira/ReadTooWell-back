package es.readtoowell.api_biblioteca.repository;

import es.readtoowell.api_biblioteca.model.Goal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface GoalRepository extends JpaRepository<Goal, Long> {
    Set<Goal> findByUsuarioId(Long id);
}
