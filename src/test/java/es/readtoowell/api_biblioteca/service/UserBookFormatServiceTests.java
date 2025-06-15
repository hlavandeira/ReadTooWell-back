package es.readtoowell.api_biblioteca.service;

import es.readtoowell.api_biblioteca.mapper.FormatMapper;
import es.readtoowell.api_biblioteca.model.DTO.book.FormatDTO;
import es.readtoowell.api_biblioteca.model.entity.*;
import es.readtoowell.api_biblioteca.model.entity.id.UserBookFormatId;
import es.readtoowell.api_biblioteca.model.entity.id.UserLibraryBookId;
import es.readtoowell.api_biblioteca.repository.book.BookRepository;
import es.readtoowell.api_biblioteca.repository.book.FormatRepository;
import es.readtoowell.api_biblioteca.repository.library.UserBookFormatRepository;
import es.readtoowell.api_biblioteca.repository.library.UserLibraryBookRepository;
import es.readtoowell.api_biblioteca.repository.user.UserRepository;
import es.readtoowell.api_biblioteca.service.library.UserBookFormatService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Clase de pruebas para el servicio de formatos de libros.
 */
@ExtendWith(MockitoExtension.class)
public class UserBookFormatServiceTests {
    @Mock
    private UserBookFormatRepository userFormatRepository;
    @Mock
    private FormatMapper formatMapper;
    @Mock
    private FormatRepository formatRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private UserLibraryBookRepository libraryRepository;
    @InjectMocks
    private UserBookFormatService formatService;

    @Test
    public void UserBookFormatService_GetFormatsForUserBook_ReturnFormats() {
        Long idUser = 1L;
        Long idBook = 2L;

        Format format1 = new Format();
        format1.setId(1L);
        Format format2 = new Format();
        format2.setId(2L);

        UserLibraryBookId libraryBookId = new UserLibraryBookId(idUser, idBook);

        UserBookFormat bookFormat1 = new UserBookFormat();
        bookFormat1.setId(new UserBookFormatId(libraryBookId, 1L));
        bookFormat1.setFormat(format1);

        UserBookFormat bookFormat2 = new UserBookFormat();
        bookFormat2.setId(new UserBookFormatId(libraryBookId, 2L));
        bookFormat2.setFormat(format2);

        when(userFormatRepository.findFormatsByUserAndBook(idUser, idBook)).thenReturn(List.of(bookFormat1, bookFormat2));
        when(formatMapper.toDTO(any(Format.class))).thenReturn(new FormatDTO());

        List<FormatDTO> result = formatService.getFormatsForUserBook(idBook, idUser);

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    public void UserBookFormatService_AddFormatToBook_ReturnFormats() {
        Long idUser = 1L;
        Long idBook = 2L;
        Long idFormat = 3L;

        User user = new User();
        user.setId(idUser);
        Book book = new Book();
        book.setId(idBook);
        Format format = new Format();
        format.setId(idFormat);
        UserLibraryBook userLibraryBook = new UserLibraryBook();
        userLibraryBook.setId(new UserLibraryBookId(idUser, idBook));

        when(userRepository.findById(idUser)).thenReturn(Optional.of(user));
        when(bookRepository.findById(idBook)).thenReturn(Optional.of(book));
        when(formatRepository.findById(idFormat)).thenReturn(Optional.of(format));
        when(libraryRepository.findByUserAndBook(user, book)).thenReturn(Optional.of(userLibraryBook));
        when(userFormatRepository.existsByLibraryBookAndFormat(userLibraryBook, format)).thenReturn(false);
        when(formatMapper.toDTO(format)).thenReturn(new FormatDTO());

        FormatDTO result = formatService.addFormatToBook(idBook, idUser, idFormat);

        assertNotNull(result);
        verify(userFormatRepository).save(any(UserBookFormat.class));
    }

    @Test
    public void UserBookFormatService_AddFormatToBook_UnexistentUser() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> formatService.addFormatToBook(2L, 1L, 3L)
        );

