package es.readtoowell.api_biblioteca.unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.readtoowell.api_biblioteca.config.security.JwtFilter;
import es.readtoowell.api_biblioteca.controller.book.BookListController;
import es.readtoowell.api_biblioteca.model.DTO.book.BookListDTO;
import es.readtoowell.api_biblioteca.model.DTO.book.BookListDetailsDTO;
import es.readtoowell.api_biblioteca.model.entity.User;
import es.readtoowell.api_biblioteca.service.book.BookListService;
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
 * Clase de pruebas para el controlador de listas de libros.
 */
@WebMvcTest(controllers = BookListController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class BookListControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private BookListService listService;
    @MockitoBean
    private UserService userService;
    @MockitoBean
    private JwtFilter jwtFilter;
    @Autowired
    private ObjectMapper objectMapper;

    private Page<BookListDTO> listsPage;
    private List<BookListDTO> listsList;
    private BookListDTO list;

    @BeforeEach
    public void init() {
        listsList = new ArrayList<>();
        list = new BookListDTO();
        listsList.add(list);
        listsList.add(list);
        listsList.add(list);

        listsPage = new PageImpl<>(listsList);
    }

    /**
     * Método de prueba. Devolver todas las listas de un usuario
     */
    @Test
    public void BookListController_GetListsByUser_ReturnLists() throws Exception {
        given(userService.getAuthenticatedUser()).willReturn(new User());
        given(listService.getListsByUser(any(), anyInt(), anyInt())).willReturn(listsPage);

        ResultActions response = mockMvc.perform(get("/listas"));

        response.andExpect(status().isOk());
    }

    /**
     * Método de prueba. Devolver los detalles de una lista
     */
    @Test
    public void BookListController_GetListDetails_ReturnList() throws Exception {
        BookListDetailsDTO listDetailsDTO = new BookListDetailsDTO();

        given(userService.getAuthenticatedUser()).willReturn(new User());
        given(listService.getListDetails(any(), any(), anyInt(), anyInt())).willReturn(listDetailsDTO);

        ResultActions response = mockMvc.perform(get("/listas/1"));

        response.andExpect(status().isOk());
    }

    /**
     * Método de prueba. Crear una lista con datos válidos
     */
    @Test
    public void BookListController_CreateList_ReturnCreated() throws Exception {
        given(userService.getAuthenticatedUser()).willReturn(new User());
        list.setName("Nombre de la lista");
        given(listService.createList(any(), any(), any())).willReturn(list);

        ResultActions response = mockMvc.perform(post("/listas?genreIds=4")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(list)));

        response.andExpect(status().isCreated());
    }

    /**
     * Método de prueba. Crear una lista con datos inválidos
     */
    @Test
    public void BookListController_CreateList_InvalidList() throws Exception {
        given(userService.getAuthenticatedUser()).willReturn(new User());
        list.setName("");
        given(listService.createList(any(), any(), any())).willReturn(list);

        ResultActions response = mockMvc.perform(post("/listas?genreIds=4")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(list)));

        response.andExpect(status().isBadRequest());
    }

    /**
     * Método de prueba. Actualizar una lista con datos válidos
     */
    @Test
    public void BookListController_UpdateList_ReturnUpdated() throws Exception {
        given(userService.getAuthenticatedUser()).willReturn(new User());
        list.setName("Nombre de la lista");
        given(listService.updateList(any(), any(), any(), any())).willReturn(list);

        ResultActions response = mockMvc.perform(put("/listas/1?genreIds=4")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(list)));

        response.andExpect(status().isOk());
    }

    /**
     * Método de prueba. Actualizar una lista con datos inválidos
     */
    @Test
    public void BookListController_UpdateList_InvalidList() throws Exception {
        given(userService.getAuthenticatedUser()).willReturn(new User());
        list.setName("");
        given(listService.updateList(any(), any(), any(), any())).willReturn(list);

        ResultActions response = mockMvc.perform(put("/listas/1?genreIds=4")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(list)));

        response.andExpect(status().isBadRequest());
    }

    /**
     * Método de prueba. Eliminar una lista
     */
    @Test
    public void BookListController_DeleteList_ReturnDeleted() throws Exception {
        given(userService.getAuthenticatedUser()).willReturn(new User());
        given(listService.deleteList(any(), any())).willReturn(list);

        ResultActions response = mockMvc.perform(delete("/listas/1"));

        response.andExpect(status().isOk());
    }

    /**
     * Método de prueba. Añadir un libro a una lista
     */
    @Test
    public void BookListController_AddBookToList_ReturnList() throws Exception {
        given(userService.getAuthenticatedUser()).willReturn(new User());
        given(listService.addBookToList(any(), any(), any())).willReturn(list);

        ResultActions response = mockMvc.perform(post("/listas/1/añadir-libro/3"));

        response.andExpect(status().isOk());
    }

    /**
     * Método de prueba. Eliminar un libro de una lista
     */
    @Test
    public void BookListController_DeleteBookFromList_ReturnList() throws Exception {
        given(userService.getAuthenticatedUser()).willReturn(new User());
        given(listService.deleteBookFromList(any(), any(), any())).willReturn(list);

        ResultActions response = mockMvc.perform(delete("/listas/1/eliminar-libro/3"));

        response.andExpect(status().isOk());
    }

    /**
     * Método de prueba. Devolver las listas que no contienen un libro concreto
     */
    @Test
    public void BookListController_GetListsWithoutBook_ReturnLists() throws Exception {
        given(userService.getAuthenticatedUser()).willReturn(new User());
        given(listService.getListsWithoutBook(any(), any(), anyInt(), anyInt())).willReturn(listsPage);

        ResultActions response = mockMvc.perform(get("/listas/1/otras-listas"));

        response.andExpect(status().isOk());
    }

    /**
     * Método de prueba. Devolver las listas excluyendo las vacías
     */
    @Test
    public void BookListController_GetAllListsExcludingEmpty_ReturnLists() throws Exception {
        given(userService.getAuthenticatedUser()).willReturn(new User());
        given(listService.getAllListsExcludingEmpty(any())).willReturn(listsList);

        ResultActions response = mockMvc.perform(get("/listas/todas-no-vacias"));

        response.andExpect(status().isOk());
    }

    /**
     * Método de prueba. Usuario no autenticado
     */
    @Test
    public void BookListController_UnauthenticatedUser() throws Exception {
        given(userService.getAuthenticatedUser()).willReturn(null);

        ResultActions response = mockMvc.perform(get("/listas/todas-no-vacias"));

        response.andExpect(status().isForbidden());
    }
}
