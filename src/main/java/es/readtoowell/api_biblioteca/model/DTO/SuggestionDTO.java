package es.readtoowell.api_biblioteca.model.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import es.readtoowell.api_biblioteca.model.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

/**
 * DTO que representa los detalles de una sugerencia de libro.
 */
public class SuggestionDTO {
    private Long id;
    @NotBlank(message = "El título no puede ser nulo")
    private String title;
    @NotBlank(message = "El autor no puede ser nulo")
    private String author;
    @NotNull(message = "El título no puede ser nulo")
    private int publicationYear;
    private int status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Europe/Madrid")
    private Date dateSent;
    private User user;
    private boolean active;

    // Métodos Getters y Setters

    /**
     * Devuelve el identificador de la sugerencia de libro.
     *
     * @return ID de la sugerencia
     */
    public Long getId() {
        return id;
    }

    /**
     * Establece un valor para el identificador de la sugerencia de libro.
     *
     * @param id Nuevo ID de la sugerencia
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Devuelve el título del libro sugerido.
     *
     * @return Título del libro sugerido
     */
    public String getTitle() {
        return title;
    }

    /**
     * Establece un valor para el título del libro sugerido.
     *
     * @param title Nuevo título del libro sugerido
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Devuelve el autor del libro sugerido.
     *
     * @return Autor del libro sugerido
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Establece un valor para el autor del libro sugerido.
     *
     * @param author Nuevo autor del libro sugerido
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * Devuelve el año de publicación del libro sugerido
     *
     * @return Año de publicación del libro sugerido
     */
    public int getPublicationYear() {
        return publicationYear;
    }

    /**
     * Establece un valor para el año de publicación del libro sugerido.
     *
     * @param publicationYear Nuevo año de publicación del libro sugerido
     */
    public void setPublicationYear(int publicationYear) {
        this.publicationYear = publicationYear;
    }

    /**
     * Devuelve el estado de la sugerencia.
     *
     * @return Estado de la sugerencia
     */
    public int getStatus() {
        return status;
    }

    /**
     * Establece un valor para el estado de la sugerencia.
     *
     * @param status Nuevo estado de la sugerencia
     */
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * Devuelve la fecha en la que se envió la sugerencia.
     *
     * @return Fecha en la que se envió la sugerencia
     */
    public Date getDateSent() {
        return dateSent;
    }

    /**
     * Establece un valor para la fecha en la que se envió la sugerencia.
     *
     * @param dateSent Nueva fecha en la que se envió la sugerencia
     */
    public void setDateSent(Date dateSent) {
        this.dateSent = dateSent;
    }

    /**
     * Devuelve el usuario que envió la sugerencia.
     *
     * @return Usuario que envió la sugerencia
     */
    public User getUser() {
        return user;
    }

    /**
     * Establece un valor para el usuario que envió la sugerencia.
     *
     * @param user Nuevo usuario que envió la sugerencia
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Comprueba si la sugerencia está activa o no.
     *
     * @return 'true' si está activa, 'false' en caso contrario
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Activa o desactiva la sugerencia de libro.
     *
     * @param active Nuevo valor para el campo active
     */
    public void setActive(boolean active) {
        this.active = active;
    }
}
