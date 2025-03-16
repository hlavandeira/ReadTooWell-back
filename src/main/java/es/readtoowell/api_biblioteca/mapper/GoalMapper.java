package es.readtoowell.api_biblioteca.mapper;

import es.readtoowell.api_biblioteca.model.DTO.GoalDTO;
import es.readtoowell.api_biblioteca.model.Goal;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

@Component
public class GoalMapper {
    public GoalDTO toDTO(Goal goal) {
        GoalDTO dto = new GoalDTO();

        dto.setId(goal.getId());
        dto.setCantidad(goal.getCantidad());
        dto.setCantidadActual(goal.getCantidadActual());
        dto.setDuration(goal.getDuracion());
        dto.setType(goal.getTipo());
        dto.setUsuario(goal.getUsuario());
        dto.setFechaFin(goal.getFechaFin());
        dto.setFechaInicio(goal.getFechaInicio());

        dto.setCompletado(objetivoCompletado(goal));

        // Si el objetivo está en curso, calcula los valores adicionales
        if (!dto.isCompletado()) {
            dto.setDiasRestantes(calcularDiasRestantes(goal));
            dto.setPorcentaje(calcularPorcentaje(goal));
        }

        return dto;
    }

    private boolean objetivoCompletado(Goal goal) {
        LocalDate fechaFin = goal.getFechaFin().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return goal.getCantidadActual() >= goal.getCantidad() || LocalDate.now().isAfter(fechaFin);
    }

    private long calcularDiasRestantes(Goal goal) {
        LocalDate fechaFin = goal.getFechaFin().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return ChronoUnit.DAYS.between(LocalDate.now(), fechaFin);
    }

    private double calcularPorcentaje(Goal goal) {
        return (goal.getCantidadActual() * 100.0) / goal.getCantidad();
    }
}
