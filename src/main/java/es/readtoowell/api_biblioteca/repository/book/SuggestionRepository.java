package es.readtoowell.api_biblioteca.repository.book;

import es.readtoowell.api_biblioteca.model.entity.Suggestion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SuggestionRepository extends JpaRepository<Suggestion, Long> {
    /**
     * Busca sugerencias de libro según su estado.
     *
     * @param status Estado de sugerencia por el que se quiere buscar
     * @param pageable Información de paginación
     * @return Página con las sugerencias resultantes, paginada según {@code pageable}
     */
    Page<Suggestion> findByStatus(int status, Pageable pageable);

    /**
     * Busca sugerencias de libro según su estado.
     *
     * @param status Estado de sugerencia por el que se quiere buscar
     * @return Lista con las sugerencias filtradas
     */
    List<Suggestion> findByStatus(int status);
}
