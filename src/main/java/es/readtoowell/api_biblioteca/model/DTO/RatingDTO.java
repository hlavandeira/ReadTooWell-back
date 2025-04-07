package es.readtoowell.api_biblioteca.model.DTO;

/**
 * DTO que representa los datos de calificación de un libro
 */
public class RatingDTO {
    private UserLibraryBookDTO libraryBook;
    private double averageRating;

    // Métodos Getters y Setters

    /**
     * Devuelve el libro puntuado.
     *
     * @return Libro puntuado
     */
    public UserLibraryBookDTO getLibraryBook() {
        return libraryBook;
    }

    /**
     * Establece un valor para el libro puntuado.
     *
     * @param libraryBook Nuevo valor para el libro puntuado
     */
    public void setLibraryBook(UserLibraryBookDTO libraryBook) {
        this.libraryBook = libraryBook;
    }

    /**
     * Devuelve la puntuación media de un libro.
     *
     * @return Puntuación media del libro
     */
    public double getAverageRating() {
        return averageRating;
    }

    /**
     * Establece un valor para la puntuación media del libro.
     *
     * @param averageRating Nueva puntuación media
     */
    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }
}
