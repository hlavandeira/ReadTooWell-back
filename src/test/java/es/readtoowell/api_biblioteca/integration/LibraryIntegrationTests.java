package es.readtoowell.api_biblioteca.integration;

import es.readtoowell.api_biblioteca.config.security.JwtUtil;
import jakarta.transaction.Transactional;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Clase para las pruebas de integración sobre añadir/eliminar libros de la biblioteca
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class LibraryIntegrationTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JwtUtil jwtUtil;

    private String token;
    private final Long idBook = 1L;
    private final Long invalidBookId = 999L;

    private String getTokenForUser() {
        return jwtUtil.generateToken("pruebasintegracion@email.com");
    }

    @BeforeEach
    void init() {
        token = getTokenForUser();
    }

    /**
     * Método de prueba. Se consultan los detalles de un libro, se añade a la biblioteca,
     * se comprueba que se añadió y se elimina
     */
    @Test
    public void Library_AllActionsCorrect() throws Exception {
        // Consultar detalles del libro
        mockMvc.perform(get("/libros/" + idBook + "/detalles")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.book.id").value(idBook));

        // Añadirlo a biblioteca
        mockMvc.perform(post("/biblioteca/" + idBook)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.book.id").value(idBook))
                .andExpect(jsonPath("$.readingStatus").value("0"));

        // Ver libros pendientes
        mockMvc.perform(get("/biblioteca")
                        .param("status", "0") // Estado 0 = Libros pendientes
                        .param("page", "0")
                        .param("size", "10")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[*].book.id").value(Matchers.hasItem(idBook.intValue())));

        // Eliminar libro de biblioteca
        mockMvc.perform(delete("/biblioteca/" + idBook)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.book.id").value(idBook));

        // Comprobar que ya no está
        mockMvc.perform(get("/biblioteca")
                        .param("status", "0")
                        .param("page", "0")
                        .param("size", "10")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[*].book.id").value(Matchers.not(Matchers.hasItem(idBook.intValue()))));
    }

    /**
     * Método de prueba. Consultar detalles de un libro inexistente
     */
    @Test
    public void Library_GetDetails_UnexistentBook_ReturnNotFound() throws Exception {
        mockMvc.perform(get("/libros/" + invalidBookId + "/detalles")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound());
    }

    /**
     * Método de prueba. Añadir a al biblioteca un libro inexistente
     */
    @Test
    public void Library_AddToLibrary_UnexistentBook_ReturnNotFound() throws Exception {
        mockMvc.perform(post("/biblioteca/" + invalidBookId)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound());
    }

    /**
     * Método de prueba. Añadir a la biblioteca un libro que ya está añadido
     */
    @Test
    public void Library_AddToLibrary_RepeatedBook_ReturnBadRequest() throws Exception {
        // Primero añadir el libro
        mockMvc.perform(post("/biblioteca/" + idBook)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isCreated());

        // Intentar añadirlo de nuevo
        mockMvc.perform(post("/biblioteca/" + idBook)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isBadRequest());
    }

    /**
     * Método de prueba. Eliminar de la biblioteca un libro inexistente
     */
    @Test
    public void Library_DeleteFromLibrary_UnexistentBook_ReturnNotFound() throws Exception {
        mockMvc.perform(delete("/biblioteca/" + invalidBookId)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound());
    }

    /**
     * Método de prueba. Eliminar de la biblioteca un libro que no pertenece a esta
     */
    @Test
    public void Library_DeleteFromLibrary_BookNotInLibrary_ReturnBadRequest() throws Exception {
        mockMvc.perform(delete("/biblioteca/" + idBook)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isBadRequest());
    }

    /**
     * Método de prueba. Consultar libros de la biblioteca con un estado de lectura inexistente
     */
    @Test
    public void Library_GetLibraryWithStatus_InvalidReadingStatus_ReturnBadRequest() throws Exception {
        mockMvc.perform(get("/biblioteca")
                        .param("status", "99") // Estado de lectura 99 no existe
                        .param("page", "0")
                        .param("size", "10")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isBadRequest());
    }
}

