package es.readtoowell.api_biblioteca.controller;

import es.readtoowell.api_biblioteca.model.DTO.SuggestionDTO;
import es.readtoowell.api_biblioteca.model.User;
import es.readtoowell.api_biblioteca.service.SuggestionService;
import es.readtoowell.api_biblioteca.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sugerencias")
public class SuggestionController {
    @Autowired
    private SuggestionService suggestionService;
    @Autowired
    private UserService userService;

    @PostMapping("/enviar-sugerencia")
    public ResponseEntity<SuggestionDTO> sendSuggestion(@Valid @RequestBody SuggestionDTO suggestion) {
        User user = userService.getAuthenticatedUser();
        if (user == null) {
            throw new AccessDeniedException("Usuario no autenticado.");
        }

        SuggestionDTO dto = suggestionService.sendSuggestion(suggestion, user);

        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @PutMapping("/{idSuggestion}")
    public ResponseEntity<SuggestionDTO> updateStatusSuggestion(@PathVariable Long idSuggestion,
                                                                @RequestParam int newStatus) {
        SuggestionDTO dto = suggestionService.updateStatusSuggestion(idSuggestion, newStatus);

        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public ResponseEntity<Page<SuggestionDTO>> getAllSuggestions(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        Page<SuggestionDTO> suggestions = suggestionService.getAllSuggestions(page, size);

        return ResponseEntity.ok(suggestions);
    }

    @GetMapping("/estado")
    public ResponseEntity<Page<SuggestionDTO>> getSuggestionsWithStatus(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "status", defaultValue = "0") int status) {

        Page<SuggestionDTO> suggestions = suggestionService.getSuggestionsWithStatus(page, size, status);

        return ResponseEntity.ok(suggestions);
    }

    @GetMapping("/{idSuggestion}")
    public ResponseEntity<SuggestionDTO> getSuggestion(@PathVariable Long idSuggestion) {
        SuggestionDTO suggestion = suggestionService.getSuggestion(idSuggestion);

        return ResponseEntity.ok(suggestion);
    }
}
