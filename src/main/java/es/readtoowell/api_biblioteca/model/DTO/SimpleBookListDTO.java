package es.readtoowell.api_biblioteca.model.DTO;

/**
 * DTO que representa los detalles resumidos de una lista de libros.
 */
public class SimpleBookListDTO {
    private Long id;
    private String name;

    // Métodos Getters y Setters

    /**
     * Devuelve el identificador del libro.
     *
     * @return ID del libro
     */
    public Long getId() {
        return id;
    }

    /**
     * Establece un valor para el identificador del libro.
     *
     * @param id Nuevo ID del libro
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
}
