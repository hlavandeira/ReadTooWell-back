package es.readtoowell.api_biblioteca.service;

import es.readtoowell.api_biblioteca.config.security.CustomUserDetails;
import es.readtoowell.api_biblioteca.mapper.*;
import es.readtoowell.api_biblioteca.model.*;
import es.readtoowell.api_biblioteca.model.DTO.*;
import es.readtoowell.api_biblioteca.model.enums.SuggestionStatus;
import es.readtoowell.api_biblioteca.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.time.LocalDate;
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
    @Autowired
    private UserLibraryBookRepository libraryRepository;
    @Autowired
    private BookListRepository listRepository;
    @Autowired
    private BookListMapper listMapper;
    @Autowired
    private BookListItemRepository listItemRepository;

    public Page<BookDTO> getAllBooks(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "añoPublicacion"));
        return bookRepository.findAll(pageable).map(bookMapper::toDTO);
    }

    public BookDTO getBook(Long idBook) {
        Book book = bookRepository.findById(idBook)
                .orElseThrow(() -> new EntityNotFoundException("El libro con ID " + idBook + " no existe."));;
        return bookMapper.toDTO(book);
    }

    public BookDTO createBook(BookDTO bookDTO, Set<Long> genreIds) {
        Book book = new Book();

        Set<Genre> genres = new HashSet<>(genreRepository.findAllById(genreIds));
        Set<GenreDTO> genreDTOs = genres.stream()
                .map(genreMapper::toDTO)
                .collect(Collectors.toSet());

        bookDTO.setGeneros(genreDTOs);
        fillBookData(null, book, bookDTO);

        book = bookRepository.save(book);

        return bookMapper.toDTO(book);
    }

    public BookDTO updateBook(Long idBook, BookDTO book, Set<Long> genreIds) {
        Book libro = bookRepository.findById(idBook)
                .orElseThrow(() -> new EntityNotFoundException("El libro con ID " + idBook + " no existe."));

        Set<Genre> genres = new HashSet<>(genreRepository.findAllById(genreIds));

        Set<GenreDTO> genreDTOs = genres.stream()
                .map(genreMapper::toDTO)
                .collect(Collectors.toSet());

        book.setGeneros(genreDTOs);
        fillBookData(idBook, libro, book);

        libro = bookRepository.save(libro);

        return bookMapper.toDTO(libro);
    }

    private void fillBookData(Long idBook, Book book, BookDTO bookDTO) {
        if (bookDTO.getIsbn() != null && !bookDTO.getIsbn().isEmpty()) {
            if (idBook == null) { // Entra al crear un libro
                if (bookRepository.findByIsbn(bookDTO.getIsbn()).isPresent()) {
                    throw new IllegalArgumentException("El ISBN ya está en uso.");
                }
            } else { // Entra al editar un libro
                if (bookRepository.findByIsbnAndIdNot(bookDTO.getIsbn(), idBook).isPresent()) {
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

        book.setActivo(true);

        if (bookDTO.getIdColeccion() != null) {
            Collection collection = collectionRepository.findById(bookDTO.getIdColeccion())
                    .orElseThrow(() -> new EntityNotFoundException("Colección no encontrada con ID: " +
                            bookDTO.getIdColeccion()));
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

    public BookDTO deleteBook(BookDTO book) {
        Book libro = bookMapper.toEntity(book);
        libro.delete();
        libro = bookRepository.save(libro);
        return bookMapper.toDTO(libro);
    }

    public Page<BookDTO> filterBooks(String searchString, Integer minPags, Integer maxPags,
                                  Integer minAño, Integer maxAño, int page, int size) {
        Page<Book> librosFiltrados = bookRepository.filterBooks(searchString, minPags, maxPags, minAño,
                maxAño, PageRequest.of(page, size));
        return librosFiltrados.map(bookMapper::toDTO);
    }

    public BookDetailsDTO getBookDetails(Long idBook, User user) {
        Book libro = bookRepository.findById(idBook)
                .orElseThrow(() -> new EntityNotFoundException("El libro con ID " + idBook + " no existe."));

        BookDetailsDTO details = new BookDetailsDTO();

        details.setLibro(bookMapper.toDTO(libro));

        Optional<UserLibraryBook> libroGuardado = libraryRepository.findByUsuarioAndLibro(user, libro);
        if (libroGuardado.isPresent()) {
            details.setGuardado(true);
            UserLibraryBook lib = libroGuardado.get();
            details.setEstadoLectura(lib.getEstadoLectura());
            details.setCalificacion(lib.getCalificacion());
            details.setReseña(lib.getReseña());
            details.setFechaInicio(lib.getFechaInicio());
            details.setFechaFin(lib.getFechaFin());
        } else {
            details.setEstadoLectura(0);
            details.setCalificacion(0);
        }

        if (libro.getIdColeccion() != null) {
            Collection collection = collectionRepository.findById(libro.getIdColeccion())
                    .orElseThrow(() -> new EntityNotFoundException("La colección con ID " +
                            libro.getIdColeccion() + " no existe."));
            details.setNombreColeccion(collection.getNombre()); // Nombre, el número se guarda en el libro
        }

        double calificacionMedia = libraryRepository.findAverageRatingByBookId(libro.getId());
        details.setCalificacionMedia(BigDecimal.valueOf(calificacionMedia)
                .setScale(2, RoundingMode.HALF_UP).doubleValue());

        Set<UserLibraryBook> reseñas = libraryRepository.findAllWithReviewByBookIdExcludingUser(libro.getId(),
                                                                                                user.getId());
        Set<ReviewDTO> otrasReseñas = reseñas.stream().map(r -> {
            ReviewDTO review = new ReviewDTO();
            review.setNombreUsuario(r.getUsuario().getNombreUsuario());
            review.setNombrePerfil(r.getUsuario().getNombrePerfil());
            review.setCalificacion(r.getCalificacion());
            review.setReseña(r.getReseña());
            return review;
        }).collect(Collectors.toSet());
        details.setReseñasOtrosUsuarios(otrasReseñas);

        Set<BookList> listas = listItemRepository.findAllListsByUserIdAndBookId(user.getId(), libro.getId());
        Set<SimpleBookListDTO> listasDTO = listas.stream().map(lista -> {
                SimpleBookListDTO simpleList = new SimpleBookListDTO();
                simpleList.setId(lista.getId());
                simpleList.setNombre(lista.getNombre());
                return simpleList;
        }).collect(Collectors.toSet());
        details.setListas(listasDTO);

        return details;
    }
}
