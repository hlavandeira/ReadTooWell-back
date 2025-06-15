package es.readtoowell.api_biblioteca.service;

import es.readtoowell.api_biblioteca.mapper.BookListItemMapper;
import es.readtoowell.api_biblioteca.mapper.BookListMapper;
import es.readtoowell.api_biblioteca.mapper.GenreMapper;
import es.readtoowell.api_biblioteca.model.DTO.book.BookListDTO;
import es.readtoowell.api_biblioteca.model.DTO.book.BookListDetailsDTO;
import es.readtoowell.api_biblioteca.model.DTO.book.BookListItemDTO;
import es.readtoowell.api_biblioteca.model.DTO.book.GenreDTO;
import es.readtoowell.api_biblioteca.model.DTO.user.UserDTO;
import es.readtoowell.api_biblioteca.model.entity.*;
import es.readtoowell.api_biblioteca.model.entity.id.BookListItemId;
import es.readtoowell.api_biblioteca.repository.book.BookListItemRepository;
import es.readtoowell.api_biblioteca.repository.book.BookListRepository;
import es.readtoowell.api_biblioteca.repository.book.BookRepository;
import es.readtoowell.api_biblioteca.repository.book.GenreRepository;
import es.readtoowell.api_biblioteca.service.book.BookListService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.security.access.AccessDeniedException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Clase de pruebas para el servicio de listas de libros.
 */
@ExtendWith(MockitoExtension.class)
public class BookListServiceTests {
    @Mock
    private BookListRepository listRepository;
    @Mock
    private BookListMapper listMapper;
    @Mock
    private GenreRepository genreRepository;
    @Mock
    private GenreMapper genreMapper;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private BookListItemRepository bookItemRepository;
    @Mock
    private BookListItemMapper bookItemMapper;
    @InjectMocks
    private BookListService listService;

    private BookList bookList;
    private BookListDTO bookListDTO;
    private BookListItem bookItem;
    private BookListItemDTO bookItemDTO;
    private Genre genre;
    private GenreDTO genreDTO;

    @BeforeEach
    public void setup() {
        User user = new User();
        user.setId(1L);
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);

        bookList = new BookList();
        bookList.setId(1L);
        bookList.setName("Lista de prueba");
        bookList.setDescription("Descripción");
        bookList.setUser(user);

        bookListDTO = new BookListDTO();
        bookListDTO.setId(1L);
        bookListDTO.setName("Lista de prueba");
        bookListDTO.setDescription("Descripción");
        bookListDTO.setUser(userDTO);

        genre = new Genre();
        genre.setId(1L);
        genre.setName("Fantasía");

        bookList.setGenres(Set.of(genre));

        bookItem = new BookListItem();
        bookItem.setList(bookList);

        bookItemDTO = new BookListItemDTO();

