package es.readtoowell.api_biblioteca.model.DTO.book;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO que representa los detalles de un género.
 */
public class GenreDTO {
    private Long id;
    @NotBlank(message = "El nombre del género no puede ser nulo")
    private String name;

    // Métodos Getters y Setters

    /**
     * Devuelve el identificador del género.
     *
     * @return ID del género
     */
    public Long getId() {
        return id;
    }

    /**
     * Establece un valor para el identificador del género.
     *
     * @param id Nuevo ID del género
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Devuelve el nombre del género.
     *
     * @return Nombre del género
     */
    public String getName() {
        return name;
    }

    /**
     * Establece un valor para el nombre del género.
     *
     * @param name Nuevo nombre del género
     */
    public void setName(String name) {
        this.name = name;
    }
}
