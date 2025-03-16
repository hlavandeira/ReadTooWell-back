package es.readtoowell.api_biblioteca.model.DTO;

import es.readtoowell.api_biblioteca.model.GoalDuration;
import es.readtoowell.api_biblioteca.model.GoalType;
import es.readtoowell.api_biblioteca.model.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.util.Date;

@Data
public class GoalDTO {
    private Long id;
    @NotBlank(message = "La cantidad del objetivo no puede ser nula")
    @Positive(message = "La cantidad debe ser mayor que 0")
    private int cantidad;
    private int cantidadActual;
    private Date fechaInicio;
    private Date fechaFin;
    private User usuario;
    @NotBlank(message = "El tipo de objetivo no puede ser nulo")
    private GoalType type;
    @NotBlank(message = "La duración del objetivo no puede ser nula")
    private GoalDuration duration;

    // Atributos adicionales para los detalles
    private boolean completado;
    private double porcentaje;
    private long diasRestantes;

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

    public GoalType getType() {
        return type;
    }
    public void setType(GoalType type) {
        this.type = type;
    }

    public GoalDuration getDuration() {
        return duration;
    }
    public void setDuration(GoalDuration duration) {
        this.duration = duration;
    }

    public boolean isCompletado() {
        return completado;
    }
    public void setCompletado(boolean completado) {
        this.completado = completado;
    }

    public double getPorcentaje() {
        return porcentaje;
    }
    public void setPorcentaje(double porcentaje) {
        this.porcentaje = porcentaje;
    }

    public long getDiasRestantes() {
        return diasRestantes;
    }
    public void setDiasRestantes(long diasRestantes) {
        this.diasRestantes = diasRestantes;
    }
}
