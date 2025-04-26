package es.readtoowell.api_biblioteca.model.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import es.readtoowell.api_biblioteca.model.DTO.book.BookDTO;
import es.readtoowell.api_biblioteca.model.entity.id.UserLibraryBookId;

import java.util.Date;

/**
 * DTO que representa los detalles de un libro de la bilbioteca de un usuario.
 */
public class UserLibraryBookDTO {
    private UserLibraryBookId id;
    private BookDTO book;
    private int readingStatus;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Europe/Madrid")
    private Date dateStart;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Europe/Madrid")
    private Date dateFinish;
    private int progress;
    private String progressType;
    private double rating;
    private String review;

    // Métodos Getters y Setters

    /**
     * Devuelve el identificador del libro de bibliteca.
     *
     * @return ID del libro de biblioteca
     */
    public UserLibraryBookId getId() {
        return id;
    }

    /**
     * Establece un valor para el identificador del libro de biblioteca.
     *
     * @param id Nuevo ID del libro de biblioteca
     */
    public void setId(UserLibraryBookId id) {
        this.id = id;
    }

    /**
     * Devuelve el libro de la biblioteca.
     *
     * @return Libro de la biblioteca
     */
    public BookDTO getBook() {
        return book;
    }

    /**
     * Establece un valor para el libro de la biblioteca.
     *
     * @param book Nuevo libro de la biblioteca
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
     * Devuelve el progreso de lectura del libro.
     *
     * @return Progreso de lectura
     */
    public int getProgress() {
        return progress;
    }

    /**
     * Establece un valor para el progreso de lectura del libro.
     *
     * @param progress Nuevo progreso de lectura del libro
     */
    public void setProgress(int progress) {
        this.progress = progress;
    }

    /**
     * Devuelve el tipo de progreso de lectura del libro.
     *
     * @return Tipo de progreso de lectura
     */
    public String getProgressType() {
        return progressType;
    }

    /**
     * Establece un valor para el tipo de progreso de lectura del libro.
     *
     * @param progressType Nuevo tipo de progreso de lectura
     */
    public void setProgressType(String progressType) {
        this.progressType = progressType;
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
