package es.readtoowell.api_biblioteca.controller;

import es.readtoowell.api_biblioteca.model.DTO.GoalDTO;
import es.readtoowell.api_biblioteca.model.DTO.UserDTO;
import es.readtoowell.api_biblioteca.service.GoalService;
import es.readtoowell.api_biblioteca.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/usuarios")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private GoalService goalService;

    @GetMapping
    public ResponseEntity<Page<UserDTO>> getUsers(@RequestParam(value = "page", defaultValue = "0") int page,
                                                  @RequestParam(value = "size", defaultValue = "10") int size) {

        Page<UserDTO> userDTOPage = userService.getAllUsers(page, size);

        return ResponseEntity.ok(userDTOPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getBook(@PathVariable(value = "id") Long id) {
        Optional<UserDTO> usuario = userService.getUser(id);

        if (usuario.isPresent()) {
            return ResponseEntity.ok(usuario.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserDTO user) {
        UserDTO newUser = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id,
                                              @Valid @RequestBody UserDTO user) {
        try {
            UserDTO usuario = userService.updateUser(id, user);
            return ResponseEntity.ok(usuario);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UserDTO> deleteUser(@PathVariable Long id) {
        UserDTO usuario = userService.deleteUser(id);
        if (usuario != null) {
            return ResponseEntity.ok(usuario);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/seguidos")
    public ResponseEntity<Set<UserDTO>> getFollows(@PathVariable Long id) {
        Set<UserDTO> seguidos = userService.getFollows(id);

        return ResponseEntity.ok(seguidos);
    }

    @GetMapping("/{id}/seguidores")
    public ResponseEntity<Set<UserDTO>> getFollowers(@PathVariable Long id) {
        Set<UserDTO> seguidores = userService.getFollowers(id);

        return ResponseEntity.ok(seguidores);
    }

    @PostMapping("/{idUser}/seguir/{idFollowed}")
    public ResponseEntity<UserDTO> followUser(@PathVariable Long idUser,
                                           @PathVariable Long idFollowed) {
        UserDTO followed = userService.followUser(idUser, idFollowed);

        return ResponseEntity.ok(followed);
    }

    @DeleteMapping("/{idUser}/dejar-seguir/{idUnfollowed}")
    public ResponseEntity<UserDTO> unfollowUser(@PathVariable Long idUser,
                                             @PathVariable Long idUnfollowed) {
        UserDTO unfollowed = userService.unfollowUser(idUser, idUnfollowed);

        return ResponseEntity.ok(unfollowed);
    }

    @GetMapping("/buscar")
    public ResponseEntity<Page<UserDTO>> searchUsers(@RequestParam String searchString,
                                                     @RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "10") int size) {
        Page<UserDTO> usuarios = userService.searchUsers(searchString, page, size);

        return ResponseEntity.ok(usuarios);
    }

    // Endpoints de Objetivos

    @GetMapping("/{idUser}/objetivos/en-curso")
    public ResponseEntity<Set<GoalDTO>> getGoalsInProgress(@PathVariable Long idUser) {
        Set<GoalDTO> objetivos = goalService.obtenerObjetivosEnCurso(idUser);
        return ResponseEntity.ok(objetivos);
    }

    @GetMapping("/{idUser}/objetivos/terminados")
    public ResponseEntity<Set<GoalDTO>> getFinishedGoals(@PathVariable Long idUser) {
        Set<GoalDTO> objetivos = goalService.obtenerObjetivosTerminados(idUser);
        return ResponseEntity.ok(objetivos);
    }
}
