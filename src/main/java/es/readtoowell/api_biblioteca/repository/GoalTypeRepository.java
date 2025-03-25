package es.readtoowell.api_biblioteca.repository;

import es.readtoowell.api_biblioteca.model.GoalType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GoalTypeRepository extends JpaRepository<GoalType, Long> {
    Optional<GoalType> findByNombre(String nombre);
}
