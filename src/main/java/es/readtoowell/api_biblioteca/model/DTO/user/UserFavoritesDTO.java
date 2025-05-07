package es.readtoowell.api_biblioteca.model.DTO.user;

import es.readtoowell.api_biblioteca.model.entity.Book;
import es.readtoowell.api_biblioteca.model.entity.Genre;
import es.readtoowell.api_biblioteca.model.entity.User;

import java.util.List;

/**
 * DTO que representa los libros y géneros favoritos de un usuario.
 */
public class UserFavoritesDTO {
    private User user;
    private List<Book> favoriteBooks;
    private List<Genre> favoriteGenres;

    // Métodos Getters y Setters

    /**
     * Devuelve el usuario.
     *
     * @return Usuario
     */
    public User getUser() {
        return user;
    }

    /**
     * Establece un valor para el usuario.
     *
     * @param user Nuevo usuario
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Devuelve los libros favoritos del usuario.
     *
     * @return Listado con los libros favoritos del usuario
     */
    public List<Book> getFavoriteBooks() {
        return favoriteBooks;
    }

    /**
     * Establece los libros favoritos del usuario.
     *
     * @param favoriteBooks Listado con los nuevos libros favoritos
     */
    public void setFavoriteBooks(List<Book> favoriteBooks) {
        this.favoriteBooks = favoriteBooks;
    }

    /**
     * Devuelve los géneros favoritos del usuario.
     *
     * @return Listado con los géneros favoritos del usuario
     */
    public List<Genre> getFavoriteGenres() {
        return favoriteGenres;
    }

    /**
     * Establece los géneros favoritos del usuario.
     *
     * @param favoriteGenres Listado con los nuevos géneros favoritos
     */
    public void setFavoriteGenres(List<Genre> favoriteGenres) {
        this.favoriteGenres = favoriteGenres;
    }
}
