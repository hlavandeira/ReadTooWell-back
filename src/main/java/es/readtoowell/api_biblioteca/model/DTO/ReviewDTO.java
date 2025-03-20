package es.readtoowell.api_biblioteca.model.DTO;

public class ReviewDTO {
    private String nombreUsuario;
    private String nombrePerfil;
    private double calificacion;
    private String reseña;

    public String getNombreUsuario() {
        return nombreUsuario;
    }
    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getNombrePerfil() {
        return nombrePerfil;
    }
    public void setNombrePerfil(String nombrePerfil) {
        this.nombrePerfil = nombrePerfil;
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
