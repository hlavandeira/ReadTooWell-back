package es.readtoowell.api_biblioteca.service.book;

import es.readtoowell.api_biblioteca.mapper.*;
import es.readtoowell.api_biblioteca.model.DTO.book.*;
import es.readtoowell.api_biblioteca.model.DTO.user.AuthorDTO;
import es.readtoowell.api_biblioteca.model.entity.*;
import es.readtoowell.api_biblioteca.model.enums.Role;
import es.readtoowell.api_biblioteca.model.enums.SuggestionStatus;
import es.readtoowell.api_biblioteca.repository.book.*;
import es.readtoowell.api_biblioteca.repository.library.UserLibraryBookRepository;
import es.readtoowell.api_biblioteca.repository.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Servicio encargado de gestionar la lógica relacionada con los libros.
 */
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
    private BookListItemRepository listItemRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CollectionMapper collectionMapper;
    @Autowired
    private SuggestionRepository suggestionRepository;
    @Autowired
    private SuggestionService suggestionService;

    /**
     * Devuelve todos los libros.
     * Si el usuario autenticado no es administrador, solo devuelve los activos.
     *
     * @param page Número de la página que se quiere devolver
     * @param size Tamaño de la página
     * @return Página con los libros resultantes como DTOs
     */
    public Page<BookDTO> getAllBooks(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "publicationYear"));
        Page<Book> books = bookRepository.findAllByActiveTrue(pageable);

        return books.map(bookMapper::toDTO);
    }

    /**
     * Devuelve un libro según su ID.
     *
     * @param idBook ID del libro a devolver
     * @return DTO con los datos del libro
     * @throws EntityNotFoundException El libro no existe
     */
    public BookDTO getBook(Long idBook) {
        Book book = bookRepository.findById(idBook)
                .orElseThrow(() -> new EntityNotFoundException("El libro con ID " + idBook + " no existe."));
        return bookMapper.toDTO(book);
    }

    /**
     * Crea un nuevo libro.
     *
     * @param bookDTO DTO con los datos del libro a añadir
     * @param genreIds Lista con los IDs de los géneros asociados al libro
     * @return DTO con los datos del libro creado
     */
    public BookDTO createBook(BookDTO bookDTO, List<Long> genreIds, User user) {
        if (user.getRole() != Role.ADMIN.getValue()) {
            throw new AccessDeniedException("Solo los admins pueden realizar esta acción.");
        }

        Book book = new Book();

        List<Genre> genres = new ArrayList<>(genreRepository.findAllById(genreIds));
        List<GenreDTO> genreDTOs = genres.stream()
                .map(genreMapper::toDTO)
                .collect(Collectors.toList());

        bookDTO.setGenres(genreDTOs);
        fillBookData(null, book, bookDTO);

        // Comprobar las solicitudes aceptadas (pendientes de añadir)
        updateAcceptedSuggestions(bookDTO, user);

        book = bookRepository.save(book);

        return bookMapper.toDTO(book);
    }

    /**
     * Actualiza los datos de un libro.
     *
     * @param idBook ID del libro a actualizar
     * @param book DTO con los datos a actualizar del libro
     * @param genreIds Lista con los IDs de los géneros asociados al libro
     * @return DTO con los datos del libro actualizado
     * @throws EntityNotFoundException El libro no existe
     * @throws AccessDeniedException El usuario no es un administrador
     */
    public BookDTO updateBook(Long idBook, BookDTO book, List<Long> genreIds, User user) {
        if (user.getRole() != Role.ADMIN.getValue()) {
            throw new AccessDeniedException("Solo los admins pueden realizar esta acción.");
        }

        Book libro = bookRepository.findById(idBook)
                .orElseThrow(() -> new EntityNotFoundException("El libro con ID " + idBook + " no existe."));

        List<Genre> genres = new ArrayList<>(genreRepository.findAllById(genreIds));

        List<GenreDTO> genreDTOs = genres.stream()
                .map(genreMapper::toDTO)
                .collect(Collectors.toList());

        book.setGenres(genreDTOs);
        fillBookData(idBook, libro, book);

        // Comprobar las solicitudes aceptadas (pendientes de añadir)
        updateAcceptedSuggestions(book, user);

        libro = bookRepository.save(libro);

        return bookMapper.toDTO(libro);
    }

    /**
     * Rellena los campos de una entidad {@code Book} con los datos de un {@code BookDTO}.
     *
     * @param idBook ID del libro
     * @param book Libro como entidad
     * @param bookDTO Libro como DTO
     * @throws IllegalArgumentException Ya existe un libro con el ISBN indicado
     * @throws EntityNotFoundException La colección o algún género no existe
     */
    private void fillBookData(Long idBook, Book book, BookDTO bookDTO) {
        if (bookDTO.getIsbn() != null && !bookDTO.getIsbn().isEmpty()) {
            if (idBook == null) { // Se está creando un libro
                if (bookRepository.findByIsbn(bookDTO.getIsbn()).isPresent()) {
                    throw new IllegalArgumentException("El ISBN ya está en uso.");
                }
            } else { // Se está editando un libro
                if (bookRepository.findByIsbnAndIdNot(bookDTO.getIsbn(), idBook).isPresent()) {
                    throw new IllegalArgumentException("El ISBN ya está en uso por otro libro.");
                }
            }
            book.setIsbn(bookDTO.getIsbn());
        }

        book.setTitle(bookDTO.getTitle().trim());
        book.setAuthor(bookDTO.getAuthor().trim());
        book.setPublicationYear(bookDTO.getPublicationYear());
        book.setPageNumber(bookDTO.getPageNumber());
        book.setPublisher(bookDTO.getPublisher().trim());
        book.setSynopsis(bookDTO.getSynopsis().trim());
        book.setNumCollection(bookDTO.getNumCollection());

        if (bookDTO.getCover() == null || bookDTO.getCover().isBlank()) {
            book.setCover("https://res.cloudinary.com/dfrgrfw4c/image/upload/" +
                    "v1743761214/readtoowell/covers/error_s7dry1.jpg");
        } else {
            book.setCover(bookDTO.getCover());
        }

        book.setActive(true);

        // Se comprueba si pertenece a una colección
        if (bookDTO.getCollectionId() != null) {
            Collection collection = collectionRepository.findById(bookDTO.getCollectionId())
                    .orElseThrow(() -> new EntityNotFoundException("Colección no encontrada con ID: " +
                            bookDTO.getCollectionId()));
            book.setCollection(collection);
        } else {
            book.setCollection(null);
        }

        if (bookDTO.getGenres() != null && !bookDTO.getGenres().isEmpty()) {
            List<Long> genreIds = bookDTO.getGenres()
                    .stream()
                    .map(GenreDTO::getId)
                    .collect(Collectors.toList());

            List<Genre> genres = new ArrayList<>(genreRepository.findAllById(genreIds));

            if (genres.size() != genreIds.size()) {
                throw new EntityNotFoundException("Algunos géneros no existen");
            }

            book.setGenres(genres);
        }
    }

    /**
     * Comprueba si alguna de las sugerencias aceptadas (es decir, pendientes de añadir),
     * coincide con los datos del libro que se está añadiendo/actualizando.
     *
     * @param book  Datos del libro que se añade/actualiza
     * @param user  Usuario autenticado
     */
    private void updateAcceptedSuggestions(BookDTO book, User user) {
        List<Suggestion> suggestions = suggestionRepository.findByStatus(SuggestionStatus.ACCEPTED.getValue());
        for (Suggestion sug : suggestions) {
            if (sug.getTitle().equals(book.getTitle()) && sug.getAuthor().equals(book.getAuthor())
                    && sug.getPublicationYear() == book.getPublicationYear()) {
                suggestionService.updateStatusSuggestion(sug.getId(), SuggestionStatus.ADDED.getValue(), user);
            }
        }
    }

    /**
     * Elimina un libro. Se realiza un borrado lógico.
     *
     * @param book DTO con los datos del libro a borrar
     * @return DTO con los datos del libro borrado
     * @throws AccessDeniedException El usuario no es un administrador
     */
    public BookDTO deleteBook(BookDTO book, User user) {
        if (user.getRole() != Role.ADMIN.getValue()) {
            throw new AccessDeniedException("Solo los admins pueden realizar esta acción.");
        }

        Book libro = bookMapper.toEntity(book);
        libro.delete();
        libro = bookRepository.save(libro);
        return bookMapper.toDTO(libro);
    }

    /**
     * Reactiva un libro que ha sido eliminado/desactivado.
     *
     * @param book DTO con los datos del libro a reactivar
     * @return DTO con los datos del libro reactivado
     * @throws AccessDeniedException El usuario no es un administrador
     */
    public BookDTO reactivateBook(BookDTO book, User user) {
        if (user.getRole() != Role.ADMIN.getValue()) {
            throw new AccessDeniedException("Solo los admins pueden realizar esta acción.");
        }

        Book libro = bookMapper.toEntity(book);
        libro.reactivate();
        libro = bookRepository.save(libro);
        return bookMapper.toDTO(libro);
    }

    /**
     * Busca libros por su título, autor o colección.
     * Permite filtrar por número de páginas y por año de publicación.
     *
     * @param searchString Cadena que se compara con el título, autor o colección
     * @param minPages Mínimo de páginas para filtrar
     * @param maxPages Máximo de páginas para filtrar
     * @param minYear Año de publicación mínimo para filtrar
     * @param maxYear Año de publicación máximo para filtrar
     * @param page Número de la página que se quiere devolver
     * @param size Tamaño de la página
     * @return Página con los libros resultantes como DTOs
     */
    public Page<BookDTO> filterBooks(String searchString, Integer minPages, Integer maxPages,
                                  Integer minYear, Integer maxYear, int page, int size) {
        Page<Book> librosFiltrados = bookRepository.filterBooks(searchString, minPages, maxPages, minYear,
                maxYear, PageRequest.of(page, size));

        return librosFiltrados.map(bookMapper::toDTO);
    }

    /**
     * Busca los libros de un género específico.
     *
     * @param idGenre ID del género que se busca
     * @param page Número de la página que se quiere devolver
     * @param size Tamaño de la página
     * @return Página con los libros resultantes como DTOs
     */
    public Page<BookDTO> filterBooksByGenre(Long idGenre, int page, int size) {
        Page<Book> librosFiltrados = bookRepository.findByGenresId(idGenre, PageRequest.of(page, size));

        return librosFiltrados.map(bookMapper::toDTO);
    }

    /**
     * Devuelve los detalles completos de un libro para un usuario.
     * Incluye la calificación, reseña y listas del usuario.
     *
     * @param idBook ID del libro
     * @param user Usuario que consulta los detalles
     * @return DTO con los detalles completos del libro
     * @throws EntityNotFoundException El libro o la colección no existen
     */
    public BookDetailsDTO getBookDetails(Long idBook, User user) {
        Book libro = bookRepository.findById(idBook)
                .orElseThrow(() -> new EntityNotFoundException("El libro con ID " + idBook + " no existe."));

        BookDetailsDTO details = new BookDetailsDTO();

        details.setBook(bookMapper.toDTO(libro));

        Optional<UserLibraryBook> libroGuardado = libraryRepository.findByUserAndBook(user, libro);
        if (libroGuardado.isPresent()) {
            details.setSaved(true);
            UserLibraryBook lib = libroGuardado.get();
            details.setReadingStatus(lib.getReadingStatus());
            details.setRating(lib.getRating());
            details.setReview(lib.getReview());
            details.setDateStart(lib.getDateStart());
            details.setDateFinish(lib.getDateFinish());
        } else {
            details.setReadingStatus(0);
            details.setRating(0);
        }

        if (libro.getCollectionId() != null) {
            Collection collection = collectionRepository.findById(libro.getCollectionId())
                    .orElseThrow(() -> new EntityNotFoundException("La colección con ID " +
                            libro.getCollectionId() + " no existe."));
            details.setCollectionName(collection.getName()); // Nombre, el número se guarda en el libro
        }

        double calificacionMedia = libraryRepository.findAverageRatingByBookId(libro.getId());
        details.setAverageRating(BigDecimal.valueOf(calificacionMedia)
                .setScale(2, RoundingMode.HALF_UP).doubleValue());

        List<UserLibraryBook> reviews = libraryRepository.findAllWithReviewByBookIdExcludingUser(libro.getId(),
                                                                                                user.getId());
        List<ReviewDTO> otherReviews = reviews.stream().map(r -> {
            ReviewDTO review = new ReviewDTO();
            review.setUsername(r.getUser().getUsername());
            review.setProfileName(r.getUser().getProfileName());
            review.setRating(r.getRating());
            review.setReview(r.getReview());
            return review;
        }).toList();
        details.setOtherUsersReviews(otherReviews);

        List<BookList> listas = listItemRepository.findAllListsByUserIdAndBookId(user.getId(), libro.getId());
        List<SimpleBookListDTO> listasDTO = listas.stream().map(lista -> {
                SimpleBookListDTO simpleList = new SimpleBookListDTO();
                simpleList.setId(lista.getId());
                simpleList.setName(lista.getName());
                return simpleList;
        }).toList();
        details.setLists(listasDTO);

        return details;
    }

    /**
     * Devuelve los libros escritos por un autor que tiene cuenta de usuario.
     *
     * @param idAuthor ID de usuario del autor
     * @return DTO con los datos del autor y sus libros escritos
     * @throws EntityNotFoundException El usuario no existe
     * @throws IllegalStateException El usuario no tiene el rol de autor
     */
    public AuthorDTO getBooksByAuthor(Long idAuthor) {
        User author = userRepository.findById(idAuthor)
                .orElseThrow(() -> new EntityNotFoundException("El usuario con ID " + idAuthor + " no existe."));
        if (author.getRoleEnum() != Role.AUTHOR) {
            throw new IllegalStateException("El usuario con ID " + idAuthor + " no es un autor.");
        }

        List<Book> libros = bookRepository.findBooksByAuthorId(idAuthor);

        List<BookDTO> librosDTO = libros.stream().map(bookMapper::toDTO).toList();

        AuthorDTO dto = new AuthorDTO();
        dto.setAuthor(author);
        dto.setBooks(librosDTO);

        return dto;
    }

    /**
     * Devuelve todos los libros escritos por un autor, dado su nombre.
     *
     * @param authorName Nombre del autor
     * @return Lista con los libros escritos por el autor
     */
    public Page<BookDTO> getAllBooksByAuthor(String authorName, int page, int size) {
        Page<Book> books = bookRepository.findBooksByAuthor(authorName,
                PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "publicationYear")));

        return books.map(bookMapper::toDTO);
    }

    /**
     * Devuelve el resto de libros que pertenecen a una colección, sin incluir el libro indicado.
     *
     * @param idBook ID del libro
     * @return Lista con el resto de libros de la colección
     * @throws EntityNotFoundException El libro no existe
     */
    public List<BookDTO> getOtherBooksFromCollection(Long idBook) {
        bookRepository.findById(idBook)
                .orElseThrow(() -> new EntityNotFoundException("El libro con ID " + idBook + " no existe."));

        List<Book> librosColeccion = bookRepository.findOtherBooksInSameCollection(idBook);

        return librosColeccion.stream().map(bookMapper::toDTO).toList();
    }

    /**
     * Devuelve todos los géneros existentes.
     *
     * @return Lista con todos los géneros
     */
    public List<GenreDTO> getGenres() {
        List<Genre> genres = genreRepository.findAll();

        return genres.stream().map(genreMapper::toDTO).toList();
    }

    /**
     * Devuelve los libros eliminados del sistema.
     *
     * @param page Número de la página que se quiere devolver
     * @param size Tamaño de la página
     * @param user Usario autenticado
     * @return Página con los libros resultantes como DTOs
     * @throws AccessDeniedException El usuario no es un administrador
     */
    public Page<BookDTO> getDeletedBooks(int page, int size, User user) {
        if (user.getRole() != Role.ADMIN.getValue()) {
            throw new AccessDeniedException("Solo los admins pueden realizar esta acción.");
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "publicationYear"));
        Page<Book> books = bookRepository.findAllByActiveFalse(pageable);

        return books.map(bookMapper::toDTO);
    }

    /**
     * Devuelve todas las colecciones.
     *
     * @return Lista con las colecciones
     */
    public List<CollectionDTO> getCollections() {
        List<Collection> collections = collectionRepository.findAll();

        return collections.stream().map(collectionMapper::toDTO).toList();
    }

    /**
     * Crea una nueva colección.
     *
     * @param collection DTO con los datos de la nueva colección
     * @return Colección creada
     * @throws ValidationException Ya existe una colección con el nombre indicado
     */
    public CollectionDTO createCollection(CollectionDTO collection) {
        Optional<Collection> existingCollection = collectionRepository.findByName(collection.getName());

        if (existingCollection.isPresent()) {
            throw new ValidationException("Ya existe una colección con el mismo nombre.");
        }

        Collection newCollection = collectionRepository.save(collectionMapper.toEntity(collection));

        return collectionMapper.toDTO(newCollection);
    }
}
