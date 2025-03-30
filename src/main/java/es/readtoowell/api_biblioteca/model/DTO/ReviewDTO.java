package es.readtoowell.api_biblioteca.model.DTO;

/**
 * DTO que representa los detalles de una reseña de un libro.
 */
public class ReviewDTO {
    private String username;
    private String profileName;
    private double rating;
    private String review;

    // Métodos Getters y Setters

    /**
     * Devuelve el nombre de usuario del usuario.
     *
     * @return Nombre de usuario
     */
    public String getUsername() {
        return username;
    }

    /**
     * Establece un valor para el nombre de usuario del usuario.
     *
     * @param username Nuevo nombre de usuario
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Devuelve el nombre de perfil del usuario.
     *
     * @return Nombre de perfil
     */
    public String getProfileName() {
        return profileName;
    }

    /**
     * Establece un valor para el nombre de perfil del usuario.
     *
     * @param profileName Nuevo nombre de perfil
     */
    public void setProfileName(String profileName) {
        this.profileName = profileName;
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

    /**
     * Devuelve la reseña del usuario para el libro.
     *
     * @return Reseña del usuario para el libro
     */
    public String getReview() {
        return review;
    }

    /**
     * Establece un valor para la reseña del usuario para el libro.
     *
     * @param review Nueva reseña del usuario para el libro
     */
    public void setReview(String review) {
        this.review = review;
    }
}
