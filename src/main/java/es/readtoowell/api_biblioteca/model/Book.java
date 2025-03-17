package es.readtoowell.api_biblioteca.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

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
    private String titulo;
    private String autor;
    @Column(name = "año_publicacion")
    private int añoPublicacion;
    @Column(name = "numero_paginas")
    private int numeroPaginas;
    private String editorial;
    @Column(name = "sinopsis", length = 2000)
    private String sinopsis;
    private String portada;
    private String isbn;
    private boolean activo;
    @ManyToOne
    @JoinColumn(name = "id_coleccion", referencedColumnName = "id_coleccion", nullable = true)
    private Collection coleccion;
    @Column(name = "num_coleccion")
    private Integer numColeccion;
    @ManyToMany
    @JoinTable(name = "libro_genero",
            joinColumns = @JoinColumn(name = "id_libro"),
            inverseJoinColumns = @JoinColumn(name = "id_genero"))
    private Set<Genre> generos = new HashSet<>();

    // Métodos Getters
    public Long getId() {
        return id;
    }
    public String getTitulo() {
        return titulo;
    }
    public String getAutor() {
        return autor;
    }
    public int getAñoPublicacion() {
        return añoPublicacion;
    }
    public int getNumeroPaginas() {
        return numeroPaginas;
    }
    public String getEditorial() {
        return editorial;
    }
    public String getSinopsis() {
        return sinopsis;
    }
    public String getPortada() {
        return portada;
    }
    public String getIsbn() {
        return isbn;
    }
    public boolean isActivo() {
        return activo;
    }
    public Long getIdColeccion() {
        if (coleccion != null) {
            return coleccion.getId();
        }
        return null;
    }
    public Integer getNumColeccion() {
        return numColeccion;
    }
    public Set<Genre> getGeneros() {
        return generos;
    }

    // Métodos Setters
    public void setId(Long id) {
        this.id = id;
    }
    public void setTitulo(String newTitulo) {
        this.titulo = newTitulo;
    }
    public void setAutor(String newAutor) {
        this.autor = newAutor;
    }
    public void setAñoPublicacion(int newAño) {
        this.añoPublicacion = newAño;
    }
    public void setNumeroPaginas(int newNumPags) {
        this.numeroPaginas = newNumPags;
    }
    public void setEditorial(String newEditorial) {
        this.editorial = newEditorial;
    }
    public void setSinopsis(String newSinopsis) {
        this.sinopsis = newSinopsis;
    }
    public void setPortada(String newPortada) {
        this.portada = newPortada;
    }
    public void setIsbn(String newIsbn) {
        this.isbn = newIsbn;
    }
    public void setGeneros(Set<Genre> newGeneros) {
        this.generos = newGeneros;
    }
    public void setColeccion(Collection newColeccion) {
        this.coleccion = newColeccion;
    }
    public void setNumColeccion(Integer newNumCol) {
        this.numColeccion = newNumCol;
    }
    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public void delete() {
        this.activo = false;
    }
}
