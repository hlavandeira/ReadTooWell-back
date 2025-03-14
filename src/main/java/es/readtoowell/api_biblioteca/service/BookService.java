package es.readtoowell.api_biblioteca.service;

import es.readtoowell.api_biblioteca.DTO.BookDTO;
import es.readtoowell.api_biblioteca.DTO.GenreDTO;
import es.readtoowell.api_biblioteca.mapper.BookMapper;
import es.readtoowell.api_biblioteca.mapper.GenreMapper;
import es.readtoowell.api_biblioteca.model.Book;
import es.readtoowell.api_biblioteca.model.Collection;
import es.readtoowell.api_biblioteca.model.Genre;
import es.readtoowell.api_biblioteca.repository.BookRepository;
import es.readtoowell.api_biblioteca.repository.CollectionRepository;
import es.readtoowell.api_biblioteca.repository.GenreRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private GenreRepository genreRepository;
    @Autowired
    private CollectionRepository collectionRepository;
    @Autowired
    private BookMapper bookMapper;
    @Autowired
    private GenreMapper genreMapper;

    public Page<Book> getAllBooks(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "añoPublicacion"));
        return bookRepository.findAll(pageable);
    }

    public Optional<Book> getBook(Long id) {
        return bookRepository.findById(id);
    }

    public Book createBook(BookDTO bookDTO, Set<Long> genreIds) {
        Book book = new Book();
        fillBookData(null, book, bookDTO);

        return bookRepository.save(book);
    }

    public Book updateBook(Long id, BookDTO book, Set<Long> genreIds) {
        Book libro = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Libro con ID " + id + " no encontrado"));

        Set<Genre> genres = new HashSet<>(genreRepository.findAllById(genreIds));

        Set<GenreDTO> genreDTOs = genres.stream()
                .map(genreMapper::toDTO)
                .collect(Collectors.toSet());

        book.setGeneros(genreDTOs);
        fillBookData(id, libro, book);

        return bookRepository.save(libro);
    }

    private void fillBookData(Long id, Book book, BookDTO bookDTO) {
        if (bookDTO.getIsbn() != null && !bookDTO.getIsbn().isEmpty()) {
            if (id == null) { // Entra al crear un libro
                if (bookRepository.findByIsbn(bookDTO.getIsbn()).isPresent()) {
                    throw new IllegalArgumentException("El ISBN ya está en uso.");
                }
            } else { // Entra al editar un libro
                if (bookRepository.findByIsbnAndIdNot(bookDTO.getIsbn(), id).isPresent()) {
                    throw new IllegalArgumentException("El ISBN ya está en uso por otro libro.");
                }
            }
            book.setIsbn(bookDTO.getIsbn());
        }

        book.setTitulo(bookDTO.getTitulo());
        book.setAutor(bookDTO.getAutor());
        book.setAñoPublicacion(bookDTO.getAñoPublicacion());
        book.setNumeroPaginas(bookDTO.getNumeroPaginas());
        book.setEditorial(bookDTO.getEditorial());
        book.setSinopsis(bookDTO.getSinopsis());
        book.setPortada(bookDTO.getPortada());
        book.setNumColeccion(bookDTO.getNumColeccion());

        if (bookDTO.getIdColeccion() != null) {
            Collection collection = collectionRepository.findById(bookDTO.getIdColeccion())
                    .orElseThrow(() -> new EntityNotFoundException("Colección no encontrada con ID: " + bookDTO.getIdColeccion()));
            book.setColeccion(collection);
        } else {
            book.setColeccion(null);
        }

        if (bookDTO.getGeneros() != null && !bookDTO.getGeneros().isEmpty()) {
            Set<Long> genreIds = bookDTO.getGeneros()
                    .stream()
                    .map(GenreDTO::getId)
                    .collect(Collectors.toSet());

            Set<Genre> genres = new HashSet<>(genreRepository.findAllById(genreIds));

            if (genres.size() != genreIds.size()) {
                throw new IllegalArgumentException("Algunos géneros no existen");
            }

            book.setGeneros(genres);
        }
    }

    public Book deleteBook(BookDTO book) {
        Book libro = bookMapper.toEntity(book);

        if (book.getIdColeccion() != null) {
            Collection collection = collectionRepository.findById(book.getIdColeccion())
                    .orElseThrow(() -> new EntityNotFoundException("Colección no encontrada"));
            libro.setColeccion(collection);
        }

        return bookRepository.save(libro);
    }

    public Page<Book> filterBooks(String searchString, Integer minPags, Integer maxPags,
                                  Integer minAño, Integer maxAño, int page, int size) {
        return bookRepository.filterBooks(searchString, minPags, maxPags, minAño,
                maxAño, PageRequest.of(page, size));
    }
}
