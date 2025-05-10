package es.readtoowell.api_biblioteca.model.DTO.book;

import jakarta.validation.constraints.NotBlank;

public class CollectionDTO {
    private Long id;
    @NotBlank(message="El nombre no puede ser nulo")
    private String name;

    // Métodos Getters y Setters

    /**
     * Devuelve el ID de la colección.
     *
     * @return ID de la colección
     */
    public Long getId() {
        return id;
    }

    /**
     * Establece un nuevo valor para el ID de la colección.
     *
     * @param id Nuevo valor para el ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Devuelve el nombre de la colección.
     *
     * @return Nombre de la colección
     */
    public String getName() {
        return name;
    }

    /**
     * Establece un nuevo valor para el nombre de la colección.
     *
     * @param name Nuevo valor para el nombre
     */
    public void setName(String name) {
        this.name = name;
    }
}
