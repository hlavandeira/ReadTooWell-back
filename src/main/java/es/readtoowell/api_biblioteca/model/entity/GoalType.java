package es.readtoowell.api_biblioteca.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidad que representa los tipos de objetivo.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tipo_objetivo")
public class GoalType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo")
    private Long id;
    @Column(name = "nombre")
    private String name;

    // Métodos Getters y Setters

    /**
     * Devuelve el identificador del tipo de objetivo.
     *
     * @return ID del tipo
     */
    public Long getId() {
        return id;
    }

    /**
     * Establece un valor para el identificador del tipo de objetivo.
     *
     * @param id Nuevo ID del tipo
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Devuelve el nombre del tipo de objetivo.
     *
     * @return Nombre del tipo
     */
    public String getName() {
        return name;
    }

    /**
     * Establece un valor para el nombre del tipo de objetivo.
     *
     * @param name Nuevo nombre del tipo
     */
    public void setName(String name) {
        this.name = name;
    }
}
