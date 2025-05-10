package es.readtoowell.api_biblioteca.repository.library;

import es.readtoowell.api_biblioteca.model.entity.*;
import es.readtoowell.api_biblioteca.model.entity.id.UserLibraryBookId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserLibraryBookRepository extends JpaRepository<UserLibraryBook, UserLibraryBookId> {
    /**
     * Busca un libro de la biblioteca de un usuario.
     *
     * @param user Usuario para el que se busca el libro
     * @param book Libro de la biblioteca que se busca
     * @return Un {@code Optional} con el libro de la biblioteca del usuario. Si no se encuentra, estará vacío.
     */
    Optional<UserLibraryBook> findByUserAndBook(User user, Book book);

    /**
     * Busca los libros de la biblioteca de un usuario.
     *
     * @param user Usuario del que se buscan los libros
     * @param pageable Información de paginación
     * @return Página con los libros de la biblioteca del usuario, paginada según {@code pageable}
     */
    Page<UserLibraryBook> findByUser(User user, Pageable pageable);

    /**
     * Busca los libros de la biblioteca de un usuario según su estado.
     *
     * @param user Usuario del que se buscan los libros
     * @param status Estado de los libros que se quieren buscar
     * @param pageable Información de paginación
     * @return Página con los libros de la biblioteca filtrados por estado, paginada según {@code pageable}
     */
    Page<UserLibraryBook> findByUserAndReadingStatus(User user, int status, Pageable pageable);

    /**
     * Calcula la puntuación media de un libro.
     *
     * @param bookId ID del libro
     * @return Calificación media del libro
     */
    @Query("""
    SELECT COALESCE(AVG(ulb.rating), 0)
    FROM UserLibraryBook ulb
    WHERE ulb.book.id = :bookId AND ulb.rating > 0
    """)
    double findAverageRatingByBookId(@Param("bookId") Long bookId);

    /**
     * Busca, para un libro, las reseñas de todos los usuarios, excluyendo el usuario especificado.
     *
     * @param bookId Libro del que se buscan las reseñas
     * @param userId Usuario del cual se excluye la reseña
     * @return Lista con las reseñas del libro
     */
    @Query("""
    SELECT ulb FROM UserLibraryBook ulb
    WHERE ulb.book.id = :bookId
    AND ulb.review IS NOT NULL AND ulb.review <> ''
    AND ulb.user.id <> :userId
    """)
    List<UserLibraryBook> findAllWithReviewByBookIdExcludingUser(@Param("bookId") Long bookId,
                                                                @Param("userId") Long userId);

    /**
     * Suma las páginas de los libros leídos en el año actual por un usuario.
     *
     * @param userId Usuario del que se suman las páginas leídas
     * @return Cantidad de páginas leídas en el año actual por el usuario
     */
    @Query("""
    SELECT COALESCE(SUM(ulb.book.pageNumber), 0)
    FROM UserLibraryBook ulb
    WHERE ulb.dateFinish IS NOT NULL
    AND YEAR(ulb.dateFinish) = YEAR(CURRENT_DATE) AND ulb.user.id = :userId
    """)
    long sumPagesReadInCurrentYear(@Param("userId") Long userId);

    /**
     * Busca los géneros más leídos por un usuario en el año actual.
     *
     * @param userId Usuario del que se buscan los géneros más leídos
     * @param limit Cantidad de géneros que se quieren buscar
     * @return Lista con los géneros más leídos por el usuario
     */
    @Query(value = """
    SELECT g.*
    FROM genero g
    JOIN libro_genero lg ON g.id_genero = lg.id_genero
    JOIN libro_biblioteca ulb ON lg.id_libro = ulb.id_libro
    WHERE ulb.id_usuario = :userId
    AND EXTRACT(YEAR FROM ulb.fecha_fin) = EXTRACT(YEAR FROM CURRENT_DATE)
    GROUP BY g.id_genero, g.nombre
    ORDER BY COUNT(g.id_genero) DESC
    LIMIT :limit
    """, nativeQuery = true)
    List<Genre> findTopGenresForCurrentYear(@Param("userId") Long userId, @Param("limit") int limit);

    /**
     * Busca los libros mejor valorados por un usuario en el año actual.
     *
     * @param userId Usuario del que se buscan los libros mejor valorados
     * @param limit Cantidad de libros que se quieren buscar
     * @return Lista con los libros mejor valorados por el usuario
     */
    @Query(value = """
    SELECT l.*
    FROM libro l
    JOIN libro_biblioteca ulb ON l.id_libro = ulb.id_libro
    WHERE ulb.id_usuario = :userId
    AND ulb.calificacion > 0
    AND EXTRACT(YEAR FROM ulb.fecha_fin) = EXTRACT(YEAR FROM CURRENT_DATE)
    ORDER BY ulb.calificacion DESC
    LIMIT :limit
    """, nativeQuery = true)
    List<Book> findTopRatedBooksByUserForCurrentYear(@Param("userId") Long userId, @Param("limit") int limit);

    /**
     * Busca los libros leídos por el usuario en el año actual.
     *
     * @param userId Usuario del que se buscan los libros leídos
     * @return Lista con los libros leídos del usuario en el año actual
     */
    @Query("""
    SELECT ulb.book FROM UserLibraryBook ulb
    WHERE ulb.user.id = :userId
    AND EXTRACT(YEAR FROM ulb.dateFinish) = EXTRACT(YEAR FROM CURRENT_DATE)
    """)
    List<Book> findBooksReadActualYear(@Param("userId") Long userId);

    /**
     * Busca los libros leídos por el usuario en el mes actual.
     *
     * @param userId Usuario del que se buscan los libros leídos
     * @return Lista con los libros leídos del usuario en el mes actual
     */
    @Query("""
    SELECT ulb.book FROM UserLibraryBook ulb
    WHERE ulb.user.id = :userId
    AND EXTRACT(YEAR FROM ulb.dateFinish) = EXTRACT(YEAR FROM CURRENT_DATE)
    AND EXTRACT(MONTH FROM ulb.dateFinish) = EXTRACT(MONTH FROM CURRENT_DATE)
    """)
    List<Book> findBooksReadActualMonth(@Param("userId") Long userId);
}
