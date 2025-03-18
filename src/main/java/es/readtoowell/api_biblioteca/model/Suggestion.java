package es.readtoowell.api_biblioteca.model;

import es.readtoowell.api_biblioteca.model.enums.Role;
import es.readtoowell.api_biblioteca.model.enums.SuggestionStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "sugerencia")
public class Suggestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_sugerencia", unique = true, updatable = false)
    private Long id;
    private String titulo;
    private String autor;
    @Column(name = "año_publicacion")
    private int añoPublicacion;
    private int estado;
    @Column(name = "fecha_enviada")
    private Date fechaEnviada;
    @ManyToOne
    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario", nullable = false)
    private User usuario;
    private boolean activo;

    @Transient
    public SuggestionStatus getSuggestionEnum() {
        return SuggestionStatus.fromValue(this.estado);
    }
    public void setRoleEnum(SuggestionStatus status) {
        this.estado = status.getValue();
    }

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

    public int getEstado() {
        return estado;
    }
    public void setEstado(int estado) {
        this.estado = estado;
    }

    public Date getFechaEnviada() {
        return fechaEnviada;
    }
    public void setFechaEnviada(Date fechaEnviada) {
        this.fechaEnviada = fechaEnviada;
    }

    public User getUsuario() {
        return usuario;
    }
    public void setUsuario(User usuario) {
        this.usuario = usuario;
    }

    public boolean isActivo() {
        return activo;
    }
    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}
