package es.readtoowell.api_biblioteca.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "libro")
public class Book {
    @Id
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
    @Column(name = "id_coleccion")
    private Integer idColeccion;

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
    public Integer getIdColeccion() {
        return idColeccion;
    }
}
