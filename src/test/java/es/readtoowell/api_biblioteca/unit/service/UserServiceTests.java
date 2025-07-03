package es.readtoowell.api_biblioteca.unit.service;

import es.readtoowell.api_biblioteca.mapper.UserMapper;
import es.readtoowell.api_biblioteca.model.DTO.AuthorRequestDTO;
import es.readtoowell.api_biblioteca.model.DTO.user.UpdateProfileDTO;
import es.readtoowell.api_biblioteca.model.DTO.user.UserDTO;
import es.readtoowell.api_biblioteca.model.DTO.user.UserFavoritesDTO;
import es.readtoowell.api_biblioteca.model.entity.Book;
import es.readtoowell.api_biblioteca.model.entity.Genre;
import es.readtoowell.api_biblioteca.model.entity.User;
import es.readtoowell.api_biblioteca.repository.book.BookRepository;
import es.readtoowell.api_biblioteca.repository.book.GenreRepository;
import es.readtoowell.api_biblioteca.repository.user.UserRepository;
import es.readtoowell.api_biblioteca.service.user.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Clase de pruebas para el servicio de usuarios.
 */
@ExtendWith(MockitoExtension.class)
public class UserServiceTests {
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private GenreRepository genreRepository;
    @Mock
    private BookRepository bookRepository;
    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    public void init() {
        user = new User();
    }

    /**
     * Método de prueba. Devolver todos los usuarios
     */
    @Test
    public void UserService_GetAllUsers_ReturnUsers() {
        UserDTO userDTO = new UserDTO();
        Page<User> userPage = new PageImpl<>(List.of(user));

        when(userRepository.findAll(any(PageRequest.class))).thenReturn(userPage);
        when(userMapper.toDTO(user)).thenReturn(userDTO);

        Page<UserDTO> result = userService.getAllUsers(0, 10);

        assertEquals(1, result.getTotalElements());
        assertEquals(userDTO, result.getContent().get(0));
        verify(userRepository).findAll(any(PageRequest.class));
    }

    /**
     * Método de prueba. Devolver un usuario
     */
    @Test
    public void UserService_GetUser_ReturnUser() {
        Long userId = 1L;
        UserDTO userDTO = new UserDTO();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userMapper.toDTO(user)).thenReturn(userDTO);

        UserDTO result = userService.getUser(userId);

