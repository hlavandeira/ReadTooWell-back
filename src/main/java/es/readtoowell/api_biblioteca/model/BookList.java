package es.readtoowell.api_biblioteca.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "lista")
public class BookList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_lista", unique = true, updatable = false)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario", nullable = false)
    private User usuario;
    private String nombre;
    private String descripcion;
    @ManyToMany
    @JoinTable(name = "genero_lista",
            joinColumns = @JoinColumn(name = "id_lista"),
            inverseJoinColumns = @JoinColumn(name = "id_genero"))
    private Set<Genre> generos = new HashSet<>();
    @ManyToMany
    @JoinTable(name = "libro_lista",
            joinColumns = @JoinColumn(name = "id_lista"),
            inverseJoinColumns = @JoinColumn(name = "id_libro"))
    private Set<Book> libros = new HashSet<>();

    // Métodos Getters y Setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public User getUsuario() {
        return usuario;
    }
    public void setUsuario(User usuario) {
        this.usuario = usuario;
    }

    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Set<Genre> getGeneros() {
        return generos;
    }
    public void setGeneros(Set<Genre> generos) {
        this.generos = generos;
    }

    public Set<Book> getLibros() {
        return libros;
    }
    public void setLibros(Set<Book> libros) {
        this.libros = libros;
    }
}
