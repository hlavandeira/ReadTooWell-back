package es.readtoowell.api_biblioteca.model;

import jakarta.persistence.*;

@Entity
@Table(name = "formato")
public class Format {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_formato", unique = true, updatable = false)
    private Long id;
    private String nombre;

    // Métodos Getters y Setters
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
