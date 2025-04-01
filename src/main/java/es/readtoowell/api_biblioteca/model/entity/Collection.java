package es.readtoowell.api_biblioteca.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *  Entidad que representa las colecciones de libros.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "coleccion")
public class Collection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_coleccion", unique = true, updatable = false)
    private Long id;
    @Column(name = "nombre")
    private String name;

    // Métodos Getters y Setters

    /**
     * Devuelve el identificador de la colección.
     *
     * @return ID de la colección
     */
    public Long getId() {
        return id;
    }

    /**
     * Establece un valor para el identificador de la colección.
     *
     * @param id Nuevo ID de la colección
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Devuelve el nombre de la colección.
     *
     * @return Nombre de la colección
     */
    public String getName() {
        return name;
    }

    /**
     * Establece un valor para el nombre de la colección.
     *
     * @param name Nuevo nombre de la colección
     */
    public void setName(String name) {
        this.name = name;
    }
}
