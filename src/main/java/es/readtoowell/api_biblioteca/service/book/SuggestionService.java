package es.readtoowell.api_biblioteca.service.book;

import es.readtoowell.api_biblioteca.mapper.SuggestionMapper;
import es.readtoowell.api_biblioteca.model.DTO.SuggestionDTO;
import es.readtoowell.api_biblioteca.model.entity.Suggestion;
import es.readtoowell.api_biblioteca.model.entity.User;
import es.readtoowell.api_biblioteca.model.enums.SuggestionStatus;
import es.readtoowell.api_biblioteca.repository.book.SuggestionRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;

@Service
public class SuggestionService {
    @Autowired
    private SuggestionRepository suggestionRepository;
    @Autowired
    private SuggestionMapper suggestionMapper;

    /**
     * Añade una sugerencia de libro por parte de un usuario.
     *
     * @param suggestionDTO DTO con los datos de la sugerencia
     * @param user Usuario que envía la sugerencia
     * @return DTO con los datos de la sugerencia enviada
     */
    public SuggestionDTO sendSuggestion(SuggestionDTO suggestionDTO, User user) {
        Suggestion suggestion = new Suggestion();

        suggestion.setTitle(suggestionDTO.getTitle());
        suggestion.setAuthor(suggestionDTO.getAuthor());
        suggestion.setPublicationYear(suggestionDTO.getPublicationYear());
        suggestion.setStatus(SuggestionStatus.PENDING.getValue());
        suggestion.setActive(true);
        suggestion.setDateSent(Date.valueOf(LocalDate.now()));
        suggestion.setUser(user);

        suggestion = suggestionRepository.save(suggestion);

        return suggestionMapper.toDTO(suggestion);
    }

    /**
     * Cambia el estado de una sugerencia.
     *
     * @param idSuggestion ID de la sugerencia
     * @param newStatus Nuevo estado para la sugerencia
     * @return DTO con los datos de la sugerencia actualizada
     * @throws ValidationException El nuevo estado es inválido
     * @throws EntityNotFoundException La sugerencia no existe
     */
    public SuggestionDTO updateStatusSuggestion(Long idSuggestion, int newStatus) {
        if (newStatus < 0 || newStatus > 3) {
            throw new ValidationException("El nuevo estado de la sugerencia es inválido.");
        }

        Suggestion suggestion = suggestionRepository.findById(idSuggestion)
                .orElseThrow(() -> new EntityNotFoundException("La sugerencia con ID " + idSuggestion + " no existe."));

        suggestion.setStatus(newStatus);

        suggestion = suggestionRepository.save(suggestion);

        return suggestionMapper.toDTO(suggestion);
    }

    /**
     * Devuelve todas las sugerencias.
     *
     * @param page Número de la página que se quiere devolver
     * @param size Tamaño de la página
     * @return Página con las sugerencias como DTOs
     */
    public Page<SuggestionDTO> getAllSuggestions(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "fechaEnviada"));

        return suggestionRepository.findAll(pageable).map(suggestionMapper::toDTO);
    }

    /**
     * Devuelve todas las sugerencias con un estado específico.
     *
     * @param page Número de la página que se quiere devolver
     * @param size Tamaño de la página
     * @param status Estado de las sugerencias para filtrar
     * @return Página con las sugerencias filtradas como DTOs
     * @throws ValidationException El estado de sugerencia es inválido
     */
    public Page<SuggestionDTO> getSuggestionsWithStatus(int page, int size, int status) {
        if (status < 0 || status > 3) {
            throw new ValidationException("El estado es inválido.");
        }
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "fechaEnviada"));

        return suggestionRepository.findByStatus(status, pageable).map(suggestionMapper::toDTO);
    }

    /**
     * Devuelve una sugerencia.
     *
     * @param idSuggestion ID de la sugerencia
     * @return DTO con los datos de la sugerencia
     * @throws EntityNotFoundException La sugerencia no existe
     */
    public SuggestionDTO getSuggestion(Long idSuggestion) {
        Suggestion suggestion = suggestionRepository.findById(idSuggestion)
                .orElseThrow(() -> new EntityNotFoundException("La sugerencia con ID " + idSuggestion + " no existe."));

        return suggestionMapper.toDTO(suggestion);
    }
}
