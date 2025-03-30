package es.readtoowell.api_biblioteca.model.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import es.readtoowell.api_biblioteca.model.entity.Book;
import es.readtoowell.api_biblioteca.model.entity.id.BookListItemId;

import java.util.Date;

/**
 * DTO que representa los detalles de un libro de una lista.
 */
public class BookListItemDTO {
    private BookListItemId id;
    private Book book;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Europe/Madrid")
    private Date dateAdded;

    // Métodos Getters y Setters

    /**
     * Devuelve el identificador del libro de una lista.
     *
     * @return ID del libro de una lista
     */
    public BookListItemId getId() {
        return id;
    }

    /**
     * Establece el identificador del libro de una lista.
     *
     * @param id Nuevo ID del libro de una lista
     */
    public void setId(BookListItemId id) {
        this.id = id;
    }

    /**
     * Devuelve el propio libro de la lista.
     *
     * @return Libro de la lista
     */
    public Book getBook() {
        return book;
    }

    /**
     * Establece un valor para el libro de la lista.
     *
     * @param book Nuevo libro de la lista
     */
    public void setBook(Book book) {
        this.book = book;
    }

    /**
     * Devuelve la fecha en la que se añadió el libro a la lista.
     *
     * @return Fecha en la que se añadió el libro
     */
    public Date getDateAdded() {
        return dateAdded;
    }

    /**
     * Establecce un valor para la fecha en la que se añadió el libro.
     *
     * @param dateAdded Nueva fecha en la que se añadió
     */
    public void setDateAdded(Date dateAdded) {
        this.dateAdded = dateAdded;
    }
}
