package es.readtoowell.api_biblioteca.repository.book;

import es.readtoowell.api_biblioteca.model.entity.BookList;
import es.readtoowell.api_biblioteca.model.entity.BookListItem;
import es.readtoowell.api_biblioteca.model.entity.id.BookListItemId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Repositorio para la gestión de entidades {@code BookListItem}
 */
public interface BookListItemRepository extends JpaRepository<BookListItem, BookListItemId> {
    /**
     * Devuelve las listas de un usuario que contienen un libro específico.
     *
     * @param userId ID del usuario
     * @param bookId ID del libro
     * @return Listado de las listas del usuario que contienen el libro
     */
    @Query("SELECT bli.list FROM BookListItem bli WHERE bli.list.user.id = :userId AND bli.book.id = :bookId")
    List<BookList> findAllListsByUserIdAndBookId(@Param("userId") Long userId, @Param("bookId") Long bookId);

    /**
     * Devuelve los libros que pertenecen a una lista específica.
     *
     * @param listId ID de la lista
     * @param pageable Información de paginación
     * @return Página con los libros pertenecientes a la lista
     */
    Page<BookListItem> findByListId(Long listId, Pageable pageable);

    /**
     * Devuelve las listas de un usuario que no contienen un libro.
     *
     * @param userId ID del usuario
     * @param bookId ID del libro
     * @return Listado de las listas del usuario que no contienen el libro
     */
    @Query("""
    SELECT bl
    FROM BookList bl
    WHERE bl.user.id = :userId
    AND NOT EXISTS (
        SELECT 1
        FROM BookListItem bli
        WHERE bli.list = bl
        AND bli.book.id = :bookId
    )
    """)
    Page<BookList> findAllListsByUserIdAndBookIdNotIn(
            @Param("userId") Long userId,
            @Param("bookId") Long bookId,
            Pageable pageable
    );
}
