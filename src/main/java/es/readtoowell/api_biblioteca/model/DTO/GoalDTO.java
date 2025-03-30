package es.readtoowell.api_biblioteca.model.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import es.readtoowell.api_biblioteca.model.entity.User;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.Date;

/**
 * DTO que representa los detalles de un objetivo.
 */
public class GoalDTO {
    private Long id;
    @NotNull(message = "La cantidad del objetivo no puede ser nula")
    @Positive(message = "La cantidad debe ser mayor que 0")
    private int amount;
    private int currentAmount;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Europe/Madrid")
    private Date dateStart;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Europe/Madrid")
    private Date dateFinish;
    private User user;
    @NotNull(message = "El tipo de objetivo no puede ser nulo")
    private String type;
    @NotNull(message = "La duración del objetivo no puede ser nula")
    private String duration;

    // Atributos adicionales para los detalles de los objetivos en curso.
    private boolean completed;
    private double percentage;
    private long remainingDays;

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
    public String getType() {
        return type;
    }

    /**
     * Establece un valor para el tipo del objetivo de lectura.
     *
     * @param type Nuevo tipo del objetivo de lectura
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Devuelve la duración del objetivo de lectura.
     *
     * @return Duración del objetivo de lectura
     */
    public String getDuration() {
        return duration;
    }

    /**
     * Establece un valor para la duración del objetivo de lectura.
     *
     * @param duration Nueva duración del objetivo de lectura
     */
    public void setDuration(String duration) {
        this.duration = duration;
    }

    /**
     * Devuelve un boolean según si el objetivo ha sido completado.
     *
     * @return 'true' si el objetivo está completado/terminado, 'false' en caso contrario
     */
    public boolean isCompleted() {
        return completed;
    }

    /**
     * Establece un valor para el campo completed.
     *
     * @param completed Nuevo valor para completed
     */
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    /**
     * Devuelve el porcentaje de progreso del objetivo.
     *
     * @return Porcentaje del objetivo
     */
    public double getPercentage() {
        return percentage;
    }

    /**
     * Establece un valor para el porcentaje de progreso del objetivo.
     *
     * @param percentage Nuevo porcentaje del objetivo
     */
    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    /**
     * Devuelve los días restantes del objetivo.
     *
     * @return Días restantes del objetivo
     */
    public long getRemainingDays() {
        return remainingDays;
    }

    /**
     * Establece un valor para los días restantes del objetivo.
     *
     * @param remainingDays Nuevos días restantes del objetivo
     */
    public void setRemainingDays(long remainingDays) {
        this.remainingDays = remainingDays;
    }
}
