package es.readtoowell.api_biblioteca.model.entity;

import es.readtoowell.api_biblioteca.model.entity.id.UserLibraryBookId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad que representa los libros de la biblioteca de un usuario.
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "libro_biblioteca")
public class UserLibraryBook {
    @EmbeddedId
    private UserLibraryBookId id;
    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario", nullable = false)
    private User user;
    @ManyToOne
    @MapsId("bookId")
    @JoinColumn(name = "id_libro", referencedColumnName = "id_libro", nullable = false)
    private Book book;
    @OneToMany(mappedBy = "libraryBook", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserBookFormat> formats = new ArrayList<>();
    @Column(name = "estado_lectura")
    private int readingStatus;
    @Column(name = "fecha_inicio")
    private Date dateStart;
    @Column(name = "fecha_fin")
    private Date dateFinish;
    @Column(name = "progreso")
    private int progress;
    @Column(name = "tipo_progreso")
    private String progressType;
    @Column(name = "calificacion")
    private double rating;
    @Column(name = "reseña", length = 2000)
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
     * Devuelve el usuario propietario del la biblioteca.
     *
     * @return Usuario propietario de la biblioteca
     */
    public User getUser() {
        return user;
    }

    /**
     * Establece un valor para el usuario propietario de la biblioteca.
     *
     * @param user Nuevo usuario propietario de la biblioteca
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Devuelve el libro de la biblioteca.
     *
     * @return Libro de la biblioteca
     */
    public Book getBook() {
        return book;
    }

    /**
     * Establece un valor para el libro de la biblioteca.
     *
     * @param book Nuevo libro de la biblioteca
     */
    public void setBook(Book book) {
        this.book = book;
    }

    /**
     * Devuelve los formatos en los que el usuario tiene el libro.
     *
     * @return Lista con los formatos del libro del usuario
     */
    public List<UserBookFormat> getFormats() {
        return formats;
    }

    /**
     * Establece los formatos en los que el usuario tiene el libro.
     *
     * @param formats Lista con los nuevos formatos del libro
     */
    public void setFormats(List<UserBookFormat> formats) {
        this.formats = formats;
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
