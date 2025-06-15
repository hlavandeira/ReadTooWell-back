package es.readtoowell.api_biblioteca.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.readtoowell.api_biblioteca.config.security.JwtFilter;
import es.readtoowell.api_biblioteca.controller.auth.AuthenticationController;
import es.readtoowell.api_biblioteca.model.DTO.user.AuthenticatedUserDTO;
import es.readtoowell.api_biblioteca.model.DTO.user.LoginDTO;
import es.readtoowell.api_biblioteca.model.DTO.user.RegisterDTO;
import es.readtoowell.api_biblioteca.model.DTO.user.UserDTO;
import es.readtoowell.api_biblioteca.service.auth.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Clase de pruebas para el controlador de autenticación de usuarios.
 */
@WebMvcTest(controllers = AuthenticationController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class AuthenticationControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private AuthenticationService authenticationService;
    @MockitoBean
    private JwtFilter jwtFilter;
    @Autowired
    private ObjectMapper objectMapper;

    private LoginDTO loginDTO;
    private RegisterDTO registerDTO;
    private AuthenticatedUserDTO authenticatedUserDTO;

    @BeforeEach
    public void init() {
        loginDTO = new LoginDTO();
        loginDTO.setEmail("hector@readtoowell.com");
        loginDTO.setPassword("Contraseña123_");

        registerDTO = new RegisterDTO();
        registerDTO.setUsername("hector");
        registerDTO.setEmail("hector@readtoowell.com");
        registerDTO.setPassword("Contraseña123_");
        registerDTO.setConfirmPassword("Contraseña123_");

        UserDTO user = new UserDTO();
        user.setEmail("hector@readtoowell.com");
        user.setPassword("Contraseña123_");

        authenticatedUserDTO = new AuthenticatedUserDTO();
        authenticatedUserDTO.setUser(user);
        authenticatedUserDTO.setToken("jwt-token-test");
    }

    /**
     * Método de prueba. Registro de un usuario con datos correctos
     */
    @Test
    public void AuthenticationController_Register_ReturnLogged() throws Exception {
        given(authenticationService.register(ArgumentMatchers.any())).willReturn(authenticatedUserDTO);

        ResultActions response = mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerDTO)));

        response.andExpect(status().isCreated());
    }

    /**
     * Método de prueba. Inicio de sesión de un usuario con credenciales correctas
     */
    @Test
    public void AuthenticationController_Login_ReturnLogged() throws Exception {
        given(authenticationService.login(ArgumentMatchers.any())).willReturn(authenticatedUserDTO);

        ResultActions response = mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginDTO)));

        response.andExpect(status().isOk());
    }

    /**
     * Método de prueba. Inicio de sesión de un usuario con credenciales incorrectas
     */
    @Test
    public void AuthenticationController_Login_NonexistentEmail() throws Exception {
        AuthenticatedUserDTO failedLoginDTO = new AuthenticatedUserDTO();
        failedLoginDTO.setToken(null);

        given(authenticationService.login(ArgumentMatchers.any())).willReturn(failedLoginDTO);

        loginDTO.setEmail("noexiste@email.es");
        ResultActions response = mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginDTO)));

        response.andExpect(status().isUnauthorized());
    }

    /**
     * Método de prueba. Validar un token válido
     */
    @Test
    public void AuthenticationController_ValidateToken_ReturnToken() throws Exception {
        given(authenticationService.validateToken(ArgumentMatchers.any())).willReturn(true);

        ResultActions response = mockMvc.perform(get("/auth/validar")
                .header("Authorization", "Bearer valid token"));

        response.andExpect(status().isOk());
    }

    /**
     * Método de prueba. Validar un token inválido
     */
    @Test
    public void AuthenticationController_ValidateToken_InvalidToken() throws Exception {
        given(authenticationService.validateToken(ArgumentMatchers.any())).willReturn(false);

        ResultActions response = mockMvc.perform(get("/auth/validar")
                .header("Authorization", "Bearer invalid token"));

        response.andExpect(status().isUnauthorized());
    }
}
