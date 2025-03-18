package es.readtoowell.api_biblioteca.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "solicitud_autor")
public class AuthorRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_solicitud", unique = true, updatable = false)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario", nullable = false)
    private User usuario;
    private String nombre;
    @Column(name = "biografia", length = 2000)
    private String biografia;
    @Column(name = "fecha_enviada")
    private Date fechaEnviada;
    private boolean activo;
    private int estado;
    @OneToMany(mappedBy = "solicitud", cascade = CascadeType.ALL)
    private Set<RequestBook> libros = new HashSet<>();

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

    public Set<RequestBook> getLibros() {
        return libros;
    }
    public void setLibros(Set<RequestBook> libros) {
        this.libros = libros;
    }
}
