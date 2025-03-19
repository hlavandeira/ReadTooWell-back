package es.readtoowell.api_biblioteca.service;

import es.readtoowell.api_biblioteca.model.AuthorRequest;
import es.readtoowell.api_biblioteca.model.DTO.UserDTO;
import es.readtoowell.api_biblioteca.mapper.UserMapper;
import es.readtoowell.api_biblioteca.model.User;
import es.readtoowell.api_biblioteca.model.enums.RequestStatus;
import es.readtoowell.api_biblioteca.model.enums.Role;
import es.readtoowell.api_biblioteca.repository.AuthorRequestRepository;
import es.readtoowell.api_biblioteca.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static es.readtoowell.api_biblioteca.model.enums.RequestStatus.PENDIENTE;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private AuthorRequestRepository requestRepository;

    public Page<UserDTO> getAllUsers(int page, int size) {
        Page<User> users = userRepository.findAll(PageRequest.of(page, size, Sort.by("nombreUsuario")));
        return users.map(userMapper::toDTO);
    }

    public UserDTO getUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("El usuario con ID " + id + " no existe."));
        return userMapper.toDTO(user);
    }

    public User getAuthenticatedUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof User) {
            return (User) principal;
        } else if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            return userRepository.findByCorreo(username).orElse(null);
        }
        return null;
    }

    public UserDTO createUser(UserDTO user) {
        user.setRol(Role.USER.getValue());
        User entity = userRepository.save(userMapper.toEntity(user));
        return userMapper.toDTO(entity);
    }

    public UserDTO deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("El usuario con ID " + id + " no existe."));

        userRepository.delete(user);

        return userMapper.toDTO(user);
    }

    @PreAuthorize("#idUser == authentication.principal.id")
    public UserDTO updateUser(Long idUser, UserDTO user) {
        User usuario = userRepository.findById(idUser)
                .orElseThrow(() -> new EntityNotFoundException("El usuario con ID " + idUser + " no existe."));

        fillUserData(usuario, user);
        usuario = userRepository.save(usuario);

        return userMapper.toDTO(usuario);
    }

    private void fillUserData(User user, UserDTO dto) {
        user.setNombreUsuario(dto.getNombreUsuario());
        user.setNombrePerfil(dto.getNombrePerfil());
        user.setContraseña(dto.getContraseña());
        user.setCorreo(dto.getCorreo());
        user.setBiografia(dto.getBiografia());
        user.setRol(dto.getRol());
        user.setFotoPerfil(dto.getFotoPerfil());
    }

    public Set<UserDTO> getFollows(Long id) {
        return userRepository.findById(id)
                .map(user -> user.getSeguidos().stream()
                        .map(userMapper::toDTO)
                        .collect(Collectors.toSet()))
                .orElse(Collections.emptySet());
    }

    public Set<UserDTO> getFollowers(Long id) {
        return userRepository.findById(id)
                .map(user -> user.getSeguidores().stream()
                        .map(userMapper::toDTO)
                        .collect(Collectors.toSet()))
                .orElse(Collections.emptySet());
    }

    @PreAuthorize("#idUser == authentication.principal.id")
    public UserDTO followUser(Long idUser, Long idFollowedUser) {
        User user = userRepository.findById(idUser)
                .orElseThrow(() -> new EntityNotFoundException("El usuario con ID " + idUser + " no existe."));
        User followedUser = userRepository.findById(idFollowedUser)
                .orElseThrow(() -> new EntityNotFoundException("El usuario con ID " + idFollowedUser + " no existe."));

        user.getSeguidos().add(followedUser);
        followedUser.getSeguidores().add(user);

        userRepository.save(user);
        userRepository.save(followedUser);

        return userMapper.toDTO(followedUser);
    }

    @PreAuthorize("#idUser == authentication.principal.id")
    public UserDTO unfollowUser(Long idUser, Long idUnfollowedUser) {
        User user = userRepository.findById(idUser)
                .orElseThrow(() -> new EntityNotFoundException("El usuario con ID " + idUser + " no existe."));
        User unfollowedUser = userRepository.findById(idUnfollowedUser)
                .orElseThrow(() -> new EntityNotFoundException("El usuario con ID " +
                        idUnfollowedUser + " no existe."));

        user.getSeguidos().remove(unfollowedUser);
        unfollowedUser.getSeguidores().remove(user);

        userRepository.save(user);
        userRepository.save(unfollowedUser);

        return userMapper.toDTO(unfollowedUser);
    }

    public Page<UserDTO> searchUsers(String searchString, int page, int size) {
        Page<User> users =  userRepository.searchUsers(searchString, PageRequest.of(page, size));
        return users.map(userMapper::toDTO);
    }

    public UserDTO promoteToAuthor(Long idUser) {
        User user = userRepository.findById(idUser)
                .orElseThrow(() -> new EntityNotFoundException("El usuario con ID " + idUser + " no existe."));

        user.setRol(Role.AUTHOR.getValue());

        AuthorRequest request = requestRepository.findLatestRequestByUserAndStatus(
                idUser, List.of(RequestStatus.PENDIENTE.getValue()))
                .orElseThrow(() -> new EntityNotFoundException("El usuario no tienen ninguna solicitud pendiente."));

        user.setNombrePerfil(request.getNombre());
        user.setBiografia(request.getBiografia());

        request.setActivo(false);

        user = userRepository.save(user);

        return userMapper.toDTO(user);
    }
}
