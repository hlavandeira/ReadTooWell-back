package es.readtoowell.api_biblioteca.model.entity.id;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

/**
 *  Representa la clave primaria compuesta de la entidad {@code UserLibraryBook}.
 *  Esta clase es utilizada en la relación entre libros y usuarios.
 */
@Embeddable
public class UserLibraryBookId implements Serializable {
    @Column(name = "id_usuario")
    private Long userId;
    @Column(name = "id_libro")
    private Long bookId;

    public UserLibraryBookId() {}
    /**
     * Constructor que inicializa los identificadores del usuario y el libro.
     *
     * @param userId Identificador del usuario.
     * @param bookId Identificador del libro.
     */
    public UserLibraryBookId(Long userId, Long bookId) {
        this.userId = userId;
        this.bookId = bookId;
    }

    // Getters y setters

    /**
     * Devuelve el identificador del usuario.
     *
     * @return ID del usuario
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * Establece un valor para el identificador del usuario.
     *
     * @param userId Nuevo ID del usuario
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * Devuelve el identificador del libro.
     *
     * @return ID del libro
     */
    public Long getBookId() {
        return bookId;
    }

    /**
     * Establece un valor para el identificador del libro.
     *
     * @param bookId Nuevo ID del libro
     */
    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserLibraryBookId that = (UserLibraryBookId) o;
        return Objects.equals(userId, that.userId) &&
                Objects.equals(bookId, that.bookId);
    }
    @Override
    public int hashCode() {
        return Objects.hash(userId, bookId);
    }
}
