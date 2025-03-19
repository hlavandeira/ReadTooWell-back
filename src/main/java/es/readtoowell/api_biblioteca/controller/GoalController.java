package es.readtoowell.api_biblioteca.controller;

import es.readtoowell.api_biblioteca.model.DTO.GoalDTO;
import es.readtoowell.api_biblioteca.model.User;
import es.readtoowell.api_biblioteca.service.GoalService;
import es.readtoowell.api_biblioteca.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/objetivos")
public class GoalController {
    @Autowired
    private GoalService goalService;
    @Autowired
    private UserService userService;

    @GetMapping("/en-curso")
    public ResponseEntity<Set<GoalDTO>> getGoalsInProgress() {
        User user = userService.getAuthenticatedUser();
        if (user == null) {
            throw new AccessDeniedException("Usuario no autenticado.");
        }

        Set<GoalDTO> objetivos = goalService.obtenerObjetivosEnCurso(user.getId());

        return ResponseEntity.ok(objetivos);
    }

    @GetMapping("/terminados")
    public ResponseEntity<Set<GoalDTO>> getFinishedGoals() {
        User user = userService.getAuthenticatedUser();
        if (user == null) {
            throw new AccessDeniedException("Usuario no autenticado.");
        }

        Set<GoalDTO> objetivos = goalService.obtenerObjetivosTerminados(user.getId());

        return ResponseEntity.ok(objetivos);
    }

    @PostMapping
    public ResponseEntity<GoalDTO> createGoal(@Valid @RequestBody GoalDTO goal) {
        User user = userService.getAuthenticatedUser();
        if (user == null) {
            throw new AccessDeniedException("Usuario no autenticado.");
        }

        GoalDTO objetivo = goalService.crearObjetivo(user.getId(), goal);

        return ResponseEntity.status(HttpStatus.CREATED).body(objetivo);
    }

    @DeleteMapping("/{idGoal}")
    public ResponseEntity<GoalDTO> deleteGoal(@PathVariable Long idGoal) {
        User user = userService.getAuthenticatedUser();
        if (user == null) {
            throw new AccessDeniedException("Usuario no autenticado.");
        }

        GoalDTO objetivo = goalService.eliminarObjetivo(user.getId(), idGoal);
        if (objetivo != null) {
            return ResponseEntity.ok(objetivo);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