        genreDTO = new GenreDTO();
        genreDTO.setId(1L);
        genreDTO.setName("Fantasía");
    }

    @Test
    public void BookListService_GetListsByUser_ReturnLists() {
        int page = 0;
        int size = 10;
        Long idUser = 1L;
        Pageable pageable = PageRequest.of(page, size);
        Page<BookList> bookListPage = new PageImpl<>(List.of(bookList));

        when(listRepository.findByUserId(idUser, pageable)).thenReturn(bookListPage);
        when(listMapper.toDTO(bookList)).thenReturn(bookListDTO);

        Page<BookListDTO> result = listService.getListsByUser(idUser, page, size);

        assertEquals(1, result.getTotalElements());
        assertEquals(bookListDTO.getId(), result.getContent().get(0).getId());
    }

    @Test
    public void BookListService_GetListDetails_ReturnListDetailsDto() {
        int page = 0, size = 10;
        Long idUser = 1L, idList = 1L;
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "dateAdded"));

        Page<BookListItem> itemPage = new PageImpl<>(List.of(bookItem));

        when(bookItemRepository.findByListId(idList, pageable)).thenReturn(itemPage);
        when(listRepository.findByIdWithRelations(idList)).thenReturn(Optional.of(bookList));
        when(bookItemMapper.toDTO(bookItem)).thenReturn(bookItemDTO);
        when(genreMapper.toDTO(genre)).thenReturn(genreDTO);

        BookListDetailsDTO result = listService.getListDetails(idUser, idList, page, size);

        assertEquals(idList, result.getId());
        assertEquals("Lista de prueba", result.getName());
        assertEquals("Descripción", result.getDescription());
        assertEquals(1, result.getBooks().getTotalElements());
        assertEquals(1, result.getGenres().size());
        assertEquals("Fantasía", result.getGenres().get(0).getName());
    }

    @Test
    public void BookListService_GetListDetails_UnexistentList() {
        when(listRepository.findByIdWithRelations(100L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> listService.getListDetails(1L, 100L, 0, 10)
        );

        assertEquals("La lista con ID 100 no existe.", exception.getMessage());
    }

    @Test
    public void BookListService_GetListDetails_UserIsNotOwner() {
        User otherUser = new User();
        otherUser.setId(2L);
        bookList.setUser(otherUser);

        when(listRepository.findByIdWithRelations(100L)).thenReturn(Optional.of(bookList));

        AccessDeniedException exception = assertThrows(
                AccessDeniedException.class,
                () -> listService.getListDetails(1L, 100L, 0, 10)
        );

        assertEquals("No tienes permiso para consultar esta lista.", exception.getMessage());
    }

    @Test
    public void BookListService_CreateList_ReturnBookListDto() {
        User user = new User();
        user.setId(1L);
        List<Long> genreIds = List.of(1L);

        when(genreRepository.findAllById(genreIds)).thenReturn(List.of(genre));
        when(listRepository.save(any(BookList.class))).thenAnswer(invocation -> {
            BookList saved = invocation.getArgument(0);
            saved.setId(2L);
            return saved;
        });
        bookListDTO.setId(2L);
        when(listMapper.toDTO(any())).thenReturn(bookListDTO);

        BookListDTO result = listService.createList(user, bookListDTO, genreIds);

        assertNotNull(result);
        assertEquals("Lista de prueba", result.getName());
        assertEquals("Descripción", result.getDescription());
        assertEquals(2L, result.getId());
    }

    @Test
    public void BookListService_UpdateList_ReturnBookListDto() {
        Long idUser = 1L;
        Long idList = 1L;
        BookListDTO dto = new BookListDTO();
        dto.setName("Lista actualizada");
        dto.setDescription("Nueva descripción");

        bookList.setName("Antiguo nombre");
        bookList.setDescription("Antigua descripción");

        when(listRepository.findByIdWithRelations(idList)).thenReturn(Optional.of(bookList));
        when(genreRepository.findAllById(List.of(1L))).thenReturn(List.of(genre));
        when(listRepository.save(any(BookList.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(listMapper.toDTO(any())).thenReturn(dto);

        BookListDTO result = listService.updateList(idUser, idList, dto, List.of(1L));

        assertNotNull(result);
        assertEquals("Lista actualizada", result.getName());
        assertEquals("Nueva descripción", result.getDescription());
    }

    @Test
    public void BookListService_UpdateList_UnexistentList() {
        when(listRepository.findByIdWithRelations(999L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> listService.updateList(1L, 999L, bookListDTO, List.of(1L))
        );

        assertEquals("La lista con ID 999 no existe.", exception.getMessage());
    }

    @Test
    public void BookListService_UpdateList_UserIsNotOwner() {
        bookList.getUser().setId(2L);

        when(listRepository.findByIdWithRelations(1L)).thenReturn(Optional.of(bookList));

        AccessDeniedException exception = assertThrows(
                AccessDeniedException.class,
                () -> listService.updateList(1L, 1L, bookListDTO, List.of(1L))
        );

        assertEquals("No tienes permiso para editar esta lista.", exception.getMessage());
    }

    @Test
    public void BookListService_DeleteList_ReturnBookListDto() {
        Long idUser = 1L;
        Long idList = 1L;

        bookList.getUser().setId(idUser);

        when(listRepository.findByIdWithRelations(idList)).thenReturn(Optional.of(bookList));
        when(listMapper.toDTO(bookList)).thenReturn(bookListDTO);

        BookListDTO result = listService.deleteList(idUser, idList);

        assertNotNull(result);
        assertEquals(bookListDTO, result);
        verify(listRepository).delete(bookList);
    }

    @Test
    public void BookListService_DeleteList_UnexistentList() {
        when(listRepository.findByIdWithRelations(999L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> listService.deleteList(1L, 999L)
        );

        assertEquals("Lista con ID 999 no encontrada.", exception.getMessage());
    }

    @Test
    public void BookListService_DeleteList_UserIsNotOwner() {
        bookList.getUser().setId(2L);

        when(listRepository.findByIdWithRelations(100L)).thenReturn(Optional.of(bookList));

        AccessDeniedException exception = assertThrows(
                AccessDeniedException.class,
                () -> listService.deleteList(1L, 100L)
        );

        assertEquals("No tienes permiso para borrar esta lista.", exception.getMessage());
    }

    @Test
    public void BookListService_AddBookToList_ReturnBookListDto() {
        Long idUser = 1L;
        Long idList = 1L;
        Long idBook = 2L;

        bookList.getUser().setId(idUser);
        bookList.setBooks(new ArrayList<>());

        Book book = new Book();
        book.setId(idBook);

        when(listRepository.findByIdWithRelations(idList)).thenReturn(Optional.of(bookList));
        when(bookRepository.findById(idBook)).thenReturn(Optional.of(book));
        when(listRepository.save(any())).thenReturn(bookList);
        when(listMapper.toDTO(bookList)).thenReturn(bookListDTO);

        BookListDTO result = listService.addBookToList(idUser, idList, idBook);

        assertNotNull(result);
        assertEquals(bookListDTO, result);
        verify(listRepository).save(any());
    }

    @Test
    public void BookListService_AddBookToList_UnexistentList() {
        when(listRepository.findByIdWithRelations(any())).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> listService.addBookToList(1L, 999L, 2L)
        );

        assertEquals("Lista con ID 999 no encontrada.", exception.getMessage());
    }

    @Test
    public void BookListService_AddBookToList_UserIsNotOwner() {
        bookList.getUser().setId(2L);

        when(listRepository.findByIdWithRelations(any())).thenReturn(Optional.of(bookList));

        AccessDeniedException exception = assertThrows(
                AccessDeniedException.class,
                () -> listService.addBookToList(1L, 1L, 2L)
        );

        assertEquals("No tienes permiso para acceder a esta lista.", exception.getMessage());
    }

    @Test
    public void BookListService_AddBookToList_UnexistentBook() {
        bookList.getUser().setId(1L);

        when(listRepository.findByIdWithRelations(1L)).thenReturn(Optional.of(bookList));
        when(bookRepository.findById(200L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> listService.addBookToList(1L, 1L, 200L)
        );

        assertEquals("Libro con ID 200 no encontrado.", exception.getMessage());
    }

    @Test
    public void BookListService_DeleteBookFromList_ReturnBookListDto() {
        Long idUser = 1L;
        Long idList = 1L;
        Long idBook = 2L;

        bookList.getUser().setId(idUser);

        Book book = new Book();
        book.setId(idBook);

        BookListItem item = new BookListItem();
        item.setId(new BookListItemId(idList, idBook));
        item.setList(bookList);
        item.setBook(book);

        bookList.setBooks(new ArrayList<>(Set.of(item)));

        when(listRepository.findByIdWithRelations(idList)).thenReturn(Optional.of(bookList));
        when(listRepository.save(any())).thenReturn(bookList);
        when(listMapper.toDTO(bookList)).thenReturn(bookListDTO);

        BookListDTO result = listService.deleteBookFromList(idUser, idList, idBook);

        assertNotNull(result);
        assertEquals(bookListDTO, result);
        verify(listRepository).save(any());
    }

    @Test
    public void BookListService_DeleteBookFromList_UnexistentList() {
        when(listRepository.findByIdWithRelations(any())).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> listService.deleteBookFromList(1L, 999L, 2L)
        );

        assertEquals("Lista con ID 999 no encontrada.", exception.getMessage());
    }

    @Test
    public void BookListService_DeleteBookFromList_UserIsNotOwner() {
        bookList.getUser().setId(2L);

        when(listRepository.findByIdWithRelations(any())).thenReturn(Optional.of(bookList));

        AccessDeniedException exception = assertThrows(
                AccessDeniedException.class,
                () -> listService.deleteBookFromList(1L, 1L, 2L)
        );

        assertEquals("No tienes permiso para acceder a esta lista.", exception.getMessage());
    }

    @Test
    public void BookListService_GetListsWithoutBooks_ReturnLists() {
        Long idUser = 1L;
        Long idBook = 200L;
        Pageable pageable = PageRequest.of(0, 10);
        Page<BookList> pageResult = new PageImpl<>(List.of(bookList));
        Book book = new Book();
        book.setId(idBook);

        when(bookRepository.findById(idBook)).thenReturn(Optional.of(book));
        when(bookItemRepository.findAllListsByUserIdAndBookIdNotIn(idUser, idBook, pageable)).thenReturn(pageResult);
        when(listMapper.toDTO(bookList)).thenReturn(bookListDTO);

        Page<BookListDTO> result = listService.getListsWithoutBook(idBook, idUser, 0, 10);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(bookListDTO, result.getContent().get(0));
    }

    @Test
    public void BookListService_GetListsWithoutBooks_UnexistentBook() {
        Long idBook = 999L;

        when(bookRepository.findById(idBook)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> listService.getListsWithoutBook(idBook, 1L, 0, 10)
        );

        assertEquals("Libro con ID 999 no encontrado.", exception.getMessage());
    }

    @Test
    public void BookListService_GetAllListsExcludingEmpty_ReturnLists() {
        bookList.setBooks(List.of(new BookListItem()));
        bookList.setGenres(Set.of());

        when(listRepository.findAllByUserId(1L)).thenReturn(List.of(bookList));
        when(listMapper.toDTO(bookList)).thenReturn(bookListDTO);

        List<BookListDTO> result = listService.getAllListsExcludingEmpty(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(bookListDTO, result.get(0));
    }

    @Test
    public void BookListService_GetAllListsExcludingEmpty_AllListsAreEmpty() {
        bookList.setBooks(List.of());
        bookList.setGenres(Set.of());

        when(listRepository.findAllByUserId(1L)).thenReturn(List.of(bookList));

        List<BookListDTO> result = listService.getAllListsExcludingEmpty(1L);

        assertNotNull(result);
        assertEquals(0, result.size());
    }
}
