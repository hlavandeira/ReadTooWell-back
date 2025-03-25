package es.readtoowell.api_biblioteca.model.DTO;

import jakarta.validation.constraints.NotBlank;

import java.util.Set;

public class BookListDTO {
    private Long id;
    @NotBlank(message = "El nombre de la lista no puede ser nulo")
    private String nombre;
    private String descripcion;
    private UserDTO usuario;
    private Set<BookListItemDTO> libros;
    private Set<GenreDTO> generos;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
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

    public Set<BookListItemDTO> getLibros() {
        return libros;
    }
    public void setLibros(Set<BookListItemDTO> libros) {
        this.libros = libros;
    }

    public Set<GenreDTO> getGeneros() {
        return generos;
    }
    public void setGeneros(Set<GenreDTO> generos) {
        this.generos = generos;
    }

    public UserDTO getUsuario() {
        return usuario;
    }
    public void setUsuario(UserDTO usuario) {
        this.usuario = usuario;
    }
}
