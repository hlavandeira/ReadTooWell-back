package es.readtoowell.api_biblioteca.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.readtoowell.api_biblioteca.config.security.JwtUtil;
import es.readtoowell.api_biblioteca.model.DTO.book.BookListDTO;
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
import java.util.stream.Collectors;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Clase para las pruebas de integración sobre las recomendaciones y listas
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class RecommendationsIntegrationTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private JwtUtil jwtUtil;

    private String token;
    /**
     * Lista de IDs para los libros de la lista y los géneros asociados a esta
     */
    private final List<Long> genreAndBookIds = List.of(1L, 2L, 3L, 4L);

    private String getTokenForUser() {
        return jwtUtil.generateToken("pruebasintegracion@email.com");
    }

    @BeforeEach
    void init() {
        token = getTokenForUser();
    }

    /**
     * Método de prueba. Se crea una nueva lista, se añaden varios libros, y se consultan las
     * recomendaciones para la lista creada
     */
    @Test
    public void Recommendations_AllActionsCorrect() throws Exception {
        // Crear una nueva lista
        BookListDTO list = new BookListDTO();
        list.setName("Lista pruebas");
        list.setDescription("Descripción para la lista de pruebas");

        String urlCreateList = "/listas?" + genreAndBookIds.stream()
                .map(id -> "genreIds=" + id)
                .collect(Collectors.joining("&"));

        MvcResult listaCreada = mockMvc.perform(post(urlCreateList)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(list))
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isCreated())
                .andReturn();

        BookListDTO lista = objectMapper.readValue(listaCreada.getResponse().getContentAsString(), BookListDTO.class);
        Long idLista = lista.getId();

        // Añadir libros a la lista
        for (Long libro : genreAndBookIds) {
            mockMvc.perform(post("/listas/" + idLista + "/añadir-libro/" + libro)
                            .header("Authorization", "Bearer " + token))
                    .andExpect(status().isOk());
        }

        // Consultamos las recomendaciones para esa lista
        mockMvc.perform(get("/recomendaciones/lista/" + idLista)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(Matchers.greaterThan(0)));
    }

    /**
     * Método de prueba. Crear una lista con un nombre inválido
     */
    @Test
    public void Recommendations_CreateList_InvalidName_ReturnBadRequest() throws Exception {
        BookListDTO list = new BookListDTO();
        list.setName("");
        list.setDescription("Descripción para la lista de pruebas");

        mockMvc.perform(post("/listas?genreIds=1L")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(list))
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isBadRequest());
    }

    /**
     * Método de prueba. Crear una lista con una descripción inválida
     */
    @Test
    public void Recommendations_CreateList_InvalidDescription_ReturnBadRequest() throws Exception {
        BookListDTO list = new BookListDTO();
        list.setName("Lista pruebas");
        list.setDescription("Descripción para la lista de pruebas".repeat(60));

        mockMvc.perform(post("/listas?genreIds=1L")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(list))
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isBadRequest());
    }

    /**
     * Método de prueba. Añadir un libro a una lista inexistente
     */
    @Test
    public void Recommendations_AddBookToList_UnexistentList_ReturnNotFound() throws Exception {
        mockMvc.perform(post("/listas/" + 9999999L + "/añadir-libro/" + 1L)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound());
    }

    /**
     * Método de prueba. Añadir un libro inexistente a una lista
     */
    @Test
    public void Recommendations_AddBookToList_UnexistentBook_ReturnNotFound() throws Exception {
        BookListDTO list = new BookListDTO();
        list.setName("Lista pruebas");
        list.setDescription("Descripción para la lista de pruebas");

        String urlCreateList = "/listas?" + genreAndBookIds.stream()
                .map(id -> "genreIds=" + id)
                .collect(Collectors.joining("&"));

        MvcResult listaCreada = mockMvc.perform(post(urlCreateList)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(list))
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isCreated())
                .andReturn();

        BookListDTO lista = objectMapper.readValue(listaCreada.getResponse().getContentAsString(), BookListDTO.class);
        Long idLista = lista.getId();

        mockMvc.perform(post("/listas/" + idLista + "/añadir-libro/" + 9999999L)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound());
    }

    /**
     * Método de prueba. Añadir un libro a una lista de otro usuario
     */
    @Test
    public void Recommendations_AddBookToList_OtherUsersList_ReturnForbidden() throws Exception {
        mockMvc.perform(post("/listas/" + 1L + "/añadir-libro/" + 1L)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }

    /**
     * Método de prueba. Recibir recomendaciones a partir de una lista inexistente
     */
    @Test
    public void Recommendations_GetRecommendationsByList_UnexistentList_ReturnNotFound() throws Exception {
        mockMvc.perform(get("/recomendaciones/lista/" + 999999L)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound());
    }

    /**
     * Método de prueba. Recibir recomendaciones a partir de una lista de otro usuario
     */
    @Test
    public void Recommendations_GetRecommendationsByList_OtherUsersList_ReturnForbidden() throws Exception {
        mockMvc.perform(get("/recomendaciones/lista/" + 1L)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }

    /**
     * Método de prueba. Recibir recomendaciones a partir de una lista vacía
     */
    @Test
    public void Recommendations_GetRecommendationsByList_EmptyList_ReturnNotRecommendations() throws Exception {
        BookListDTO list = new BookListDTO();
        list.setName("Lista pruebas");
        list.setDescription("Descripción para la lista de pruebas");

        MvcResult listaCreada = mockMvc.perform(post("/listas?genreIds=")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(list))
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isCreated())
                .andReturn();

        BookListDTO lista = objectMapper.readValue(listaCreada.getResponse().getContentAsString(), BookListDTO.class);
        Long idLista = lista.getId();

        mockMvc.perform(get("/recomendaciones/lista/" + idLista)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }
}
