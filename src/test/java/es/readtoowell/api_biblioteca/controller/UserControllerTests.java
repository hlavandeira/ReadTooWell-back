package es.readtoowell.api_biblioteca.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.readtoowell.api_biblioteca.config.security.JwtFilter;
import es.readtoowell.api_biblioteca.controller.user.UserController;
import es.readtoowell.api_biblioteca.model.DTO.user.UpdateProfileDTO;
import es.readtoowell.api_biblioteca.model.DTO.user.UserDTO;
import es.readtoowell.api_biblioteca.model.DTO.user.UserFavoritesDTO;
import es.readtoowell.api_biblioteca.model.entity.User;
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

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Clase de pruebas para el controlador de usuarios.
 */
@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class UserControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private UserService userService;
    @MockitoBean
    private JwtFilter jwtFilter;
    @Autowired
    private ObjectMapper objectMapper;

    private Page<UserDTO> usersPage;
    private List<UserDTO> usersList;
    private UserDTO userDTO;
    private UserFavoritesDTO userFavoritesDTO;

    @BeforeEach
    public void init() {
        userDTO = new UserDTO();
        userDTO.setUsername("NombreUsuario");
        userDTO.setEmail("correo@correo.es");
        userDTO.setPassword("Contraseña123_");
        userDTO.setRole(0);

        usersList = new ArrayList<>();
        usersList.add(userDTO);
        usersList.add(userDTO);
        usersList.add(userDTO);

        usersPage = new PageImpl<>(usersList);

        userFavoritesDTO = new UserFavoritesDTO();
    }

    /**
     * Método de prueba. Devolver todos los usuarios
     */
    @Test
    public void UserController_GetUsers_ReturnUsers() throws Exception {
        given(userService.getAuthenticatedUser()).willReturn(new User());
        given(userService.getAllUsers(anyInt(), anyInt())).willReturn(usersPage);

        ResultActions response = mockMvc.perform(get("/usuarios"));

        response.andExpect(status().isOk());
    }

    /**
     * Método de prueba. Devolver un usuario
     */
    @Test
    public void UserController_GetUser_ReturnUser() throws Exception {
        given(userService.getAuthenticatedUser()).willReturn(new User());
        given(userService.getUser(any())).willReturn(userDTO);

        ResultActions response = mockMvc.perform(get("/usuarios/1"));

        response.andExpect(status().isOk());
    }

    /**
     * Método de prueba. Crear un usuario con datos válidos
     */
    @Test
    public void UserController_CreateUser_ReturnCreated() throws Exception {
        given(userService.getAuthenticatedUser()).willReturn(new User());
        given(userService.createUser(any())).willReturn(userDTO);

        ResultActions response = mockMvc.perform(post("/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO)));

        response.andExpect(status().isCreated());
    }

    /**
     * Método de prueba. Crear un usuario con datos inválidos
     */
    @Test
    public void UserController_CreateUser_InvalidUser() throws Exception {
        given(userService.getAuthenticatedUser()).willReturn(new User());
        userDTO.setUsername("");
        userDTO.setEmail("correoInvalido");
        userDTO.setPassword("invalid");
        userDTO.setRole(70);
        given(userService.createUser(any())).willReturn(userDTO);

        ResultActions response = mockMvc.perform(post("/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO)));

        response.andExpect(status().isBadRequest());
    }

    /**
     * Método de prueba. Eliminar un usuario
     */
    @Test
    public void UserController_DeleteUser_ReturnDeleted() throws Exception {
        given(userService.getAuthenticatedUser()).willReturn(new User());
        given(userService.deleteUser(any())).willReturn(userDTO);

        ResultActions response = mockMvc.perform(delete("/usuarios/1"));

        response.andExpect(status().isOk());
    }

    /**
     * Método de prueba. Actualizar un usuario con datos válidos
     */
    @Test
    public void UserController_UpdateUser_ReturnUpdated() throws Exception {
        given(userService.getAuthenticatedUser()).willReturn(new User());
        given(userService.updateUser(any(), any())).willReturn(userDTO);

        ResultActions response = mockMvc.perform(put("/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO)));

        response.andExpect(status().isOk());
    }

    /**
     * Método de prueba. Actualizar un usuario con datos inválidos
     */
    @Test
    public void UserController_UpdateUser_InvalidUser() throws Exception {
        given(userService.getAuthenticatedUser()).willReturn(new User());
        userDTO.setUsername("");
        userDTO.setEmail("correoInvalido");
        userDTO.setPassword("invalid");
        userDTO.setRole(70);
        given(userService.updateUser(any(), any())).willReturn(userDTO);

        ResultActions response = mockMvc.perform(put("/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO)));

        response.andExpect(status().isBadRequest());
    }

    /**
     * Método de prueba. Actualizar el perfil de un usuario con datos válidos
     */
    @Test
    public void UserController_UpdateUserProfile_ReturnUpdated() throws Exception {
        UpdateProfileDTO profile = new UpdateProfileDTO();
        profile.setProfileName("Nombre perfil");

        given(userService.getAuthenticatedUser()).willReturn(new User());
        given(userService.updateUserProfile(any(), any())).willReturn(userDTO);

        ResultActions response = mockMvc.perform(put("/usuarios/perfil")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(profile)));

        response.andExpect(status().isOk());
    }

    /**
     * Método de prueba. Actualizar el perfil de un usuario con datos inválidos
     */
    @Test
    public void UserController_UpdateUserProfile_InvalidProfileData() throws Exception {
        UpdateProfileDTO profile = new UpdateProfileDTO();
        profile.setProfileName("Nombre de perfil invalido (demasiado largo)");

        given(userService.getAuthenticatedUser()).willReturn(new User());
        given(userService.updateUserProfile(any(), any())).willReturn(userDTO);

        ResultActions response = mockMvc.perform(put("/usuarios/perfil")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(profile)));

        response.andExpect(status().isBadRequest());
    }

    /**
     * Método de prueba. Devolver los usuario seguidos de un usuario
     */
    @Test
    public void UserController_GetFollows_ReturnUsers() throws Exception {
        given(userService.getAuthenticatedUser()).willReturn(new User());
        given(userService.getFollows(any())).willReturn(usersList);

        ResultActions response = mockMvc.perform(get("/usuarios/1/seguidos"));

        response.andExpect(status().isOk());
    }

    /**
     * Método de prueba. Devolver los usuarios que siguen a otro
     */
    @Test
    public void UserController_GetFollowers_ReturnUsers() throws Exception {
        given(userService.getAuthenticatedUser()).willReturn(new User());
        given(userService.getFollowers(any())).willReturn(usersList);

        ResultActions response = mockMvc.perform(get("/usuarios/1/seguidores"));

        response.andExpect(status().isOk());
    }

    /**
     * Método de prueba. Seguir a un usuario
     */
    @Test
    public void UserController_FollowUser_ReturnUser() throws Exception {
        given(userService.getAuthenticatedUser()).willReturn(new User());
        given(userService.followUser(any(), any())).willReturn(userDTO);

        ResultActions response = mockMvc.perform(post("/usuarios/seguir/3"));

        response.andExpect(status().isOk());
    }

    /**
     * Método de prueba. Dejar de seguir a un usuario
     */
    @Test
    public void UserController_UnfollowUser_ReturnUser() throws Exception {
        given(userService.getAuthenticatedUser()).willReturn(new User());
        given(userService.unfollowUser(any(), any())).willReturn(userDTO);

        ResultActions response = mockMvc.perform(delete("/usuarios/dejar-seguir/3"));

        response.andExpect(status().isOk());
    }

    /**
     * Método de prueba. Buscar usuarios
     */
    @Test
    public void UserController_SearchUsers_ReturnUsers() throws Exception {
        given(userService.getAuthenticatedUser()).willReturn(new User());
        given(userService.searchUsers(any(), anyInt(), anyInt())).willReturn(usersPage);

        ResultActions response = mockMvc.perform(get("/usuarios/buscar?searchString=Búsqueda de usuarios"));

        response.andExpect(status().isOk());
    }

    /**
     * Método de prueba. Actualizar géneros favoritos del usuario
     */
    @Test
    public void UserController_UpdateFavoriteGenres_ReturnFavorites() throws Exception {
        given(userService.getAuthenticatedUser()).willReturn(new User());
        given(userService.getFavorites(any())).willReturn(userFavoritesDTO);

        ResultActions response = mockMvc.perform(put("/usuarios/generos-favoritos?genreIds=8"));

        response.andExpect(status().isOk());
    }

    /**
     * Método de prueba. Actualizar libros favoritos del usuario
     */
    @Test
    public void UserController_UpdateFavoriteBooks_ReturnFavorites() throws Exception {
        given(userService.getAuthenticatedUser()).willReturn(new User());
        given(userService.getFavorites(any())).willReturn(userFavoritesDTO);

        ResultActions response = mockMvc.perform(put("/usuarios/libros-favoritos?bookIds=8"));

        response.andExpect(status().isOk());
    }

    /**
     * Método de prueba. Devolver los favoritos de un usuario
     */
    @Test
    public void UserController_GetFavorites_ReturnFavorites() throws Exception {
        given(userService.getAuthenticatedUser()).willReturn(new User());
        given(userService.getFavorites(any())).willReturn(userFavoritesDTO);

        ResultActions response = mockMvc.perform(get("/usuarios/1/favoritos"));

        response.andExpect(status().isOk());
    }

    /**
     * Método de prueba. Verificar si un usuario tiene rol de administrador
     */
    @Test
    public void UserController_VerifyAdmin_ReturnBoolean() throws Exception {
        given(userService.getAuthenticatedUser()).willReturn(new User());
        given(userService.verifyAdmin(any())).willReturn(false);

        ResultActions response = mockMvc.perform(get("/usuarios/verificar-admin"));

        response.andExpect(status().isOk());
    }

    /**
     * Método de prueba. Devolver los autores registrados
     */
    @Test
    public void UserController_GetAuthors_ReturnUsers() throws Exception {
        List<UserDTO> list = new ArrayList<>();
        list.add(new UserDTO()); list.add(new UserDTO()); list.add(new UserDTO());
        Page<UserDTO> page = new PageImpl<>(list);

        given(userService.getAuthenticatedUser()).willReturn(new User());
        given(userService.getAuthors(anyInt(), anyInt())).willReturn(page);

        ResultActions response = mockMvc.perform(get("/usuarios/autores"));

        response.andExpect(status().isOk());
    }

    /**
     * Método de prueba. Usuario no autenticado
     */
    @Test
    public void UserController_UnauthenticatedUser() throws Exception {
        given(userService.getAuthenticatedUser()).willReturn(null);

        ResultActions response = mockMvc.perform(get("/usuarios/1"));

        response.andExpect(status().isForbidden());
    }
}
