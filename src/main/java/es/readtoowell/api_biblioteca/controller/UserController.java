package es.readtoowell.api_biblioteca.controller;

import es.readtoowell.api_biblioteca.DTO.UserDTO;
import es.readtoowell.api_biblioteca.mapper.UserMapper;
import es.readtoowell.api_biblioteca.model.User;
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
import java.util.stream.Collectors;

@RestController
@RequestMapping("/usuarios")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserMapper userMapper;

    @GetMapping
    public ResponseEntity<Page<UserDTO>> getUsers(@RequestParam(value = "page", defaultValue = "0") int page,
                                                  @RequestParam(value = "size", defaultValue = "10") int size) {

        Page<User> userPage = userService.getAllUsers(page, size);
        Page<UserDTO> userDTOPage = userPage.map(userMapper::toDTO);

        return ResponseEntity.ok(userDTOPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getBook(@PathVariable(value = "id") Long id) {
        Optional<User> usuario = userService.getUser(id);

        if (usuario.isPresent()) {
            return ResponseEntity.ok(userMapper.toDTO(usuario.get()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserDTO user) {
        User newUser = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(userMapper.toDTO(newUser));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id,
                                              @Valid @RequestBody UserDTO user) {
        try {
            User usuario = userService.updateUser(id, user);
            return ResponseEntity.ok(userMapper.toDTO(usuario));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UserDTO> deleteUser(@PathVariable Long id) {
        Optional<User> usuario = userService.getUser(id);

        if (usuario.isPresent()) {
            usuario.get().delete();
            userService.deleteUser(userMapper.toDTO(usuario.get()));
            return ResponseEntity.ok(userMapper.toDTO(usuario.get()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/buscar")
    public ResponseEntity<Page<UserDTO>> searchUsers(@RequestParam String searchString,
                                                    @RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "10") int size) {
        Page<User> usuarios = userService.searchUsers(searchString, page, size);
        Page<UserDTO> userDTOPage = usuarios.map(userMapper::toDTO);

        return ResponseEntity.ok(userDTOPage);
    }

    @GetMapping("/{id}/seguidos")
    public ResponseEntity<Set<UserDTO>> getFollows(@PathVariable Long id) {
        Set<User> seguidos = userService.getFollows(id);
        Set<UserDTO> seguidosDTO = seguidos.stream().map(userMapper::toDTO).collect(Collectors.toSet());

        return ResponseEntity.ok(seguidosDTO);
    }

    @GetMapping("/{id}/seguidores")
    public ResponseEntity<Set<UserDTO>> getFollowers(@PathVariable Long id) {
        Set<User> seguidores = userService.getFollowers(id);
        Set<UserDTO> seguidoresDTO = seguidores.stream().map(userMapper::toDTO).collect(Collectors.toSet());

        return ResponseEntity.ok(seguidoresDTO);
    }

    @PostMapping("/{idUser}/seguir/{idFollowed}")
    public ResponseEntity<UserDTO> followUser(@PathVariable Long idUser,
                                           @PathVariable Long idFollowed) {
        User followed = userService.followUser(idUser, idFollowed);

        return ResponseEntity.ok(userMapper.toDTO(followed));
    }

    @DeleteMapping("/{idUser}/dejar-seguir/{idUnfollowed}")
    public ResponseEntity<UserDTO> unfollowUser(@PathVariable Long idUser,
                                             @PathVariable Long idUnfollowed) {
        User unfollowed = userService.unfollowUser(idUser, idUnfollowed);

        return ResponseEntity.ok(userMapper.toDTO(unfollowed));
    }
}
