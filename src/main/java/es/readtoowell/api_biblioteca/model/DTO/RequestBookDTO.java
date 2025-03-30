package es.readtoowell.api_biblioteca.model.DTO;

/**
 * DTO que representa los detalles del libro de una solicitud de autor.
 */
public class RequestBookDTO {
    private Long id;
    private String title;
    private int publicationYear;

    // Métodos Getters y Setters

    /**
     * Devuelve el identificador del libro de una solicitud.
     *
     * @return ID del libro de solicitud
     */
    public Long getId() {
        return id;
    }

    /**
     * Establece un valor para el identificador del libro de una solicitud.
     *
     * @param id Nuevo ID del libro de solicitud
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Devuelve el título del libro.
     *
     * @return Título del libro
     */
    public String getTitle() {
        return title;
    }

    /**
     * Establece un valor para el título del libro.
     *
     * @param title Nuevo valor para el título
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Devuelve el año de publicación del libro.
     *
     * @return Año de publicación del libro
     */
    public int getPublicationYear() {
        return publicationYear;
    }

    /**
     * Establece un valor para el año de publicación del libro.
     *
     * @param publicationYear Nuevo valor para el año de publicación
     */
    public void setPublicationYear(int publicationYear) {
        this.publicationYear = publicationYear;
    }
}
