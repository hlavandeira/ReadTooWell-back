package es.readtoowell.api_biblioteca.service;

import es.readtoowell.api_biblioteca.mapper.BookListMapper;
import es.readtoowell.api_biblioteca.model.*;
import es.readtoowell.api_biblioteca.model.DTO.BookListDTO;
import es.readtoowell.api_biblioteca.repository.BookListRepository;
import es.readtoowell.api_biblioteca.repository.BookRepository;
import es.readtoowell.api_biblioteca.repository.GenreRepository;
import es.readtoowell.api_biblioteca.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class BookListService {
    @Autowired
    private BookListRepository listRepository;
    @Autowired
    private BookListMapper listMapper;
    @Autowired
    private GenreRepository genreRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BookRepository bookRepository;

    public Page<BookListDTO> getListsByUser(Long idUser, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<BookList> lists = listRepository.findByUsuarioId(idUser, pageable);

        return lists.map(listMapper::toDTO);
    }

    public BookListDTO createList(User user, BookListDTO dto, Set<Long> genreIds) {
        BookList lista = new BookList();

        lista.setNombre(dto.getNombre());
        lista.setDescripcion(dto.getDescripcion());
        lista.setUsuario(user);

        Set<Genre> genres = new HashSet<>(genreRepository.findAllById(genreIds));

        lista.setGeneros(genres);

        lista = listRepository.save(lista);

        return listMapper.toDTO(lista);
    }

    public BookListDTO updateList(Long idUser, Long idList, BookListDTO dto, Set<Long> genreIds) {
        BookList lista = listRepository.findByIdWithRelations(idList)
                .orElseThrow(() -> new EntityNotFoundException("La lista con ID " + idList + " no existe."));

        if (!lista.getUsuario().getId().equals(idUser)) {
            throw new AccessDeniedException("No tienes permiso para editar esta lista.");
        }

        lista.setNombre(dto.getNombre());
        lista.setDescripcion(dto.getDescripcion());

        Set<Genre> genres = new HashSet<>(genreRepository.findAllById(genreIds));
        lista.setGeneros(genres);

        lista = listRepository.save(lista);

        return listMapper.toDTO(lista);
    }

    public BookListDTO deleteList(Long idUser, Long idList) {
        BookList list = listRepository.findByIdWithRelations(idList)
                .orElseThrow(() -> new EntityNotFoundException("Lista con ID " + idList + " no encontrada."));

        if (!list.getUsuario().getId().equals(idUser)) {
            throw new AccessDeniedException("No tienes permiso para borrar esta lista.");
        }

        listRepository.delete(list);

        return listMapper.toDTO(list);
    }

    public BookListDTO addBookToList(Long idUser, Long idList, Long idBook) {
        BookList list = listRepository.findByIdWithRelations(idList)
                .orElseThrow(() -> new EntityNotFoundException("Lista con ID " + idList + " no encontrada."));

        if (!list.getUsuario().getId().equals(idUser)) {
            throw new AccessDeniedException("No tienes permiso para acceder a esta lista");
        }

        Book book = bookRepository.findById(idBook)
                .orElseThrow(() -> new EntityNotFoundException("Libro con ID " + idList + " no encontrado."));

        if (!list.getLibros().contains(book)) {
            BookListItem addedBook = new BookListItem();
            addedBook.setLista(list);
            addedBook.setLibro(book);
            addedBook.setFechaAñadido(Date.valueOf(LocalDate.now()));
            addedBook.setId(new BookListItemId(list.getId(), book.getId()));

            list.getLibros().add(addedBook);
            list = listRepository.save(list);
        }

        return listMapper.toDTO(list);
    }

    public BookListDTO deleteBookFromList(Long idUser, Long idList, Long idBook) {
        BookList list = listRepository.findByIdWithRelations(idList)
                .orElseThrow(() -> new EntityNotFoundException("Lista con ID " + idList + " no encontrada."));

        if (!list.getUsuario().getId().equals(idUser)) {
            throw new AccessDeniedException("No tienes permiso para acceder a esta lista");
        }

        Book book = bookRepository.findById(idBook)
                .orElseThrow(() -> new EntityNotFoundException("Libro con ID " + idBook + " no encontrado."));

        if (list.getLibros().contains(book)) {
            list.getLibros().remove(book);
            list = listRepository.save(list);
        }

        return listMapper.toDTO(list);
    }
}
