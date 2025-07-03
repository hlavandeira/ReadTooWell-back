package es.readtoowell.api_biblioteca.unit.service;

import es.readtoowell.api_biblioteca.mapper.BookMapper;
import es.readtoowell.api_biblioteca.mapper.CollectionMapper;
import es.readtoowell.api_biblioteca.mapper.GenreMapper;
import es.readtoowell.api_biblioteca.model.DTO.book.BookDTO;
import es.readtoowell.api_biblioteca.model.DTO.book.BookDetailsDTO;
import es.readtoowell.api_biblioteca.model.DTO.book.CollectionDTO;
import es.readtoowell.api_biblioteca.model.DTO.book.GenreDTO;
import es.readtoowell.api_biblioteca.model.DTO.user.AuthorDTO;
import es.readtoowell.api_biblioteca.model.entity.*;
import es.readtoowell.api_biblioteca.repository.book.*;
import es.readtoowell.api_biblioteca.repository.library.UserLibraryBookRepository;
import es.readtoowell.api_biblioteca.repository.user.UserRepository;
import es.readtoowell.api_biblioteca.service.book.BookService;
import es.readtoowell.api_biblioteca.service.book.SuggestionService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Clase de pruebas para el servicio de libros.
 */
@ExtendWith(MockitoExtension.class)
public class BookServiceTests {
    @Mock
    private BookRepository bookRepository;
    @Mock
    private GenreRepository genreRepository;
    @Mock
    private CollectionRepository collectionRepository;
    @Mock
    private BookMapper bookMapper;
    @Mock
    private GenreMapper genreMapper;
    @Mock
    private UserLibraryBookRepository libraryRepository;
    @Mock
    private BookListItemRepository listItemRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CollectionMapper collectionMapper;
    @Mock
    private SuggestionRepository suggestionRepository;
    @Mock
    private SuggestionService suggestionService;
    @InjectMocks
    private BookService bookService;

    private Book book;
    private BookDTO bookDTO;
    private User user;

    @BeforeEach
    public void init() {
        book = new Book();
        book.setId(1L);

        bookDTO = new BookDTO();
        bookDTO.setId(1L);

        user = new User();
    }

    /**
     * Método de prueba. Devolver todos los libros
     */
    @Test
    public void BookService_GetAllBooks_ReturnBooks() {
        book.setTitle("Libro");
        bookDTO.setTitle("Libro");

        Page<Book> bookPage = new PageImpl<>(List.of(book));
        when(bookRepository.findAllByActiveTrue(any())).thenReturn(bookPage);
        when(bookMapper.toDTO(book)).thenReturn(bookDTO);

        Page<BookDTO> result = bookService.getAllBooks(0, 10);

        assertEquals(1, result.getTotalElements());
        assertEquals("Libro", result.getContent().get(0).getTitle());
        verify(bookRepository).findAllByActiveTrue(any(Pageable.class));
        verify(bookMapper).toDTO(book);
    }

    /**
     * Método de prueba. Devolver un libro
     */
    @Test
    public void BookService_GetBook_ReturnBook() {
        book.setTitle("Libro");
        bookDTO.setTitle("Libro");

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookMapper.toDTO(book)).thenReturn(bookDTO);

        BookDTO result = bookService.getBook(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Libro", result.getTitle());
        verify(bookRepository).findById(1L);
        verify(bookMapper).toDTO(book);
    }

    /**
     * Método de prueba. Devolver un libro inexistente
     */
    @Test
    public void BookService_GetBook_UnexistentBook() {
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> bookService.getBook(99L)
        );

