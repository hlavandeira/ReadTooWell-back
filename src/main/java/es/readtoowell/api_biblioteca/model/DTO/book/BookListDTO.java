package es.readtoowell.api_biblioteca.model.DTO.book;

import es.readtoowell.api_biblioteca.model.DTO.user.UserDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Set;

/**
 * DTO que representa los detalles de una lista de libros.
 */
public class BookListDTO {
    private Long id;
    private UserDTO user;
    @NotBlank(message = "El nombre de la lista no puede ser nulo")
    private String name;
    @Size(max = 2000, message = "La descripción no puede superar los 2000 caracteres")
    private String description;
    private Set<GenreDTO> genres;
    private Set<BookListItemDTO> books;

    // Métodos Getters y Setters

    /**
     * Devuelve el identificador de la lista.
     *
     * @return ID de la lista
     */
    public Long getId() {
        return id;
    }

    /**
     * Establece un valor para el identificador de la lista.
     *
     * @param id Nuevo ID de la lista
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Devuelve el usuario propietario de la lista.
     *
     * @return Usuario propietario de la lista
     */
    public UserDTO getUser() {
        return user;
    }

    /**
     * Establece un valor para el usuario propietario de la lista.
     *
     * @param user Nuevo usuario propietario de la lista
     */
    public void setUser(UserDTO user) {
        this.user = user;
    }

    /**
     * Devuelve el nombre de la lista.
     *
     * @return Nombre de la lista
     */
    public String getName() {
        return name;
    }

    /**
     * Establece un valor para el nombre de la lista.
     *
     * @param name Nuevo nombre de la lista
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Devuelve la descripción de la lista.
     *
     * @return Descripción de la lista
     */
    public String getDescription() {
        return description;
    }

    /**
     * Establece un valor para la descripción de la lista.
     *
     * @param description Nueva descripción de la lista
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Devuelve los géneros asociados a la lista.
     *
     * @return Listado con los géneros asociados a la lista
     */
    public Set<GenreDTO> getGenres() {
        return genres;
    }

    /**
     * Establece los géneros asociados a la lista.
     *
     * @param genres Nuevos géneros asociados
     */
    public void setGenres(Set<GenreDTO> genres) {
        this.genres = genres;
    }

    /**
     * Devuelve los libros pertenecientes a la lista.
     *
     * @return Listado de los libros de la lista
     */
    public Set<BookListItemDTO> getBooks() {
        return books;
    }

    /**
     * Establece los libros de la lista.
     *
     * @param books Nuevos libros de la lista
     */
    public void setBooks(Set<BookListItemDTO> books) {
        this.books = books;
    }

}
