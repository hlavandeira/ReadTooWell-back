package es.readtoowell.api_biblioteca.repository;

import es.readtoowell.api_biblioteca.model.Goal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface GoalRepository extends JpaRepository<Goal, Long> {
    Set<Goal> findByUsuarioId(Long id);

    @Query("""
    SELECT g FROM Goal g 
    WHERE g.usuario.id = :idUsuario 
    AND g.duracion.nombre = 'Anual'
    AND EXTRACT(YEAR FROM g.fechaFin) = EXTRACT(YEAR FROM CURRENT_DATE)
    """)
    List<Goal> findAnnualGoalsForCurrentYear(@Param("idUsuario") Long idUsuario);
}
