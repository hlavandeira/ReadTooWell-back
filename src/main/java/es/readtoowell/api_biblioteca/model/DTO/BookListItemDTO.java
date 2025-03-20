package es.readtoowell.api_biblioteca.model.DTO;

import es.readtoowell.api_biblioteca.model.Book;
import es.readtoowell.api_biblioteca.model.BookList;
import es.readtoowell.api_biblioteca.model.BookListItemId;

import java.util.Date;

public class BookListItemDTO {
    private BookListItemId id;
    private Book libro;
    private Date fechaAñadido;

    public BookListItemId getId() {
        return id;
    }
    public void setId(BookListItemId id) {
        this.id = id;
    }

    public Book getLibro() {
        return libro;
    }
    public void setLibro(Book libro) {
        this.libro = libro;
    }

    public Date getFechaAñadido() {
        return fechaAñadido;
    }
    public void setFechaAñadido(Date fechaAñadido) {
        this.fechaAñadido = fechaAñadido;
    }
}
