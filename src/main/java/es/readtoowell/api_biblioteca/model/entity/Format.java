package es.readtoowell.api_biblioteca.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidad que representa los formatos de libro.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "formato")
public class Format {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_formato", unique = true, updatable = false)
    private Long id;
    @Column(name = "nombre")
    private String name;

    // Métodos Getters y Setters

    /**
     * Devuelve el identificador del formato.
     *
     * @return ID del formato
     */
    public Long getId() {
        return id;
    }

    /**
     * Establece un valor para el identificador del formato.
     *
     * @param id Nuevo ID del formato
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Devuelve el nombre del formato.
     *
     * @return Nombre del formato
     */
    public String getName() {
        return name;
    }

    /**
     * Establece un valor para el nombre del formato.
     *
     * @param name Nuevo nombre del formato
     */
    public void setName(String name) {
        this.name = name;
    }
}
