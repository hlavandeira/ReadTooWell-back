package es.readtoowell.api_biblioteca.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidad que representa los libros que pertenecen a una solicitud.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "libro_solicitud")
public class RequestBook {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, updatable = false)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "id_solicitud", nullable = false)
    private AuthorRequest request;
    @Column(name = "titulo")
    private String title;
    @Column(name = "año_publicacion")
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
     * Devuelve la solicitud a la que pertenece el libro.
     *
     * @return Solicitud a la que pertenece el libro
     */
    public AuthorRequest getRequest() {
        return request;
    }

    /**
     * Establece un valor para la solicitud a la que pertenece el libro.
     *
     * @param request Nueva solicitud para el libro
     */
    public void setRequest(AuthorRequest request) {
        this.request = request;
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
