package es.readtoowell.api_biblioteca.model;

import jakarta.persistence.*;

@Entity
@Table(name = "coleccion")
public class Collection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_coleccion", unique = true, updatable = false)
    private Long id;

    private String nombre;

    // Métodos Getters
    public Long getId() {
        return id;
    }
    public String getNombre() {
        return nombre;
    }

    // Métodos Setters
    public void setId(Long id) {
        this.id = id;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
