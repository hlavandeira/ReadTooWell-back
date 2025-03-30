package es.readtoowell.api_biblioteca.model.entity;

import es.readtoowell.api_biblioteca.model.entity.id.UserBookFormatId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidad que representa los formatos de los libros de un usuario.
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "formato_usuario")
public class UserBookFormat {
    @EmbeddedId
    private UserBookFormatId id = new UserBookFormatId();
    @ManyToOne
    @MapsId("userLibraryBookId")
    @JoinColumns({
            @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario"),
            @JoinColumn(name = "id_libro", referencedColumnName = "id_libro")
    })
    private UserLibraryBook libraryBook;
    @ManyToOne
    @MapsId("formatId")
    @JoinColumn(name = "id_formato", referencedColumnName = "id_formato", nullable = false)
    private Format format;

    // Métodos Getters y Setters

    /**
     * Devuelve el identificador del formato de un libro de un usuario.
     *
     * @return ID del formato del libro del usuario
     */
    public UserBookFormatId getId() {
        return id;
    }

    /**
     * Establece un valor para el identificador del formato de un libro de un usuario.
     *
     * @param id Nuevo ID del formato del libro del usuario
     */
    public void setId(UserBookFormatId id) {
        this.id = id;
    }

    /**
     * Devuelve el libro de la biblioteca del usuario con el que se relaciona el formato.
     *
     * @return Libro de la biblioteca del usuario
     */
    public UserLibraryBook getLibraryBook() {
        return libraryBook;
    }

    /**
     * Establece un valor para el libro de la biblioteca del usuario con el que se relaciona el formato.
     *
     * @param libraryBook Nuevo libro de la biblioteca del usuario
     */
    public void setLibraryBook(UserLibraryBook libraryBook) {
        this.libraryBook = libraryBook;
    }

    /**
     * Devuelve el formato relacionado con el libro.
     *
     * @return Formato del libro
     */
    public Format getFormat() {
        return format;
    }

    /**
     * Establecce un valor para el formato relacionado con el libro.
     *
     * @param format Nuevo formato del libro
     */
    public void setFormat(Format format) {
        this.format = format;
    }
}
