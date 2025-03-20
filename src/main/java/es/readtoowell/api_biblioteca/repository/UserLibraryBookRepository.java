package es.readtoowell.api_biblioteca.repository;

import es.readtoowell.api_biblioteca.model.Book;
import es.readtoowell.api_biblioteca.model.User;
import es.readtoowell.api_biblioteca.model.UserLibraryBook;
import es.readtoowell.api_biblioteca.model.UserLibraryBookId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.Set;

public interface UserLibraryBookRepository extends JpaRepository<UserLibraryBook, UserLibraryBookId> {
    Optional<UserLibraryBook> findByUsuarioAndLibro(User user, Book book);

    Page<UserLibraryBook> findByUsuario(User user, Pageable pageable);

    Page<UserLibraryBook> findByUsuarioAndEstadoLectura(User user, int status, Pageable pageable);

    @Query("""
    SELECT COALESCE(AVG(ulb.calificacion), 0) 
    FROM UserLibraryBook ulb 
    WHERE ulb.libro.id = :idLibro AND ulb.calificacion > 0
    """)
    double findAverageRatingByBookId(@Param("idLibro") Long idLibro);

    @Query("""
    SELECT ulb FROM UserLibraryBook ulb 
    WHERE ulb.libro.id = :idLibro 
    AND ulb.reseña IS NOT NULL AND ulb.reseña <> ''
    AND ulb.usuario.id <> :idUsuario
    """)
    Set<UserLibraryBook> findAllWithReviewByBookIdExcludingUser(@Param("idLibro") Long idLibro,
                                                                @Param("idUsuario") Long idUsuario);
}
