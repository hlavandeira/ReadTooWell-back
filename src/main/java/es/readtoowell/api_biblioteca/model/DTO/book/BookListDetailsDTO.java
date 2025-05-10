package es.readtoowell.api_biblioteca.model.DTO.book;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * DTO que representa los detalles de una lista con paginación para los libros.
 */
public class BookListDetailsDTO {
    private Long id;
    @NotBlank(message = "El nombre de la lista no puede ser nulo")
    private String name;
    @Size(max = 2000, message = "La descripción no puede superar los 2000 caracteres")
    private String description;
    private List<GenreDTO> genres;
    private Page<BookListItemDTO> books;

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
     * @return Listado con los géneros asociados
     */
    public List<GenreDTO> getGenres() {
        return genres;
    }

    /**
     * Establece los géneros asociados a una lista.
     *
     * @param genres Nuevos géneros asociados a la lista
     */
    public void setGenres(List<GenreDTO> genres) {
        this.genres = genres;
    }

    /**
     * Devuelve los libros pertenecientes a una lista.
     *
     * @return Listado con los libros de la lista
     */
    public Page<BookListItemDTO> getBooks() {
        return books;
    }

    /**
     * Establece los libros pertenecientes a una lista.
     *
     * @param books Nuevos libros pertenecientes a la lista
     */
    public void setBooks(Page<BookListItemDTO> books) {
        this.books = books;
    }
}
