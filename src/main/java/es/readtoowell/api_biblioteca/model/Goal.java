package es.readtoowell.api_biblioteca.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "objetivo")
public class Goal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_objetivo")
    private Long id;
    private int cantidad;
    @Column(name = "cantidad_actual")
    private int cantidadActual;
    @Column(name = "fecha_inicio")
    private Date fechaInicio;
    @Column(name = "fecha_fin")
    private Date fechaFin;
    @ManyToOne
    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario", nullable = false)
    @JsonIgnore
    private User usuario;
    @ManyToOne
    @JoinColumn(name = "id_tipo", referencedColumnName = "id_tipo", nullable = false)
    private GoalType tipo;
    @ManyToOne
    @JoinColumn(name = "id_duracion", referencedColumnName = "id_duracion", nullable = false)
    private GoalDuration duracion;

    // Métodos Getters y Setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public int getCantidad() {
        return cantidad;
    }
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getCantidadActual() {
        return cantidadActual;
    }
    public void setCantidadActual(int cantidadActual) {
        this.cantidadActual = cantidadActual;
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

    public User getUsuario() {
        return usuario;
    }
    public void setUsuario(User usuario) {
        this.usuario = usuario;
    }

    public GoalType getTipo() {
        return tipo;
    }
    public void setTipo(GoalType type) {
        this.tipo = type;
    }

    public GoalDuration getDuracion() {
        return duracion;
    }
    public void setDuracion(GoalDuration duration) {
        this.duracion = duration;
    }
}
