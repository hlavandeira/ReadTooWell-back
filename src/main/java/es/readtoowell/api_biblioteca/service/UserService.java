package es.readtoowell.api_biblioteca.service;

import es.readtoowell.api_biblioteca.DTO.UserDTO;
import es.readtoowell.api_biblioteca.mapper.UserMapper;
import es.readtoowell.api_biblioteca.model.User;
import es.readtoowell.api_biblioteca.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;

    public Page<User> getAllUsers(int page, int size) {
        return userRepository.findAll(PageRequest.of(page, size, Sort.by("nombreUsuario")));
    }

    public Optional<User> getUser(Long id) {
        return userRepository.findById(id);
    }

    public User createUser(UserDTO user) {
        return userRepository.save(userMapper.toEntity(user));
    }

    public User deleteUser(UserDTO user) {
        return userRepository.save(userMapper.toEntity(user));
    }

    public User updateUser(Long id, UserDTO user) {
        User usuario = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario con ID " + id + " no encontrado"));

        fillUserData(usuario, user);

        return userRepository.save(usuario);
    }

    private void fillUserData(User user, UserDTO dto) {
        user.setNombreUsuario(dto.getNombreUsuario());
        user.setNombrePerfil(dto.getNombrePerfil());
        user.setContraseña(dto.getContraseña());
        user.setCorreo(dto.getCorreo());
        user.setBiografia(dto.getBiografia());
        user.setRol(dto.getRol());
        user.setFotoPerfil(dto.getFotoPerfil());
        user.setActivo(dto.isActivo());
    }

    public Set<User> getFollows(Long id) {
        return userRepository.findById(id)
                .map(User::getSeguidos)
                .orElse(Collections.emptySet());
    }

    public Set<User> getFollowers(Long id) {
        return userRepository.findById(id)
                .map(User::getSeguidores)
                .orElse(Collections.emptySet());
    }

    public User followUser(Long idUser, Long idFollowedUser) {
        User user = userRepository.findById(idUser).orElseThrow();
        User followedUser = userRepository.findById(idFollowedUser).orElseThrow();

        user.getSeguidos().add(followedUser);
        followedUser.getSeguidores().add(user);

        userRepository.save(user);
        userRepository.save(followedUser);

        return followedUser;
    }

    public User unfollowUser(Long idUser, Long idUnfollowedUser) {
        User user = userRepository.findById(idUser).orElseThrow();
        User unfollowedUser = userRepository.findById(idUnfollowedUser).orElseThrow();

        user.getSeguidos().remove(unfollowedUser);
        unfollowedUser.getSeguidores().remove(user);

        userRepository.save(user);
        userRepository.save(unfollowedUser);

        return unfollowedUser;
    }

    public Page<User> searchUsers(String searchString, int page, int size) {
        return userRepository.searchUsers(searchString, PageRequest.of(page, size));
    }
}
