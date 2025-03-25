package es.readtoowell.api_biblioteca.repository;

import es.readtoowell.api_biblioteca.model.BookList;
import es.readtoowell.api_biblioteca.model.BookListItem;
import es.readtoowell.api_biblioteca.model.BookListItemId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface BookListItemRepository extends JpaRepository<BookListItem, BookListItemId> {
    @Query("SELECT bli.lista FROM BookListItem bli WHERE bli.lista.usuario.id = :userId AND bli.libro.id = :bookId")
    Set<BookList> findAllListsByUserIdAndBookId(@Param("userId") Long userId, @Param("bookId") Long bookId);
}
