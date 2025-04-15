package es.readtoowell.api_biblioteca.model.entity;

import es.readtoowell.api_biblioteca.model.entity.id.BookListItemId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.Objects;

/**
 * Entidad que representa los libros pertenecientes a una lista.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "libro_lista")
public class BookListItem {
    @EmbeddedId
    private BookListItemId id;
    @ManyToOne
    @MapsId("listId")
    @JoinColumn(name = "id_lista")
    private BookList list;
    @ManyToOne
    @MapsId("bookId")
    @JoinColumn(name = "id_libro")
    private Book book;
    @Column(name = "fecha_añadido")
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
     * Devuelve la lista a la que pertenece el libro.
     *
     * @return Lista a la que pertenece el libro
     */
    public BookList getList() {
        return list;
    }

    /**
     * Establece la lista a la que pertenece el libro.
     *
     * @param list Nueva lista a la que pertenece el libro
     */
    public void setList(BookList list) {
        this.list = list;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookListItem that = (BookListItem) o;
        return id.equals(that.id) && list.equals(that.list) && book.equals(that.book);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, list, book);
    }
}