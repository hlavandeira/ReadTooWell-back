package es.readtoowell.api_biblioteca.service.user;

import es.readtoowell.api_biblioteca.model.DTO.AuthorRequestDTO;
import es.readtoowell.api_biblioteca.model.DTO.user.UpdateProfileDTO;
import es.readtoowell.api_biblioteca.model.entity.Book;
import es.readtoowell.api_biblioteca.model.DTO.user.UserDTO;
import es.readtoowell.api_biblioteca.mapper.UserMapper;
import es.readtoowell.api_biblioteca.model.DTO.user.UserFavoritesDTO;
import es.readtoowell.api_biblioteca.model.entity.Genre;
import es.readtoowell.api_biblioteca.model.entity.User;
import es.readtoowell.api_biblioteca.model.enums.Role;
import es.readtoowell.api_biblioteca.repository.book.BookRepository;
import es.readtoowell.api_biblioteca.repository.book.GenreRepository;
import es.readtoowell.api_biblioteca.repository.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio encargado de gestionar la lógica relacionada con los usuarios.
 */
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private GenreRepository genreRepository;
    @Autowired
    private BookRepository bookRepository;

    /**
     * Devuelve todos los usuarios.
     *
     * @param page Número de la página que se quiere devolver
     * @param size Tamaño de la página
     * @return Página con los usuarios como DTOs
     */
    public Page<UserDTO> getAllUsers(int page, int size) {
        Page<User> users = userRepository.findAll(PageRequest.of(page, size, Sort.by("username")));
        return users.map(userMapper::toDTO);
    }

    /**
     * Devuelve un usuario según su ID.
     *
     * @param id ID del usuario
     * @return DTO con los datos del usuario
     * @throws EntityNotFoundException El usuario no existe
     */
    public UserDTO getUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("El usuario con ID " + id + " no existe."));
        return userMapper.toDTO(user);
    }

    /**
     * Devuelve el usuario autenticado.
     *
     * @return Usuario autenticado, 'null' si no hay ninguno
     */
    public User getAuthenticatedUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof User) {
            return (User) principal;
        } else if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            return userRepository.findByEmail(username).orElse(null);
        }
        return null;
    }

    /**
     * Crea un nuevo usuario.
     *
     * @param user DTO con los datos del usuario a crear
     * @return DTO con los datos del usuario creado
     */
    public UserDTO createUser(UserDTO user) {
        user.setRole(Role.USER.getValue());
        User entity = userRepository.save(userMapper.toEntity(user));
        return userMapper.toDTO(entity);
    }

    /**
     * Elimina un usuario.
     *
     * @param id ID del usuario a eliminar
     * @return DTO con los datos del usuario eliminado
     * @throws EntityNotFoundException El usuario no existe
     */
    public UserDTO deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("El usuario con ID " + id + " no existe."));

        userRepository.delete(user);

        return userMapper.toDTO(user);
    }

    /**
     * Actualiza los datos de un usuario.
     *
     * @param idUser ID del usuario a actualizar
     * @param user DTO con los nuevos datos del usuario
     * @return DTO con los datos del usuario actualizado
     * @throws EntityNotFoundException El usuario no existe
     */
    public UserDTO updateUser(Long idUser, UserDTO user) {
        User usuario = userRepository.findById(idUser)
                .orElseThrow(() -> new EntityNotFoundException("El usuario con ID " + idUser + " no existe."));

        usuario.setUsername(user.getUsername().trim());
        usuario.setProfileName(user.getProfileName().trim());
        usuario.setPassword(user.getPassword());
        usuario.setEmail(user.getEmail());
        usuario.setBiography(user.getBiography().trim());
        usuario.setRole(user.getRole());
        usuario.setProfilePic(user.getProfilePic());

        usuario = userRepository.save(usuario);

        return userMapper.toDTO(usuario);
    }

    /**
     * Actualiza los datos que puede editar el usuario en su perfil.
     *
     * @param idUser ID del usuario que edita su perfil
     * @param user Datos introducidos por el usuario
     * @return Datos actualizados del usuario
     * @throws EntityNotFoundException El usuario no existe
     */
    public UserDTO updateUserProfile(Long idUser, UpdateProfileDTO user) {
        User usuario = userRepository.findById(idUser)
                .orElseThrow(() -> new EntityNotFoundException("El usuario con ID " + idUser + " no existe."));

        usuario.setProfileName(user.getProfileName().trim());
        usuario.setBiography(user.getBiography().trim());
        usuario.setProfilePic(user.getProfilePic());

        usuario = userRepository.save(usuario);

        return userMapper.toDTO(usuario);
    }

    /**
     * Devuelve los usuarios que sigue un usuario específico.
     *
     * @param id ID del usuario del que se consultan los seguidos
     * @return Lista con los usuarios seguidos como DTOs
     */
    public List<UserDTO> getFollows(Long id) {
        return userRepository.findById(id)
                .map(user -> user.getFollowedUsers().stream()
                        .map(userMapper::toDTO)
                        .collect(Collectors.toList()))
                .orElse(Collections.emptyList());
    }

    /**
     * Devuelve los seguidores de un usuario específico.
     *
     * @param id ID del usuario del que se consultan los seguidores
     * @return Lista con los usuarios seguidores como DTOs
     */
    public List<UserDTO> getFollowers(Long id) {
        return userRepository.findById(id)
                .map(user -> user.getFollowers().stream()
                        .map(userMapper::toDTO)
                        .collect(Collectors.toList()))
                .orElse(Collections.emptyList());
    }

    /**
     * Un usuario sigue a otro usuario.
     *
     * @param idUser ID del usuario seguidor
     * @param idFollowedUser ID del usuario al que sigue
     * @return DTO con los datos del usuario seguido
     * @throws EntityNotFoundException Alguno de los usuarios no existe
     * @throws IllegalStateException Alguno de los usuarios es un administrador
     */
    public UserDTO followUser(Long idUser, Long idFollowedUser) {
        User user = userRepository.findById(idUser)
                .orElseThrow(() -> new EntityNotFoundException("El usuario con ID " + idUser + " no existe."));
        User followedUser = userRepository.findById(idFollowedUser)
                .orElseThrow(() -> new EntityNotFoundException("El usuario con ID " + idFollowedUser + " no existe."));

        if (user.getRole() == Role.ADMIN.getValue() || followedUser.getRole() == Role.ADMIN.getValue()) {
            throw new IllegalStateException("No se puede seguir a un usuario administrador.");
        }

        user.getFollowedUsers().add(followedUser);
        followedUser.getFollowers().add(user);

        userRepository.save(user);
        userRepository.save(followedUser);

        return userMapper.toDTO(followedUser);
    }

    /**
     * Un usuario deja de seguir a otro usuario.
     *
     * @param idUser ID del usuario seguidor
     * @param idUnfollowedUser ID del usuario al que deja de seguir
     * @return DTO con los datos del usuario que se ha dejado de seguir
     * @throws EntityNotFoundException Alguno de los usuarios no existe
     * @throws IllegalStateException Alguno de los usuarios es un administrador
     */
    public UserDTO unfollowUser(Long idUser, Long idUnfollowedUser) {
        User user = userRepository.findById(idUser)
                .orElseThrow(() -> new EntityNotFoundException("El usuario con ID " + idUser + " no existe."));
        User unfollowedUser = userRepository.findById(idUnfollowedUser)
                .orElseThrow(() -> new EntityNotFoundException("El usuario con ID " +
                        idUnfollowedUser + " no existe."));

        if (user.getRole() == Role.ADMIN.getValue() || unfollowedUser.getRole() == Role.ADMIN.getValue()) {
            throw new IllegalStateException("No se puede dejar de seguir a un usuario administrador.");
        }

        user.getFollowedUsers().remove(unfollowedUser);
        unfollowedUser.getFollowers().remove(user);

        userRepository.save(user);
        userRepository.save(unfollowedUser);

        return userMapper.toDTO(unfollowedUser);
    }

    /**
     * Busca usuarios mediante una cadena de texto.
     *
     * @param searchString Cadena por la que se buscan usuarios
     * @param page Número de la página que se quiere devolver
     * @param size Tamaño de la página
     * @return Página con los usuarios encontrados como DTOs
     */
    public Page<UserDTO> searchUsers(String searchString, int page, int size) {
        Page<User> users =  userRepository.searchUsers(searchString, PageRequest.of(page, size));
        return users.map(userMapper::toDTO);
    }

    /**
     * Se promociona a un usuario a autor.
     * Su rol pasa a ser AUTHOR.
     *
     * @param request Solicitud de verificación enviada por el usuario
     * @return DTO con los datos del usuario actualizado
     * @throws EntityNotFoundException El usuario no existe o no tiene ninguna solicitud pendiente
     */
    public UserDTO promoteToAuthor(AuthorRequestDTO request) {
        Long idUser = request.getUser().getId();
        User user = userRepository.findById(idUser)
                .orElseThrow(() -> new EntityNotFoundException("El usuario con ID " + idUser + " no existe."));

        user.setRole(Role.AUTHOR.getValue());

        user.setProfileName(request.getName());
        user.setBiography(request.getBiography());

        request.setActive(false);

        user = userRepository.save(user);

        return userMapper.toDTO(user);
    }

    /**
     * Añade géneros favoritos a un usuario.
     *
     * @param user Usuario que añade sus géneros favoritos
     * @param genreIds Lista de IDs de los géneros seleccionados
     * @throws ValidationException Hay más géneros de los que se pueden añadir
     * @throws EntityNotFoundException Alguno de los géneros no existe
     */
    public void addFavoriteGenres(User user, List<Long> genreIds) {
        if (genreIds.size() > 10) {
            throw new ValidationException("Sólo se pueden elegir 10 géneros favoritos como máximo.");
        }

        List<Genre> newGenres = new ArrayList<>(genreRepository.findAllById(genreIds));

        if (newGenres.size() != genreIds.size()) {
            throw new EntityNotFoundException("Uno o más géneros no existen.");
        }

        user.setFavoriteGenres(newGenres);
        userRepository.save(user);

        /*
        Set<GenreDTO> genreDTOs = user.getGenerosFavoritos()
                .stream().map(genreMapper::toDTO).collect(Collectors.toSet());

        return genreDTOs;
         */
    }

    /**
     * Añade libros favoritos a un usuario.
     *
     * @param user Usuario que añade sus libros favoritos
     * @param bookIds Lista de IDs de los libros seleccionados
     * @throws ValidationException Hay más libros de los que se pueden añadir
     * @throws EntityNotFoundException Alguno de los libros no existe
     */
    public void addFavoriteBooks(User user, List<Long> bookIds) {
        if (bookIds.size() > 4) {
            throw new ValidationException("Sólo se pueden elegir 4 libros favoritos como máximo.");
        }

        List<Book> newBooks = new ArrayList<>(bookRepository.findAllById(bookIds));

        if (newBooks.size() != bookIds.size()) {
            throw new EntityNotFoundException("Uno o más libros no existen.");
        }

        user.setFavoriteBooks(newBooks);
        userRepository.save(user);

        /*
        Set<BookDTO> bookDTOs = user.getLibrosFavoritos()
                .stream().map(bookMapper::toDTO).collect(Collectors.toSet());

        return bookDTOs;
         */
    }

    /**
     * Devuelve los libros y géneros favoritos de un usuario.
     *
     * @param idUser ID del usuario del que se devuelven sus favoritos
     * @return DTO con los datos de los libros y géneros favoritos del usuario
     * @throws EntityNotFoundException El usuario no existe
     */
    public UserFavoritesDTO getFavorites(Long idUser) {
        User user = userRepository.findById(idUser)
                .orElseThrow(() -> new EntityNotFoundException("El usuario con ID " + idUser + " no existe."));

        UserFavoritesDTO favoritos = new UserFavoritesDTO();

        favoritos.setUser(user);
        favoritos.setFavoriteGenres(user.getFavoriteGenres());
        favoritos.setFavoriteBooks(user.getFavoriteBooks());

        return favoritos;
    }

    /**
     * Verifica si un usuario tiene el rol de administrador.
     *
     * @param user Usuario a verificar
     * @return 'true' si tiene rol administrador, 'false' en caso contrario
     */
    public Boolean verifyAdmin(User user) {
        return user.getRole() == Role.ADMIN.getValue();
    }

    /**
     * Devuelve todos los usuarios que tienen el rol de autor.
     *
     * @param page Número de la página que se quiere devolver
     * @param size Tamaño de la página
     * @return Página con los usuarios resultantes como DTOs
     */
    public Page<UserDTO> getAuthors(int page, int size) {
        Page<User> users = userRepository.findAuthors(PageRequest.of(page, size));
        return users.map(userMapper::toDTO);
    }
}
