package es.readtoowell.api_biblioteca.service.book;

import es.readtoowell.api_biblioteca.mapper.SuggestionMapper;
import es.readtoowell.api_biblioteca.model.DTO.SuggestionDTO;
import es.readtoowell.api_biblioteca.model.entity.Suggestion;
import es.readtoowell.api_biblioteca.model.entity.User;
import es.readtoowell.api_biblioteca.model.enums.Role;
import es.readtoowell.api_biblioteca.model.enums.SuggestionStatus;
import es.readtoowell.api_biblioteca.repository.book.SuggestionRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;

/**
 * Servicio encargado de gestionar la lógica relacionada con las sugerencias de libros.
 */
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

        suggestion.setTitle(suggestionDTO.getTitle().trim());
        suggestion.setAuthor(suggestionDTO.getAuthor().trim());
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
     * @throws AccessDeniedException El usuario no es un administrador
     */
    public SuggestionDTO updateStatusSuggestion(Long idSuggestion, int newStatus, User user) {
        if (newStatus < 0 || newStatus > 3) {
            throw new ValidationException("El nuevo estado de la sugerencia es inválido.");
        }
        if (user.getRole() != Role.ADMIN.getValue()) {
            throw new AccessDeniedException("Solo los admins pueden realizar esta acción.");
        }

        Suggestion suggestion = suggestionRepository.findById(idSuggestion)
                .orElseThrow(() -> new EntityNotFoundException("La sugerencia con ID " + idSuggestion + " no existe."));

        suggestion.setStatus(newStatus);

        if (newStatus == SuggestionStatus.ADDED.getValue() || newStatus == SuggestionStatus.REJECTED.getValue()) {
            suggestion.setActive(false);
        }

        suggestion = suggestionRepository.save(suggestion);

        return suggestionMapper.toDTO(suggestion);
    }

    /**
     * Devuelve todas las sugerencias.
     *
     * @param page Número de la página que se quiere devolver
     * @param size Tamaño de la página
     * @return Página con las sugerencias como DTOs
     * @throws AccessDeniedException El usuario no es un administrador
     */
    public Page<SuggestionDTO> getAllSuggestions(int page, int size, User user) {
        if (user.getRole() != Role.ADMIN.getValue()) {
            throw new AccessDeniedException("Solo los admins pueden realizar esta acción.");
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "dateSent"));

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
     * @throws AccessDeniedException El usuario no es un administrador
     */
    public Page<SuggestionDTO> getSuggestionsWithStatus(int page, int size, int status, User user) {
        if (status < 0 || status > 3) {
            throw new ValidationException("El estado es inválido.");
        }
        if (user.getRole() != Role.ADMIN.getValue()) {
            throw new AccessDeniedException("Solo los admins pueden realizar esta acción.");
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "dateSent"));

        return suggestionRepository.findByStatus(status, pageable).map(suggestionMapper::toDTO);
    }

    /**
     * Devuelve una sugerencia.
     *
     * @param idSuggestion ID de la sugerencia
     * @return DTO con los datos de la sugerencia
     * @throws EntityNotFoundException La sugerencia no existe
     * @throws AccessDeniedException El usuario no es un administrador
     */
    public SuggestionDTO getSuggestion(Long idSuggestion, User user) {
        if (user.getRole() != Role.ADMIN.getValue()) {
            throw new AccessDeniedException("Solo los admins pueden realizar esta acción.");
        }

        Suggestion suggestion = suggestionRepository.findById(idSuggestion)
                .orElseThrow(() -> new EntityNotFoundException("La sugerencia con ID " + idSuggestion + " no existe."));

        return suggestionMapper.toDTO(suggestion);
    }
}
