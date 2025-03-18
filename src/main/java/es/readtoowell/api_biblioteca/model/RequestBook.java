package es.readtoowell.api_biblioteca.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "libro_solicitud")
public class RequestBook {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, updatable = false)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "id_solicitud", nullable = false)
    private AuthorRequest solicitud;
    private String titulo;
    @Column(name = "año_publicacion")
    private int añoPublicacion;

    // Métodos Getters y Setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public AuthorRequest getSolicitud() {
        return solicitud;
    }
    public void setSolicitud(AuthorRequest solicitud) {
        this.solicitud = solicitud;
    }

    public String getTitulo() {
        return titulo;
    }
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public int getAñoPublicacion() {
        return añoPublicacion;
    }
    public void setAñoPublicacion(int añoPublicacion) {
        this.añoPublicacion = añoPublicacion;
    }
}
