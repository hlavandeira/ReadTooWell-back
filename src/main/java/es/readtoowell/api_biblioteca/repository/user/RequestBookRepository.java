package es.readtoowell.api_biblioteca.repository.user;

import es.readtoowell.api_biblioteca.model.entity.RequestBook;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio para la gestión de entidades {@code RequestBook}
 */
public interface RequestBookRepository extends JpaRepository<RequestBook, Long> {
}
