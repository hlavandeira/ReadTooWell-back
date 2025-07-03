package es.readtoowell.api_biblioteca.unit.service;

import es.readtoowell.api_biblioteca.mapper.GenreMapper;
import es.readtoowell.api_biblioteca.mapper.GoalMapper;
import es.readtoowell.api_biblioteca.mapper.UserLibraryBookMapper;
import es.readtoowell.api_biblioteca.model.DTO.GoalDTO;
import es.readtoowell.api_biblioteca.model.DTO.UserLibraryBookDTO;
import es.readtoowell.api_biblioteca.model.DTO.YearRecapDTO;
import es.readtoowell.api_biblioteca.model.DTO.book.BookDTO;
import es.readtoowell.api_biblioteca.model.DTO.book.GenreDTO;
import es.readtoowell.api_biblioteca.model.DTO.book.RatingDTO;
import es.readtoowell.api_biblioteca.model.entity.*;
import es.readtoowell.api_biblioteca.repository.book.BookRepository;
import es.readtoowell.api_biblioteca.repository.goal.GoalRepository;
import es.readtoowell.api_biblioteca.repository.library.UserLibraryBookRepository;
import es.readtoowell.api_biblioteca.service.goal.GoalService;
import es.readtoowell.api_biblioteca.service.library.UserLibraryBookService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Clase de pruebas para el servicio de libros de biblioteca de usuario.
 */
@ExtendWith(MockitoExtension.class)
public class UserLibraryBookServiceTests {
    @Mock
    private UserLibraryBookRepository libraryRepository;
    @Mock
    private UserLibraryBookMapper libraryMapper;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private GoalRepository goalRepository;
    @Mock
    private GoalMapper goalMapper;
    @Mock
    private GenreMapper genreMapper;
    @Mock
    private GoalService goalService;
    @InjectMocks
    private UserLibraryBookService libraryService;

    private User user;
    private Book book;
    private UserLibraryBook libraryBook;

    @BeforeEach
    public void init() {
        user = new User();
        book = new Book();
        libraryBook = new UserLibraryBook();
    }

    /**
     * Método de prueba. Devolver la biblioteca de un usuario
     */
    @Test
    public void UserLibraryBookService_GetLibraryFromUser_ReturnBooks() {
        Pageable pageable = PageRequest.of(0, 10);

        List<UserLibraryBook> libros = List.of(libraryBook, libraryBook);
        Page<UserLibraryBook> page = new PageImpl<>(libros);

        when(libraryRepository.findByUser(user, pageable)).thenReturn(page);
        when(libraryMapper.toDTO(any(UserLibraryBook.class))).thenReturn(new UserLibraryBookDTO());

        Page<UserLibraryBookDTO> result = libraryService.getLibraryFromUser(user, 0, 10);

        assertEquals(2, result.getContent().size());
        verify(libraryRepository).findByUser(user, pageable);
    }

