package es.readtoowell.api_biblioteca.unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.readtoowell.api_biblioteca.config.security.JwtFilter;
import es.readtoowell.api_biblioteca.controller.library.UserLibraryBookController;
import es.readtoowell.api_biblioteca.model.DTO.UserLibraryBookDTO;
import es.readtoowell.api_biblioteca.model.DTO.YearRecapDTO;
import es.readtoowell.api_biblioteca.model.DTO.book.RatingDTO;
import es.readtoowell.api_biblioteca.model.entity.User;
import es.readtoowell.api_biblioteca.service.library.UserLibraryBookService;
import es.readtoowell.api_biblioteca.service.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Clase de pruebas para el controlador de bibliotecas de usuario.
 */
@WebMvcTest(controllers = UserLibraryBookController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class UserLibraryBookControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private UserLibraryBookService libraryService;
    @MockitoBean
    private UserService userService;
    @MockitoBean
    private JwtFilter jwtFilter;
    @Autowired
    private ObjectMapper objectMapper;

    private Page<UserLibraryBookDTO> libraryPage;
    private UserLibraryBookDTO libraryBook;

    @BeforeEach
    public void init() {
        List<UserLibraryBookDTO> list = new ArrayList<>();
        libraryBook = new UserLibraryBookDTO();
        list.add(libraryBook);
        list.add(libraryBook);
        list.add(libraryBook);

        libraryPage = new PageImpl<>(list);
    }

    /**
     * Método de prueba. Devolver los libros de la biblioteca de un usuario
     */
    @Test
    public void UserLibraryBookController_GetLibraryBooks_ReturnBooks() throws Exception {
        given(userService.getAuthenticatedUser()).willReturn(new User());
        given(libraryService.getLibraryFromUser(any(), anyInt(), anyInt())).willReturn(libraryPage);

        ResultActions response = mockMvc.perform(get("/biblioteca/todos"));

        response.andExpect(status().isOk());
    }

    /**
     * Método de prueba. Devolver los libros de la biblioteca de un usuario con un estado de lectura concreto
     */
    @Test
    public void UserLibraryBookController_GetLibraryBooksByStatus_ReturnBooks() throws Exception {
        given(userService.getAuthenticatedUser()).willReturn(new User());
        given(libraryService.getLibraryByStatus(any(), anyInt(), anyInt(), anyInt())).willReturn(libraryPage);

        ResultActions response = mockMvc.perform(get("/biblioteca?status=1"));

        response.andExpect(status().isOk());
    }

    /**
     * Método de prueba. Añadir un libro a una biblioteca
     */
    @Test
    public void UserLibraryBookController_AddBookToLibrary_ReturnCreated() throws Exception {
        given(userService.getAuthenticatedUser()).willReturn(new User());
        given(libraryService.addBookToLibrary(any(), any())).willReturn(libraryBook);

        ResultActions response = mockMvc.perform(post("/biblioteca/1"));

        response.andExpect(status().isCreated());
    }

    /**
     * Método de prueba. Eliminar un libro de una biblioteca
     */
    @Test
    public void UserLibraryBookController_DeleteBookFromLibrary_ReturnDeleted() throws Exception {
        given(userService.getAuthenticatedUser()).willReturn(new User());
        given(libraryService.deleteBookFromLibrary(any(), any())).willReturn(libraryBook);

        ResultActions response = mockMvc.perform(delete("/biblioteca/1"));

        response.andExpect(status().isOk());
    }

    /**
     * Método de prueba. Calificar un libro
     */
    @Test
    public void UserLibraryBookController_RateBook_ReturnUpdated() throws Exception {
        RatingDTO rating = new RatingDTO();

        given(userService.getAuthenticatedUser()).willReturn(new User());
        given(libraryService.rateBook(any(), any(), anyDouble())).willReturn(rating);

        ResultActions response = mockMvc.perform(put("/biblioteca/1/calificar?calificacion=5"));

        response.andExpect(status().isOk());
    }

    /**
     * Método de prueba. Escribir una reseña para un libro
     */
    @Test
    public void UserLibraryBookController_ReviewBook_ReturnUpdated() throws Exception {
        given(userService.getAuthenticatedUser()).willReturn(new User());
        given(libraryService.reviewBook(any(), any(), any())).willReturn(libraryBook);

        ResultActions response = mockMvc.perform(put("/biblioteca/1/escribir-reseña?review=Reseña para libro"));

        response.andExpect(status().isOk());
    }

    /**
     * Método de prueba. Actualizar el estado de lectura de un libro
     */
    @Test
    public void UserLibraryBookController_UpdateReadingStatus_ReturnUpdated() throws Exception {
        given(userService.getAuthenticatedUser()).willReturn(new User());
        given(libraryService.updateReadingStatus(any(), any(), anyInt())).willReturn(libraryBook);

        ResultActions response = mockMvc.perform(put("/biblioteca/1/estado?estado=2"));

        response.andExpect(status().isOk());
    }

    /**
     * Método de prueba. Actualizar el progreso de un libro
     */
    @Test
    public void UserLibraryBookController_UpdateProgress_ReturnUpdated() throws Exception {
        given(userService.getAuthenticatedUser()).willReturn(new User());
        given(libraryService.updateProgress(any(), any(), anyInt(), any())).willReturn(libraryBook);

        ResultActions response = mockMvc.perform(put("/biblioteca/1/progreso?progreso=100&tipoProgreso=porcentaje"));

        response.andExpect(status().isOk());
    }

    /**
     * Método de prueba. Devolver el resumen anual de un usuario
     */
    @Test
    public void UserLibraryBookController_GetYearRecap_ReturnYearRecap() throws Exception {
        YearRecapDTO yearRecapDTO = new YearRecapDTO();

        given(userService.getAuthenticatedUser()).willReturn(new User());
        given(libraryService.getYearRecap(any())).willReturn(yearRecapDTO);

        ResultActions response = mockMvc.perform(get("/biblioteca/resumen-anual"));

        response.andExpect(status().isOk());
    }

    /**
     * Método de prueba. Usuario no autenticado
     */
    @Test
    public void UserLibraryBookController_UnauthenticatedUser() throws Exception {
        given(userService.getAuthenticatedUser()).willReturn(null);

        ResultActions response = mockMvc.perform(get("/biblioteca/resumen-anual"));

        response.andExpect(status().isForbidden());
    }
}
