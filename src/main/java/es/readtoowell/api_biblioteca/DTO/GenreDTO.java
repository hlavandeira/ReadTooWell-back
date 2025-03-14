package es.readtoowell.api_biblioteca.DTO;

import lombok.Data;

@Data
public class GenreDTO {
    private Long id;
    private String nombre;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
