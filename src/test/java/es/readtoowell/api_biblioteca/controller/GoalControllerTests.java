package es.readtoowell.api_biblioteca.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.readtoowell.api_biblioteca.config.security.JwtFilter;
import es.readtoowell.api_biblioteca.controller.goal.GoalController;
import es.readtoowell.api_biblioteca.model.DTO.GoalDTO;
import es.readtoowell.api_biblioteca.model.entity.User;
import es.readtoowell.api_biblioteca.service.goal.GoalService;
import es.readtoowell.api_biblioteca.service.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
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
 * Clase de pruebas para el controlador de objetivos.
 */
@WebMvcTest(controllers = GoalController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class GoalControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private GoalService goalService;
    @MockitoBean
    private UserService userService;
    @MockitoBean
    private JwtFilter jwtFilter;
    @Autowired
    private ObjectMapper objectMapper;

    private List<GoalDTO> goalsList;

    @BeforeEach
    public void init() {
        goalsList = new ArrayList<>();
        goalsList.add(new GoalDTO()); goalsList.add(new GoalDTO()); goalsList.add(new GoalDTO());
    }

    /**
     * Método de prueba. Devolver los objetivos de lectura en curso
     */
    @Test
    public void GoalController_GetGoalsInProgress_ReturnGoals() throws Exception {
        given(userService.getAuthenticatedUser()).willReturn(new User());
        given(goalService.getGoalsInProgress(any())).willReturn(goalsList);

        ResultActions response = mockMvc.perform(get("/objetivos/en-curso"));

        response.andExpect(status().isOk());
    }

    /**
     * Método de prueba. Devolver los objetivos de lectura finalizados
     */
    @Test
    public void GoalController_GetFinishedGoals_ReturnGoals() throws Exception {
        given(userService.getAuthenticatedUser()).willReturn(new User());
        given(goalService.getFinishedGoals(any())).willReturn(goalsList);

        ResultActions response = mockMvc.perform(get("/objetivos/terminados"));

        response.andExpect(status().isOk());
    }

    /**
     * Método de prueba. Devolver los objetivos de lectura finalizados en el año actual
     */
    @Test
    public void GoalController_GetFinishedGoalsActualYear_ReturnGoals() throws Exception {
        given(userService.getAuthenticatedUser()).willReturn(new User());
        given(goalService.getFinishedGoalsActualYear(any())).willReturn(goalsList);

        ResultActions response = mockMvc.perform(get("/objetivos/terminados/año-actual"));

        response.andExpect(status().isOk());
    }

    /**
     * Método de prueba. Crear un objetivo de lectura con datos válidos
     */
    @Test
    public void GoalController_CreateGoal_ReturnCreated() throws Exception {
        GoalDTO goalDTO = new GoalDTO();
        goalDTO.setAmount(11);
        goalDTO.setType("tipo");
        goalDTO.setDuration("duracion");

        given(userService.getAuthenticatedUser()).willReturn(new User());
        given(goalService.createGoal(any(), any())).willReturn(goalDTO);

        ResultActions response = mockMvc.perform(post("/objetivos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(goalDTO)));

        response.andExpect(status().isCreated());
    }

    /**
     * Método de prueba. Crear un objetivo de lectura con datos inválidos
     */
    @Test
    public void GoalController_CreateGoal_InvalidGoal() throws Exception {
        GoalDTO goalDTO = new GoalDTO();
        goalDTO.setAmount(0);
        goalDTO.setType(null);
        goalDTO.setDuration(null);

        given(userService.getAuthenticatedUser()).willReturn(new User());
        given(goalService.createGoal(any(), any())).willReturn(goalDTO);

        ResultActions response = mockMvc.perform(post("/objetivos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(goalDTO)));

        response.andExpect(status().isBadRequest());
    }

    /**
     * Método de prueba. Eliminar un objetivo de lectura
     */
    @Test
    public void GoalController_DeleteGoal_ReturnsDeleted() throws Exception {
        given(userService.getAuthenticatedUser()).willReturn(new User());
        given(goalService.deleteGoal(any(), any())).willReturn(new GoalDTO());

        ResultActions response = mockMvc.perform(delete("/objetivos/1"));

        response.andExpect(status().isOk());
    }

    /**
     * Método de prueba. Eliminar un objetivo de lectura inexistente
     */
    @Test
    public void GoalController_DeleteGoal_NonexistentGoal() throws Exception {
        given(userService.getAuthenticatedUser()).willReturn(new User());
        given(goalService.deleteGoal(any(), any())).willReturn(null);

        ResultActions response = mockMvc.perform(delete("/objetivos/2"));

        response.andExpect(status().isNotFound());
    }

    /**
     * Método de prueba. Usuario no autenticado
     */
    @Test
    public void GoalController_UnauthenticatedUser() throws Exception {
        given(userService.getAuthenticatedUser()).willReturn(null);

        ResultActions response = mockMvc.perform(delete("/objetivos/2"));

        response.andExpect(status().isForbidden());
    }
}
