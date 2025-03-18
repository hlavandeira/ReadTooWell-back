package es.readtoowell.api_biblioteca.model.DTO;

import es.readtoowell.api_biblioteca.model.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

public class SuggestionDTO {
    private Long id;
    @NotBlank(message = "El título no puede ser nulo")
    private String titulo;
    @NotBlank(message = "El autor no puede ser nulo")
    private String autor;
    @NotNull(message = "El título no puede ser nulo")
    private int añoPublicacion;
    private Date fechaEnviada;
    private int estado;
    private boolean activo;
    private User usuario;

    // Métodos Getters y Setters
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

    public Date getFechaEnviada() {
        return fechaEnviada;
    }
    public void setFechaEnviada(Date fechaEnviada) {
        this.fechaEnviada = fechaEnviada;
    }

    public int getEstado() {
        return estado;
    }
    public void setEstado(int estado) {
        this.estado = estado;
    }

    public boolean isActivo() {
        return activo;
    }
    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public User getUsuario() {
        return usuario;
    }
    public void setUsuario(User usuario) {
        this.usuario = usuario;
    }
}
