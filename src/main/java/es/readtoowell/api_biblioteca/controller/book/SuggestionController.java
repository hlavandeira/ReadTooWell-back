package es.readtoowell.api_biblioteca.controller.book;

import es.readtoowell.api_biblioteca.model.DTO.SuggestionDTO;
import es.readtoowell.api_biblioteca.model.entity.User;
import es.readtoowell.api_biblioteca.service.book.SuggestionService;
import es.readtoowell.api_biblioteca.service.user.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador que gestiona las peticiones HTTP relativas a las sugerencias de libros.
 */
@RestController
@RequestMapping("/sugerencias")
public class SuggestionController {
    @Autowired
    private SuggestionService suggestionService;
    @Autowired
    private UserService userService;

    /**
     * Añade una sugerencia de libro por parte de un usuario.
     *
     * @param suggestion DTO con los datos de la sugerencia
     * @return DTO con los datos de la sugerencia enviada
     */
    @PostMapping("/enviar-sugerencia")
    public ResponseEntity<SuggestionDTO> sendSuggestion(@Valid @RequestBody SuggestionDTO suggestion) {
        User user = userService.getAuthenticatedUser();
        if (user == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        SuggestionDTO dto = suggestionService.sendSuggestion(suggestion, user);

        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    /**
     * Cambia el estado de una sugerencia.
     *
     * @param idSuggestion ID de la sugerencia
     * @param newStatus Nuevo estado para la sugerencia
     * @return DTO con los datos de la sugerencia actualizada
     */
    @PutMapping("/{idSuggestion}")
    public ResponseEntity<SuggestionDTO> updateStatusSuggestion(@PathVariable Long idSuggestion,
                                                                @RequestParam int newStatus) {
        User user = userService.getAuthenticatedUser();
        if (user == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        SuggestionDTO dto = suggestionService.updateStatusSuggestion(idSuggestion, newStatus, user);

        return ResponseEntity.ok(dto);
    }

    /**
     * Devuelve todas las sugerencias.
     *
     * @param page Número de la página que se quiere devolver
     * @param size Tamaño de la página
     * @return Página con las sugerencias como DTOs
     */
    @GetMapping
    public ResponseEntity<Page<SuggestionDTO>> getAllSuggestions(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        User user = userService.getAuthenticatedUser();
        if (user == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Page<SuggestionDTO> suggestions = suggestionService.getAllSuggestions(page, size, user);

        return ResponseEntity.ok(suggestions);
    }

    /**
     * Devuelve todas las sugerencias con un estado específico.
     *
     * @param page Número de la página que se quiere devolver
     * @param size Tamaño de la página
     * @param status Estado de las sugerencias para filtrar
     * @return Página con las sugerencias filtradas como DTOs
     */
    @GetMapping("/estado")
    public ResponseEntity<Page<SuggestionDTO>> getSuggestionsWithStatus(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "status", defaultValue = "0") int status) {

        User user = userService.getAuthenticatedUser();
        if (user == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Page<SuggestionDTO> suggestions = suggestionService.getSuggestionsWithStatus(page, size, status, user);

        return ResponseEntity.ok(suggestions);
    }

    /**
     * Devuelve una sugerencia.
     *
     * @param idSuggestion ID de la sugerencia
     * @return DTO con los datos de la sugerencia
     */
    @GetMapping("/{idSuggestion}")
    public ResponseEntity<SuggestionDTO> getSuggestion(@PathVariable Long idSuggestion) {
        User user = userService.getAuthenticatedUser();
        if (user == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        SuggestionDTO suggestion = suggestionService.getSuggestion(idSuggestion, user);

        return ResponseEntity.ok(suggestion);
    }
}
