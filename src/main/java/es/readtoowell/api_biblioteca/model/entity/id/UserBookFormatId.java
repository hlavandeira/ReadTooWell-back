package es.readtoowell.api_biblioteca.model.entity.id;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

/**
 *  Representa la clave primaria compuesta de la entidad {@code UserBookFormat}.
 *  Esta clase es utilizada en la relación entre libros de la biblioteca de un usuario y formatos.
 */
@Embeddable
public class UserBookFormatId implements Serializable {
    private UserLibraryBookId userLibraryBookId;
    private Long formatId;

    public UserBookFormatId() {}
    /**
     * Constructor que inicializa los identificadores del formato y del libro del usuario.
     *
     * @param userLibraryBookId Identificador del libro de la biblioteca del usuario.
     * @param formatId Identificador del formato del libro.
     */
    public UserBookFormatId(UserLibraryBookId userLibraryBookId, Long formatId) {
        this.userLibraryBookId = userLibraryBookId;
        this.formatId = formatId;
    }

    // Métodos Getters y Setters

    /**
     * Devuelve el identificador del libro de la biblioteca del usuario.
     *
     * @return ID del libro de la biblioteca del usuario
     */
    public UserLibraryBookId getUserLibraryBookId() {
        return userLibraryBookId;
    }

    /**
     * Establece un valor para el identificador del libro de la biblioteca del usuario.
     *
     * @param userLibraryBookId Nuevo ID del libro de la biblioteca del usuario
     */
    public void setUserLibraryBookId(UserLibraryBookId userLibraryBookId) {
        this.userLibraryBookId = userLibraryBookId;
    }

    /**
     * Devuelve el identificador del formato del libro.
     *
     * @return ID del formato del libro
     */
    public Long getFormatId() {
        return formatId;
    }

    /**
     * Establece un valor para el identificador del formato del libro.
     *
     * @param formatId Nuevo ID del formato del libro
     */
    public void setFormatId(Long formatId) {
        this.formatId = formatId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserBookFormatId that = (UserBookFormatId) o;
        return Objects.equals(userLibraryBookId, that.userLibraryBookId) &&
                Objects.equals(formatId, that.formatId);
    }
    @Override
    public int hashCode() {
        return Objects.hash(userLibraryBookId, formatId);
    }
}
