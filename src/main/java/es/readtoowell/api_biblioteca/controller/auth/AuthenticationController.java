package es.readtoowell.api_biblioteca.controller.auth;

import es.readtoowell.api_biblioteca.model.DTO.LoginDTO;
import es.readtoowell.api_biblioteca.model.DTO.RegisterDTO;
import es.readtoowell.api_biblioteca.model.DTO.AuthenticatedUserDTO;
import es.readtoowell.api_biblioteca.service.auth.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    @Autowired
    private AuthenticationService authService;

    /**
     * Inicio de sesión para un usuario.
     *
     * @param login Datos del inicio de sesión
     * @return Token de sesión para el usuario
     */
    @PostMapping("/login")
    public ResponseEntity<AuthenticatedUserDTO> login(@Valid @RequestBody LoginDTO login) {
        AuthenticatedUserDTO user = authService.login(login);

        if (user.getToken() != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(user);
        }
    }

    /**
     * Registro de un nuevo usuario.
     *
     * @param register DTO con los datos de registro del usuario
     * @return DTO con los datos del usuario registrado
     */
    @PostMapping("/register")
    public ResponseEntity<AuthenticatedUserDTO> register(@Valid @RequestBody RegisterDTO register) {
        AuthenticatedUserDTO user = authService.register(register);

        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }
}
