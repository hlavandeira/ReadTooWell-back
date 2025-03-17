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
        dto.setDuracion(goal.getDuracion().getId());
        dto.setTipo(goal.getTipo().getId());
        dto.setUsuario(goal.getUsuario());
        dto.setFechaFin(goal.getFechaFin());
        dto.setFechaInicio(goal.getFechaInicio());
        dto.setActivo(goal.isActivo());

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

    public Goal toEntity(GoalDTO dto) {
        Goal goal = new Goal();

        goal.setId(dto.getId());
        goal.setCantidad(dto.getCantidad());
        goal.setCantidadActual(dto.getCantidadActual());
        goal.setUsuario(dto.getUsuario());
        goal.setFechaFin(dto.getFechaFin());
        goal.setFechaInicio(dto.getFechaInicio());
        goal.setActivo(dto.isActivo());
        // para tipo y duracion se hace un set en el servicio, para pasar la entidad en lugar de solo el id

        return goal;
    }
}
