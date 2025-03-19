package es.readtoowell.api_biblioteca.model.DTO;

import es.readtoowell.api_biblioteca.model.UserLibraryBookId;

import java.util.Date;

public class UserLibraryBookDTO {
    private UserLibraryBookId id;
    private BookDTO libro;
    private int estadoLectura;
    private Date fechaInicio;
    private Date fechaFin;
    private int progreso;
    private String tipoProgreso;
    private double calificacion;
    private String reseña;

    // Métodos Getters y Setters
    public UserLibraryBookId getId() {
        return id;
    }
    public void setId(UserLibraryBookId id) {
        this.id = id;
    }

    public BookDTO getLibro() {
        return libro;
    }
    public void setLibro(BookDTO libro) {
        this.libro = libro;
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
