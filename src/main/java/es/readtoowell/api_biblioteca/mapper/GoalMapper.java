package es.readtoowell.api_biblioteca.mapper;

import es.readtoowell.api_biblioteca.model.DTO.GoalDTO;
import es.readtoowell.api_biblioteca.model.entity.Goal;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

@Component
public class GoalMapper {
    /**
     * Convierte una instancia de {@code Goal} en {@code GoalDTO}.
     *
     * @param goal La entidad {@code Goal} a convertir.
     * @return Una instancia de {@code GoalDTO} con los datos del objetivo.
     */
    public GoalDTO toDTO(Goal goal) {
        GoalDTO dto = new GoalDTO();

        dto.setId(goal.getId());
        dto.setAmount(goal.getAmount());
        dto.setCurrentAmount(goal.getCurrentAmount());
        dto.setDuration(goal.getDuration().getName());
        dto.setType(goal.getType().getName());
        dto.setUser(goal.getUser());
        dto.setDateStart(goal.getDateStart());
        dto.setDateFinish(goal.getDateFinish());

        dto.setCompleted(objetivoCompletado(goal));

        // Si el objetivo está en curso, calcula los valores adicionales
        if (!dto.isCompleted()) {
            dto.setRemainingDays(calcularDiasRestantes(goal));
            dto.setPercentage(calcularPorcentaje(goal));
        }

        return dto;
    }

    /**
     * Comprueba si un objetivo se ha completado/terminado o no.
     *
     * @param goal Objetivo a comprobar.
     * @return 'true' si se ha completado/terminado, 'false' en caso contrario.
     */
    private boolean objetivoCompletado(Goal goal) {
        LocalDate fechaFin = goal.getDateFinish().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return goal.getCurrentAmount() >= goal.getAmount() || LocalDate.now().isAfter(fechaFin);
    }

    /**
     * Calcula los días restantes para que acabe un objetivo.
     *
     * @param goal Objetivo del que se van a calcular los días restantes.
     * @return Cantidad de días restantes para el objetivo.
     */
    private long calcularDiasRestantes(Goal goal) {
        LocalDate fechaFin = goal.getDateFinish().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return ChronoUnit.DAYS.between(LocalDate.now(), fechaFin);
    }

    /**
     * Calcula el porcentaje de progreso de un objetivo.
     *
     * @param goal Objetivo del que se va a calcular el porcentaje.
     * @return Porcentaje de progreso del objetivo.
     */
    private double calcularPorcentaje(Goal goal) {
        return (goal.getCurrentAmount() * 100.0) / goal.getAmount();
    }

    /**
     * Convierte una instancia de {@code GoalDTO} en {@code Goal}.
     *
     * @param dto El {@code GoalDTO} a convertir.
     * @return Una instancia de {@code Goal} con los datos del DTO.
     */
    public Goal toEntity(GoalDTO dto) {
        Goal goal = new Goal();

        goal.setId(dto.getId());
        goal.setAmount(dto.getAmount());
        goal.setCurrentAmount(dto.getCurrentAmount());
        goal.setUser(dto.getUser());
        goal.setDateStart(dto.getDateStart());
        goal.setDateFinish(dto.getDateFinish());
        // Para tipo y duracion se hace un set en el servicio, para pasar la entidad en lugar de solo el id

        return goal;
    }
}
