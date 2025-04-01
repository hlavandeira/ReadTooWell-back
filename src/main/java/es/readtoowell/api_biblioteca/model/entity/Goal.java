package es.readtoowell.api_biblioteca.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

/**
 * Entidad que representa los objetivos de lectura.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "objetivo")
public class Goal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_objetivo")
    private Long id;
    @Column(name = "cantidad")
    private int amount;
    @Column(name = "cantidad_actual")
    private int currentAmount;
    @Column(name = "fecha_inicio")
    private Date dateStart;
    @Column(name = "fecha_fin")
    private Date dateFinish;
    @ManyToOne
    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario", nullable = false)
    private User user;
    /**
     * Tipo de objetivo. Puede ser 'Libros' o 'Páginas'
     */
    @ManyToOne
    @JoinColumn(name = "id_tipo", referencedColumnName = "id_tipo", nullable = false)
    private GoalType type;
    /**
     * Duración del objetivo. Puede ser 'Anual' o 'Mensual'
     */
    @ManyToOne
    @JoinColumn(name = "id_duracion", referencedColumnName = "id_duracion", nullable = false)
    private GoalDuration duration;

    // Métodos Getters y Setters

    /**
     * Devuelve el identificador del objetivo de lectura.
     *
     * @return ID del objetivo
     */
    public Long getId() {
        return id;
    }

    /**
     * Establece un valor para el identificador del objetivo de lectura.
     *
     * @param id Nuevo ID del objetivo
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Devuelve la cantidad a cumplir del objetivo.
     *
     * @return Cantidad a cumplir del objetivo
     */
    public int getAmount() {
        return amount;
    }

    /**
     * Establece un valor para la cantidad a cumplir del objetivo.
     *
     * @param amount Nueva cantidad a cumplir del objetivo
     */
    public void setAmount(int amount) {
        this.amount = amount;
    }

    /**
     * Devuelve la cantidad actual del objetivo.
     *
     * @return Cantidad actual del objetivo
     */
    public int getCurrentAmount() {
        return currentAmount;
    }

    /**
     * Establece un valor para la cantidad actual del objetivo.
     *
     * @param currentAmount Nueva cantidad actual del objetivo
     */
    public void setCurrentAmount(int currentAmount) {
        this.currentAmount = currentAmount;
    }

    /**
     * Devuelve la fecha de inicio del objetivo.
     *
     * @return Fecha de inicio del objetivo
     */
    public Date getDateStart() {
        return dateStart;
    }

    /**
     * Establece un valor para la fecha de inicio del objetivo.
     *
     * @param dateStart Nueva fecha de inicio
     */
    public void setDateStart(Date dateStart) {
        this.dateStart = dateStart;
    }

    /**
     * Devuelve la fecha de fin del objetivo.
     *
     * @return Fecha de fin del objetivo
     */
    public Date getDateFinish() {
        return dateFinish;
    }

    /**
     * Establece un valor para la fecha de fin del objetivo.
     *
     * @param dateFinish Nueva fecha de fin
     */
    public void setDateFinish(Date dateFinish) {
        this.dateFinish = dateFinish;
    }

    /**
     * Devuelve el usuario propietario del objetivo.
     *
     * @return Usuario propietario del objetivo
     */
    public User getUser() {
        return user;
    }

    /**
     * Establece un valor para el usuario propietario del objetivo.
     *
     * @param user Nuevo usuario propietario del objetivo
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Devuelve el tipo del objetivo de lectura.
     *
     * @return Tipo del objetivo de lectura
     */
    public GoalType getType() {
        return type;
    }

    /**
     * Establece un valor para el tipo del objetivo de lectura.
     *
     * @param type Nuevo tipo del objetivo de lectura
     */
    public void setType(GoalType type) {
        this.type = type;
    }

    /**
     * Devuelve la duración del objetivo de lectura.
     *
     * @return Duración del objetivo de lectura
     */
    public GoalDuration getDuration() {
        return duration;
    }

    /**
     * Establece un valor para la duración del objetivo de lectura.
     *
     * @param duration Nueva duración del objetivo de lectura
     */
    public void setDuration(GoalDuration duration) {
        this.duration = duration;
    }
}
