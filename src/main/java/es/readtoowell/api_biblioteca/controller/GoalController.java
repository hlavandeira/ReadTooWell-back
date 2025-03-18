package es.readtoowell.api_biblioteca.controller;

import es.readtoowell.api_biblioteca.model.DTO.GoalDTO;
import es.readtoowell.api_biblioteca.service.GoalService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/objetivos")
public class GoalController {
    @Autowired
    private GoalService goalService;

    @GetMapping("/{idUser}/en-curso")
    public ResponseEntity<Set<GoalDTO>> getGoalsInProgress(@PathVariable Long idUser) {
        Set<GoalDTO> objetivos = goalService.obtenerObjetivosEnCurso(idUser);

        return ResponseEntity.ok(objetivos);
    }

    @GetMapping("/{idUser}/terminados")
    public ResponseEntity<Set<GoalDTO>> getFinishedGoals(@PathVariable Long idUser) {
        Set<GoalDTO> objetivos = goalService.obtenerObjetivosTerminados(idUser);

        return ResponseEntity.ok(objetivos);
    }

    @PostMapping("/{idUser}")
    public ResponseEntity<GoalDTO> createGoal(@PathVariable Long idUser,
                                              @Valid @RequestBody GoalDTO goal) {
        GoalDTO objetivo = goalService.crearObjetivo(idUser, goal);

        return ResponseEntity.status(HttpStatus.CREATED).body(objetivo);
    }

    @DeleteMapping("/{idUser}/{idGoal}")
    public ResponseEntity<GoalDTO> deleteGoal(@PathVariable Long idUser,
                                              @PathVariable Long idGoal) {
        GoalDTO objetivo = goalService.eliminarObjetivo(idUser, idGoal);
        if (objetivo != null) {
            return ResponseEntity.ok(objetivo);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
