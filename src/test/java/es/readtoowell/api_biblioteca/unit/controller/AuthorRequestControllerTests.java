package es.readtoowell.api_biblioteca.unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.readtoowell.api_biblioteca.config.security.JwtFilter;
import es.readtoowell.api_biblioteca.controller.user.AuthorRequestController;
import es.readtoowell.api_biblioteca.model.DTO.AuthorRequestDTO;
import es.readtoowell.api_biblioteca.model.entity.User;
import es.readtoowell.api_biblioteca.service.user.AuthorRequestService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Clase de pruebas para el controlador de solicitudes de autor.
 */
@WebMvcTest(controllers = AuthorRequestController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class AuthorRequestControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private AuthorRequestService requestService;
    @MockitoBean
    private UserService userService;
    @MockitoBean
    private JwtFilter jwtFilter;
    @Autowired
    private ObjectMapper objectMapper;

    private AuthorRequestDTO requestDTO;
    private Page<AuthorRequestDTO> requestsPage;

    @BeforeEach
    public void init() {
        requestDTO = new AuthorRequestDTO();
        requestDTO.setName("Nombre");
        requestDTO.setBiography("Biografía");

        List<AuthorRequestDTO> list = new ArrayList<>();
        list.add(requestDTO);
        list.add(requestDTO);
        list.add(requestDTO);

        requestsPage = new PageImpl<>(list);
    }

    /**
     * Método de prueba. Enviar solicitud de autor con datos válidos
     */
    @Test
    public void AuthorRequestController_SendAuthorRequest_ReturnCreated() throws Exception {
        given(userService.getAuthenticatedUser()).willReturn(new User());
        given(requestService.sendAuthorRequest(any(), any())).willReturn(requestDTO);

        ResultActions response = mockMvc.perform(post("/solicitud-autor")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)));

        response.andExpect(status().isCreated());
    }

    /**
     * Método de prueba. Enviar solicitud de autor con datos inválidos
     */
    @Test
    public void AuthorRequestController_SendAuthorRequest_InvalidRequest() throws Exception {
        given(userService.getAuthenticatedUser()).willReturn(new User());
        requestDTO.setName("");
        requestDTO.setBiography("");
        given(requestService.sendAuthorRequest(any(), any())).willReturn(requestDTO);

        ResultActions response = mockMvc.perform(post("/solicitud-autor")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)));

        response.andExpect(status().isBadRequest());
    }

    /**
     * Método de prueba. Actualizar estado de una sugerencia de autor
     */
    @Test
    public void AuthorRequestController_UpdateStatusRequest_ReturnUpdated() throws Exception {
        given(userService.getAuthenticatedUser()).willReturn(new User());
        given(requestService.updateStatusRequest(any(), anyInt(), any())).willReturn(requestDTO);

        ResultActions response = mockMvc.perform(put("/solicitud-autor/1?newStatus=0"));

        response.andExpect(status().isOk());
    }

    /**
     * Método de prueba. Devolver todas las solicitudes
     */
    @Test
    public void AuthorRequestController_GetAllRequests_ReturnRequests() throws Exception {
        given(userService.getAuthenticatedUser()).willReturn(new User());
        given(requestService.getAllRequests(anyInt(), anyInt(), any())).willReturn(requestsPage);

        ResultActions response = mockMvc.perform(get("/solicitud-autor"));

        response.andExpect(status().isOk());
    }

    /**
     * Método de prueba. Devolver todas las solicitudes con un estado concreto
     */
    @Test
    public void AuthorRequestController_GetRequestsWithStatus_ReturnRequests() throws Exception {
        given(userService.getAuthenticatedUser()).willReturn(new User());
        given(requestService.getRequestsWithStatus(anyInt(), anyInt(), anyInt(), any())).willReturn(requestsPage);

        ResultActions response = mockMvc.perform(get("/solicitud-autor/estado"));

        response.andExpect(status().isOk());
    }

    /**
     * Método de prueba. Devolver una solicitud
     */
    @Test
    public void AuthorRequestController_GetRequest_ReturnRequest() throws Exception {
        given(userService.getAuthenticatedUser()).willReturn(new User());
        given(requestService.getRequest(any(), any())).willReturn(requestDTO);

        ResultActions response = mockMvc.perform(get("/solicitud-autor/1"));

        response.andExpect(status().isOk());
    }

    /**
     * Método de prueba. Comprobar si existen solicitudes pendientes para un usuario
     */
    @Test
    public void AuthorRequestController_CheckIfPendingRequest_ReturnBoolean() throws Exception {
        given(userService.getAuthenticatedUser()).willReturn(new User());
        given(requestService.checkIfPendingRequest(any())).willReturn(false);

        ResultActions response = mockMvc.perform(get("/solicitud-autor/comprobar-pendiente"));

        response.andExpect(status().isOk());
    }

    /**
     * Método de prueba. Usuario no autenticado
     */
    @Test
    public void AuthorRequestController_UnauthenticatedUser() throws Exception {
        given(userService.getAuthenticatedUser()).willReturn(null);

        ResultActions response = mockMvc.perform(get("/solicitud-autor/1"));

        response.andExpect(status().isForbidden());
    }
}
