package es.readtoowell.api_biblioteca.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.readtoowell.api_biblioteca.config.security.JwtUtil;
import es.readtoowell.api_biblioteca.model.DTO.user.UpdateProfileDTO;
import es.readtoowell.api_biblioteca.model.DTO.user.UserDTO;
import jakarta.transaction.Transactional;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Clase para las pruebas de integración sobre el perfil de un usuario y sus seguidos
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ProfileIntegrationTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private JwtUtil jwtUtil;

    private String token;
    private final Long idUsuarioPruebas = 200L;
    private final Long idUsuarioAdmin = 1L;
    private final Long idUsuarioNoExiste = 0L;

    private String getTokenForUser() {
        return jwtUtil.generateToken("pruebasintegracion@email.com");
    }

    @BeforeEach
    void init() {
        token = getTokenForUser();
    }

    /**
     * Método de prueba. Se consulta el perfil del propio usuario, se edita, se ve su listado
     * de seguidos y se deja de seguir al usuario, para después volver a seguirlo
     */
    @Test
    public void Profile_AllActionsCorrect() throws Exception {
        // Consultar el perfil del propio usuario
        mockMvc.perform(get("/usuarios/" + idUsuarioPruebas)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("usuarioPruebas"))
                .andExpect(jsonPath("$.profileName").value("Usuario13"))
                .andExpect(jsonPath("$.biography").value(""));

        // Editar el perfil
        UpdateProfileDTO updateProfile = new UpdateProfileDTO();
        updateProfile.setProfileName("Usuario1989");
        updateProfile.setBiography("Biografía para las pruebas");

        mockMvc.perform(put("/usuarios/perfil")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateProfile))
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.profileName").value("Usuario1989"))
                .andExpect(jsonPath("$.biography").value("Biografía para las pruebas"));

        // Consultar lista de seguidos
        MvcResult seguidos = mockMvc.perform(get("/usuarios/" + idUsuarioPruebas + "/seguidos")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andReturn();

        String json = seguidos.getResponse().getContentAsString();
        UserDTO amigo = objectMapper.readValue(json, new TypeReference<List<UserDTO>>() {}).get(0);

        // Dejar de seguir al usuario y comprobar que no aparece en el listado
        mockMvc.perform(delete("/usuarios/dejar-seguir/" + amigo.getId())
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());

        mockMvc.perform(get("/usuarios/" + idUsuarioPruebas + "/seguidos")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].id").value(Matchers.not(Matchers.hasItem(amigo.getId()))));

        // Volver a seguir al usuario y comprobar que vuelve a aparecer
        mockMvc.perform(post("/usuarios/seguir/" + amigo.getId())
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());

        mockMvc.perform(get("/usuarios/" + idUsuarioPruebas + "/seguidos")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].id").value(Matchers.not(Matchers.hasItem(amigo.getId()))));
    }

    /**
     * Método de prueba. Actualizar el perfil de usuario con nombre de perfil demasiado largo
     */
    @Test
    public void Profile_UpdateUserProfile_InvalidProfileName_ReturnBadRequest() throws Exception {
        UpdateProfileDTO updateProfile = new UpdateProfileDTO();
        updateProfile.setProfileName("Usuario1989".repeat(5));
        updateProfile.setBiography("Biografía para las pruebras");

        mockMvc.perform(put("/usuarios/perfil")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateProfile))
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isBadRequest());
    }

    /**
     * Método de prueba. Actualizar el perfil de usuario con biografía demasiado larga
     */
    @Test
    public void Profile_UpdateUserProfile_InvalidBiography_ReturnBadRequest() throws Exception {
        UpdateProfileDTO updateProfile = new UpdateProfileDTO();
        updateProfile.setProfileName("Usuario1989");
        updateProfile.setBiography("Biografía para las pruebas".repeat(100));

        mockMvc.perform(put("/usuarios/perfil")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateProfile))
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isBadRequest());
    }

    /**
     * Método de prueba. Dejar de seguir a un usuario inexistente
     */
    @Test
    public void Profile_UnfollowUser_UnexistentUser_ReturnNotFound() throws Exception {
        mockMvc.perform(delete("/usuarios/dejar-seguir/" + idUsuarioNoExiste)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound());
    }

    /**
     * Método de prueba. Dejar de seguir a un administrador
     */
    @Test
    public void Profile_UnfollowUser_Admin_ReturnBadRequest() throws Exception {
        mockMvc.perform(delete("/usuarios/dejar-seguir/" + idUsuarioAdmin)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isBadRequest());
    }

    /**
     * Método de prueba. Dejar de seguirse a sí mismo
     */
    @Test
    public void Profile_FollowUser_UnfollowSelf_ReturnNotFound() throws Exception {
        mockMvc.perform(delete("/usuarios/dejar-seguir/" + idUsuarioPruebas)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isBadRequest());
    }

    /**
     * Método de prueba. Seguir a un usuario inexistente
     */
    @Test
    public void Profile_FollowUser_UnexistentUser_ReturnNotFound() throws Exception {
        mockMvc.perform(post("/usuarios/seguir/" + idUsuarioNoExiste)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound());
    }

    /**
     * Método de prueba. Seguir a un administrador
     */
    @Test
    public void Profile_FollowUser_Admin_ReturnBadRequest() throws Exception {
        mockMvc.perform(post("/usuarios/seguir/" + idUsuarioAdmin)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isBadRequest());
    }

    /**
     * Método de prueba. Seguirse a sí mismo
     */
    @Test
    public void Profile_FollowUser_FollowSelf_ReturnNotFound() throws Exception {
        mockMvc.perform(post("/usuarios/seguir/" + idUsuarioPruebas)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isBadRequest());
    }
}
