package es.readtoowell.api_biblioteca.unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.readtoowell.api_biblioteca.config.security.JwtFilter;
import es.readtoowell.api_biblioteca.controller.library.UserBookFormatController;
import es.readtoowell.api_biblioteca.model.DTO.book.FormatDTO;
import es.readtoowell.api_biblioteca.model.entity.User;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Clase de prueba para el controlador de formatos de libros de usuarios.
 */
@WebMvcTest(controllers = UserBookFormatController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class UserBookFormatControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private UserBookFormatService formatService;
    @MockitoBean
    private UserService userService;
    @MockitoBean
    private JwtFilter jwtFilter;
    @Autowired
    private ObjectMapper objectMapper;

    private List<FormatDTO> formatsList;

    @BeforeEach
    public void init() {
        formatsList = new ArrayList<>();
        formatsList.add(new FormatDTO());
        formatsList.add(new FormatDTO());
        formatsList.add(new FormatDTO());
    }

    /**
     * Método de prueba. Devolver los formatos de un libro de un usuario
     */
    @Test
    public void UserBookFormatController_GetFormatsForUserBook_ReturnFormats() throws Exception {
        given(userService.getAuthenticatedUser()).willReturn(new User());
        given(formatService.getFormatsForUserBook(any(), any())).willReturn(formatsList);

        ResultActions response = mockMvc.perform(get("/biblioteca/1/formatos"));

        response.andExpect(status().isOk());
    }

    /**
     * Método de prueba. Devolver los formatos de un libro de un usuario cuando no hay ninguno asociado
     */
    @Test
    public void UserBookFormatController_GetFormatsForUserBook_NoFormats() throws Exception {
        given(userService.getAuthenticatedUser()).willReturn(new User());
        given(formatService.getFormatsForUserBook(any(), any())).willReturn(new ArrayList<>());

        ResultActions response = mockMvc.perform(get("/biblioteca/1/formatos"));

        response.andExpect(status().isNoContent());
    }

    /**
     * Método de prueba. Añadir un formato a un libro
     */
    @Test
    public void UserBookFormatController_AddFormatToBook_ReturnFormats() throws Exception {
        given(userService.getAuthenticatedUser()).willReturn(new User());
        given(formatService.addFormatToBook(any(), any(), any())).willReturn(new FormatDTO());

        ResultActions response = mockMvc.perform(post("/biblioteca/1/formatos/3"));

        response.andExpect(status().isOk());
    }

    /**
     * Método de prueba. Eliminar un formato de un libro
     */
    @Test
    public void UserBookFormatController_DeleteFormatFromBook_ReturnFormats() throws Exception {
        given(userService.getAuthenticatedUser()).willReturn(new User());
        given(formatService.addFormatToBook(any(), any(), any())).willReturn(new FormatDTO());

        ResultActions response = mockMvc.perform(delete("/biblioteca/1/formatos/3"));

        response.andExpect(status().isOk());
    }

    /**
     * Método de prueba. Usuario no autenticado
     */
    @Test
    public void UserBookFormatController_UnauthenticatedUser() throws Exception {
        given(userService.getAuthenticatedUser()).willReturn(null);

        ResultActions response = mockMvc.perform(delete("/biblioteca/1/formatos/3"));

        response.andExpect(status().isForbidden());
    }
}
