package es.readtoowell.api_biblioteca.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad que representa las solicitudes de autor.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "solicitud_autor")
public class AuthorRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_solicitud", unique = true, updatable = false)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario", nullable = false)
    private User user;
    @Column(name = "nombre")
    private String name;
    @Column(name = "biografia", length = 2000)
    private String biography;
    @Column(name = "fecha_enviada")
    private Date dateSent;
    @Column(name = "activo")
    private boolean active;
    @Column(name = "estado")
    private int status;
    /**
     * Lista de libros añadidos a la solicitud de autor.
     */
    @OneToMany(mappedBy = "request", cascade = CascadeType.ALL)
    private List<RequestBook> books = new ArrayList<>();

    // Métodos Getters y Setters

    /**
     * Devuelve el identificador de la solicitud de verificación de autor.
     *
     * @return ID de la solicitud
     */
    public Long getId() {
        return id;
    }

    /**
     * Establece un valor para el identificador de la solicitud de verificación de autor.
     *
     * @param id Nuevo ID de la solicitud
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Devuelve el usuario que envió la solicitud.
     *
     * @return Usuario que envió la solicitud
     */
    public User getUser() {
        return user;
    }

    /**
     * Establece un valor para el usuario de la solicitud.
     *
     * @param user Nuevo usuario de la solicitud
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Devuelve el nombre del autor.
     *
     * @return Nombre del autor
     */
    public String getName() {
        return name;
    }

    /**
     * Establece un valor para el nombre del autor.
     *
     * @param name Nuevo nombre del autor
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Devuelve la biografía del autor.
     *
     * @return Biografía del autor
     */
    public String getBiography() {
        return biography;
    }

    /**
     * Establece un valor para la biografía del autor.
     *
     * @param biography Nueva biografía del autor
     */
    public void setBiography(String biography) {
        this.biography = biography;
    }

    /**
     * Devuelve la fecha en la que envió la solicitud.
     *
     * @return Fecha en la que se envió la solicitud
     */
    public Date getDateSent() {
        return dateSent;
    }

    /**
     * Establece un valor para la fecha en la que se envió la solicitud.
     *
     * @param dateSent Nueva fecha de la solicitud
     */
    public void setDateSent(Date dateSent) {
        this.dateSent = dateSent;
    }

    /**
     * Devuelve si la solicitud está activa o no.
     *
     * @return 'true' si está activo, 'false' en caso contrario
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Activa o desactiva la solicitud.
     *
     * @param active Nuevo valor para el campo activo
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Devuelve el estado de la solicitud.
     *
     * @return Estado de la solicitud
     */
    public int getStatus() {
        return status;
    }

    /**
     * Establece un valor para el estado de la solicitud.
     *
     * @param status Nuevo estado de la solicitud
     */
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * Devuelve los libros asociados a la solicitud de autor.
     *
     * @return Libros asociados a la solicitud
     */
    public List<RequestBook> getBooks() {
        return books;
    }

    /**
     * Establece los libros asociados a la solicitud.
     *
     * @param books Libros asociados a la solicitud
     */
    public void setBooks(List<RequestBook> books) {
        this.books = books;
    }
}
