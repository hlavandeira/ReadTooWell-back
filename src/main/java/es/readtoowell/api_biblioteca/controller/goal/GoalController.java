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
     * @throws AccessDeniedException Usuario no autenticado
     */
    @GetMapping("/en-curso")
    public ResponseEntity<List<GoalDTO>> getGoalsInProgress() {
        User user = userService.getAuthenticatedUser();
        if (user == null) {
            throw new AccessDeniedException("Usuario no autenticado.");
        }

        List<GoalDTO> objetivos = goalService.getGoalsInProgress(user.getId());

        return ResponseEntity.ok(objetivos);
    }

    /**
     * Devuelve los objetivos terminados de un usuario.
     *
     * @return Lista con los objetivos como DTOs
     * @throws AccessDeniedException Usuario no autenticado
     */
    @GetMapping("/terminados")
    public ResponseEntity<List<GoalDTO>> getFinishedGoals() {
        User user = userService.getAuthenticatedUser();
        if (user == null) {
            throw new AccessDeniedException("Usuario no autenticado.");
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
            throw new AccessDeniedException("Usuario no autenticado.");
        }

        List<GoalDTO> objetivos = goalService.getFinishedGoalsActualYear(user.getId());

        return ResponseEntity.ok(objetivos);
    }

    /**
     * Crea un nuevo objetivo para un usuario.
     *
     * @param goal DTO con los datos del objetivo a crear
     * @return DTO con los datos del objetivo creado
     * @throws AccessDeniedException Usuario no autenticado
     */
    @PostMapping
    public ResponseEntity<GoalDTO> createGoal(@Valid @RequestBody GoalDTO goal) {
        User user = userService.getAuthenticatedUser();
        if (user == null) {
            throw new AccessDeniedException("Usuario no autenticado.");
        }

        GoalDTO objetivo = goalService.createGoal(user.getId(), goal);

        return ResponseEntity.status(HttpStatus.CREATED).body(objetivo);
    }

    /**
     * Elimina un objetivo de un usuario.
     *
     * @param idGoal ID del objetivo a borrar
     * @return DTO con los datos del objetivo borrado
     * @throws AccessDeniedException Usuario no autenticado
     */
    @DeleteMapping("/{idGoal}")
    public ResponseEntity<GoalDTO> deleteGoal(@PathVariable Long idGoal) {
        User user = userService.getAuthenticatedUser();
        if (user == null) {
            throw new AccessDeniedException("Usuario no autenticado.");
        }

        GoalDTO objetivo = goalService.deleteGoal(user.getId(), idGoal);
        if (objetivo != null) {
            return ResponseEntity.ok(objetivo);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