    /**
     * Método de prueba. Devolver los libros guardados por un usuario con un estado
     */
    @Test
    public void UserLibraryBookService_GetLibraryByStatus_ReturnBooks() {
        int status = 2;
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "dateFinish"));
        List<UserLibraryBook> libros = List.of(libraryBook);
        Page<UserLibraryBook> page = new PageImpl<>(libros);

        when(libraryRepository.findByUserAndReadingStatus(user, status, pageable)).thenReturn(page);
        when(libraryMapper.toDTO(any(UserLibraryBook.class))).thenReturn(new UserLibraryBookDTO());

        Page<UserLibraryBookDTO> result = libraryService.getLibraryByStatus(user, status, 0, 10);

        assertEquals(1, result.getContent().size());
        verify(libraryRepository).findByUserAndReadingStatus(user, status, pageable);
    }

    /**
     * Método de prueba. Devolver los libros guardados por un usuario con un estado inválido
     */
    @Test
    public void UserLibraryBookService_GetLibraryByStatus_InvalidStatus() {
        int status = 200;

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> libraryService.getLibraryByStatus(user, status, 0, 10)
        );

        assertEquals("El estado de lectura indicado es inválido.", exception.getMessage());
    }

    /**
     * Método de prueba. Añadir un libro a la biblioteca
     */
    @Test
    public void UserLibraryBookService_AddBookToLibrary_ReturnAdded() {
        Long bookId = 1L;
        user.setId(1L);
        book.setId(bookId);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(libraryRepository.findByUserAndBook(user, book)).thenReturn(Optional.empty());
        when(libraryRepository.save(any(UserLibraryBook.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(libraryMapper.toDTO(any(UserLibraryBook.class))).thenReturn(new UserLibraryBookDTO());

        UserLibraryBookDTO result = libraryService.addBookToLibrary(bookId, user);

        assertNotNull(result);
        verify(bookRepository).findById(bookId);
        verify(libraryRepository).findByUserAndBook(user, book);
        verify(libraryRepository).save(any(UserLibraryBook.class));
        verify(libraryMapper).toDTO(any(UserLibraryBook.class));
    }

    /**
     * Método de prueba. Añadir un libro inexistente a la biblioteca
     */
    @Test
    public void UserLibraryBookService_AddBookToLibrary_UnexistentBook() {
        Long bookId = 999L;
        user.setId(1L);
        book.setId(bookId);

        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> libraryService.addBookToLibrary(bookId, user)
        );

        assertEquals("Libro con ID 999 no encontrado.", exception.getMessage());
    }

    /**
     * Método de prueba. Añadir un libro de nuevo a la biblioteca
     */
    @Test
    public void UserLibraryBookService_AddBookToLibrary_BookAlreadyInLibrary() {
        Long bookId = 1L;
        user.setId(1L);
        book.setId(bookId);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(libraryRepository.findByUserAndBook(user, book)).thenReturn(Optional.of(libraryBook));

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> libraryService.addBookToLibrary(bookId, user)
        );

        assertEquals("El libro ya pertenece a la biblioteca del usuario.", exception.getMessage());
    }

    /**
     * Método de prueba. ELiminar un libro de la biblioteca de un usuario
     */
    @Test
    public void UserLibraryBookService_DeleteBookFromLibrary_ReturnDeleted() {
        Long bookId = 1L;

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(libraryRepository.findByUserAndBook(user, book)).thenReturn(Optional.of(libraryBook));
        when(libraryMapper.toDTO(libraryBook)).thenReturn(new UserLibraryBookDTO());

        UserLibraryBookDTO result = libraryService.deleteBookFromLibrary(bookId, user);

        assertNotNull(result);
        verify(libraryRepository).delete(libraryBook);
        verify(libraryMapper).toDTO(libraryBook);
    }

    /**
     * Método de prueba. Eliminar un libro inexistente de la biblioteca
     */
    @Test
    public void UserLibraryBookService_DeleteBookFromLibrary_UnexistentBook() {
        Long bookId = 999L;

        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> libraryService.deleteBookFromLibrary(bookId, user)
        );

        assertEquals("Libro con ID 999 no encontrado.", exception.getMessage());
    }

    /**
     * Método de prueba. Eliminar un libro no añadido a la biblioteca
     */
    @Test
    public void UserLibraryBookService_DeleteBookFromLibrary_BookNotInLibrary() {
        Long bookId = 1L;

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(libraryRepository.findByUserAndBook(user, book)).thenReturn(Optional.empty());

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> libraryService.deleteBookFromLibrary(bookId, user)
        );

        assertEquals("El libro no pertenece a la biblioteca del usuario.", exception.getMessage());
    }

    /**
     * Método de prueba. Calificar un libro
     */
    @Test
    public void UserLibraryBookService_RateBook_ReturnUpdated() {
        Long bookId = 1L;
        user.setId(1L);
        book.setId(bookId);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(libraryRepository.findByUserAndBook(user, book)).thenReturn(Optional.of(libraryBook));
        when(libraryRepository.save(any(UserLibraryBook.class))).thenAnswer(i -> i.getArgument(0));
        when(libraryMapper.toDTO(any(UserLibraryBook.class))).thenReturn(new UserLibraryBookDTO() {{setRating(4.5);}});
        when(libraryRepository.findAverageRatingByBookId(bookId)).thenReturn(4.0);

        RatingDTO result = libraryService.rateBook(bookId, user, 4.5);

        assertNotNull(result);
        assertEquals(4.0, result.getAverageRating());
        assertEquals(4.5, result.getLibraryBook().getRating());
        verify(goalService).updateGoals(user.getId(), 0);
        verify(libraryRepository).save(any(UserLibraryBook.class));
    }

    /**
     * Método de prueba. Calificar un libro con una puntuación inválida
     */
    @Test
    public void UserLibraryBookService_RateBook_InvalidRating() {
        Long bookId = 1L;
        user.setId(1L);
        book.setId(bookId);

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> libraryService.rateBook(bookId, user, 56.89)
        );

        assertEquals("La calificación debe estar entre 0.5 y 5, en incrementos de 0.5.",
                exception.getMessage());
    }

    /**
     * Método de prueba. Calificar un libro inexistente
     */
    @Test
    public void UserLibraryBookService_RateBook_UnexistentBook() {
        Long bookId = 999L;
        user.setId(1L);
        book.setId(bookId);

        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> libraryService.rateBook(bookId, user, 4.5)
        );

        assertEquals("Libro con ID 999 no encontrado.",
                exception.getMessage());
    }

    /**
     * Método de prueba. Reseñar un libro
     */
    @Test
    public void UserLibraryBookService_ReviewBook_ReturnUpdated() {
        Long bookId = 1L;

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(libraryRepository.findByUserAndBook(user, book)).thenReturn(Optional.of(libraryBook));
        when(libraryRepository.save(libraryBook)).thenReturn(libraryBook);
        when(libraryMapper.toDTO(libraryBook)).thenReturn(new UserLibraryBookDTO());

        UserLibraryBookDTO result = libraryService.reviewBook(bookId, user, "Muy bien");

        assertNotNull(result);
        assertEquals("Muy bien", libraryBook.getReview());
        verify(libraryRepository).save(libraryBook);
    }

    /**
     * Método de prueba. Reseñar un libro con un texto demasiado largo
     */
    @Test
    public void UserLibraryBookService_ReviewBook_ReviewTooLong() {
        Long bookId = 1L;

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> libraryService.reviewBook(bookId, user, "Reseña muy larga".repeat(200))
        );

        assertEquals("La reseña es demasiado larga.", exception.getMessage());
    }

    /**
     * Método de prueba. Reseñar un libro inexistente
     */
    @Test
    public void UserLibraryBookService_ReviewBook_UnexistentBook() {
        Long bookId = 999L;

        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> libraryService.reviewBook(bookId, user, "Muy bien")
        );

        assertEquals("Libro con ID 999 no encontrado.", exception.getMessage());
    }

    /**
     * Método de prueba. Reseñar un libro no añadido a la biblioteca
     */
    @Test
    public void UserLibraryBookService_ReviewBook_BookNotInLibrary() {
        Long bookId = 999L;

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(libraryRepository.findByUserAndBook(user, book)).thenReturn(Optional.empty());

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> libraryService.reviewBook(bookId, user, "Muy bien")
        );

        assertEquals("El libro no pertenece a la biblioteca del usuario.", exception.getMessage());
    }

    /**
     * Método de prueba. Actualizar el estado de lectura de un libro
     */
    @Test
    public void UserLibraryBookService_UpdateReadingStatus_ReturnUpdated() {
        int status = 2;
        Long bookId = 1L;
        user.setId(1L);
        book.setId(bookId);
        libraryBook.setReadingStatus(1);
        libraryBook.setBook(book);

        BookDTO bookDTO = new BookDTO();
        bookDTO.setPageNumber(100);

        UserLibraryBookDTO dto = new UserLibraryBookDTO();
        dto.setBook(bookDTO);
        dto.setReadingStatus(1);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(libraryRepository.findByUserAndBook(user, book)).thenReturn(Optional.of(libraryBook));
        when(libraryRepository.save(any(UserLibraryBook.class))).thenReturn(libraryBook);
        when(libraryMapper.toDTO(any(UserLibraryBook.class))).thenReturn(dto);

        UserLibraryBookDTO result = libraryService.updateReadingStatus(bookId, user, status);

        assertNotNull(result);
        verify(goalService).updateGoals(user.getId(), 0);
    }

    /**
     * Método de prueba. Actualizar el estado de lectura de un libro a un estado inválido
     */
    @Test
    public void UserLibraryBookService_UpdatedReadingStatus_InvalidStatus() {
        int status = 100;
        Long bookId = 1L;
        user.setId(1L);
        book.setId(bookId);
        libraryBook.setReadingStatus(1);

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> libraryService.updateReadingStatus(bookId, user, status)
        );

        assertEquals("El estado de lectura proporcionado es inválido.", exception.getMessage());
    }

    /**
     * Método de prueba. Actualizar el estado de lectura de un libro inexistente
     */
    @Test
    public void UserLibraryBookService_UpdatedReadingStatus_UnexistentBook() {
        int status = 2;
        Long bookId = 1L;
        user.setId(1L);
        book.setId(bookId);
        libraryBook.setReadingStatus(1);

        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> libraryService.updateReadingStatus(bookId, user, status)
        );

        assertEquals("Libro con ID 1 no encontrado.", exception.getMessage());
    }

    /**
     * Método de prueba. Actualizar el estado de lectura de un libro que no pertenece a la biblioteca
     */
    @Test
    public void UserLibraryBookService_UpdatedReadingStatus_BookNotInLibrary() {
        int status = 2;
        Long bookId = 1L;
        user.setId(1L);
        book.setId(bookId);
        libraryBook.setReadingStatus(1);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(libraryRepository.findByUserAndBook(user, book)).thenReturn(Optional.empty());

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> libraryService.updateReadingStatus(bookId, user, status)
        );

        assertEquals("El libro no pertenece a la biblioteca del usuario.", exception.getMessage());
    }

    /**
     * Método de prueba. Actualizar el progreso de un libro
     */
    @Test
    public void UserLibraryBookService_UpdateProgress_ReturnUpdated() {
        Long bookId = 1L;
        user.setId(1L);
        book.setId(bookId);
        book.setPageNumber(300);

        libraryBook.setReadingStatus(1);
        libraryBook.setProgress(50);
        libraryBook.setProgressType("porcentaje");

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(libraryRepository.findByUserAndBook(user, book)).thenReturn(Optional.of(libraryBook));
        when(libraryRepository.save(any(UserLibraryBook.class))).thenReturn(libraryBook);
        when(libraryMapper.toDTO(any(UserLibraryBook.class))).thenReturn(new UserLibraryBookDTO());

        UserLibraryBookDTO result = libraryService.updateProgress(bookId, user, 100, "porcentaje");

        assertNotNull(result);
        verify(goalService).updateGoals(eq(user.getId()), anyInt());
    }

    /**
     * Método de prueba. Actualizar el progreso de un libro con un tipo de progreso inválido
     */
    @Test
    public void UserLibraryBookService_UpdatedProgress_InvalidProgressType() {
        Long bookId = 1L;
        user.setId(1L);
        book.setId(bookId);
        book.setPageNumber(300);

        libraryBook.setReadingStatus(1);
        libraryBook.setProgress(50);
        libraryBook.setProgressType("porcentaje");

        int progress = 100;
        String progressType = "tipo de progreso inválido";

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> libraryService.updateProgress(bookId, user, progress, progressType)
        );

        assertEquals("El tipo de progreso de lectura proporcionado es inválido.", exception.getMessage());
    }

    /**
     * Método de prueba. Actualizar el progreso de un libro a una cantidad inválida
     */
    @Test
    public void UserLibraryBookService_UpdatedProgress_InvalidProgressAmount() {
        Long bookId = 1L;
        user.setId(1L);
        book.setId(bookId);
        book.setPageNumber(300);

        libraryBook.setReadingStatus(1);
        libraryBook.setProgress(50);
        libraryBook.setProgressType("porcentaje");

        int progress = -100;
        String progressType = "porcentaje";

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> libraryService.updateProgress(bookId, user, progress, progressType)
        );

        assertEquals("La cantidad del progreso es inválida.", exception.getMessage());
    }

    /**
     * Método de prueba. Actualizar el progreso de un libro inexistente
     */
    @Test
    public void UserLibraryBookService_UpdateProgress_UnexistentBook() {
        Long bookId = 1L;
        user.setId(1L);
        book.setId(bookId);
        book.setPageNumber(300);

        libraryBook.setReadingStatus(1);
        libraryBook.setProgress(50);
        libraryBook.setProgressType("porcentaje");

        int progress = 100;
        String progressType = "porcentaje";

        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> libraryService.updateProgress(bookId, user, progress, progressType)
        );

        assertEquals("Libro con ID 1 no encontrado.", exception.getMessage());
    }

    /**
     * Método de prueba. Actualizar el progreso de un libro que no pertenece a la biblioteca
     */
    @Test
    public void UserLibraryBookService_UpdateProgress_BookNotInLibrary() {
        Long bookId = 1L;
        user.setId(1L);
        book.setId(bookId);
        book.setPageNumber(300);

        libraryBook.setReadingStatus(1);
        libraryBook.setProgress(50);
        libraryBook.setProgressType("porcentaje");

        int progress = 100;
        String progressType = "porcentaje";

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(libraryRepository.findByUserAndBook(user, book)).thenReturn(Optional.empty());

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> libraryService.updateProgress(bookId, user, progress, progressType)
        );

        assertEquals("El libro no pertenece a la biblioteca del usuario.", exception.getMessage());
    }

    /**
     * Método de prueba. Actualizar el progreso de un libro que no está en estado Leyendo
     */
    @Test
    public void UserLibraryBookService_UpdateProgress_NotReadingStatus() {
        Long bookId = 1L;
        user.setId(1L);
        book.setId(bookId);
        book.setPageNumber(300);

        libraryBook.setReadingStatus(2);
        libraryBook.setProgress(50);
        libraryBook.setProgressType("porcentaje");

        int progress = 100;
        String progressType = "porcentaje";

        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> libraryService.updateProgress(bookId, user, progress, progressType)
        );

        assertEquals("Libro con ID 1 no encontrado.",
                exception.getMessage());
    }

    /**
     * Método de prueba. Devolver el resumen anual
     */
    @Test
    public void UserLibraryBookService_GetYearRecap_ReturnYearRecap() {
        user.setId(1L);

        List<GoalDTO> goalsActualYear = List.of(new GoalDTO(), new GoalDTO());
        List<Goal> lastYearAnnualGoals = List.of(new Goal());

        when(goalService.getFinishedGoalsActualYear(user.getId())).thenReturn(goalsActualYear);
        when(goalRepository.findAnnualGoalsByYear(eq(user.getId()), anyInt())).thenReturn(lastYearAnnualGoals);
        when(goalMapper.toDTO(any(Goal.class))).thenReturn(new GoalDTO());

        when(libraryRepository.findBooksReadActualYear(user.getId())).thenReturn(List.of(new Book()));
        when(libraryRepository.sumPagesReadInCurrentYear(user.getId())).thenReturn(1500L);

        Genre genre = new Genre();
        book.setId(1L);
        book.setTitle("Título");
        book.setAuthor("Autor");
        book.setCover("Portada");
        book.setPageNumber(300);

        when(libraryRepository.findTopGenresForCurrentYear(user.getId(), 5)).thenReturn(List.of(genre));
        when(genreMapper.toDTO(genre)).thenReturn(new GenreDTO());

        when(libraryRepository.findTopRatedBooksByUserForCurrentYear(user.getId(), 4)).thenReturn(List.of(book));
        when(libraryRepository.findByUserAndBook(user, book)).thenReturn(Optional.of(new UserLibraryBook()));

        YearRecapDTO result = libraryService.getYearRecap(user);

        assertNotNull(result);
        assertEquals(3, result.getAnnualGoals().size());
        assertEquals(1, result.getMostReadGenres().size());
        assertEquals(1, result.getTopRatedBooks().size());
        assertEquals(1500L, result.getTotalPagesRead());
    }
}
