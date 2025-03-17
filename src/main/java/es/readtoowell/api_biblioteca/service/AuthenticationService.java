package es.readtoowell.api_biblioteca.service;

import es.readtoowell.api_biblioteca.mapper.UserMapper;
import es.readtoowell.api_biblioteca.model.DTO.LoginDTO;
import es.readtoowell.api_biblioteca.model.DTO.RegisterDTO;
import es.readtoowell.api_biblioteca.model.DTO.RegisteredDTO;
import es.readtoowell.api_biblioteca.model.User;
import es.readtoowell.api_biblioteca.model.enums.Role;
import es.readtoowell.api_biblioteca.repository.UserRepository;
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

    public RegisteredDTO register(RegisterDTO registerDTO) {
        if (userRepository.findByCorreo(registerDTO.getCorreo()).isPresent()) {
            throw new ValidationException("El correo ya está en uso.");
        }

        if (!registerDTO.getContraseña().equals(registerDTO.getConfirmarContraseña())) {
            throw new ValidationException("Las contraseñas no coinciden.");
        }

        User user = new User();
        user.setNombreUsuario(registerDTO.getNombreUsuario());
        user.setCorreo(registerDTO.getCorreo());

        user.setContraseña(passwordEncoder.encode(registerDTO.getContraseña()));

        user.setActivo(true);
        user.setFotoPerfil("https://res.cloudinary.com/dfrgrfw4c/image/" +
                "upload/v1741801696/readtoowell/profilepics/pfp.jpg");
        user.setRol(Role.USER.getValue());
        user.setNombrePerfil(registerDTO.getNombreUsuario().toLowerCase());

        user = userRepository.save(user);

        String token = jwtUtil.generateToken(user.getCorreo());

        RegisteredDTO dto = new RegisteredDTO();
        dto.setUser(userMapper.toDTO(user));
        dto.setToken(token);

        return dto;
    }

    public String login(LoginDTO loginDTO) {
        Optional<User> userOpt = userRepository.findByCorreo(loginDTO.getCorreo());

        if (userOpt.isEmpty()) {
            throw new ValidationException("No existe ningún usuario con el correo proporcionado");
        }
        User user = userOpt.get();

        if (!passwordEncoder.matches(loginDTO.getContraseña(), user.getContraseña())) {
            throw new ValidationException("Los datos son incorrectos");
        }

        return jwtUtil.generateToken(user.getCorreo());
    }
}
