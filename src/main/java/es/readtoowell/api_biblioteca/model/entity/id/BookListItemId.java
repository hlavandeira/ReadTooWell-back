package es.readtoowell.api_biblioteca.model.entity.id;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

/**
 *  Representa la clave primaria compuesta de la entidad {@code BookListItem}.
 *  Esta clase es utilizada en la relación entre listas de libros y libros.
 */
@Embeddable
public class BookListItemId implements Serializable {
    private Long listId;
    private Long bookId;

    public BookListItemId() {}
    /**
     * Constructor que inicializa los identificadores de la lista y del libro.
     *
     * @param listId Identificador de la lista de libros.
     * @param bookId Identificador del libro dentro de la lista.
     */
    public BookListItemId(Long listId, Long bookId) {
        this.listId = listId;
        this.bookId = bookId;
    }

    // Métodos Getters y Setters

    /**
     * Devuelve el identificador de la lista a la que pertenece un libro.
     *
     * @return ID de la lista
     */
    public Long getListId() {
        return listId;
    }

    /**
     * Establece el identificador de la lista a la que pertenece un libro.
     *
     * @param listId Nuevo ID de la lista
     */
    public void setListId(Long listId) {
        this.listId = listId;
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
     * Establece el identificador del libro.
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
        BookListItemId that = (BookListItemId) o;
        return Objects.equals(listId, that.listId) && Objects.equals(bookId, that.bookId);
    }
    @Override
    public int hashCode() {
        return Objects.hash(listId, bookId);
    }
}
