package es.readtoowell.api_biblioteca.model.DTO;

public class RequestBookDTO {
    private Long id;
    private String titulo;
    private int añoPublicacion;

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

    public int getAñoPublicacion() {
        return añoPublicacion;
    }
    public void setAñoPublicacion(int añoPublicacion) {
        this.añoPublicacion = añoPublicacion;
    }
}
