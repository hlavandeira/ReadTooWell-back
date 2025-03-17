package es.readtoowell.api_biblioteca.service;

import es.readtoowell.api_biblioteca.mapper.BookListMapper;
import es.readtoowell.api_biblioteca.mapper.GenreMapper;
import es.readtoowell.api_biblioteca.mapper.UserMapper;
import es.readtoowell.api_biblioteca.model.BookList;
import es.readtoowell.api_biblioteca.model.DTO.BookListDTO;
import es.readtoowell.api_biblioteca.model.DTO.GenreDTO;
import es.readtoowell.api_biblioteca.model.DTO.UserDTO;
import es.readtoowell.api_biblioteca.model.Genre;
import es.readtoowell.api_biblioteca.model.User;
import es.readtoowell.api_biblioteca.repository.BookListRepository;
import es.readtoowell.api_biblioteca.repository.GenreRepository;
import es.readtoowell.api_biblioteca.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BookListService {
    @Autowired
    private BookListRepository listRepository;
    @Autowired
    private BookListMapper listMapper;
    @Autowired
    private GenreRepository genreRepository;
    @Autowired
    private GenreMapper genreMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;

    @PreAuthorize("#idUser == authentication.principal.id")
    public Page<BookListDTO> getListsByUser(Long idUser, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<BookList> lists = listRepository.findByUsuarioId(idUser, pageable);

        return lists.map(listMapper::toDTO);
    }

    @PreAuthorize("#idUser == authentication.principal.id")
    public BookListDTO createList(Long idUser, BookListDTO dto, Set<Long> genreIds) {
        BookList lista = new BookList();

        lista.setNombre(dto.getNombre());
        lista.setDescripcion(dto.getDescripcion());

        Set<Genre> genres = new HashSet<>(genreRepository.findAllById(genreIds));

        lista.setGeneros(genres);

        Optional<User> user = userRepository.findById(idUser);
        if (user.isPresent()) {
            lista.setUsuario(user.get());
        } else {
            throw new EntityNotFoundException("El usuario con ID " + idUser + " no existe.");
        }

        lista = listRepository.save(lista);

        return listMapper.toDTO(lista);
    }

    @PreAuthorize("#idUser == authentication.principal.id")
    public BookListDTO deleteList(Long idUser, Long idList) {
        BookList list = listRepository.findByIdWithRelations(idList)
                .orElseThrow(() -> new EntityNotFoundException("Lista con ID " + idList + " no encontrada."));

        listRepository.delete(list);

        return listMapper.toDTO(list);
    }
}
