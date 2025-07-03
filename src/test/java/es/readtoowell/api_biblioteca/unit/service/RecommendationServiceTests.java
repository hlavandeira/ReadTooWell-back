package es.readtoowell.api_biblioteca.unit.service;

import es.readtoowell.api_biblioteca.mapper.BookMapper;
import es.readtoowell.api_biblioteca.model.DTO.book.RatedBookDTO;
import es.readtoowell.api_biblioteca.model.DTO.user.UserFavoritesDTO;
import es.readtoowell.api_biblioteca.model.entity.*;
import es.readtoowell.api_biblioteca.model.entity.id.BookListItemId;
import es.readtoowell.api_biblioteca.repository.book.BookListRepository;
import es.readtoowell.api_biblioteca.repository.book.BookRepository;
import es.readtoowell.api_biblioteca.repository.library.UserLibraryBookRepository;
import es.readtoowell.api_biblioteca.service.book.RecommendationService;
import es.readtoowell.api_biblioteca.service.user.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

/**
 * Clase de pruebas para el servicio de recomendaciones.
 */
@ExtendWith(MockitoExtension.class)
public class RecommendationServiceTests {
    @Mock
    private BookRepository bookRepository;
    @Mock
    private UserService userService;
    @Mock
    private UserLibraryBookRepository libraryRepository;
    @Mock
    private BookListRepository listRepository;
    @Mock
    private BookMapper bookMapper;
    @InjectMocks
    private RecommendationService recommendationService;

    /**
     * Método de prueba. Devolver recomendaciones basadas en libros favoritos
     */
    @Test
    public void RecommendationService_GetRecommendationsByFavoriteBooks_ReturnBooks() {
        List<Book> books = List.of(new Book());
        Book b1 = new Book(); b1.setId(1L);
        Book b2 = new Book(); b2.setId(2L);
        UserFavoritesDTO favorites = new UserFavoritesDTO();

        when(userService.getFavorites(anyLong())).thenReturn(favorites);
        favorites.setFavoriteBooks(List.of(b1, b2));
        when(bookRepository.findSimilarBooksByFavoriteBooks(anyList(), anyLong())).thenReturn(books);

        List<RatedBookDTO> result = recommendationService.getRecommendationsByFavoriteBooks(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    /**
     * Método de prueba. Devolver recomendaciones basadas en géneros favoritos
     */
    @Test
    public void RecommendationService_GetRecommendationsByFavoriteGenres_ReturnBooks() {
        List<Book> books = List.of(new Book());
        Genre g1 = new Genre(); g1.setId(1L);
        Genre g2 = new Genre(); g2.setId(2L);
        UserFavoritesDTO favorites = new UserFavoritesDTO();

        when(userService.getFavorites(anyLong())).thenReturn(favorites);
        favorites.setFavoriteGenres(List.of(g1, g2));
        when(bookRepository.findBooksWithSimilarGenres(anyList(), anyLong())).thenReturn(books);

        List<RatedBookDTO> result = recommendationService.getRecommendationsByFavoriteGenres(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    /**
     * Método de prueba. Devolver recomendaciones basadas en libros leídos
     */
    @Test
    public void RecommendationService_GetRecommendationsByBooksRead_ReturnBooks() {
        List<Book> books = List.of(new Book());

        when(bookRepository.findBooksSimilarToReadOnes(anyLong())).thenReturn(books);

        List<RatedBookDTO> result = recommendationService.getRecommendationsByReadBooks(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    /**
     * Método de prueba. Devolver recomendaciones generales del catálogo
     */
    @Test
    public void RecommendationService_GetGeneralRecommendations_ReturnBooks() {
        List<Book> books = List.of(new Book());

        when(bookRepository.findGeneralRecommendations(anyLong(), anyInt())).thenReturn(books);

        List<RatedBookDTO> result = recommendationService.getGeneralRecommendations(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    /**
     * Método de prueba. Devolver recomendaciones basadas en una lista de libros
     */
    @Test
    public void RecommendationService_GetRecommendationsByList_ReturnBooks() {
        Long idUser = 1L;
        Long idList = 10L;

        User user = new User();
        user.setId(idUser);

        BookList bookList = new BookList();
        bookList.setId(idList);
        bookList.setUser(user);

        List<BookListItem> items = new ArrayList<>();
        for (long i = 1; i <= 4; i++) {
            Book book = new Book();
            book.setId(i);

            BookListItem item = new BookListItem();
            BookListItemId id = new BookListItemId(idList, i);
            item.setId(id);
            item.setBook(book);

            items.add(item);
        }

        bookList.setBooks(items);
        bookList.setGenres(new HashSet<>());

        List<Book> recommendedBooks = List.of(new Book());

        when(listRepository.findById(idList)).thenReturn(Optional.of(bookList));
        when(bookRepository.findSimilarBooksByFavoriteBooks(anyList(), any())).thenReturn(recommendedBooks);

        List<RatedBookDTO> result = recommendationService.getRecommendationsByList(idUser, idList);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    /**
     * Método de prueba. Devolver recomendaciones basadas en una lista inexistente
     */
    @Test
    public void RecommendationService_GetRecommendationsByList_UnexistentList() {
        when(listRepository.findById(anyLong())).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> recommendationService.getRecommendationsByList(1L, 99L)
        );

        assertEquals("La lista con ID 99 no existe.", exception.getMessage());
    }

    /**
     * Método de prueba. Devolver recomendaciones basadas en la lista de otro usuario
     */
    @Test
    public void RecommendationService_GetRecommendationsByList_UserIsNotOwner() {
        Long idUser = 1L;
        Long idList = 10L;

        User owner = new User();
        owner.setId(2L);

        BookList bookList = new BookList();
        bookList.setId(idList);
        bookList.setUser(owner);

        when(listRepository.findById(idList)).thenReturn(Optional.of(bookList));

        AccessDeniedException exception = assertThrows(
                AccessDeniedException.class,
                () -> recommendationService.getRecommendationsByList(idUser, idList)
        );

        assertEquals("Solo el propietario de la lista puede acceder a esta.", exception.getMessage());
    }

    /**
     * Método de prueba. Devolver recomendaciones basadas en una lista vacía
     */
    @Test
    public void RecommendationService_GetRecommendationsByList_ListIsEmpty() {
        Long idUser = 1L;
        Long idList = 10L;

        User user = new User();
        user.setId(idUser);

        BookList bookList = new BookList();
        bookList.setId(idList);
        bookList.setUser(user);
        bookList.setBooks(new ArrayList<>());
        bookList.setGenres(new HashSet<>());

        when(listRepository.findById(idList)).thenReturn(Optional.of(bookList));

        List<RatedBookDTO> result = recommendationService.getRecommendationsByList(idUser, idList);

        assertNull(result);
    }
}
