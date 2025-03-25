package es.readtoowell.api_biblioteca.controller;

import es.readtoowell.api_biblioteca.model.Book;
import es.readtoowell.api_biblioteca.model.DTO.UserDTO;
import es.readtoowell.api_biblioteca.model.DTO.UserFavoritesDTO;
import es.readtoowell.api_biblioteca.model.Genre;
import es.readtoowell.api_biblioteca.model.User;
import es.readtoowell.api_biblioteca.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping
    public ResponseEntity<UserDTO> updateUser(@Valid @RequestBody UserDTO userDTO) {
        User user = userService.getAuthenticatedUser();
        if (user == null) {
            throw new AccessDeniedException("Usuario no autenticado.");
        }

        UserDTO usuario = userService.updateUser(user.getId(), userDTO);
        return ResponseEntity.ok(usuario);
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

    @PostMapping("/seguir/{idFollowed}")
    public ResponseEntity<UserDTO> followUser(@PathVariable Long idFollowed) {
        User user = userService.getAuthenticatedUser();
        if (user == null) {
            throw new AccessDeniedException("Usuario no autenticado.");
        }

        UserDTO followed = userService.followUser(user.getId(), idFollowed);

        return ResponseEntity.ok(followed);
    }

    @DeleteMapping("/dejar-seguir/{idUnfollowed}")
    public ResponseEntity<UserDTO> unfollowUser(@PathVariable Long idUnfollowed) {
        User user = userService.getAuthenticatedUser();
        if (user == null) {
            throw new AccessDeniedException("Usuario no autenticado.");
        }

        UserDTO unfollowed = userService.unfollowUser(user.getId(), idUnfollowed);

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

    @PutMapping("/generos-favoritos")
    public ResponseEntity<UserFavoritesDTO> updateFavoriteGenres(@RequestParam Set<Long> genreIds) {
        User user = userService.getAuthenticatedUser();
        if (user == null) {
            throw new AccessDeniedException("Usuario no autenticado.");
        }

        Set<Genre> generos = userService.addFavoriteGenres(user, genreIds);

        UserFavoritesDTO favs = new UserFavoritesDTO();
        favs.setUser(user);
        favs.setGenerosFavoritos(generos);
        favs.setLibrosFavoritos(user.getLibrosFavoritos());

        return ResponseEntity.ok(favs);
    }

    @PutMapping("/libros-favoritos")
    public ResponseEntity<UserFavoritesDTO> updateFavoriteBooks(@RequestParam Set<Long> bookIds) {
        User user = userService.getAuthenticatedUser();
        if (user == null) {
            throw new AccessDeniedException("Usuario no autenticado.");
        }

        Set<Book> libros = userService.addFavoriteBooks(user, bookIds);

        UserFavoritesDTO favs = new UserFavoritesDTO();
        favs.setUser(user);
        favs.setLibrosFavoritos(libros);
        favs.setGenerosFavoritos(user.getGenerosFavoritos());

        return ResponseEntity.ok(favs);
    }

    @GetMapping("/favoritos")
    public ResponseEntity<UserFavoritesDTO> getFavorites() {
        User user = userService.getAuthenticatedUser();
        if (user == null) {
            throw new AccessDeniedException("Usuario no autenticado.");
        }

        UserFavoritesDTO favs = userService.getFavorites(user);

        return ResponseEntity.ok(favs);
    }
}
