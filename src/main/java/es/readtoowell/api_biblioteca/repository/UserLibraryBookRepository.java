package es.readtoowell.api_biblioteca.repository;

import es.readtoowell.api_biblioteca.model.Book;
import es.readtoowell.api_biblioteca.model.User;
import es.readtoowell.api_biblioteca.model.UserLibraryBook;
import es.readtoowell.api_biblioteca.model.UserLibraryBookId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserLibraryBookRepository extends JpaRepository<UserLibraryBook, UserLibraryBookId> {
    Optional<UserLibraryBook> findByUsuarioAndLibro(User user, Book book);
    Page<UserLibraryBook> findByUsuario(User user, Pageable pageable);
}
