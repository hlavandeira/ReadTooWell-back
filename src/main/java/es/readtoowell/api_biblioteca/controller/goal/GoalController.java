package es.readtoowell.api_biblioteca.controller.goal;

import es.readtoowell.api_biblioteca.model.DTO.GoalDTO;
import es.readtoowell.api_biblioteca.model.entity.User;
import es.readtoowell.api_biblioteca.service.goal.GoalService;
import es.readtoowell.api_biblioteca.service.user.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador que gestiona las peticiones HTTP relativas a los objetivos de lectura.
 */
@RestController
@RequestMapping("/objetivos")
public class GoalController {
    @Autowired
    private GoalService goalService;
    @Autowired
    private UserService userService;

    /**
     * Devuelve los objetivos en curso de un usuario.
     *
     * @return Lista con los objetivos como DTOs
     */
    @GetMapping("/en-curso")
    public ResponseEntity<List<GoalDTO>> getGoalsInProgress() {
        User user = userService.getAuthenticatedUser();
        if (user == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        List<GoalDTO> objetivos = goalService.getGoalsInProgress(user.getId());

        return ResponseEntity.ok(objetivos);
    }

    /**
     * Devuelve los objetivos terminados de un usuario.
     *
     * @return Lista con los objetivos como DTOs
     */
    @GetMapping("/terminados")
    public ResponseEntity<List<GoalDTO>> getFinishedGoals() {
        User user = userService.getAuthenticatedUser();
        if (user == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        List<GoalDTO> objetivos = goalService.getFinishedGoals(user.getId());

        return ResponseEntity.ok(objetivos);
    }

    /**
     * Devuelve los objetivos finalizados y completados por un usuario en el año actual.
     *
     * @return Lista con los objetivos completados en el año actual
     */
    @GetMapping("/terminados/año-actual")
    public ResponseEntity<List<GoalDTO>> getFinishedGoalsActualYear() {
        User user = userService.getAuthenticatedUser();
        if (user == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        List<GoalDTO> objetivos = goalService.getFinishedGoalsActualYear(user.getId());

        return ResponseEntity.ok(objetivos);
    }

    /**
     * Crea un nuevo objetivo para un usuario.
     *
     * @param goal DTO con los datos del objetivo a crear
     * @return DTO con los datos del objetivo creado
     */
    @PostMapping
    public ResponseEntity<GoalDTO> createGoal(@Valid @RequestBody GoalDTO goal) {
        User user = userService.getAuthenticatedUser();
        if (user == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        GoalDTO objetivo = goalService.createGoal(user.getId(), goal);

        return ResponseEntity.status(HttpStatus.CREATED).body(objetivo);
    }

    /**
     * Elimina un objetivo de un usuario.
     *
     * @param idGoal ID del objetivo a borrar
     * @return DTO con los datos del objetivo borrado
     */
    @DeleteMapping("/{idGoal}")
    public ResponseEntity<GoalDTO> deleteGoal(@PathVariable Long idGoal) {
        User user = userService.getAuthenticatedUser();
        if (user == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        GoalDTO objetivo = goalService.deleteGoal(user.getId(), idGoal);
        if (objetivo != null) {
            return ResponseEntity.ok(objetivo);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
