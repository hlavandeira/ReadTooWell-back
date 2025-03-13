package es.readtoowell.api_biblioteca.model;

import jakarta.persistence.*;

@Entity
@Table(name = "coleccion")
public class Collection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_coleccion")
    private Long id;

    private String nombre;

    public Long getId() {
        return id;
    }
    public String getNombre() {
        return nombre;
    }
}
