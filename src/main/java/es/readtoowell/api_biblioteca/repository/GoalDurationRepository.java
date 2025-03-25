package es.readtoowell.api_biblioteca.repository;

import es.readtoowell.api_biblioteca.model.GoalDuration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GoalDurationRepository extends JpaRepository<GoalDuration, Long> {
    Optional<GoalDuration> findByNombre(String nombre);
}
