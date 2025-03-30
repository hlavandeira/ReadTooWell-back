package es.readtoowell.api_biblioteca.repository.goal;

import es.readtoowell.api_biblioteca.model.entity.GoalType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GoalTypeRepository extends JpaRepository<GoalType, Long> {
    /**
     * Busca un tipo de objetivo por nombre.
     *
     * @param name Nombre del tipo de objetivo.
     * @return Un {@code Optional} con el tipo de objetivo. Si no se encuentra, estará vacío.
     */
    Optional<GoalType> findByName(String name);
}
