package es.readtoowell.api_biblioteca.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidad que representa las duraciones de objetivo.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "duracion_objetivo")
public class GoalDuration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_duracion")
    private Long id;
    @Column(name = "nombre")
    private String name;

    // Métodos Getters y Setters

    /**
     * Devuelve el identificador de la duración de objetivo.
     *
     * @return ID de la duración
     */
    public Long getId() {
        return id;
    }

    /**
     * Establece un valor para el identificador de la duración de objetivo.
     *
     * @param id Nuevo ID de la duración
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Devuelve el nombre de la duración de objetivo.
     *
     * @return Nombre de la duración
     */
    public String getName() {
        return name;
    }

    /**
     * Establece un valor para el nombre de la duración de objetivo.
     *
     * @param name Nuevo nombre de la duración
     */
    public void setName(String name) {
        this.name = name;
    }
}
