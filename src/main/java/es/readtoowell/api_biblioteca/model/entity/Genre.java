package es.readtoowell.api_biblioteca.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidad que representa los géneros de libro.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "genero")
public class Genre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_genero", unique = true, updatable = false)
    private Long id;
    @Column(name = "nombre")
    private String name;

    // Métodos Getters y Setters

    /**
     * Devuelve el identificador del género.
     *
     * @return ID del género
     */
    public Long getId() {
        return id;
    }

    /**
     * Establece un valor para el identificador del género.
     *
     * @param id Nuevo ID del género
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Devuelve el nombre del género.
     *
     * @return Nombre del género
     */
    public String getName() {
        return name;
    }

    /**
     * Establece un valor para el nombre del género.
     *
     * @param name Nuevo nombre del género
     */
    public void setName(String name) {
        this.name = name;
    }
}
