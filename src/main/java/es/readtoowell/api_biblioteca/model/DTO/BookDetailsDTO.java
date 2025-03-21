package es.readtoowell.api_biblioteca.model.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import java.util.Set;

public class BookDetailsDTO {
    private BookDTO libro;
    private int estadoLectura;
    private double calificacion;
    private String reseña;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Europe/Madrid")
    private Date fechaInicio;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Europe/Madrid")
    private Date fechaFin;
    private double calificacionMedia;
    private String nombreColeccion;
    private Set<SimpleBookListDTO> listas;
    private Set<ReviewDTO> reseñasOtrosUsuarios;
    private boolean guardado;

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

    public double getCalificacionMedia() {
        return calificacionMedia;
    }
    public void setCalificacionMedia(double calificacionMedia) {
        this.calificacionMedia = calificacionMedia;
    }

    public String getNombreColeccion() {
        return nombreColeccion;
    }
    public void setNombreColeccion(String nombreColeccion) {
        this.nombreColeccion = nombreColeccion;
    }

    public Set<SimpleBookListDTO> getListas() {
        return listas;
    }
    public void setListas(Set<SimpleBookListDTO> listas) {
        this.listas = listas;
    }

    public Set<ReviewDTO> getReseñasOtrosUsuarios() {
        return reseñasOtrosUsuarios;
    }
    public void setReseñasOtrosUsuarios(Set<ReviewDTO> reseñasOtrosUsuarios) {
        this.reseñasOtrosUsuarios = reseñasOtrosUsuarios;
    }

    public boolean isGuardado() {
        return guardado;
    }
    public void setGuardado(boolean guardado) {
        this.guardado = guardado;
    }
}
