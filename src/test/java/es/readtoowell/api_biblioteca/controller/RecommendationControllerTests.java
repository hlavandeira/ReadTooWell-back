package es.readtoowell.api_biblioteca.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.readtoowell.api_biblioteca.config.security.JwtFilter;
import es.readtoowell.api_biblioteca.controller.book.RecommendationController;
import es.readtoowell.api_biblioteca.model.DTO.book.FormatDTO;
import es.readtoowell.api_biblioteca.model.DTO.book.RatedBookDTO;
import es.readtoowell.api_biblioteca.model.entity.User;
import es.readtoowell.api_biblioteca.service.book.RecommendationService;
import es.readtoowell.api_biblioteca.service.library.UserBookFormatService;
import es.readtoowell.api_biblioteca.service.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Clase de prueba para el controlador de recomendaciones.
 */
@WebMvcTest(controllers = RecommendationController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class RecommendationControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private RecommendationService recommendationService;
    @MockitoBean
    private UserService userService;
    @MockitoBean
    private JwtFilter jwtFilter;
    @Autowired
    private ObjectMapper objectMapper;

    private List<RatedBookDTO> booksList;

    @BeforeEach
    public void init() {
        booksList = new ArrayList<>();
        booksList.add(new RatedBookDTO());
        booksList.add(new RatedBookDTO());
        booksList.add(new RatedBookDTO());
    }

    /**
     * Método de prueba. Recibir recomendaciones a partir de libros favoritos
     */
    @Test
    public void RecommendationController_GetRecommendationsByFavoriteBooks_ReturnBooks() throws Exception {
        given(userService.getAuthenticatedUser()).willReturn(new User());
        given(recommendationService.getRecommendationsByFavoriteBooks(any())).willReturn(booksList);

        ResultActions response = mockMvc.perform(get("/recomendaciones/libros-favoritos"));

        response.andExpect(status().isOk());
    }

    /**
     * Método de prueba. Recibir recomendaciones a partir de géneros favoritos
     */
    @Test
    public void RecommendationController_GetRecommendationsByFavoriteGenres_ReturnBooks() throws Exception {
        given(userService.getAuthenticatedUser()).willReturn(new User());
        given(recommendationService.getRecommendationsByFavoriteGenres(any())).willReturn(booksList);

        ResultActions response = mockMvc.perform(get("/recomendaciones/generos-favoritos"));

        response.andExpect(status().isOk());
    }

    /**
     * Método de prueba. Recibir recomendaciones a partir de libros leídos
     */
    @Test
    public void RecommendationController_GetRecommendationsByReadBooks_ReturnBooks() throws Exception {
        given(userService.getAuthenticatedUser()).willReturn(new User());
        given(recommendationService.getRecommendationsByReadBooks(any())).willReturn(booksList);

        ResultActions response = mockMvc.perform(get("/recomendaciones/libros-leidos"));

        response.andExpect(status().isOk());
    }

    /**
     * Método de prueba. Recibir recomendaciones a partir de una lista del usuario
     */
    @Test
    public void RecommendationController_GetRecommendationsByList_ReturnBooks() throws Exception {
        given(userService.getAuthenticatedUser()).willReturn(new User());
        given(recommendationService.getRecommendationsByList(any(), any())).willReturn(booksList);

        ResultActions response = mockMvc.perform(get("/recomendaciones/lista/1"));

        response.andExpect(status().isOk());
    }

    /**
     * Método de prueba. Recibir recomendaciones generales
     */
    @Test
    public void RecommendationController_GetGeneralRecommendations_ReturnBooks() throws Exception {
        given(userService.getAuthenticatedUser()).willReturn(new User());
        given(recommendationService.getGeneralRecommendations(any())).willReturn(booksList);

        ResultActions response = mockMvc.perform(get("/recomendaciones/catalogo"));

        response.andExpect(status().isOk());
    }

    /**
     * Método de prueba. Usuario no autenticado
     */
    @Test
    public void RecommendationController_UnauthenticatedUser() throws Exception {
        given(userService.getAuthenticatedUser()).willReturn(null);

        ResultActions response = mockMvc.perform(get("/recomendaciones/catalogo"));

        response.andExpect(status().isForbidden());
    }
}
