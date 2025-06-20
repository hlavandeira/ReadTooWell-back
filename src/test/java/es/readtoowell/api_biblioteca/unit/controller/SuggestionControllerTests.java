package es.readtoowell.api_biblioteca.unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.readtoowell.api_biblioteca.config.security.JwtFilter;
import es.readtoowell.api_biblioteca.controller.book.SuggestionController;
import es.readtoowell.api_biblioteca.model.DTO.SuggestionDTO;
import es.readtoowell.api_biblioteca.model.entity.User;
import es.readtoowell.api_biblioteca.service.book.SuggestionService;
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
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Clase de pruebas para el controlador de sugerencias.
 */
@WebMvcTest(controllers = SuggestionController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class SuggestionControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private SuggestionService suggestionService;
    @MockitoBean
    private UserService userService;
    @MockitoBean
    private JwtFilter jwtFilter;
    @Autowired
    private ObjectMapper objectMapper;

    private SuggestionDTO suggestionDTO;
    private Page<SuggestionDTO> suggestionsPage;

    @BeforeEach
    public void init() {
        suggestionDTO = new SuggestionDTO();
        suggestionDTO.setTitle("Título");
        suggestionDTO.setAuthor("Autor");
        suggestionDTO.setPublicationYear(1999);

        List<SuggestionDTO> list = new ArrayList<>();
        list.add(suggestionDTO);
        list.add(suggestionDTO);
        list.add(suggestionDTO);

        suggestionsPage = new PageImpl<>(list);
    }

    /**
     * Método de prueba. Enviar una sugerencia de libro con datos válidos
     */
    @Test
    public void SuggestionController_SendSuggestion_ReturnCreated() throws Exception {
        given(userService.getAuthenticatedUser()).willReturn(new User());
        given(suggestionService.sendSuggestion(any(), any())).willReturn(suggestionDTO);

        ResultActions response = mockMvc.perform(post("/sugerencias/enviar-sugerencia")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(suggestionDTO)));

        response.andExpect(status().isCreated());
    }

    /**
     * Método de prueba. Enviar una sugerencia de libro con datos inválidos
     */
    @Test
    public void SuggestionController_SendSuggestion_InvalidSuggestion() throws Exception {
        given(userService.getAuthenticatedUser()).willReturn(new User());
        suggestionDTO.setAuthor("");
        suggestionDTO.setTitle("");
        given(suggestionService.sendSuggestion(any(), any())).willReturn(suggestionDTO);

        ResultActions response = mockMvc.perform(post("/sugerencias/enviar-sugerencia")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(suggestionDTO)));

        response.andExpect(status().isBadRequest());
    }

    /**
     * Método de prueba. Actualiza el estado de una sugerencia
     */
    @Test
    public void SuggestionController_UpdateStatusSuggestion_ReturnUpdated() throws Exception {
        given(userService.getAuthenticatedUser()).willReturn(new User());
        given(suggestionService.updateStatusSuggestion(any(), anyInt(), any())).willReturn(suggestionDTO);

        ResultActions response = mockMvc.perform(put("/sugerencias/1?newStatus=0"));

        response.andExpect(status().isOk());
    }

    /**
     * Método de prueba. Devolver todas las sugerencias
     */
    @Test
    public void SuggestionController_GetAllSuggestions_ReturnSuggestions() throws Exception {
        given(userService.getAuthenticatedUser()).willReturn(new User());
        given(suggestionService.getAllSuggestions(anyInt(), anyInt(), any())).willReturn(suggestionsPage);

        ResultActions response = mockMvc.perform(get("/sugerencias"));

        response.andExpect(status().isOk());
    }

    /**
     * Método de prueba. Devolver las sugerencias con un estado concreto
     */
    @Test
    public void SuggestionController_GetSuggestionsWithStatus_ReturnSuggestions() throws Exception {
        given(userService.getAuthenticatedUser()).willReturn(new User());
        given(suggestionService.getSuggestionsWithStatus(anyInt(), anyInt(), anyInt(), any())).willReturn(suggestionsPage);

        ResultActions response = mockMvc.perform(get("/sugerencias/estado?status=0"));

        response.andExpect(status().isOk());
    }

    /**
     * Método de prueba. Devolver una sugerencia
     */
    @Test
    public void SuggestionController_GetSuggestion_ReturnSuggestion() throws Exception {
        given(userService.getAuthenticatedUser()).willReturn(new User());
        given(suggestionService.getSuggestion(any(), any())).willReturn(suggestionDTO);

        ResultActions response = mockMvc.perform(get("/sugerencias/1"));

        response.andExpect(status().isOk());
    }

    /**
     * Método de prueba. Usuario no autenticado
     */
    @Test
    public void SuggestionController_UnathenticatedUser() throws Exception {
        given(userService.getAuthenticatedUser()).willReturn(null);

        ResultActions response = mockMvc.perform(get("/sugerencias/1"));

        response.andExpect(status().isForbidden());
    }
}
