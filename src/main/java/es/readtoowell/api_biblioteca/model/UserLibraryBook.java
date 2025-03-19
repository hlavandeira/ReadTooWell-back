package es.readtoowell.api_biblioteca.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "libro_biblioteca")
public class UserLibraryBook {
    @EmbeddedId
    private UserLibraryBookId id;
    @ManyToOne
    @MapsId("idUsuario")
    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario", nullable = false)
    private User usuario;
    @ManyToOne
    @MapsId("idLibro")
    @JoinColumn(name = "id_libro", referencedColumnName = "id_libro", nullable = false)
    private Book libro;
    @OneToMany(mappedBy = "libroBiblioteca", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserBookFormat> formatos = new HashSet<>();
    @Column(name = "estado_lectura")
    private int estadoLectura;
    @Column(name = "fecha_inicio")
    private Date fechaInicio;
    @Column(name = "fecha_fin")
    private Date fechaFin;
    private int progreso;
    @Column(name = "tipo_progreso")
    private String tipoProgreso;
    private double calificacion;
    @Column(name = "reseña", length = 2000)
    private String reseña;

    // Métodos Getters y Setters
    public UserLibraryBookId getId() {
        return id;
    }
    public void setId(UserLibraryBookId id) {
        this.id = id;
    }

    public User getUsuario() {
        return usuario;
    }
    public void setUsuario(User usuario) {
        this.usuario = usuario;
    }

    public Book getLibro() {
        return libro;
    }
    public void setLibro(Book libro) {
        this.libro = libro;
    }

    public Set<UserBookFormat> getFormats() {
        return formatos;
    }
    public void setFormats(Set<UserBookFormat> formatos) {
        this.formatos = formatos;
    }

    public int getEstadoLectura() {
        return estadoLectura;
    }
    public void setEstadoLectura(int estadoLectura) {
        this.estadoLectura = estadoLectura;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }
    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }
    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public int getProgreso() {
        return progreso;
    }
    public void setProgreso(int progreso) {
        this.progreso = progreso;
    }

    public String getTipoProgreso() {
        return tipoProgreso;
    }
    public void setTipoProgreso(String tipoProgreso) {
        this.tipoProgreso = tipoProgreso;
    }

    public double getCalificacion() {
        return calificacion;
    }
    public void setCalificacion(double calificacion) {
        this.calificacion = calificacion;
    }

    public String getReseña() {
        return reseña;
    }
    public void setReseña(String reseña) {
        this.reseña = reseña;
    }
}
