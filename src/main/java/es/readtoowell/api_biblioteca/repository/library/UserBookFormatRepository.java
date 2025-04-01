package es.readtoowell.api_biblioteca.repository.library;

import es.readtoowell.api_biblioteca.model.entity.Format;
import es.readtoowell.api_biblioteca.model.entity.UserBookFormat;
import es.readtoowell.api_biblioteca.model.entity.id.UserBookFormatId;
import es.readtoowell.api_biblioteca.model.entity.UserLibraryBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserBookFormatRepository extends JpaRepository<UserBookFormat, UserBookFormatId> {
    /**
     * Busca los formatos de un libro de un usuario.
     *
     * @param user Usuario al que pertenece el libro
     * @param book Libro del que se buscan los formatos
     * @return Lista con los formatos del libro
     */
    @Query("""
    SELECT ubf
    FROM UserBookFormat ubf
    WHERE ubf.libraryBook.book.id = :book
    AND ubf.libraryBook.user.id = :user
    """)
    List<UserBookFormat> findFormatsByUserAndBook(@Param("user") Long user, @Param("book") Long book);

    /**
     * Comprueba si un usuario tiene un libro en un formato específico
     *
     * @param libraryBook Libro de la biblioteca del usuario
     * @param format Formato que se comprueba
     * @return 'true' si existe, 'false' en caso contrario
     */
    boolean existsByLibraryBookAndFormat(UserLibraryBook libraryBook, Format format);

    /**
     * Busca un libro de la biblioteca del usuario con un formato específico
     *
     * @param libraryBook Libro de la biblioteca del usuario
     * @param format Formato del libro que se busca
     * @return Un {@code Optional} con el formato y el libro. Si no se encuentra, estará vacío.
     */
    Optional<UserBookFormat> findByLibraryBookAndFormat(UserLibraryBook libraryBook, Format format);
}
