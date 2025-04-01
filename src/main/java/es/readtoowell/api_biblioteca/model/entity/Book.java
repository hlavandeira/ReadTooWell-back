package es.readtoowell.api_biblioteca.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Entidad que representa los libros.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "libro")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_libro", unique = true, updatable = false)
    private Long id;
    @Column(name = "titulo")
    private String title;
    @Column(name = "autor")
    private String author;
    @Column(name = "año_publicacion")
    private int publicationYear;
    @Column(name = "numero_paginas")
    private int pageNumber;
    @Column(name = "editorial")
    private String publisher;
    @Column(name = "sinopsis", length = 2000)
    private String synopsis;
    @Column(name = "portada")
    private String cover;
    private String isbn;
    @Column(name = "activo")
    private boolean active;
    @ManyToOne
    @JoinColumn(name = "id_coleccion", referencedColumnName = "id_coleccion", nullable = true)
    private Collection collection;
    @Column(name = "num_coleccion")
    private Integer numCollection;
    @ManyToMany
    @JoinTable(name = "libro_genero",
            joinColumns = @JoinColumn(name = "id_libro"),
            inverseJoinColumns = @JoinColumn(name = "id_genero"))
    private Set<Genre> genres = new HashSet<>();

    /**
     * Simula el borrado de un libro, poniendo el atributo 'activo' a 'false'.
     */
    public void delete() {
        this.active = false;
    }

    // Métodos Getters y Setters

    /**
     * Devuelve el identificador del libro.
     *
     * @return ID del libro
     */
    public Long getId() {
        return id;
    }

    /**
     * Establece un valor para el identificador del libro.
     *
     * @param id Nuevo ID del libro
     */
    public void setId(Long id) {
        this.id = id;
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
     * @param title Nuevo título del libro
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Devuelve el autor del libro.
     *
     * @return Autor del libro
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Establece un valor para el autor del libro.
     *
     * @param author Nuevo autor del libro
     */
    public void setAuthor(String author) {
        this.author = author;
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
     * @param publicationYear Nuevo año de publicación del libro
     */
    public void setPublicationYear(int publicationYear) {
        this.publicationYear = publicationYear;
    }

    /**
     * Devuelve el número de páginas del libro.
     *
     * @return Número de páginas del libro
     */
    public int getPageNumber() {
        return pageNumber;
    }

    /**
     * Establece un valor para el número de páginas del libro.
     *
     * @param pageNumber Nuevo número de páginas
     */
    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    /**
     * Devuelve la editorial del libro.
     *
     * @return Editorial del libro
     */
    public String getPublisher() {
        return publisher;
    }

    /**
     * Establece un valor para la editorial del libro.
     *
     * @param publisher Nueva editorial del libro
     */
    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    /**
     * Devuelve el valor de la sinopsis del libro.
     *
     * @return Sinopsis del libro
     */
    public String getSynopsis() {
        return synopsis;
    }

    /**
     * Establece un valor para la sinopsis del libro.
     *
     * @param synopsis Nueva sinopsis del libro
     */
    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    /**
     * Devuelve el valor de la URL de la portada del libro.
     *
     * @return URL de la portada
     */
    public String getCover() {
        return cover;
    }

    /**
     * Establece un valor para la URL de la portada del libro.
     *
     * @param cover Nueva URL de la portada
     */
    public void setCover(String cover) {
        this.cover = cover;
    }

    /**
     * Devuelve el ISBN del libro.
     *
     * @return ISBN del libro
     */
    public String getIsbn() {
        return isbn;
    }

    /**
     * Establece un valor para el ISBN del libro.
     *
     * @param isbn Nuevo ISBN del libro
     */
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    /**
     * Devuelve si el libro está activo o no.
     *
     * @return 'true' si está activo, 'false' en caso contrario
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Activa o desactiva el libro.
     *
     * @param active Nuevo valor para el campo active
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Devuelve el ID de la colección, siempre que el libro pertenezca a una.
     *
     * @return ID de la colección a la que pertenece el libro, 'null' si no pertenece a ninguna.
     */
    public Long getCollectionId() {
        if (collection != null) {
            return collection.getId();
        }
        return null;
    }

    /**
     * Establece un valor para la colección del libro.
     *
     * @param collection Nueva colección del libro
     */
    public void setCollection(Collection collection) {
        this.collection = collection;
    }

    /**
     * Devuelve el número de la colección del libro.
     *
     * @return Número de la colección del libro
     */
    public Integer getNumCollection() {
        return numCollection;
    }

    /**
     * Establece un valor para el número de la colección del libro.
     *
     * @param numCollection Nuevo número de la colección
     */
    public void setNumCollection(Integer numCollection) {
        this.numCollection = numCollection;
    }

    /**
     * Devuelve los géneros asociados al libro.
     *
     * @return Lista con los géneros asociados al libro
     */
    public Set<Genre> getGenres() {
        return genres;
    }

    /**
     * Establece los géneros asociados al libro.
     *
     * @param genres Nueva lista de géneros asociados
     */
    public void setGenres(Set<Genre> genres) {
        this.genres = genres;
    }
}
