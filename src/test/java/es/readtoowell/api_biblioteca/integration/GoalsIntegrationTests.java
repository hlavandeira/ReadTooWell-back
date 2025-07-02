package es.readtoowell.api_biblioteca.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.readtoowell.api_biblioteca.config.security.JwtUtil;
import es.readtoowell.api_biblioteca.model.DTO.GoalDTO;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Clase para las pruebas de integración respectivas a objetivos de lectura
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class GoalsIntegrationTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private JwtUtil jwtUtil;

    private String token;

    private String getTokenForUser() {
        return jwtUtil.generateToken("pruebasintegracion@email.com");
    }

    @BeforeEach
    void init() {
        token = getTokenForUser();
    }

    /**
     * Método de prueba. Se crean dos objetivos de lectura, se marca un libro como "Leído",
     * se comprueba que los objetivos se actualizaron, y se elimina uno
     */
    @Test
    public void Goals_AllActionsCorrect() throws Exception {
        Long idBook = 5L;

        // Crear objetivo mensual de 1 libro
        GoalDTO nuevoObjetivo = new GoalDTO();
        nuevoObjetivo.setDuration("Mensual");
        nuevoObjetivo.setType("Libros");
        nuevoObjetivo.setAmount(1);

        mockMvc.perform(post("/objetivos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nuevoObjetivo))
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isCreated());

        // Crear objetivo anual de 10000 páginas
        nuevoObjetivo = new GoalDTO();
        nuevoObjetivo.setDuration("Anual");
        nuevoObjetivo.setType("Páginas");
        nuevoObjetivo.setAmount(10000);

        MvcResult segundoObjetivo = mockMvc.perform(post("/objetivos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nuevoObjetivo))
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isCreated())
                .andReturn();

        GoalDTO objetivoPaginas = objectMapper.readValue(segundoObjetivo.getResponse().getContentAsString(), GoalDTO.class);

        // Consultar objetivos en curso
        mockMvc.perform(get("/objetivos/en-curso")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));

        // Añadir libro a biblioteca y cambiar el estado de lectura a "Leído"
        mockMvc.perform(post("/biblioteca/" + idBook)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isCreated());

        mockMvc.perform(put("/biblioteca/" + idBook + "/estado")
                        .param("estado", "2") // Estado leído = 2
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());

        // Comprobar que el objetivo de 1 libro pasó al listado de objetivos terminados
        mockMvc.perform(get("/objetivos/terminados")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));

        // Comprobar que queda uno en curso
        mockMvc.perform(get("/objetivos/en-curso")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                // Comprobar que se ha actualizado (lleva >0 páginas)
                .andExpect(jsonPath("$[0].currentAmount").value(Matchers.not(0)));

        // Eliminar el objetivo que queda en curso
        mockMvc.perform(delete("/objetivos/" + objetivoPaginas.getId())
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());

        // Comprobar que no hay objetivos en curso
        mockMvc.perform(get("/objetivos/en-curso")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    /**
     * Método de prueba. Crear un objetivo con tipo inválido
     */
    @Test
    public void Goals_CreateGoal_InvalidGoalType_ReturnNotFound() throws Exception {
        GoalDTO nuevoObjetivo = new GoalDTO();
        nuevoObjetivo.setDuration("Mensual");
        nuevoObjetivo.setType("TipoInválido");
        nuevoObjetivo.setAmount(1);

        mockMvc.perform(post("/objetivos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nuevoObjetivo))
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound());
    }

    /**
     * Método de prueba. Crear un objetivo con duración inválida
     */
    @Test
    public void Goals_CreateGoal_InvalidGoalDuration_ReturnNotFound() throws Exception {
        GoalDTO nuevoObjetivo = new GoalDTO();
        nuevoObjetivo.setDuration("DuraciónInválida");
        nuevoObjetivo.setType("Páginas");
        nuevoObjetivo.setAmount(1);

        mockMvc.perform(post("/objetivos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nuevoObjetivo))
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound());
    }

    /**
     * Método de prueba. Crear un objetivo repetido
     */
    @Test
    public void Goals_CreateGoal_RepeatedGoal_ReturnBadRequest() throws Exception {
        GoalDTO nuevoObjetivo = new GoalDTO();
        nuevoObjetivo.setDuration("Mensual");
        nuevoObjetivo.setType("Libros");
        nuevoObjetivo.setAmount(1);

        // Crear objetivo
        mockMvc.perform(post("/objetivos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nuevoObjetivo))
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isCreated());

        // Intentar crear exactamente el mismo
        mockMvc.perform(post("/objetivos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nuevoObjetivo))
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isBadRequest());
    }

    /**
     * Método de prueba. Actualizar el estado de lectura de un libro a un estado inválido
     */
    @Test
    public void Goals_UpdateReadingStatus_InvalidStatus_ReturnBadRequest() throws Exception {
        Long idBook = 5L;

        mockMvc.perform(post("/biblioteca/" + idBook)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isCreated());

        mockMvc.perform(put("/biblioteca/" + idBook + "/estado")
                        .param("estado", "100") // Estado 100 inválido
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isBadRequest());
    }

    /**
     * Método de prueba. Actualizar el estado de lectura de un libro que no está en la biblioteca
     */
    @Test
    public void Goals_UpdateReadingStatus_BookNotInLibrary_ReturnBadRequest() throws Exception {
        Long idBook = 10L;

        mockMvc.perform(put("/biblioteca/" + idBook + "/estado")
                        .param("estado", "2")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isBadRequest());
    }

    /**
     * Método de prueba. Actualizar el estado de lectura de un libro inexistente
     */
    @Test
    public void Goals_UpdateReadingStatus_UnexistentBook_ReturnNotFound() throws Exception {
        Long idBook = 10934L;

        mockMvc.perform(put("/biblioteca/" + idBook + "/estado")
                        .param("estado", "2")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound());
    }

    /**
     * Método de prueba. Intentar borrar un objetivo inexistente
     */
    @Test
    public void Goals_DeleteGoal_UnexistentGoal_ReturnNotFound() throws Exception {
        Long idGoal = 44444L;

        mockMvc.perform(delete("/objetivos/" + idGoal)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound());
    }
}