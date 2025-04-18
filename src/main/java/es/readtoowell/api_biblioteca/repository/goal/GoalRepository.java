package es.readtoowell.api_biblioteca.repository.goal;

import es.readtoowell.api_biblioteca.model.entity.Goal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface GoalRepository extends JpaRepository<Goal, Long> {
    /**
     * Busca objetivos de lectura de un usuario.
     *
     * @param id ID del usuario
     * @return Lista con los objetivos del usuario
     */
    Set<Goal> findByUserId(Long id);

    /**
     * Busca los objetivos anuales del año indicado de un usuario.
     *
     * @param userId ID del usuario
     * @param year Año para el que se consultan los objetivos
     * @return Lista con los objetivos anuales en curso de un usuario
     */
    @Query("""
    SELECT g FROM Goal g
    WHERE g.user.id = :userId
    AND g.duration.name = 'Anual'
    AND EXTRACT(YEAR FROM g.dateFinish) = :year
    """)
    List<Goal> findAnnualGoalsByYear(@Param("userId") Long userId, @Param("year") int year);
}
