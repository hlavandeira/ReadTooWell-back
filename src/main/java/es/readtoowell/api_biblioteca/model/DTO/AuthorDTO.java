package es.readtoowell.api_biblioteca.model.DTO;

import es.readtoowell.api_biblioteca.model.User;

import java.util.Set;

public class AuthorDTO {
    private User autor;
    private Set<BookDTO> libros;

    public User getAutor() {
        return autor;
    }
    public void setAutor(User autor) {
        this.autor = autor;
    }

    public Set<BookDTO> getLibros() {
        return libros;
    }
    public void setLibros(Set<BookDTO> libros) {
        this.libros = libros;
    }
}
