package es.readtoowell.api_biblioteca.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.readtoowell.api_biblioteca.model.DTO.user.LoginDTO;
import es.readtoowell.api_biblioteca.repository.user.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Clase para las pruebas de integración respectivas al inicio de sesión de un usuario
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class LoginIntegrationTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Método de prueba. Inicio de sesión de un usuario con credenciales correctas
     */
    @Test
    void Login_ValidCredentials_ReturnTokenAndUser() throws Exception {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail("pruebasintegracion@email.com");
        loginDTO.setPassword("Contraseña123_");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.user.email").value("pruebasintegracion@email.com"))
                .andExpect(jsonPath("$.user.username").value("usuarioPruebas"))
                .andExpect(jsonPath("$.user.profileName").value("Usuario13"));
    }

    /**
     * Método de prueba. Inicio de sesión de un usuario con contraseña incorrecta
     */
    @Test
    void Login_WrongPassword_ReturnBadRequest() throws Exception {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail("pruebasintegracion@email.com");
        loginDTO.setPassword("ContraseñaMAL");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("La contraseña es incorrecta"));
    }

    /**
     * Método de prueba. Inicio de sesión de un usuario con correo electrónico inexistente
     */
    @Test
    void Login_UnexistentEmail_ReturnUnauthorized() throws Exception {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail("inexistente@readtoowell.com");
        loginDTO.setPassword("Contraseña123_");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("No existe ningún usuario con el correo proporcionado"));
    }
}
