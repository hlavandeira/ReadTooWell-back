package es.readtoowell.api_biblioteca.DTO;

import lombok.Data;

import java.util.Set;

@Data
public class BookDTO {
    private Long id;
    private String titulo;
    private String autor;
    private int añoPublicacion;
    private int numeroPaginas;
    private String editorial;
    private String sinopsis;
    private String portada;
    private String isbn;
    private boolean activo;
    private Long idColeccion;
    private Integer numColeccion;
    private Set<GenreDTO> generos;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }
    public void setAutor(String autor) {
        this.autor = autor;
    }

    public int getAñoPublicacion() {
        return añoPublicacion;
    }
    public void setAñoPublicacion(int añoPublicacion) {
        this.añoPublicacion = añoPublicacion;
    }

    public int getNumeroPaginas() {
        return numeroPaginas;
    }
    public void setNumeroPaginas(int numeroPaginas) {
        this.numeroPaginas = numeroPaginas;
    }

    public String getEditorial() {
        return editorial;
    }
    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }

    public String getSinopsis() {
        return sinopsis;
    }
    public void setSinopsis(String sinopsis) {
        this.sinopsis = sinopsis;
    }

    public String getPortada() {
        return portada;
    }
    public void setPortada(String portada) {
        this.portada = portada;
    }

    public String getIsbn() {
        return isbn;
    }
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public boolean isActivo() {
        return activo;
    }
    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public Long getIdColeccion() {
        return idColeccion;
    }
    public void setIdColeccion(Long idColeccion) {
        this.idColeccion = idColeccion;
    }

    public Integer getNumColeccion() {
        return numColeccion;
    }
    public void setNumColeccion(Integer numColeccion) {
        this.numColeccion = numColeccion;
    }

    public Set<GenreDTO> getGeneros() {
        return generos;
    }
    public void setGeneros(Set<GenreDTO> genres) {
        this.generos = genres;
    }
}
