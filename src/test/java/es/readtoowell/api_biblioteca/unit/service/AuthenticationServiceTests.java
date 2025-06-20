package es.readtoowell.api_biblioteca.unit.service;

import es.readtoowell.api_biblioteca.config.security.JwtUtil;
import es.readtoowell.api_biblioteca.mapper.UserMapper;
import es.readtoowell.api_biblioteca.model.DTO.user.AuthenticatedUserDTO;
import es.readtoowell.api_biblioteca.model.DTO.user.LoginDTO;
import es.readtoowell.api_biblioteca.model.DTO.user.RegisterDTO;
import es.readtoowell.api_biblioteca.model.DTO.user.UserDTO;
import es.readtoowell.api_biblioteca.model.entity.User;
import es.readtoowell.api_biblioteca.repository.user.UserRepository;
import es.readtoowell.api_biblioteca.service.auth.AuthenticationService;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Clase de pruebas para el servicio de autenticación de usuarios.
 */
@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTests {
    @Mock
    private UserRepository userRepository;
    @Mock
    private JwtUtil jwtUtil;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserMapper userMapper;
    @InjectMocks
    private AuthenticationService authenticationService;

    private RegisterDTO registerDTO;
    private LoginDTO loginDTO;
    private User user;

    @BeforeEach
    public void init() {
        registerDTO = new RegisterDTO();
        registerDTO.setPassword("Contraseña123_");
        registerDTO.setConfirmPassword("Contraseña123_");
        registerDTO.setUsername("usuario");
        registerDTO.setEmail("email");

        loginDTO = new LoginDTO();
        loginDTO.setEmail("email");
        loginDTO.setPassword("Contraseña123_");

        user = new User();
        user.setEmail("email");
    }

    /**
     * Método de prueba. Registrar a un usuario con datos válidos.
     */
    @Test
    public void AuthenticationService_Register_ReturnsAuthenticatedUserDto() {
        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());
        when(userRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(jwtUtil.generateToken(any())).thenReturn("Token para usuario");
        when(userMapper.toDTO(any())).thenReturn(new UserDTO());

        AuthenticatedUserDTO result = authenticationService.register(registerDTO);

        assertNotNull(result);
        assertNotNull(result.getToken());
        assertNotNull(result.getUser());
    }

    /**
     * Método de prueba. Registrar a un usuario con correo existente.
     */
    @Test
    public void AuthenticationService_Register_RepeatedEmail() {
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));

        ValidationException exception = Assertions.assertThrows(
                ValidationException.class,
                () -> authenticationService.register(registerDTO)
        );

        assertEquals("El correo ya está en uso.", exception.getMessage());
    }

    /**
     * Método de prueba. Registrar a un usuario con confirmación de contraseña incorrecta.
     */
    @Test
    public void AuthenticationService_Register_PasswordsDoNotMatch() {
        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());

        registerDTO.setConfirmPassword("otra contraseña");

        ValidationException exception = Assertions.assertThrows(
                ValidationException.class,
                () -> authenticationService.register(registerDTO)
        );

        assertEquals("Las contraseñas no coinciden.", exception.getMessage());
    }

    /**
     * Método de prueba. Iniciar sesión con datos correctos
     */
    @Test
    public void AuthenticationService_Login_ReturnsAuthenticatedUserDto() {
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(any(), any())).thenReturn(true);
        when(jwtUtil.generateToken(any())).thenReturn("Token para usuario");
        when(userMapper.toDTO(any())).thenReturn(new UserDTO());

        AuthenticatedUserDTO result = authenticationService.login(loginDTO);

        assertNotNull(result);
        assertNotNull(result.getToken());
        assertNotNull(result.getUser());
    }

    /**
     * Método de prueba. Iniciar sesión con correo inexistente.
     */
    @Test
    public void AuthenticationService_Login_NonexistentEmail() {
        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());

        ValidationException exception = Assertions.assertThrows(
                ValidationException.class,
                () -> authenticationService.login(loginDTO)
        );

        assertEquals("No existe ningún usuario con el correo proporcionado", exception.getMessage());
    }

    /**
     * Método de prueba. Iniciar sesión con contraseña incorrecta.
     */
    @Test
    public void AuthenticationService_Login_IncorrectPassword() {
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(any(), any())).thenReturn(false);

        ValidationException exception = Assertions.assertThrows(
                ValidationException.class,
                () -> authenticationService.login(loginDTO)
        );

        assertEquals("La contraseña es incorrecta", exception.getMessage());
    }
}
