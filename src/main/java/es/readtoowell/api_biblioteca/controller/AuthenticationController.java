package es.readtoowell.api_biblioteca.controller;

import es.readtoowell.api_biblioteca.model.DTO.LoginDTO;
import es.readtoowell.api_biblioteca.model.DTO.RegisterDTO;
import es.readtoowell.api_biblioteca.model.DTO.RegisteredDTO;
import es.readtoowell.api_biblioteca.service.AuthenticationService;
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

    @PostMapping("/login")
    public ResponseEntity<LoginDTO> login(@Valid @RequestBody LoginDTO login) {
        String token = authService.login(login);
        login.setToken(token);

        if (token != null) {
            return ResponseEntity.ok(login);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<RegisteredDTO> register(@Valid @RequestBody RegisterDTO register) {
        RegisteredDTO user = authService.register(register);

        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }
}
