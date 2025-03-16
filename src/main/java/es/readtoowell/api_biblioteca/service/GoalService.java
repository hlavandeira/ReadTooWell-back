package es.readtoowell.api_biblioteca.service;

import es.readtoowell.api_biblioteca.model.DTO.GoalDTO;
import es.readtoowell.api_biblioteca.mapper.GoalMapper;
import es.readtoowell.api_biblioteca.model.Goal;
import es.readtoowell.api_biblioteca.repository.GoalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class GoalService {
    @Autowired
    private GoalRepository goalRepository;
    @Autowired
    private GoalMapper goalMapper;

    public Set<GoalDTO> obtenerObjetivosEnCurso(Long idUser) {
        Set<Goal> objetivos = goalRepository.findByUsuarioId(idUser);

        return objetivos.stream()
                .filter(goal -> !objetivoCompletado(goal))
                .map(goalMapper::toDTO)
                .collect(Collectors.toSet());
    }

    public Set<GoalDTO> obtenerObjetivosTerminados(Long idUser) {
        Set<Goal> objetivos = goalRepository.findByUsuarioId(idUser);

        return objetivos.stream()
                .filter(goal -> objetivoCompletado(goal))
                .map(goalMapper::toDTO)
                .collect(Collectors.toSet());
    }

    private boolean objetivoCompletado(Goal goal) {
        LocalDate fechaFin = goal.getFechaFin().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return goal.getCantidadActual() >= goal.getCantidad() || LocalDate.now().isAfter(fechaFin);
    }
}
