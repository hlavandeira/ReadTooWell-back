package es.readtoowell.api_biblioteca.model.DTO.book;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import java.util.List;

/**
 * DTO que representa los detalles completos de un libro.
 * Incluye la calificaciones, reseñas y las listas del usuario en las que aparece el libro.
 */
public class BookDetailsDTO {
    private BookDTO book;
    private int readingStatus;
    private double rating;
    private String review;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Europe/Madrid")
    private Date dateStart;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Europe/Madrid")
    private Date dateFinish;
    private double averageRating;
    private String collectionName;
    private List<SimpleBookListDTO> lists;
    private List<ReviewDTO> otherUsersReviews;
    private boolean saved;

    // Métodos Getters y Setters

    /**
     * Devuelve el valor del libro.
     *
     * @return Libro
     */
    public BookDTO getBook() {
        return book;
    }

    /**
     * Establece un valor para el libro.
     *
     * @param book Nuevo libro
     */
    public void setBook(BookDTO book) {
        this.book = book;
    }

    /**
     * Devuelve el estado de lectura del libro.
     *
     * @return Estado de lectura del libro
     */
    public int getReadingStatus() {
        return readingStatus;
    }

    /**
     * Establece un valor para el estado de lectura del libro.
     *
     * @param readingStatus Nuevo estado de lectura
     */
    public void setReadingStatus(int readingStatus) {
        this.readingStatus = readingStatus;
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

    /**
     * Devuelve la fecha de inicio de lectura del libro.
     *
     * @return Fecha de inicio del libro
     */
    public Date getDateStart() {
        return dateStart;
    }

    /**
     * Establece un valor para la fecha de inicio de lectura del libro.
     *
     * @param dateStart Nueva fecha de inicio
     */
    public void setDateStart(Date dateStart) {
        this.dateStart = dateStart;
    }

    /**
     * Devuelve la fecha de fin de lectura del libro.
     *
     * @return Fecha de fin de lectura
     */
    public Date getDateFinish() {
        return dateFinish;
    }

    /**
     * Establece un valor para le fecha de fin de lectura del libro.
     *
     * @param dateFinish Nueva fecha de fin
     */
    public void setDateFinish(Date dateFinish) {
        this.dateFinish = dateFinish;
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
     * Establece un valor para la calificación media del libro
     *
     * @param averageRating Nueva calificación media
     */
    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    /**
     * Devuelve el nombre de la colección del libro.
     *
     * @return Nombre de la colección
     */
    public String getCollectionName() {
        return collectionName;
    }

    /**
     * Establece un valor para el nombre de la colección del libro.
     *
     * @param collectionName Nuevo nombre de la colección
     */
    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }

    /**
     * Devuelve las listas del usuario en las que está el libro.
     *
     * @return Listas del usuario a las que pertenece el libro
     */
    public List<SimpleBookListDTO> getLists() {
        return lists;
    }

    /**
     * Establece valores para las listas en las que está el libro.
     *
     * @param lists Lista con las nuevas listas del usuario
     */
    public void setLists(List<SimpleBookListDTO> lists) {
        this.lists = lists;
    }

    /**
     * Devuelve las reseñas para el libro del resto de usuarios.
     *
     * @return Lista con las reseñas de otros usuarios
     */
    public List<ReviewDTO> getOtherUsersReviews() {
        return otherUsersReviews;
    }

    /**
     * Establece valores para las reseñas de otros usuarios.
     *
     * @param otherUsersReviews Lista con las nuevas reseñas de otros usuarios
     */
    public void setOtherUsersReviews(List<ReviewDTO> otherUsersReviews) {
        this.otherUsersReviews = otherUsersReviews;
    }

    /**
     * Comprueba si el libro está guardado en la biblioteca del usuario.
     *
     * @return 'true' si pertenece a la biblioteca, 'false' en caso contrario
     */
    public boolean isSaved() {
        return saved;
    }

    /**
     * Cambia el valor de saved sobre si el libro está guardado en la biblioteca del usuario.
     *
     * @param saved Nuevo valor para el campo saved
     */
    public void setSaved(boolean saved) {
        this.saved = saved;
    }
}
