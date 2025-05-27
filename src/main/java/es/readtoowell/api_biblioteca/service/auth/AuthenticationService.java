package es.readtoowell.api_biblioteca.service.auth;

import es.readtoowell.api_biblioteca.mapper.UserMapper;
import es.readtoowell.api_biblioteca.model.DTO.user.LoginDTO;
import es.readtoowell.api_biblioteca.model.DTO.user.RegisterDTO;
import es.readtoowell.api_biblioteca.model.DTO.user.AuthenticatedUserDTO;
import es.readtoowell.api_biblioteca.model.entity.User;
import es.readtoowell.api_biblioteca.model.enums.Role;
import es.readtoowell.api_biblioteca.repository.user.UserRepository;
import es.readtoowell.api_biblioteca.config.security.JwtUtil;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private JwtUtil jwtUtil;

    /**
     * Registro de un nuevo usuario.
     *
     * @param registerDTO DTO con los datos de registro del usuario
     * @return DTO con los datos del usuario registrado
     * @throws ValidationException El correo está en uso o las contraseñas no coinciden
     */
    public AuthenticatedUserDTO register(RegisterDTO registerDTO) {
        if (userRepository.findByEmail(registerDTO.getEmail()).isPresent()) {
            throw new ValidationException("El correo ya está en uso.");
        }
        if (!registerDTO.getPassword().equals(registerDTO.getConfirmPassword())) {
            throw new ValidationException("Las contraseñas no coinciden.");
        }

        User user = new User();
        user.setUsername(registerDTO.getUsername().replaceAll("\\s+", ""));
        user.setEmail(registerDTO.getEmail().toLowerCase().trim());

        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));

        user.setProfilePic("https://res.cloudinary.com/dfrgrfw4c/image/" +
                "upload/v1741801696/readtoowell/profilepics/pfp.jpg"); // Foto de perfil por defecto
        user.setRole(Role.USER.getValue());
        user.setProfileName(registerDTO.getUsername().toLowerCase());

        user = userRepository.save(user);

        String token = jwtUtil.generateToken(user.getEmail());

        AuthenticatedUserDTO dto = new AuthenticatedUserDTO();
        dto.setUser(userMapper.toDTO(user));
        dto.setToken(token);

        return dto;
    }

    /**
     * Inicio de sesión para un usuario.
     *
     * @param loginDTO Datos del inicio de sesión
     * @return DTO con los datos del usuario auenticado
     * @throws ValidationException El usuario no existe o la contraseña es incorrecta
     */
    public AuthenticatedUserDTO login(LoginDTO loginDTO) {
        Optional<User> userOpt = userRepository.findByEmail(loginDTO.getEmail().toLowerCase());

        if (userOpt.isEmpty()) {
            throw new ValidationException("No existe ningún usuario con el correo proporcionado");
        }
        User user = userOpt.get();

        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new ValidationException("Los datos son incorrectos");
        }

        String token = jwtUtil.generateToken(user.getEmail());

        AuthenticatedUserDTO authUser = new AuthenticatedUserDTO();
        authUser.setUser(userMapper.toDTO(user));
        authUser.setToken(token);

        return authUser;
    }

    /**
     * Valida el token para comprobar si la sesión es válida o inválida.
     *
     * @param token Token a validar
     * @return 'true' si el token es válido, 'false' en caso contrario
     */
    public boolean validateToken(String token) {
        return jwtUtil.validateToken(token);
    }
}