        assertEquals("El libro con ID 99 no existe.", exception.getMessage());
    }

    /**
     * Método de prueba. Crear un libro
     */
    @Test
    public void BookService_CreateBook_ReturnCreated() {
        user.setRole(2);

        bookDTO.setTitle("Nuevo libro");
        bookDTO.setAuthor("Autor");
        bookDTO.setPublisher("Editorial");
        bookDTO.setSynopsis("sinopsis....");

        Genre genre = new Genre();
        genre.setId(1L);
        genre.setName("Fantasía");

        GenreDTO genreDTO = new GenreDTO();
        genreDTO.setId(1L);
        genreDTO.setName("Fantasía");

        Book savedBook = new Book();
        savedBook.setId(10L);
        savedBook.setTitle("Nuevo libro");

        BookDTO savedBookDTO = new BookDTO();
        savedBookDTO.setId(10L);
        savedBookDTO.setTitle("Nuevo libro");

        when(genreRepository.findAllById(List.of(1L))).thenReturn(List.of(genre));
        when(genreMapper.toDTO(genre)).thenReturn(genreDTO);
        when(bookRepository.save(any(Book.class))).thenReturn(savedBook);
        when(bookMapper.toDTO(savedBook)).thenReturn(savedBookDTO);

        BookDTO result = bookService.createBook(bookDTO, List.of(1L), user);

        assertNotNull(result);
        assertEquals(10L, result.getId());
        assertEquals("Nuevo libro", result.getTitle());
        verify(bookRepository).save(any(Book.class));
        verify(bookMapper).toDTO(savedBook);
    }

    /**
     * Método de prueba. Crear un libro cuando el usuario no es administrador
     */
    @Test
    public void BookService_CreateBook_UserIsNotAdmin() {
        user.setRole(0);

        AccessDeniedException exception = assertThrows(
                AccessDeniedException.class,
                () -> bookService.createBook(bookDTO, List.of(1L), user)
        );

        assertEquals("Solo los admins pueden realizar esta acción.", exception.getMessage());
    }

    /**
     * Método de prueba. Actualizar un libro
     */
    @Test
    public void BookService_UpdateBook_ReturnUpdated() {
        user.setRole(2);

        bookDTO.setTitle("Nuevo título");
        bookDTO.setAuthor("Autor");
        bookDTO.setPublisher("Editorial");
        bookDTO.setSynopsis("sinopsis....");

        Genre genre = new Genre();
        genre.setId(1L);
        GenreDTO genreDTO = new GenreDTO();
        genreDTO.setId(1L);

        Book savedBook = new Book();
        savedBook.setId(1L);
        savedBook.setTitle("Nuevo título");

        BookDTO savedBookDTO = new BookDTO();
        savedBookDTO.setId(1L);
        savedBookDTO.setTitle("Nuevo título");

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(genreRepository.findAllById(List.of(1L))).thenReturn(List.of(genre));
        when(genreMapper.toDTO(genre)).thenReturn(genreDTO);
        when(bookRepository.save(any(Book.class))).thenReturn(savedBook);
        when(bookMapper.toDTO(savedBook)).thenReturn(savedBookDTO);

        BookDTO result = bookService.updateBook(1L, bookDTO, List.of(1L), user);

        assertEquals(1L, result.getId());
        assertEquals("Nuevo título", result.getTitle());
        verify(bookRepository).findById(1L);
        verify(bookRepository).save(any(Book.class));
    }

    /**
     * Método de prueba. Actualizar un libro inexistente
     */
    @Test
    public void BookService_UpdateBook_UnexistentBook() {
        user.setRole(2);

        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> bookService.updateBook(1L, new BookDTO(), List.of(1L), user)
        );

        assertEquals("El libro con ID 1 no existe.", exception.getMessage());
    }

    /**
     * Método de prueba. Actualizar un libro cuando el usuario no es administrador
     */
    @Test
    public void BookService_UpdateBook_UserIsNotAdmin() {
        user.setRole(0);

        AccessDeniedException exception = assertThrows(
                AccessDeniedException.class,
                () -> bookService.updateBook(1L, bookDTO, List.of(1L), user)
        );

        assertEquals("Solo los admins pueden realizar esta acción.", exception.getMessage());
    }

    /**
     * Método de prueba. Eliminar un libro
     */
    @Test
    public void BookService_DeleteBook_ReturnDeleted() {
        user.setRole(2);

        bookDTO.setTitle("Libro a borrar");

        book.setTitle("Libro a borrar");

        Book deletedBook = new Book();
        deletedBook.setId(1L);
        deletedBook.setTitle("Libro a borrar");
        deletedBook.delete();

        BookDTO deletedBookDTO = new BookDTO();
        deletedBookDTO.setId(1L);
        deletedBookDTO.setTitle("Libro a borrar");

        when(bookMapper.toEntity(bookDTO)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(deletedBook);
        when(bookMapper.toDTO(deletedBook)).thenReturn(deletedBookDTO);

        BookDTO result = bookService.deleteBook(bookDTO, user);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(bookRepository).save(book);
    }

    /**
     * Método de prueba. Eliminar un libro cuando el usuario no es administrador
     */
    @Test
    public void BookService_DeleteBook_UserIsNotAdmin() {
        user.setRole(0);

        AccessDeniedException exception = assertThrows(
                AccessDeniedException.class,
                () -> bookService.deleteBook(bookDTO, user)
        );

        assertEquals("Solo los admins pueden realizar esta acción.", exception.getMessage());
    }

    /**
     * Método de prueba. Reactivar un libro
     */
    @Test
    public void BookService_ReactivateBook_ReturnReactivated() {
        user.setRole(2);

        bookDTO.setId(2L);
        bookDTO.setTitle("Libro reactivado");

        book.setId(2L);
        book.setTitle("Libro reactivado");

        Book reactivatedBook = new Book();
        reactivatedBook.setId(2L);
        reactivatedBook.setTitle("Libro reactivado");
        reactivatedBook.reactivate();

        BookDTO reactivatedBookDTO = new BookDTO();
        reactivatedBookDTO.setId(2L);
        reactivatedBookDTO.setTitle("Libro reactivado");

        when(bookMapper.toEntity(bookDTO)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(reactivatedBook);
        when(bookMapper.toDTO(reactivatedBook)).thenReturn(reactivatedBookDTO);

        BookDTO result = bookService.reactivateBook(bookDTO, user);

        assertEquals(2L, result.getId());
        assertEquals("Libro reactivado", result.getTitle());
        verify(bookRepository).save(book);
    }

    /**
     * Método de prueba. Buscar libros mediante un término de búsqueda y filtros
     */
    @Test
    public void BookService_FilterBooks_ReturnBooks() {
        Page<Book> bookPage = new PageImpl<>(List.of(book));
        when(bookRepository.filterBooks(any(), any(), any(), any(), any(), any())).thenReturn(bookPage);
        when(bookMapper.toDTO(any())).thenReturn(bookDTO);

        Page<BookDTO> result = bookService
                .filterBooks("test", 100, 500, 1990, 2020, 0, 10);

        assertEquals(1, result.getTotalElements());
    }

    /**
     * Método de prueba. Buscar libros por género
     */
    @Test
    public void BookService_FilterBooksByGenre_ReturnBooks() {
        Page<Book> bookPage = new PageImpl<>(List.of(book));
        when(bookRepository.findByGenresId(eq(1L), any())).thenReturn(bookPage);
        when(bookMapper.toDTO(any())).thenReturn(bookDTO);

        Page<BookDTO> result = bookService.filterBooksByGenre(1L, 0, 10);

        assertEquals(1, result.getTotalElements());
        verify(bookRepository).findByGenresId(eq(1L), any());
    }

    /**
     * Método de prueba. Consultar los detalles de un libro
     */
    @Test
    public void BookService_GetBookDetails_ReturnBookDetaisl() {
        user.setUsername("Usuario");
        user.setProfileName("Usuario");

        UserLibraryBook savedBook = new UserLibraryBook();
        savedBook.setBook(book);
        savedBook.setUser(user);
        savedBook.setRating(4);
        savedBook.setReview("Muy bueno");
        savedBook.setReadingStatus(2);

        BookList list = new BookList();
        list.setId(1L);
        list.setName("Favoritos");

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookMapper.toDTO(book)).thenReturn(bookDTO);
        when(libraryRepository.findByUserAndBook(user, book)).thenReturn(Optional.of(savedBook));
        when(libraryRepository.findAverageRatingByBookId(1L)).thenReturn(4.5);
        when(libraryRepository.findAllWithReviewByBookIdExcludingUser(1L, user.getId()))
                .thenReturn(List.of(savedBook));
        when(listItemRepository.findAllListsByUserIdAndBookId(user.getId(), book.getId()))
                .thenReturn(List.of(list));

        BookDetailsDTO result = bookService.getBookDetails(1L, user);

        assertTrue(result.isSaved());
        assertEquals(2, result.getReadingStatus());
        assertEquals(4, result.getRating());
        assertEquals("Muy bueno", result.getReview());
        assertEquals(1, result.getOtherUsersReviews().size());
        assertEquals("Usuario", result.getOtherUsersReviews().get(0).getUsername());
        assertEquals("Usuario", result.getOtherUsersReviews().get(0).getProfileName());
        assertEquals(4.5, result.getAverageRating());
    }

    /**
     * Método de prueba. Consultar los detalles de un libro inexistente
     */
    @Test
    public void BookService_GetBookDetails_UnexistentBook() {
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> bookService.getBookDetails(99L, user)
        );

        assertEquals("El libro con ID 99 no existe.", exception.getMessage());
    }

    /**
     * Método de prueba. Devolver los libros de un autor verificado
     */
    @Test
    public void BookService_GetBooksByAuthor_ReturnBooks() {
        user.setRole(1);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bookRepository.findBooksByAuthorId(1L)).thenReturn(List.of(book));
        when(bookMapper.toDTO(book)).thenReturn(bookDTO);

        AuthorDTO result = bookService.getBooksByAuthor(1L);

        assertEquals(user, result.getAuthor());
        assertEquals(1, result.getBooks().size());
    }

    /**
     * Método de prueba. Devolver los libros de un autor verificado inexistente
     */
    @Test
    public void BookService_GetBooksByAuthor_UnexistentUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> bookService.getBooksByAuthor(1L)
        );

        assertEquals("El usuario con ID 1 no existe.", exception.getMessage());
    }

    /**
     * Método de prueba. Devolver los libros de un usuario que no es autor
     */
    @Test
    public void BookService_GetBooksByAuthor_UserIsNotAuthor() {
        user.setRole(0);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> bookService.getBooksByAuthor(1L)
        );

        assertEquals("El usuario con ID 1 no es un autor.", exception.getMessage());
    }

    /**
     * Método de prueba. Devolver los libros escritos por un autor
     */
    @Test
    public void BookService_GetAllBooksByAuthor_ReturnBooks() {
        Page<Book> booksPage = new PageImpl<>(List.of(book));
        when(bookRepository.findBooksByAuthor(eq("autor"), any(Pageable.class))).thenReturn(booksPage);
        when(bookMapper.toDTO(book)).thenReturn(bookDTO);

        Page<BookDTO> result = bookService.getAllBooksByAuthor("autor", 0, 10);

        assertEquals(1, result.getTotalElements());
        verify(bookMapper).toDTO(book);
    }

    /**
     * Método de prueba. Devolver los libros de la misma colección que otro
     */
    @Test
    public void BookService_GetOtherBooksFromCollection_ReturnBooks() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookRepository.findOtherBooksInSameCollection(1L)).thenReturn(List.of(book));
        when(bookMapper.toDTO(book)).thenReturn(bookDTO);

        List<BookDTO> result = bookService.getOtherBooksFromCollection(1L);

        assertEquals(1, result.size());
        verify(bookMapper).toDTO(book);
    }

    /**
     * Método de prueba. Devolver los libros de la misma colección que otro inexistente
     */
    @Test
    public void BookService_GetOtherBooksFromCollection_UnexistentBook() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> bookService.getOtherBooksFromCollection(1L)
        );

        assertEquals("El libro con ID 1 no existe.", exception.getMessage());
    }

    /**
     * Método de prueba. Devolver los géneros
     */
    @Test
    public void BookService_GetGenres_ReturnGenres() {
        Genre genre = new Genre(); genre.setId(1L); genre.setName("Género");
        GenreDTO genreDTO = new GenreDTO(); genreDTO.setId(1L); genreDTO.setName("Género");

        when(genreRepository.findAll()).thenReturn(List.of(genre));
        when(genreMapper.toDTO(genre)).thenReturn(genreDTO);

        List<GenreDTO> result = bookService.getGenres();

        assertEquals(1, result.size());
        assertEquals("Género", result.get(0).getName());
    }

    /**
     * Método de prueba. Devolver los libros eliminados
     */
    @Test
    public void BookService_GetDeletedBooks_ReturnBooks() {
        user.setRole(2);
        Page<Book> deletedBooks = new PageImpl<>(List.of(book));

        when(bookRepository.findAllByActiveFalse(any(Pageable.class))).thenReturn(deletedBooks);
        when(bookMapper.toDTO(book)).thenReturn(bookDTO);

        Page<BookDTO> result = bookService.getDeletedBooks(0, 10, user);

        assertEquals(1, result.getTotalElements());
        verify(bookMapper).toDTO(book);
    }

    /**
     * Método de prueba. Devolver los libros eliminados cuando el usuario no es administrador
     */
    @Test
    public void BookService_GetDeletedBooks_UserIsNotAdmin() {
        user.setRole(0);

        assertThrows(AccessDeniedException.class,
                () -> bookService.getDeletedBooks(0, 10, user));
    }

    /**
     * Método de prueba. Devolver las colecciones
     */
    @Test
    public void BookService_GetCollections_ReturnCollections() {
        Collection collection = new Collection(); collection.setId(1L); collection.setName("Colección");
        CollectionDTO dto = new CollectionDTO(); dto.setId(1L); dto.setName("Colección");

        when(collectionRepository.findAll()).thenReturn(List.of(collection));
        when(collectionMapper.toDTO(collection)).thenReturn(dto);

        List<CollectionDTO> result = bookService.getCollections();

        assertEquals(1, result.size());
        assertEquals("Colección", result.get(0).getName());
    }

    /**
     * Método de prueba. Crear una colección
     */
    @Test
    public void BookService_CreateCollection_ReturnCreated() {
        CollectionDTO dto = new CollectionDTO(); dto.setName("Nueva colección");
        Collection entity = new Collection(); entity.setName("Nueva colección");

        when(collectionRepository.findByName("Nueva colección")).thenReturn(Optional.empty());
        when(collectionMapper.toEntity(dto)).thenReturn(entity);
        when(collectionRepository.save(entity)).thenReturn(entity);
        when(collectionMapper.toDTO(entity)).thenReturn(dto);

        CollectionDTO result = bookService.createCollection(dto);

        assertEquals("Nueva colección", result.getName());
    }

    /**
     * Método de prueba. Crear una colección con nombre repetido
     */
    @Test
    public void BookService_CreateCollection_RepeatedCollection() {
        CollectionDTO dto = new CollectionDTO(); dto.setName("Repetida");
        Collection entity = new Collection(); entity.setName("Repetida");

        when(collectionRepository.findByName("Repetida")).thenReturn(Optional.of(entity));

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> bookService.createCollection(dto)
        );

        assertEquals("Ya existe una colección con el mismo nombre.", exception.getMessage());
    }
}
