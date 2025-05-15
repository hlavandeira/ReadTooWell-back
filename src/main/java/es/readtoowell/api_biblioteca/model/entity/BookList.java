package es.readtoowell.api_biblioteca.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Entidad qeu representa las listas de libros.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "lista")
public class BookList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_lista", unique = true, updatable = false)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario", nullable = false)
    private User user;
    @Column(name = "nombre")
    private String name;
    @Column(name = "descripcion", length = 2000)
    private String description;
    @ManyToMany
    @JoinTable(name = "genero_lista",
            joinColumns = @JoinColumn(name = "id_lista"),
            inverseJoinColumns = @JoinColumn(name = "id_genero"))
    private Set<Genre> genres = new HashSet<>();
    /**
     * Listado de libros añadidos a la lista.
     */
    @OneToMany(mappedBy = "list", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BookListItem> books = new ArrayList<>();

    // Métodos Getters y Setters

    /**
     * Devuelve el identificador de la lista.
     *
     * @return ID de la lista
     */
    public Long getId() {
        return id;
    }

    /**
     * Establece un valor para el identificador de la lista.
     *
     * @param id Nuevo ID de la lista
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Devuelve el usuario propietario de la lista.
     *
     * @return Usuario propietario de la lista
     */
    public User getUser() {
        return user;
    }

    /**
     * Establece un valor para el usuario propietario de la lista.
     *
     * @param user Nuevo usuario propietario de la lista
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Devuelve el nombre de la lista.
     *
     * @return Nombre de la lista
     */
    public String getName() {
        return name;
    }

    /**
     * Establece un valor para el nombre de la lista.
     *
     * @param name Nuevo nombre de la lista
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Devuelve la descripción de la lista.
     *
     * @return Descripción de la lista
     */
    public String getDescription() {
        return description;
    }

    /**
     * Establece un valor para la descripción de la lista.
     *
     * @param description Nueva descripción de la lista
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Devuelve los géneros asociados a la lista.
     *
     * @return Listado con los géneros asociados a la lista
     */
    public Set<Genre> getGenres() {
        return genres;
    }

    /**
     * Establece los géneros asociados a la lista.
     *
     * @param genres Nuevos géneros asociados
     */
    public void setGenres(Set<Genre> genres) {
        this.genres = genres;
    }

    /**
     * Devuelve los libros pertenecientes a la lista.
     *
     * @return Listado de los libros de la lista
     */
    public List<BookListItem> getBooks() {
        return books;
    }

    /**
     * Establece los libros de la lista.
     *
     * @param books Nuevos libros de la lista
     */
    public void setBooks(List<BookListItem> books) {
        this.books = books;
    }
}