        assertEquals(userDTO, result);
        verify(userRepository).findById(userId);
    }

    /**
     * Método de prueba. Devolver un usuario inexistente
     */
    @Test
    public void UserService_GetUser_UnexistentUser() {
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> userService.getUser(userId)
        );

        assertEquals("El usuario con ID 1 no existe.", exception.getMessage());
    }

    /**
     * Método de prueba. Devolver al usuario autenticado utilizando Principal
     */
    @Test
    public void UserService_GetAuthenticatedUser_Principal() {
        Authentication authentication = mock(Authentication.class);

        when(authentication.getPrincipal()).thenReturn(user);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        User result = userService.getAuthenticatedUser();

        assertEquals(user, result);
    }

    /**
     * Método de prueba. Devolver al usuario autenticado utilizando UserDetails
     */
    @Test
    public void UserService_GetAuthenticatedUser_UserDetails() {
        String email = "prueba@email.es";
        UserDetails userDetails = mock(UserDetails.class);
        Authentication authentication = mock(Authentication.class);

        when(userDetails.getUsername()).thenReturn(email);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        User result = userService.getAuthenticatedUser();

        assertEquals(user, result);
    }

    /**
     * Método de prueba. Devolver el usuario autenticado cuando no hay ninguno autenticado
     */
    @Test
    public void UserService_GetAuthenticatedUser_NoUser() {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn("anonymous");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        User result = userService.getAuthenticatedUser();

        assertNull(result);
    }

    /**
     * Método de prueba. Crear un usuario
     */
    @Test
    public void UserService_CreateUser_ReturnCreated() {
        UserDTO userDTO = new UserDTO();

        when(userMapper.toEntity(userDTO)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toDTO(user)).thenReturn(userDTO);

        UserDTO result = userService.createUser(userDTO);

        assertEquals(userDTO, result);
        assertEquals(0, userDTO.getRole());
        verify(userRepository).save(user);
    }

    /**
     * Método de prueba. Eliminar un usuario
     */
    @Test
    public void UserService_DeleteUser_ReturnDeleted() {
        Long userId = 1L;
        UserDTO userDTO = new UserDTO();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userMapper.toDTO(user)).thenReturn(userDTO);

        UserDTO result = userService.deleteUser(userId);

        assertEquals(userDTO, result);
        verify(userRepository).delete(user);
    }

    /**
     * Método de prueba. Eliminar un usuario inexistente
     */
    @Test
    public void UserService_DeleteUser_UnexistentUser() {
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> userService.deleteUser(userId)
        );

        assertEquals("El usuario con ID 1 no existe.", exception.getMessage());
    }

    /**
     * Método de prueba. Actualizar un usuario
     */
    @Test
    public void UserService_UpdateUser_ReturnUpdated() {
        Long userId = 1L;
        UserDTO inputDTO = new UserDTO();
        inputDTO.setUsername("Usuario");
        inputDTO.setProfileName("Usuario");
        inputDTO.setPassword("Contraseña123_");
        inputDTO.setEmail("prueba@email.es");
        inputDTO.setBiography("Biografía");
        inputDTO.setRole(0);
        inputDTO.setProfilePic("Foto perfil");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toDTO(user)).thenReturn(inputDTO);

        UserDTO result = userService.updateUser(userId, inputDTO);

        assertEquals(inputDTO, result);
        verify(userRepository).save(user);
    }

    /**
     * Método de prueba. Actualizar un usuario inexistente
     */
    @Test
    public void UserService_UpdateUser_UnexistentUser() {
        Long userId = 1L;
        UserDTO inputDTO = new UserDTO();
        inputDTO.setUsername("Usuario");
        inputDTO.setProfileName("Usuario");
        inputDTO.setPassword("Contraseña123_");
        inputDTO.setEmail("prueba@email.es");
        inputDTO.setBiography("Biografía");
        inputDTO.setRole(0);
        inputDTO.setProfilePic("Foto perfil");

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> userService.updateUser(userId, inputDTO)
        );

        assertEquals("El usuario con ID 1 no existe.", exception.getMessage());
    }

    /**
     * Método de prueba. Actualizar el perfil de un usuario
     */
    @Test
    public void UserService_UpdateUserProfile_ReturnUpdated() {
        Long userId = 1L;
        UpdateProfileDTO updateDTO = new UpdateProfileDTO();
        updateDTO.setProfileName("Nombre");
        updateDTO.setBiography("Biografía");
        updateDTO.setProfilePic("foto.png");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDTO expectedDTO = new UserDTO();
        when(userMapper.toDTO(user)).thenReturn(expectedDTO);

        UserDTO result = userService.updateUserProfile(userId, updateDTO);

        assertEquals(expectedDTO, result);
        verify(userRepository).save(user);
    }

    /**
     * Método de prueba. Actualizar el perfil de un usuario inexistente
     */
    @Test
    public void UserService_UpdateUserProfile_UnexistentUser() {
        Long userId = 1L;
        UpdateProfileDTO updateDTO = new UpdateProfileDTO();
        updateDTO.setProfileName("Nombre");
        updateDTO.setBiography("Biografía");
        updateDTO.setProfilePic("foto.png");

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> userService.updateUserProfile(userId, updateDTO)
        );

        assertEquals("El usuario con ID 1 no existe.", exception.getMessage());
    }

    /**
     * Método de prueba. Devolver los usuarios seguidos por el usuario
     */
    @Test
    public void UserService_GetFollows_ReturnUsers() {
        Long userId = 1L;
        User followed = new User();
        user.getFollowedUsers().add(followed);

        UserDTO followedDTO = new UserDTO();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userMapper.toDTO(followed)).thenReturn(followedDTO);

        List<UserDTO> result = userService.getFollows(userId);

        assertEquals(1, result.size());
        assertEquals(followedDTO, result.get(0));
    }

    /**
     * Método de prueba. Devolver los usuarios que siguen al usuario
     */
    @Test
    public void UserService_GetFollowers_ReturnUsers() {
        Long userId = 1L;
        User follower = new User();
        user.getFollowers().add(follower);

        UserDTO followerDTO = new UserDTO();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userMapper.toDTO(follower)).thenReturn(followerDTO);

        List<UserDTO> result = userService.getFollowers(userId);

        assertEquals(1, result.size());
        assertEquals(followerDTO, result.get(0));
    }

    /**
     * Método de prueba. Seguir a otro usuario
     */
    @Test
    public void UserService_FollowUser_ReturnUser() {
        Long userId = 1L;
        Long followedId = 2L;
        user.setRole(0);

        User followed = new User();
        followed.setRole(0);

        UserDTO followedDTO = new UserDTO();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.findById(followedId)).thenReturn(Optional.of(followed));
        when(userMapper.toDTO(followed)).thenReturn(followedDTO);

        UserDTO result = userService.followUser(userId, followedId);

        assertTrue(user.getFollowedUsers().contains(followed));
        assertTrue(followed.getFollowers().contains(user));
        assertEquals(followedDTO, result);
        verify(userRepository).save(user);
        verify(userRepository).save(followed);
    }

    /**
     * Método de prueba. Un usuario inexistente sigue a otro
     */
    @Test
    public void UserService_FollowUser_UnexistentUser() {
        Long userId = 1L;
        Long followedId = 2L;
        user.setRole(0);

        User followed = new User();
        followed.setRole(0);

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> userService.followUser(userId, followedId)
        );

        assertEquals("El usuario con ID 1 no existe.", exception.getMessage());
    }

    /**
     * Método de prueba. Un usuario sigue a otro usuario inexistente
     */
    @Test
    public void UserService_FollowUser_UnexistentFollowedUser() {
        Long userId = 1L;
        Long followedId = 2L;
        user.setRole(0);

        User followed = new User();
        followed.setRole(0);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.findById(followedId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> userService.followUser(userId, followedId)
        );

        assertEquals("El usuario con ID 2 no existe.", exception.getMessage());
    }

    /**
     * Método de prueba. Seguir a un usuario con rol administrador
     */
    @Test
    public void UserService_FollowUser_FollowedUserIsAdmin() {
        Long userId = 1L;
        Long followedId = 2L;
        user.setRole(0);

        User followed = new User();
        followed.setRole(2);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.findById(followedId)).thenReturn(Optional.of(followed));

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> userService.followUser(userId, followedId)
        );

        assertEquals("No se puede seguir a un usuario administrador.", exception.getMessage());
    }

    /**
     * Método de prueba. Dejar de seguir a otro usuario
     */
    @Test
    public void UserService_UnfollowUser_ReturnUser() {
        Long userId = 1L;
        Long unfollowedId = 2L;
        user.setRole(0);

        User unfollowed = new User();
        unfollowed.setRole(0);

        user.getFollowedUsers().add(unfollowed);
        unfollowed.getFollowers().add(user);

        UserDTO dto = new UserDTO();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.findById(unfollowedId)).thenReturn(Optional.of(unfollowed));
        when(userMapper.toDTO(unfollowed)).thenReturn(dto);

        UserDTO result = userService.unfollowUser(userId, unfollowedId);

        assertFalse(user.getFollowedUsers().contains(unfollowed));
        assertFalse(unfollowed.getFollowers().contains(user));
        assertEquals(dto, result);
        verify(userRepository).save(user);
        verify(userRepository).save(unfollowed);
    }

    /**
     * Método de prueba. Un usuario inexistente deja de seguir a otro
     */
    @Test
    public void UserService_UnfollowUser_UnexistentUser() {
        Long userId = 1L;
        Long unfollowedId = 2L;
        user.setRole(0);

        User unfollowed = new User();
        unfollowed.setRole(0);

        user.getFollowedUsers().add(unfollowed);
        unfollowed.getFollowers().add(user);

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> userService.unfollowUser(userId, unfollowedId)
        );

        assertEquals("El usuario con ID 1 no existe.", exception.getMessage());
    }

    /**
     * Método de prueba. Un usuario deja de seguir a otro inexistente
     */
    @Test
    public void UserService_UnfollowUser_UnexistentUnfollowedUser() {
        Long userId = 1L;
        Long unfollowedId = 2L;
        user.setRole(0);

        User unfollowed = new User();
        unfollowed.setRole(0);

        user.getFollowedUsers().add(unfollowed);
        unfollowed.getFollowers().add(user);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.findById(unfollowedId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> userService.unfollowUser(userId, unfollowedId)
        );

        assertEquals("El usuario con ID 2 no existe.", exception.getMessage());
    }

    /**
     * Método de prueba. Dejar de seguir a un usuario con rol administrador
     */
    @Test
    public void UserService_UnfollowUser_UnfollowedUserIsAdmin() {
        Long userId = 1L;
        Long unfollowedId = 2L;
        user.setRole(0);

        User unfollowed = new User();
        unfollowed.setRole(2);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.findById(unfollowedId)).thenReturn(Optional.of(unfollowed));

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> userService.unfollowUser(userId, unfollowedId)
        );

        assertEquals("No se puede dejar de seguir a un usuario administrador.", exception.getMessage());
    }

    /**
     * Método de prueba. Buscar usuarios mediante un término de búsqueda
     */
    @Test
    public void UserService_SearchUsers_ReturnUsers() {
        String search = "pepe";
        Long userId = 1L;
        int page = 0;
        int size = 10;
        Page<User> users = new PageImpl<>(List.of(new User()));

        when(userRepository.searchUsers(any(), any(), any(Pageable.class))).thenReturn(users);
        when(userMapper.toDTO(any())).thenReturn(new UserDTO());

        Page<UserDTO> result = userService.searchUsers(search, userId, page, size);

        assertEquals(1, result.getContent().size());
    }

    /**
     * Método de prueba. Promocionar a un usuario a autor
     */
    @Test
    public void UserService_PromoteToAuthor_UpdatesUser() {
        AuthorRequestDTO request = new AuthorRequestDTO();
        UserDTO userDTO = new UserDTO();
        User user = new User();
        user.setId(1L);
        request.setUser(user);
        request.setName("Autor");
        request.setBiography("Biografía de autor");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toDTO(any())).thenReturn(userDTO);

        UserDTO result = userService.promoteToAuthor(request);

        assertEquals(userDTO, result);
        assertEquals(1, user.getRole());
    }

    /**
     * Método de prueba. Añadir géneros favoritos
     */
    @Test
    public void UserService_AddFavoriteGenres_ReturnFavorites() {
        User user = new User();
        List<Long> genreIds = List.of(1L, 2L);
        List<Genre> genres = List.of(new Genre(), new Genre());

        when(genreRepository.findAllById(genreIds)).thenReturn(genres);

        userService.addFavoriteGenres(user, genreIds);

        assertEquals(2, user.getFavoriteGenres().size());
        verify(userRepository).save(user);
    }

    /**
     * Método de prueba. Añadir demasiados géneros favoritos
     */
    @Test
    public void UserService_AddFavoriteGenres_TooManyGenres() {
        User user = new User();
        List<Long> genreIds = List.of(1L, 2L, 3L, 4L, 5L, 6L);

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> userService.addFavoriteGenres(user, genreIds)
        );

        assertEquals("Sólo se pueden elegir 5 géneros favoritos como máximo.", exception.getMessage());
    }

    /**
     * Método de prueba. Añadir géneros favoritos inexistentes
     */
    @Test
    public void UserService_AddFavoriteGenres_UnexistentGenre() {
        User user = new User();
        List<Long> genreIds = List.of(1L, 2L);
        List<Genre> genres = List.of(new Genre());

        when(genreRepository.findAllById(genreIds)).thenReturn(genres);

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> userService.addFavoriteGenres(user, genreIds)
        );

        assertEquals("Uno o más géneros no existen.", exception.getMessage());
    }

    /**
     * Método de prueba. Añadir libros favoritos
     */
    @Test
    public void UserService_AddFavoriteBooks_ReturnFavorites() {
        User user = new User();
        List<Long> bookIds = List.of(1L, 2L);
        List<Book> books = List.of(new Book(), new Book());

        when(bookRepository.findAllById(bookIds)).thenReturn(books);

        userService.addFavoriteBooks(user, bookIds);

        assertEquals(2, user.getFavoriteBooks().size());
        verify(userRepository).save(user);
    }

    /**
     * Método de prueba. Añadir demasiados libros favoritos
     */
    @Test
    public void UserService_AddFavoriteBooks_TooManyBooks() {
        User user = new User();
        List<Long> bookIds = List.of(1L, 2L, 3L, 4L, 5L);

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> userService.addFavoriteBooks(user, bookIds)
        );

        assertEquals("Sólo se pueden elegir 4 libros favoritos como máximo.", exception.getMessage());
    }

    /**
     * Método de prueba. Añadir libros favoritos inexistentes
     */
    @Test
    public void UserService_AddFavoriteBooks_UnexistentBook() {
        User user = new User();
        List<Long> bookIds = List.of(1L, 2L);
        List<Book> books = List.of(new Book());

        when(bookRepository.findAllById(bookIds)).thenReturn(books);

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> userService.addFavoriteBooks(user, bookIds)
        );

        assertEquals("Uno o más libros no existen.", exception.getMessage());
    }

    /**
     * Método de prueba. Devolver los libros y géneros favoritos de un usuario
     */
    @Test
    public void UserService_GetFavorites_ReturnFavorites() {
        User user = new User();
        user.setFavoriteBooks(List.of(new Book()));
        user.setFavoriteGenres(List.of(new Genre()));

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserFavoritesDTO result = userService.getFavorites(1L);

        assertEquals(user, result.getUser());
        assertEquals(1, result.getFavoriteBooks().size());
        assertEquals(1, result.getFavoriteGenres().size());
    }

    /**
     * Método de prueba. Devolver los libros y géneros favoritos de un usuario inexistente
     */
    @Test
    public void UserService_GetFavorites_UnexistentUser() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> userService.getFavorites(99L)
        );

        assertEquals("El usuario con ID 99 no existe.", exception.getMessage());
    }

    /**
     * Método de prueba. Verificar que un usuario administrador tiene rol de administrador
     */
    @Test
    public void UserService_VerifyAdmin_UserIsAdmin() {
        User user = new User();
        user.setRole(2);

        assertTrue(userService.verifyAdmin(user));
    }

    /**
     * Método de prueba. Verificar que un usuario estándar no tiene rol de administrador
     */
    @Test
    public void UserService_VerifyAdmin_UserIsNotAdmin() {
        User user = new User();
        user.setRole(0);

        assertFalse(userService.verifyAdmin(user));
    }

    /**
     * Método de prueba. Devolver los autores verificados
     */
    @Test
    public void UserService_GetAuthors_ReturnUsers() {
        int page = 0, size = 5;
        Page<User> users = new PageImpl<>(List.of(new User()));
        when(userRepository.findAuthors(any(Pageable.class))).thenReturn(users);
        when(userMapper.toDTO(any())).thenReturn(new UserDTO());

        Page<UserDTO> result = userService.getAuthors(page, size);

        assertEquals(1, result.getContent().size());
    }
}
