package es.readtoowell.api_biblioteca.model.DTO.book;

/**
 * DTO que representa los detalles resumidos de un libro.
 */
public class SimpleBookDTO {
    private Long id;
    private String title;
    private String author;
    private String cover;
    private double rating;

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
     * @param title Nuevo título del libro
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Devuelve el autor del libro.
     *
     * @return Autor del libro
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Establece un valor para el autor del libro.
     *
     * @param author Nuevo autor del libro
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * Devuelve el valor de la URL de la portada del libro.
     *
     * @return URL de la portada
     */
    public String getCover() {
        return cover;
    }

    /**
     * Establece un valor para la URL de la portada del libro.
     *
     * @param cover Nueva URL de la portada
     */
    public void setCover(String cover) {
        this.cover = cover;
    }

    /**
     * Devuelve la calificación del usuario para el libro.
     *
     * @return Calificación del usuario para el libro
     */
    public double getRating() {
        return rating;
    }

    /**
     * Establece un valor para la calificación del usuario para el libro.
     *
     * @param rating Nueva calificación del usuario para el libro
     */
    public void setRating(double rating) {
        this.rating = rating;
    }
}
