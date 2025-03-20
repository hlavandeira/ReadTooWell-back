package es.readtoowell.api_biblioteca.model.DTO;

import es.readtoowell.api_biblioteca.model.Book;
import es.readtoowell.api_biblioteca.model.Genre;
import es.readtoowell.api_biblioteca.model.User;

import java.util.Set;

public class UserFavoritesDTO {
    private User user;
    private Set<Book> librosFavoritos;
    private Set<Genre> generosFavoritos;

    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }

    public Set<Book> getLibrosFavoritos() {
        return librosFavoritos;
    }
    public void setLibrosFavoritos(Set<Book> librosFavoritos) {
        this.librosFavoritos = librosFavoritos;
    }

    public Set<Genre> getGenerosFavoritos() {
        return generosFavoritos;
    }
    public void setGenerosFavoritos(Set<Genre> generosFavoritos) {
        this.generosFavoritos = generosFavoritos;
    }
}