        assertEquals("El usuario con ID 1 no existe.", exception.getMessage());
    }

    @Test
    public void UserBookFormatService_AddFormatToBook_BookNotFromLibrary() {
        User user = new User(); user.setId(1L);
        Book book = new Book(); book.setId(2L);
        Format format = new Format(); format.setId(3L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bookRepository.findById(2L)).thenReturn(Optional.of(book));
        when(formatRepository.findById(3L)).thenReturn(Optional.of(format));
        when(libraryRepository.findByUserAndBook(user, book)).thenReturn(Optional.empty());

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> formatService.addFormatToBook(2L, 1L, 3L)
        );

        assertEquals("El libro no pertenece a la biblioteca del usuario.", exception.getMessage());
    }

    @Test
    public void UserBookFormatService_AddFormatToBook_FormatAlreadyAdded() {
        User user = new User(); user.setId(1L);
        Book book = new Book(); book.setId(2L);
        Format format = new Format(); format.setId(3L);
        UserLibraryBook libraryBook = new UserLibraryBook();
        libraryBook.setId(new UserLibraryBookId(1L, 2L));

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bookRepository.findById(2L)).thenReturn(Optional.of(book));
        when(formatRepository.findById(3L)).thenReturn(Optional.of(format));
        when(libraryRepository.findByUserAndBook(user, book)).thenReturn(Optional.of(libraryBook));
        when(userFormatRepository.existsByLibraryBookAndFormat(libraryBook, format)).thenReturn(true);

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> formatService.addFormatToBook(2L, 1L, 3L)
        );

        assertEquals("El formato ya está asociado a este libro para el usuario.", exception.getMessage());
    }

    @Test
    public void UserBookFormatService_RemoveFormatFromBook_ReturnFormats() {
        Long idUser = 1L, idBook = 2L, idFormat = 3L;

        User user = new User(); user.setId(idUser);
        Book book = new Book(); book.setId(idBook);
        Format format = new Format(); format.setId(idFormat);

        UserLibraryBook userLibraryBook = new UserLibraryBook();
        userLibraryBook.setId(new UserLibraryBookId(idUser, idBook));

        UserBookFormat userBookFormat = new UserBookFormat();
        userBookFormat.setId(new UserBookFormatId(userLibraryBook.getId(), idFormat));
        userBookFormat.setLibraryBook(userLibraryBook);
        userBookFormat.setFormat(format);

        when(userRepository.findById(idUser)).thenReturn(Optional.of(user));
        when(bookRepository.findById(idBook)).thenReturn(Optional.of(book));
        when(formatRepository.findById(idFormat)).thenReturn(Optional.of(format));
        when(libraryRepository.findByUserAndBook(user, book)).thenReturn(Optional.of(userLibraryBook));
        when(userFormatRepository.findByLibraryBookAndFormat(userLibraryBook, format)).thenReturn(Optional.of(userBookFormat));

        assertDoesNotThrow(() -> formatService.removeFormatFromBook(idBook, idUser, idFormat));
        verify(userFormatRepository).delete(userBookFormat);
    }

    @Test
    public void UserBookFormatService_RemoveFormatFromBook_FormatNotAdded() {
        User user = new User(); user.setId(1L);
        Book book = new Book(); book.setId(2L);
        Format format = new Format(); format.setId(3L);
        UserLibraryBook libraryBook = new UserLibraryBook();
        libraryBook.setId(new UserLibraryBookId(1L, 2L));

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bookRepository.findById(2L)).thenReturn(Optional.of(book));
        when(formatRepository.findById(3L)).thenReturn(Optional.of(format));
        when(libraryRepository.findByUserAndBook(user, book)).thenReturn(Optional.of(libraryBook));
        when(userFormatRepository.findByLibraryBookAndFormat(libraryBook, format)).thenReturn(Optional.empty());

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> formatService.removeFormatFromBook(2L, 1L, 3L)
        );

        assertEquals("El formato no está asociado a este libro para el usuario.", exception.getMessage());
    }
}
