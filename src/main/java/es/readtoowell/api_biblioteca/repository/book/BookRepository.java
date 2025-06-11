package es.readtoowell.api_biblioteca.repository.book;

import es.readtoowell.api_biblioteca.model.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

/**
 * Repositorio para la gestión de entidades {@code Book}
 */
@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    /**
     * Busca todos los libros activos del sistema.
     *
     * @param pageable Información de paginación
     * @return Página con los libros activos
     */
    Page<Book> findAllByActiveTrue(Pageable pageable);

    /**
     * Busca todos los libros borrados del sistema.
     *
     * @param pageable Información de paginación
     * @return Página con los libros activos
     */
    Page<Book> findAllByActiveFalse(Pageable pageable);

    /**
     * Busca un libro mediante su ISBN y lo devuelve.
     *
     * @param isbn ISBN del libro
     * @return Un {@code Optional} con el libro. Si no se encuentra, estará vacío.
     */
    Optional<Book> findByIsbn(String isbn);

    /**
     * Busca un libro mediante su ISBN, ignorando el libro con el ID indicado.
     *
     * @param isbn ISBN del libro
     * @param id ID del libro a ignorar
     * @return Un {@code Optional} con el libro. Si no se encuentra, estará vacío.
     */
    Optional<Book> findByIsbnAndIdNot(String isbn, Long id);

    /**
     * Busca libros por título, autor o colección, pudiendo filtrar por número de páginas y año de publicación.
     *
     * @param searchString Cadena por la que se buscará por título, autor o colección
     * @param minPages Número mínimo de páginas
     * @param maxPages Número máximo de páginas
     * @param minYear Año mínimo de publicación
     * @param maxYear Año máximo de publicación
     * @param pageable Información de paginación
     * @return Página con los libros resultantes de la búsqueda, paginada según {@code pageable}
     */
    @Query("""
            select b from Book b left join b.collection c
            where (:searchString is null or
                lower(b.title) like lower(concat('%', :searchString, '%'))
                or lower(b.author) like lower(concat('%', :searchString, '%'))
                or lower(c.name) like lower(concat('%', :searchString, '%')))
            and (:minPages is null or b.pageNumber >= :minPages)
            and (:maxPages is null or b.pageNumber <= :maxPages)
            and (:minYear is null or b.publicationYear >= :minYear)
            and (:maxYear is null or b.publicationYear <= :maxYear)
            and b.active is true""")
    Page<Book> filterBooks(
            @Param("searchString") String searchString,
            @Param("minPages") Integer minPages,
            @Param("maxPages") Integer maxPages,
            @Param("minYear") Integer minYear,
            @Param("maxYear") Integer maxYear,
            Pageable pageable
    );

    /**
     * Busca libros por género.
     *
     * @param genreId ID del género que se busca
     * @param pageable Información de paginación
     * @return Página con los libros resultantes de la búsqueda, paginada según {@code pageable}
     */
    Page<Book> findByGenresId(Long genreId, Pageable pageable);

    /**
     * Busca libros escritos por un autor, el cual tiene una cuenta de usuario.
     *
     * @param authorId ID del autor
     * @return Lista de libros escritos por el autor con ID indicado
     */
    @Query(value = """
    SELECT * FROM libro l 
    WHERE l.id_libro IN (SELECT la.id_libro FROM libro_autor la WHERE la.id_autor = :authorId)
    """, nativeQuery = true) // Query nativa porque libro_autor no existe como entidad
    List<Book> findBooksByAuthorId(@Param("authorId") Long authorId);

    /**
     * Devuelve el resto de libros de una colección a la que pertenece el libro indicado.
     *
     * @param bookId ID del libro
     * @return Lista con el resto de libros de la colección
     */
    @Query("""
    SELECT b FROM Book b
    WHERE b.collection.id = (
        SELECT b2.collection.id FROM Book b2
        WHERE b2.id = :bookId AND b2.collection IS NOT NULL
    )
    AND b.id <> :bookId
    """)
    List<Book> findOtherBooksInSameCollection(@Param("bookId") Long bookId);

    /**
     * Devuelve todos los libros escritos por un autor.
     *
     * @param author Nombre del autor
     * @return Página con los libros escritos por el autor
     */
    Page<Book> findBooksByAuthor(String author, Pageable pageable);

    // Métodos para las recomendaciones

    /**
     * Busca libros que tengan géneros similares a los de los libros de la lista que se pasa como parámetro.
     * Ignora los libros guardados en la biblioteca personal del usuario.
     *
     * @param bookIds Lista de IDs de libros
     * @param userId ID del usuario
     * @return Lista de libros resultantes
     */
    @Query(value = """
    SELECT b.*, COUNT(*) AS generos_similares
    FROM libro b
    JOIN libro_genero lg ON b.id_libro = lg.id_libro
    WHERE lg.id_genero IN (
        SELECT lg2.id_genero
        FROM libro_genero lg2
        WHERE lg2.id_libro IN (:bookIds)
    )
    AND b.id_libro NOT IN (
        SELECT ub.id_libro
        FROM libro_biblioteca ub
        WHERE ub.id_usuario = :userId
    )
    AND b.id_libro NOT IN (:bookIds)
    AND b.activo = true
    GROUP BY b.id_libro
    ORDER BY generos_similares DESC,
             (SELECT AVG(ub2.calificacion)
              FROM libro_biblioteca ub2
              WHERE ub2.id_libro = b.id_libro) DESC
    LIMIT 30
    """, nativeQuery = true)
    List<Book> findSimilarBooksByFavoriteBooks(@Param("bookIds") List<Long> bookIds, @Param("userId") Long userId);

    /**
     * Busca libros que tengan géneros similares a los que se pasan como parámetro.
     * Ignora los libros guardados en la biblioteca personal del usuario.
     *
     * @param genreIds Lista de IDs de géneros
     * @param userId ID del usuario
     * @return Lista de libros resultantes
     */
    @Query(value = """
    SELECT b.*, COUNT(*) AS generos_similares
    FROM libro b
    JOIN libro_genero lg ON b.id_libro = lg.id_libro
    WHERE lg.id_genero IN (:genreIds)
    AND b.id_libro NOT IN (
        SELECT ub.id_libro
        FROM libro_biblioteca ub
        WHERE ub.id_usuario = :userId
    )
    AND b.activo = true
    GROUP BY b.id_libro
    ORDER BY generos_similares DESC,
             (SELECT AVG(ub2.calificacion)
              FROM libro_biblioteca ub2
              WHERE ub2.id_libro = b.id_libro) DESC
    LIMIT 30
    """, nativeQuery = true)
    List<Book> findBooksWithSimilarGenres(@Param("genreIds") List<Long> genreIds, @Param("userId") Long userId);

    /**
     * Busca libros que tengan géneros similares los leídos y mejor calificados por el usuario.
     * Ignora los libros guardados en la biblioteca personal del usuario.
     *
     * @param userId ID del usuario
     * @return Lista de libros resultantes
     */
    @Query(value = """
    SELECT b.*
    FROM libro b
    JOIN libro_genero lg ON b.id_libro = lg.id_libro
    WHERE b.id_libro NOT IN (
        SELECT ub.id_libro
        FROM libro_biblioteca ub
        WHERE ub.id_usuario = :userId
    )
    AND lg.id_genero IN (
        SELECT DISTINCT lg2.id_genero
        FROM libro_biblioteca ub2
        JOIN libro_genero lg2 ON ub2.id_libro = lg2.id_libro
        WHERE ub2.id_usuario = :userId AND ub2.estado_lectura = 2 AND ub2.calificacion >= 3
    )
    AND b.activo = true
    GROUP BY b.id_libro
    ORDER BY COUNT(lg.id_genero) DESC,
             (SELECT AVG(ub3.calificacion)
              FROM libro_biblioteca ub3
              WHERE ub3.id_libro = b.id_libro) DESC
    LIMIT 30
    """, nativeQuery = true)
    List<Book> findBooksSimilarToReadOnes(@Param("userId") Long userId);


    /**
     * Busca los libros con mejor calificación media, publicados a partir del año {@code minPublicationYear}.
     * Ignora los libros guardados en la biblioteca personal del usuario.
     *
     * @param userId ID del usuario
     * @param minPublicationYear Año mínimo de publicación
     * @return Lista de libros resultantes
     */
    @Query(value = """
    SELECT b.*
    FROM libro b
    WHERE b.activo = true
      AND b.año_publicacion >= :minPublicationYear
      AND b.id_libro NOT IN (
          SELECT ub.id_libro
          FROM libro_biblioteca ub
          WHERE ub.id_usuario = :userId
      )
    ORDER BY (
        SELECT COALESCE(AVG(ub2.calificacion), 0)
        FROM libro_biblioteca ub2
        WHERE ub2.id_libro = b.id_libro
    ) DESC
    LIMIT 30
    """, nativeQuery = true)
    List<Book> findGeneralRecommendations(@Param("userId") Long userId,
                                          @Param("minPublicationYear") int minPublicationYear);

}
