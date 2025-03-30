package es.readtoowell.api_biblioteca.repository.goal;

import es.readtoowell.api_biblioteca.model.entity.GoalDuration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GoalDurationRepository extends JpaRepository<GoalDuration, Long> {
    /**
     * Busca una duración de objetivo por nombre.
     *
     * @param name Nombre de la duración de objetivo.
     * @return Un {@code Optional} con la duración de objetivo. Si no se encuentra, estará vacío.
     */
    Optional<GoalDuration> findByName(String name);
}
