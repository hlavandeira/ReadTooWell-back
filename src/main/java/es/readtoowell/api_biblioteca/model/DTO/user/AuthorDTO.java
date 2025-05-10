package es.readtoowell.api_biblioteca.model.DTO.user;

import es.readtoowell.api_biblioteca.model.DTO.book.BookDTO;
import es.readtoowell.api_biblioteca.model.entity.User;

import java.util.List;

/**
 * DTO que representa a un autor y los libros escritos por este.
 */
public class AuthorDTO {
    private User author;
    private List<BookDTO> books;

    // Métodos Getters y Setters

    /**
     * Devuelve el autor.
     *
     * @return Autor
     */
    public User getAuthor() {
        return author;
    }

    /**
     * Establece un valor para el autor.
     *
     * @param author Nuevo autor
     */
    public void setAuthor(User author) {
        this.author = author;
    }

    /**
     * Devuelve los libros escritos por un usuario autor.
     *
     * @return Lista de libros escritos por el autor
     */
    public List<BookDTO> getBooks() {
        return books;
    }

    /**
     * Establece valores para los libros escritos por el autor
     *
     * @param books Lista de nuevos libros escritos por el autor
     */
    public void setBooks(List<BookDTO> books) {
        this.books = books;
    }
}
