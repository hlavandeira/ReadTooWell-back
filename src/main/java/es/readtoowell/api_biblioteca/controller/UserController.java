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
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/usuarios")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<Page<UserDTO>> getUsers(@RequestParam(value = "page", defaultValue = "0") int page,
                                                  @RequestParam(value = "size", defaultValue = "10") int size) {

        Page<UserDTO> userDTOPage = userService.getAllUsers(page, size);

        return ResponseEntity.ok(userDTOPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable(value = "id") Long id) {
        UserDTO usuario = userService.getUser(id);

        return ResponseEntity.ok(usuario);
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

    @PutMapping("/{idUser}/autor")
    public ResponseEntity<UserDTO> promoteToAuthor(@PathVariable Long idUser) {
        UserDTO user = userService.promoteToAuthor(idUser);

        return ResponseEntity.ok(user);
    }
}
