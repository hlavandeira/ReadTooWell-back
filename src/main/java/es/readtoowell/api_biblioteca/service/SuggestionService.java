package es.readtoowell.api_biblioteca.service;

import es.readtoowell.api_biblioteca.config.security.CustomUserDetails;
import es.readtoowell.api_biblioteca.mapper.SuggestionMapper;
import es.readtoowell.api_biblioteca.model.DTO.SuggestionDTO;
import es.readtoowell.api_biblioteca.model.Suggestion;
import es.readtoowell.api_biblioteca.model.User;
import es.readtoowell.api_biblioteca.model.enums.SuggestionStatus;
import es.readtoowell.api_biblioteca.repository.SuggestionRepository;
import es.readtoowell.api_biblioteca.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.sql.Date;
import java.time.LocalDate;

@Service
public class SuggestionService {
    @Autowired
    private SuggestionRepository suggestionRepository;
    @Autowired
    private SuggestionMapper suggestionMapper;
    @Autowired
    private UserRepository userRepository;

    public SuggestionDTO sendSuggestion(SuggestionDTO suggestionDTO) {
        Suggestion suggestion = new Suggestion();

        suggestion.setTitulo(suggestionDTO.getTitulo());
        suggestion.setAutor(suggestionDTO.getAutor());
        suggestion.setAñoPublicacion(suggestionDTO.getAñoPublicacion());
        suggestion.setEstado(SuggestionStatus.PENDIENTE.getValue());
        suggestion.setActivo(true);
        suggestion.setFechaEnviada(Date.valueOf(LocalDate.now()));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new EntityNotFoundException("Usuario con ID " +
                        userDetails.getId() + " no encontrado"));

        suggestion.setUsuario(user);

        suggestion = suggestionRepository.save(suggestion);

        return suggestionMapper.toDTO(suggestion);
    }

    public SuggestionDTO updateStatusSuggestion(Long idSuggestion, int newStatus) {
        if (newStatus < 0 || newStatus > 3) {
            throw new ValidationException("El nuevo estado de la sugerencia es inválido.");
        }

        Suggestion suggestion = suggestionRepository.findById(idSuggestion)
                .orElseThrow(() -> new EntityNotFoundException("La sugerencia con ID " + idSuggestion + " no existe."));

        suggestion.setEstado(newStatus);

        suggestion = suggestionRepository.save(suggestion);

        return suggestionMapper.toDTO(suggestion);
    }
}
