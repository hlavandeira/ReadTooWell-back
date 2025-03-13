package es.readtoowell.api_biblioteca.model;

import jakarta.persistence.*;
import lombok.*;

import jakarta.validation.constraints.*;

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
    @NotBlank(message="El título no puede ser nulo")
    private String titulo;
    @NotBlank(message="El autor no puede ser nulo")
    private String autor;
    @Positive(message="El año no puede ser un número negativo")
    @Column(name = "año_publicacion")
    private int añoPublicacion;
    @Positive(message="El número de páginas no puede ser un número negativo")
    @Column(name = "numero_paginas")
    private int numeroPaginas;
    private String editorial;
    @Column(name = "sinopsis", length = 2000)
    private String sinopsis;
    private String portada;
    @Pattern(regexp="^(\\d{10}|\\d{13})$")
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

    public void delete() {
        this.activo = false;
    }
}
