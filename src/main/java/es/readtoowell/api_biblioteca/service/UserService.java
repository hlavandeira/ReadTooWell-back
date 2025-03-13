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

import java.util.Optional;

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
}
