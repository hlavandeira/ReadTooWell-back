package es.readtoowell.api_biblioteca.model.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import es.readtoowell.api_biblioteca.model.User;

import java.util.Date;
import java.util.Set;

public class AuthorRequestDTO {
    private Long id;
    private User usuario;
    private String nombre;
    private String biografia;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Europe/Madrid")
    private Date fechaEnviada;
    private boolean activo;
    private int estado;
    private Set<RequestBookDTO> libros;

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

    public String getBiografia() {
        return biografia;
    }
    public void setBiografia(String biografia) {
        this.biografia = biografia;
    }

    public Date getFechaEnviada() {
        return fechaEnviada;
    }
    public void setFechaEnviada(Date fechaEnviada) {
        this.fechaEnviada = fechaEnviada;
    }

    public boolean isActivo() {
        return activo;
    }
    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public int getEstado() {
        return estado;
    }
    public void setEstado(int estado) {
        this.estado = estado;
    }

    public Set<RequestBookDTO> getLibros() {
        return libros;
    }
    public void setLibros(Set<RequestBookDTO> libros) {
        this.libros = libros;
    }
}
