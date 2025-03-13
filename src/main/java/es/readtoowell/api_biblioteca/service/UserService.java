package es.readtoowell.api_biblioteca.service;

import es.readtoowell.api_biblioteca.model.Book;
import es.readtoowell.api_biblioteca.model.User;
import es.readtoowell.api_biblioteca.repository.BookRepository;
import es.readtoowell.api_biblioteca.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public Page<User> getAllUsers(int page, int size) {
        return userRepository.findAll(PageRequest.of(page, size, Sort.by("nombreUsuario")));
    }

    public Optional<User> getUser(Long id) {
        return userRepository.findById(id);
    }

    public User createBook(User user) {
        return userRepository.save(user);
    }

    public User deleteUser(User user) {
        return userRepository.save(user);
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

        return followedUser;
    }

    public User unfollowUser(Long idUser, Long idUnfollowedUser) {
        User user = userRepository.findById(idUser).orElseThrow();
        User unfollowedUser = userRepository.findById(idUnfollowedUser).orElseThrow();

        user.getSeguidos().remove(unfollowedUser);
        unfollowedUser.getSeguidores().remove(user);

        return unfollowedUser;
    }
}
