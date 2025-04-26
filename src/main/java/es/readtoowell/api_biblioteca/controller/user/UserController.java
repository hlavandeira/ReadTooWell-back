package es.readtoowell.api_biblioteca.controller.user;

import es.readtoowell.api_biblioteca.model.DTO.user.UpdateProfileDTO;
import es.readtoowell.api_biblioteca.model.DTO.user.UserDTO;
import es.readtoowell.api_biblioteca.model.DTO.user.UserFavoritesDTO;
import es.readtoowell.api_biblioteca.model.entity.User;
import es.readtoowell.api_biblioteca.service.user.UserService;
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

    /**
     * Devuelve todos los usuarios.
     *
     * @param page Número de la página que se quiere devolver
     * @param size Tamaño de la página
     * @return Página con los usuarios como DTOs
     */
    @GetMapping
    public ResponseEntity<Page<UserDTO>> getUsers(@RequestParam(value = "page", defaultValue = "0") int page,
                                                  @RequestParam(value = "size", defaultValue = "10") int size) {

        Page<UserDTO> userDTOPage = userService.getAllUsers(page, size);

        return ResponseEntity.ok(userDTOPage);
    }

    /**
     * Devuelve un usuario según su ID.
     *
     * @param id ID del usuario
     * @return DTO con los datos del usuario
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable(value = "id") Long id) {
        UserDTO usuario = userService.getUser(id);

        return ResponseEntity.ok(usuario);
    }

    /**
     * Crea un nuevo usuario.
     *
     * @param user DTO con los datos del usuario a crear
     * @return DTO con los datos del usuario creado
     */
    @PostMapping
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserDTO user) {
        UserDTO newUser = userService.createUser(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    /**
     * Elimina un usuario.
     *
     * @param id ID del usuario a eliminar
     * @return DTO con los datos del usuario eliminado
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<UserDTO> deleteUser(@PathVariable Long id) {
        UserDTO usuario = userService.deleteUser(id);
        if (usuario != null) {
            return ResponseEntity.ok(usuario);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Actualiza los datos de un usuario.
     *
     * @param userDTO DTO con los nuevos datos del usuario
     * @return DTO con los datos del usuario actualizado
     * @throws AccessDeniedException Usuario no autenticado
     */
    @PutMapping
    public ResponseEntity<UserDTO> updateUser(@Valid @RequestBody UserDTO userDTO) {
        User user = userService.getAuthenticatedUser();
        if (user == null) {
            throw new AccessDeniedException("Usuario no autenticado.");
        }

        UserDTO usuario = userService.updateUser(user.getId(), userDTO);
        return ResponseEntity.ok(usuario);
    }

    /**
     * Actualiza los datos del perfil de un usuario.
     *
     * @param updateDTO Datos del perfil a actualizar
     * @return DTO con el usuario actualizado
     */
    @PutMapping("/perfil")
    public ResponseEntity<UserDTO> updateUserProfile(@Valid @RequestBody UpdateProfileDTO updateDTO) {
        User user = userService.getAuthenticatedUser();
        if (user == null) {
            throw new AccessDeniedException("Usuario no autenticado.");
        }

        UserDTO usuario = userService.updateUserProfile(user.getId(), updateDTO);
        return ResponseEntity.ok(usuario);
    }

    /**
     * Devuelve los usuarios que sigue un usuario específico.
     *
     * @param id ID del usuario del que se consultan los seguidos
     * @return Lista con los usuarios seguidos como DTOs
     */
    @GetMapping("/{id}/seguidos")
    public ResponseEntity<Set<UserDTO>> getFollows(@PathVariable Long id) {
        Set<UserDTO> seguidos = userService.getFollows(id);

        return ResponseEntity.ok(seguidos);
    }

    /**
     * Devuelve los seguidores de un usuario específico.
     *
     * @param id ID del usuario del que se consultan los seguidores
     * @return Lista con los usuarios seguidores como DTOs
     */
    @GetMapping("/{id}/seguidores")
    public ResponseEntity<Set<UserDTO>> getFollowers(@PathVariable Long id) {
        Set<UserDTO> seguidores = userService.getFollowers(id);

        return ResponseEntity.ok(seguidores);
    }

    /**
     * Un usuario sigue a otro usuario.
     *
     * @param idFollowed ID del usuario al que sigue
     * @return DTO con los datos del usuario seguido
     * @throws AccessDeniedException Usuario no autenticado
     */
    @PostMapping("/seguir/{idFollowed}")
    public ResponseEntity<UserDTO> followUser(@PathVariable Long idFollowed) {
        User user = userService.getAuthenticatedUser();
        if (user == null) {
            throw new AccessDeniedException("Usuario no autenticado.");
        }

        UserDTO followed = userService.followUser(user.getId(), idFollowed);

        return ResponseEntity.ok(followed);
    }

    /**
     * Un usuario deja de seguir a otro usuario.
     *
     * @param idUnfollowed ID del usuario al que deja de seguir
     * @return DTO con los datos del usuario que se ha dejado de seguir
     * @throws AccessDeniedException Usuario no autenticado
     */
    @DeleteMapping("/dejar-seguir/{idUnfollowed}")
    public ResponseEntity<UserDTO> unfollowUser(@PathVariable Long idUnfollowed) {
        User user = userService.getAuthenticatedUser();
        if (user == null) {
            throw new AccessDeniedException("Usuario no autenticado.");
        }

        UserDTO unfollowed = userService.unfollowUser(user.getId(), idUnfollowed);

        return ResponseEntity.ok(unfollowed);
    }

    /**
     * Busca usuarios mediante una cadena de texto.
     *
     * @param searchString Cadena por la que se buscan usuarios
     * @param page Número de la página que se quiere devolver
     * @param size Tamaño de la página
     * @return Página con los usuarios encontrados como DTOs
     */
    @GetMapping("/buscar")
    public ResponseEntity<Page<UserDTO>> searchUsers(@RequestParam String searchString,
                                                     @RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "10") int size) {
        Page<UserDTO> usuarios = userService.searchUsers(searchString, page, size);

        return ResponseEntity.ok(usuarios);
    }

    /**
     * Se promociona a un usuario a autor.
     *
     * @param idUser ID del usuario que se actualiza
     * @return DTO con los datos del usuario actualizado
     */
    @PutMapping("/{idUser}/autor")
    public ResponseEntity<UserDTO> promoteToAuthor(@PathVariable Long idUser) {
        UserDTO user = userService.promoteToAuthor(idUser);

        return ResponseEntity.ok(user);
    }

    /**
     * Añade géneros favoritos a un usuario.
     *
     * @param genreIds Lista de IDs de los géneros seleccionados
     * @return DTO con los datos de los libros y géneros favoritos del usuario
     * @throws AccessDeniedException Usuario no autenticado
     */
    @PutMapping("/generos-favoritos")
    public ResponseEntity<UserFavoritesDTO> updateFavoriteGenres(@RequestParam Set<Long> genreIds) {
        User user = userService.getAuthenticatedUser();
        if (user == null) {
            throw new AccessDeniedException("Usuario no autenticado.");
        }

        userService.addFavoriteGenres(user, genreIds);

        UserFavoritesDTO favorites = userService.getFavorites(user.getId());

        return ResponseEntity.ok(favorites);
    }

    /**
     * Añade libros favoritos a un usuario.
     *
     * @param bookIds Lista de IDs de los libros seleccionados
     * @return DTO con los datos de los libros y géneros favoritos del usuario
     * @throws AccessDeniedException Usuario no autenticado
     */
    @PutMapping("/libros-favoritos")
    public ResponseEntity<UserFavoritesDTO> updateFavoriteBooks(@RequestParam Set<Long> bookIds) {
        User user = userService.getAuthenticatedUser();
        if (user == null) {
            throw new AccessDeniedException("Usuario no autenticado.");
        }

        userService.addFavoriteBooks(user, bookIds);

        UserFavoritesDTO favorites = userService.getFavorites(user.getId());

        return ResponseEntity.ok(favorites);
    }

    /**
     * Devuelve los libros y géneros favoritos de un usuario.
     *
     * @param idUser ID del usuario del que se devuelven los favoritos
     * @return DTO con los datos de los libros y géneros favoritos del usuario
     * @throws AccessDeniedException Usuario no autenticado
     */
    @GetMapping("/{idUser}/favoritos")
    public ResponseEntity<UserFavoritesDTO> getFavorites(@PathVariable Long idUser) {
        UserFavoritesDTO favs = userService.getFavorites(idUser);

        return ResponseEntity.ok(favs);
    }
}
