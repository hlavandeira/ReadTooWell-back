package es.readtoowell.api_biblioteca.controller;

import es.readtoowell.api_biblioteca.model.DTO.SuggestionDTO;
import es.readtoowell.api_biblioteca.service.SuggestionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sugerencias")
public class SuggestionController {
    @Autowired
    private SuggestionService suggestionService;

    @PostMapping("/enviar-sugerencia")
    public ResponseEntity<SuggestionDTO> sendSuggestion(@Valid @RequestBody SuggestionDTO suggestion) {
        SuggestionDTO dto = suggestionService.sendSuggestion(suggestion);

        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{idSuggestion}")
    public ResponseEntity<SuggestionDTO> updateStatusSuggestion(@PathVariable Long idSuggestion,
                                                                @RequestParam int newStatus) {
        SuggestionDTO dto = suggestionService.updateStatusSuggestion(idSuggestion, newStatus);

        return ResponseEntity.ok(dto);
    }
}
