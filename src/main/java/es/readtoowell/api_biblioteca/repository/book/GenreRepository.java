package es.readtoowell.api_biblioteca.repository.book;

import es.readtoowell.api_biblioteca.model.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para la gestión de entidades {@code Genre}
 */
@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {
}
