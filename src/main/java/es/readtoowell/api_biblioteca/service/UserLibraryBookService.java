package es.readtoowell.api_biblioteca.service;

import es.readtoowell.api_biblioteca.mapper.UserLibraryBookMapper;
import es.readtoowell.api_biblioteca.model.DTO.UserLibraryBookDTO;
import es.readtoowell.api_biblioteca.model.User;
import es.readtoowell.api_biblioteca.model.UserLibraryBook;
import es.readtoowell.api_biblioteca.repository.UserLibraryBookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class UserLibraryBookService {
    @Autowired
    private UserLibraryBookRepository libraryRepository;
    @Autowired
    private UserLibraryBookMapper libraryMapper;

    public Page<UserLibraryBookDTO> getLibraryFromUser(User user, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<UserLibraryBook> libros = libraryRepository.findByUsuario(user, pageable);

        return libros.map(libraryMapper::toDTO);
    }
}
