package es.readtoowell.api_biblioteca.repository.book;

import es.readtoowell.api_biblioteca.model.entity.BookList;
import es.readtoowell.api_biblioteca.model.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BookListRepository extends JpaRepository<BookList, Long> {
    /**
     * Devuelve las listas de un usuario.
     *
     * @param id ID del usuario
     * @param pageable Información de paginación.
     * @return Página con listas asociadas al usuario, paginada según {@code pageable}
     */
    Page<BookList> findByUserId(Long id, Pageable pageable);

    /**
     * Devuelve una lista de libros y sus géneros asociados.
     *
     * @param id ID de la lista
     * @return Un {@code Optional} con la lista. Si no se encuentra, estará vacío.
     */
    @Query("SELECT l FROM BookList l LEFT JOIN FETCH l.books LEFT JOIN FETCH l.genres WHERE l.id = :id")
    Optional<BookList> findByIdWithRelations(@Param("id") Long id);
}
