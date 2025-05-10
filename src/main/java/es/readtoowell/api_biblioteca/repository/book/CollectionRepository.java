package es.readtoowell.api_biblioteca.repository.book;

import es.readtoowell.api_biblioteca.model.entity.Collection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CollectionRepository extends JpaRepository<Collection, Long> {
    /**
     * Busca una colección por su nombre.
     *
     * @param name Nombre de la colección
     * @return Un {@code Optional} con la colección. Si no se encuentra, estará vacío.
     */
    Optional<Collection> findByName(String name);
}
