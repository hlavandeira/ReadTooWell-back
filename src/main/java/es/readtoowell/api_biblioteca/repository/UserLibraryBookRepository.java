package es.readtoowell.api_biblioteca.repository;

import es.readtoowell.api_biblioteca.model.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
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

    @Query("""
    SELECT COUNT(ulb) 
    FROM UserLibraryBook ulb
    WHERE ulb.fechaFin IS NOT NULL
    AND YEAR(ulb.fechaFin) = YEAR(CURRENT_DATE) AND ulb.usuario.id = :idUsuario
    """)
    long countBooksReadInCurrentYear(@Param("idUsuario") Long idUsuario);

    @Query("""
    SELECT COALESCE(SUM(ulb.libro.numeroPaginas), 0) 
    FROM UserLibraryBook ulb
    WHERE ulb.fechaFin IS NOT NULL
    AND YEAR(ulb.fechaFin) = YEAR(CURRENT_DATE) AND ulb.usuario.id = :idUsuario
    """)
    long sumPagesReadInCurrentYear(@Param("idUsuario") Long idUsuario);

    @Query(value = """
    SELECT g.*
    FROM genero g
    JOIN libro_genero lg ON g.id_genero = lg.id_genero
    JOIN libro_biblioteca ulb ON lg.id_libro = ulb.id_libro
    WHERE ulb.id_usuario = :idUsuario
    AND EXTRACT(YEAR FROM ulb.fecha_fin) = EXTRACT(YEAR FROM CURRENT_DATE)
    GROUP BY g.id_genero, g.nombre
    ORDER BY COUNT(g.id_genero) DESC
    LIMIT 5
    """, nativeQuery = true)
    List<Genre> findTopGenresForCurrentYear(@Param("idUsuario") Long idUsuario);

    @Query(value = """
    SELECT l.*
    FROM libro l
    JOIN libro_biblioteca ulb ON l.id_libro = ulb.id_libro
    WHERE ulb.id_usuario = :idUsuario
    AND ulb.calificacion > 0
    AND EXTRACT(YEAR FROM ulb.fecha_fin) = EXTRACT(YEAR FROM CURRENT_DATE)
    ORDER BY ulb.calificacion DESC
    LIMIT :limit
    """, nativeQuery = true)
    List<Book> findTopRatedBooksByUserForCurrentYear(@Param("idUsuario") Long idUsuario, @Param("limit") int limit);
}
