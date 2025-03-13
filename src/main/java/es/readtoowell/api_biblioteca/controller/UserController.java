package es.readtoowell.api_biblioteca.controller;

import es.readtoowell.api_biblioteca.model.Book;
import es.readtoowell.api_biblioteca.model.User;
import es.readtoowell.api_biblioteca.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/usuarios")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<Page<User>> getUsers(@RequestParam(value = "page", defaultValue = "0") int page,
                                               @RequestParam(value = "size", defaultValue = "10") int size) {
        return ResponseEntity.ok().body(userService.getAllUsers(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getBook(@PathVariable(value = "id") Long id) {
        Optional<User> usuario = userService.getUser(id);

        if (usuario.isPresent()) {
            return ResponseEntity.ok(usuario.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        return ResponseEntity.created(URI.create("/usuarios/usuarioID")).body(userService.createBook(user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable Long id) {
        Optional<User> usuario = userService.getUser(id);

        if (usuario.isPresent()) {
            usuario.get().delete();
            userService.deleteUser(usuario.get());
            return ResponseEntity.ok(usuario.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/seguidos")
    public ResponseEntity<Set<User>> getFollows(@PathVariable Long id) {
        Set<User> seguidos = userService.getFollows(id);
        return ResponseEntity.ok(seguidos);
    }

    @GetMapping("/{id}/seguidores")
    public ResponseEntity<Set<User>> getFollowers(@PathVariable Long id) {
        Set<User> seguidores = userService.getFollowers(id);
        return ResponseEntity.ok(seguidores);
    }

    @PostMapping("/{idUser}/seguir/{idFollowed}")
    public ResponseEntity<User> followUser(@PathVariable Long idUser, @PathVariable Long idFollowed) {
        User followed = userService.followUser(idUser, idFollowed);
        return ResponseEntity.ok(followed);
    }

    @DeleteMapping("/{idUser}/dejar-seguir/{idUnfollowed}")
    public ResponseEntity<User> unfollowUser(@PathVariable Long idUser, @PathVariable Long idUnfollowed) {
        User unfollowed = userService.unfollowUser(idUser, idUnfollowed);
        return ResponseEntity.ok(unfollowed);
    }
}
