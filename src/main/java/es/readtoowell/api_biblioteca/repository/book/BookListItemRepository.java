package es.readtoowell.api_biblioteca.repository.book;

import es.readtoowell.api_biblioteca.model.entity.BookList;
import es.readtoowell.api_biblioteca.model.entity.BookListItem;
import es.readtoowell.api_biblioteca.model.entity.id.BookListItemId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface BookListItemRepository extends JpaRepository<BookListItem, BookListItemId> {
    /**
     * Devuelve las listas de un usuario que contienen un libro específico.
     *
     * @param userId ID del usuario
     * @param bookId ID del libro
     * @return Lista de las listas del usuario que contienen el libro
     */
    @Query("SELECT bli.list FROM BookListItem bli WHERE bli.list.user.id = :userId AND bli.book.id = :bookId")
    Set<BookList> findAllListsByUserIdAndBookId(@Param("userId") Long userId, @Param("bookId") Long bookId);
}
