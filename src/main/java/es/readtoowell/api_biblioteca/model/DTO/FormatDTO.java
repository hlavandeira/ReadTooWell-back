package es.readtoowell.api_biblioteca.model.DTO;

/**
 * DTO que representa los detalles de un formato.
 */
public class FormatDTO {
    private Long id;
    private String name;

    // Métodos Getters y Setters

    /**
     * Devuelve el identificador del formato.
     *
     * @return ID del formato
     */
    public Long getId() {
        return id;
    }

    /**
     * Establece un valor para el identificador del formato.
     *
     * @param id Nuevo ID del formato
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Devuelve el nombre del formato.
     *
     * @return Nombre del formato
     */
    public String getName() {
        return name;
    }

    /**
     * Establece un valor para el nombre del formato.
     *
     * @param name Nuevo nombre del formato
     */
    public void setName(String name) {
        this.name = name;
    }
}
