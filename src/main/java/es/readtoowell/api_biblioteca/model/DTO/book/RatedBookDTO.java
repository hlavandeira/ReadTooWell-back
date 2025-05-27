package es.readtoowell.api_biblioteca.model.DTO.book;

public class RatedBookDTO {
    private BookDTO book;
    private double averageRating;

    // Métodos Getters y Setters

    /**
     * Devuelve el libro.
     *
     * @return DTO con los datos del libro
     */
    public BookDTO getBook() {
        return book;
    }

    /**
     * Establece un nuevo valor para el libro.
     *
     * @param book Nuevo libro
     */
    public void setBook(BookDTO book) {
        this.book = book;
    }

    /**
     * Devuelve la calificación media del libro.
     *
     * @return Calificación media del libro
     */
    public double getAverageRating() {
        return averageRating;
    }

    /**
     * Establece un nuevo valor para la calificación media del libro.
     *
     * @param averageRating Nueva calificación media
     */
    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }
}
