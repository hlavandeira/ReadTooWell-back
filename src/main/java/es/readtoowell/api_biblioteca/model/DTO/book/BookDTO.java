package es.readtoowell.api_biblioteca.model.DTO.book;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.util.List;

/**
 * DTO que representa los detalles de un libro.
 */
public class BookDTO {
    private Long id;
    @NotBlank(message="El título no puede ser nulo")
    private String title;
    @NotBlank(message="El autor no puede ser nulo")
    private String author;
    @Positive(message="El año no puede ser un número negativo")
    private int publicationYear;
    @Positive(message="El número de páginas no puede ser un número negativo")
    private int pageNumber;
    private String publisher;
    @Size(max = 2000, message = "La sinopsis no puede superar los 2000 caracteres")
    private String synopsis;
    private String cover;
    @Pattern(regexp="^(\\d{10}|\\d{13})$") // El ISBN debe tener 10 o 13 números.
    private String isbn;
    private boolean active;
    private Long collectionId;
    private Integer numCollection;
    private List<GenreDTO> genres;

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
        return collectionId;
    }

    /**
     * Establece un valor para el ID de la colección del libro.
     *
     * @param collectionId Nuevo ID de la colección del libro
     */
    public void setCollectionId(Long collectionId) {
        this.collectionId = collectionId;
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
    public List<GenreDTO> getGenres() {
        return genres;
    }

    /**
     * Establece los géneros asociados al libro.
     *
     * @param genres Nueva lista de géneros asociados
     */
    public void setGenres(List<GenreDTO> genres) {
        this.genres = genres;
    }
}
