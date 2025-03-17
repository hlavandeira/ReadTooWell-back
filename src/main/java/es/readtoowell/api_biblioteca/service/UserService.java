package es.readtoowell.api_biblioteca.service;

import es.readtoowell.api_biblioteca.model.DTO.UserDTO;
import es.readtoowell.api_biblioteca.mapper.UserMapper;
import es.readtoowell.api_biblioteca.model.User;
import es.readtoowell.api_biblioteca.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;

    public Page<UserDTO> getAllUsers(int page, int size) {
        Page<User> users = userRepository.findAll(PageRequest.of(page, size, Sort.by("nombreUsuario")));
        return users.map(userMapper::toDTO);
    }

    public Optional<UserDTO> getUser(Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.map(userMapper::toDTO);
    }

    public UserDTO createUser(UserDTO user) {
        User entity = userRepository.save(userMapper.toEntity(user));
        return userMapper.toDTO(entity);
    }

    public UserDTO deleteUser(Long id) {
        Optional<User> usuario = userRepository.findById(id);
        if (usuario.isPresent()) {
            User user = usuario.get();
            user.delete();
            user = userRepository.save(user);
            return userMapper.toDTO(user);
        }
        return null;
    }

    @PreAuthorize("#id == authentication.principal.id")
    public UserDTO updateUser(Long id, UserDTO user) {
        User usuario = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario con ID " + id + " no encontrado"));

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

        user.setActivo(true);
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
        User user = userRepository.findById(idUser).orElseThrow();
        User followedUser = userRepository.findById(idFollowedUser).orElseThrow();

        user.getSeguidos().add(followedUser);
        followedUser.getSeguidores().add(user);

        userRepository.save(user);
        userRepository.save(followedUser);

        return userMapper.toDTO(followedUser);
    }

    @PreAuthorize("#idUser == authentication.principal.id")
    public UserDTO unfollowUser(Long idUser, Long idUnfollowedUser) {
        User user = userRepository.findById(idUser).orElseThrow();
        User unfollowedUser = userRepository.findById(idUnfollowedUser).orElseThrow();

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
}
