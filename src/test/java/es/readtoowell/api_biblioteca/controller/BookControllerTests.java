package es.readtoowell.api_biblioteca.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.readtoowell.api_biblioteca.config.security.JwtFilter;
import es.readtoowell.api_biblioteca.controller.book.BookController;
import es.readtoowell.api_biblioteca.model.DTO.book.BookDTO;
import es.readtoowell.api_biblioteca.model.DTO.book.BookDetailsDTO;
import es.readtoowell.api_biblioteca.model.DTO.book.CollectionDTO;
import es.readtoowell.api_biblioteca.model.DTO.book.GenreDTO;
import es.readtoowell.api_biblioteca.model.DTO.user.AuthorDTO;
import es.readtoowell.api_biblioteca.model.entity.User;
import es.readtoowell.api_biblioteca.service.book.BookService;
import es.readtoowell.api_biblioteca.service.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Clase de pruebas para el controlador de libros.
 */
@WebMvcTest(controllers = BookController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class BookControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private BookService bookService;
    @MockitoBean
    private UserService userService;
    @MockitoBean
    private JwtFilter jwtFilter;
    @Autowired
    private ObjectMapper objectMapper;

    private BookDTO bookDTO;
    private Page<BookDTO> booksPage;
    private BookDetailsDTO detailsDTO;
    private List<BookDTO> booksList;

    @BeforeEach
    public void init() {
        bookDTO = new BookDTO();
        bookDTO.setTitle("Prueba de libro");
        bookDTO.setAuthor("Autor de prueba");
        bookDTO.setPublicationYear(2000);
        bookDTO.setPageNumber(200);
        bookDTO.setIsbn("1122123999234");
        bookDTO.setActive(true);

        booksList = new ArrayList<>();
        booksList.add(bookDTO);
        booksList.add(bookDTO);
        booksList.add(bookDTO);
        booksPage = new PageImpl<>(booksList);

        detailsDTO = new BookDetailsDTO();
        detailsDTO.setBook(bookDTO);
    }

    /**
     * Método de prueba. Devolver todos los libros
     */
    @Test
    public void BookController_GetBooks_ReturnBooks() throws Exception {
        given(bookService.getAllBooks(0, 3)).willReturn(booksPage);

        ResultActions response = mockMvc.perform(get("/libros"));

        response.andExpect(status().isOk());
    }

    /**
     * Método de prueba. Devolver un libro
     */
    @Test
    public void BookController_GetBook_ReturnBook() throws Exception {
        given(bookService.getBook(any())).willReturn(bookDTO);

        ResultActions response = mockMvc.perform(get("/libros/1"));

        response.andExpect(status().isOk());
    }

    /**
     * Método de prueba. Crear un libro con datos válidos
     */
    @Test
    public void BookController_CreateBook_ReturnNewBook() throws Exception {
        given(userService.getAuthenticatedUser()).willReturn(new User());
        given(bookService.createBook(any(), any(), any())).willReturn(bookDTO);

        ResultActions response = mockMvc.perform(post("/libros?genreIds=1&genreIds=2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookDTO)));

        response.andExpect(status().isCreated());
    }

    /**
     * Método de prueba. Crear un libro con datos inválidos
     */
    @Test
    public void BookController_CreateBook_InvalidBookParams() throws Exception {
        given(userService.getAuthenticatedUser()).willReturn(new User());
        given(bookService.createBook(any(), any(), any())).willReturn(bookDTO);

        bookDTO.setTitle("");
        bookDTO.setIsbn("isbn incorrecto");
        ResultActions response = mockMvc.perform(post("/libros?genreIds=1&genreIds=2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookDTO)));

        response.andExpect(status().isBadRequest());
    }

    /**
     * Método de prueba. Actualizar un libro con datos válidos
     */
    @Test
    public void BookController_UpdateBook_ReturnUpdatedBook() throws Exception {
        given(userService.getAuthenticatedUser()).willReturn(new User());
        given(bookService.updateBook(any(), any(), any(), any())).willReturn(bookDTO);

        ResultActions response = mockMvc.perform(put("/libros/1?genreIds=1&genreIds=2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookDTO)));

        response.andExpect(status().isOk());
    }

    /**
     * Método de prueba. Actualizar un libro con datos inválidos
     */
    @Test
    public void BookController_UpdateBook_InvalidBookParams() throws Exception {
        given(userService.getAuthenticatedUser()).willReturn(new User());
        given(bookService.updateBook(any(), any(), any(), any())).willReturn(bookDTO);

        bookDTO.setAuthor("");
        bookDTO.setPageNumber(-89);
        ResultActions response = mockMvc.perform(put("/libros/1?genreIds=1&genreIds=2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookDTO)));

        response.andExpect(status().isBadRequest());
    }

    /**
     * Método de prueba. Eliminar un libro
     */
    @Test
    public void BookController_DeleteBook_ReturnDeleted() throws Exception {
        given(userService.getAuthenticatedUser()).willReturn(new User());
        given(bookService.deleteBook(any(), any())).willReturn(bookDTO);

        ResultActions response = mockMvc.perform(delete("/libros/1"));

        response.andExpect(status().isOk());
    }

    /**
     * Método de prueba. Reactivar un libro
     */
    @Test
    public void BookController_ReactivateBook_ReturnReactivated() throws Exception {
        given(userService.getAuthenticatedUser()).willReturn(new User());
        given(bookService.reactivateBook(any(), any())).willReturn(bookDTO);

        ResultActions response = mockMvc.perform(put("/libros/reactivar/1"));

        response.andExpect(status().isOk());
    }

    /**
     * Método de prueba. Buscar libros
     */
    @Test
    public void BookController_SearchBooks_ReturnBooks() throws Exception {
        given(userService.getAuthenticatedUser()).willReturn(new User());
        given(bookService.filterBooks(any(), any(), any(), any(), any(), anyInt(), anyInt()))
                .willReturn(booksPage);

        ResultActions response = mockMvc.perform(get("/libros/buscar"));

        response.andExpect(status().isOk());
    }

    /**
     * Método de prueba. Filtrar libros por un género concreto
     */
    @Test
    public void BookController_SearchBooksByGenre_ReturnBooks() throws Exception {
        given(userService.getAuthenticatedUser()).willReturn(new User());
        given(bookService.filterBooksByGenre(any(), anyInt(), anyInt())).willReturn(booksPage);

        ResultActions response = mockMvc.perform(get("/libros/buscar-genero?idGenre=1"));

        response.andExpect(status().isOk());
    }

    /**
     * Método de prueba. Devolver los detalles completos de un libro
     */
    @Test
    public void BookController_GetBookDetails_ReturnBook() throws Exception {
        given(userService.getAuthenticatedUser()).willReturn(new User());
        given(bookService.getBookDetails(any(), any())).willReturn(detailsDTO);

        ResultActions response = mockMvc.perform(get("/libros/1/detalles"));

        response.andExpect(status().isOk());
    }

    /**
     * Método de prueba. Devolver los libros escritos por un autor registrado
     */
    @Test
    public void BookController_GetBooksByAuthor_ReturnAuthor() throws Exception {
        AuthorDTO author = new AuthorDTO();
        author.setAuthor(new User());
        author.setBooks(new ArrayList<>());

        given(userService.getAuthenticatedUser()).willReturn(new User());
        given(bookService.getBooksByAuthor(any())).willReturn(author);

        ResultActions response = mockMvc.perform(get("/libros/1/autor"));

        response.andExpect(status().isOk());
    }

    /**
     * Método de prueba. Devolver los libros escritos por un autor
     */
    @Test
    public void BookController_GetAllBooksByAuthor_ReturnBooks() throws Exception {
        given(userService.getAuthenticatedUser()).willReturn(new User());
        given(bookService.getAllBooksByAuthor(any(), anyInt(), anyInt())).willReturn(booksPage);

        ResultActions response = mockMvc.perform(get("/libros/libros-autor?authorName=Autor"));

        response.andExpect(status().isOk());
    }

    /**
     * Método de prueba. Devolver los libros pertenecientes a una colección
     */
    @Test
    public void BookController_GetOtherBooksFromCollection_ReturnBooks() throws Exception {
        given(userService.getAuthenticatedUser()).willReturn(new User());
        given(bookService.getOtherBooksFromCollection(any())).willReturn(booksList);

        ResultActions response = mockMvc.perform(get("/libros/coleccion/1"));

        response.andExpect(status().isOk());
    }

    /**
     * Método de prueba. Devolver los géneros
     */
    @Test
    public void BookController_GetGenres_ReturnGenres() throws Exception {
        List<GenreDTO> genresList = new ArrayList<>();
        genresList.add(new GenreDTO()); genresList.add(new GenreDTO()); genresList.add(new GenreDTO());

        given(userService.getAuthenticatedUser()).willReturn(new User());
        given(bookService.getGenres()).willReturn(genresList);

        ResultActions response = mockMvc.perform(get("/libros/generos"));

        response.andExpect(status().isOk());
    }

    /**
     * Método de prueba. Devolver los libros desactivados/borrados
     */
    @Test
    public void BookController_GetDeletedBooks_ReturnBooks() throws Exception {
        given(userService.getAuthenticatedUser()).willReturn(new User());
        given(bookService.getDeletedBooks(anyInt(), anyInt(), any())).willReturn(booksPage);

        ResultActions response = mockMvc.perform(get("/libros/desactivados"));

        response.andExpect(status().isOk());
    }

    /**
     * Método de prueba. Devolver las colecciones
     */
    @Test
    public void BookController_GetCollections_ReturnCollections() throws Exception {
        List<CollectionDTO> collectionsList = new ArrayList<>();
        collectionsList.add(new CollectionDTO()); collectionsList.add(new CollectionDTO());
        collectionsList.add(new CollectionDTO());

        given(userService.getAuthenticatedUser()).willReturn(new User());
        given(bookService.getCollections()).willReturn(collectionsList);

        ResultActions response = mockMvc.perform(get("/libros/colecciones"));

        response.andExpect(status().isOk());
    }

    /**
     * Método de prueba. Crear una colección con datos válidos
     */
    @Test
    public void BookController_CreateCollection_ReturnCollection() throws Exception {
        CollectionDTO collectionDTO = new CollectionDTO();
        collectionDTO.setName("Nombre de la colección");

        given(userService.getAuthenticatedUser()).willReturn(new User());
        given(bookService.createCollection(any())).willReturn(collectionDTO);

        ResultActions response = mockMvc.perform(post("/libros/colecciones")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(collectionDTO)));

        response.andExpect(status().isOk());
    }

    /**
     * Método de prueba. Crear una colección con datos inválidos
     */
    @Test
    public void BookController_CreateCollection_InvalidName() throws Exception {
        CollectionDTO collectionDTO = new CollectionDTO();
        collectionDTO.setName("");

        given(userService.getAuthenticatedUser()).willReturn(new User());
        given(bookService.createCollection(any())).willReturn(collectionDTO);

        ResultActions response = mockMvc.perform(post("/libros/colecciones")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(collectionDTO)));

        response.andExpect(status().isBadRequest());
    }

    /**
     * Método de prueba. Usuario no autenticado
     */
    @Test
    public void BookController_UnauthenticatedUser_AccessDenied() throws Exception {
        given(userService.getAuthenticatedUser()).willReturn(null);

        ResultActions response = mockMvc.perform(get("/libros/1/detalles"));

        response.andExpect(status().isForbidden());
    }
}
