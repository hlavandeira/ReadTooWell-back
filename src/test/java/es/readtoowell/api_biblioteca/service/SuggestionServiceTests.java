package es.readtoowell.api_biblioteca.service;

import es.readtoowell.api_biblioteca.mapper.SuggestionMapper;
import es.readtoowell.api_biblioteca.model.DTO.SuggestionDTO;
import es.readtoowell.api_biblioteca.model.entity.Suggestion;
import es.readtoowell.api_biblioteca.model.entity.User;
import es.readtoowell.api_biblioteca.repository.book.SuggestionRepository;
import es.readtoowell.api_biblioteca.service.book.SuggestionService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Clase de pruebas para el servicio de sugerencias.
 */
@ExtendWith(MockitoExtension.class)
public class SuggestionServiceTests {
    @Mock
    private SuggestionRepository suggestionRepository;
    @Mock
    private SuggestionMapper suggestionMapper;
    @InjectMocks
    private SuggestionService suggestionService;

    private SuggestionDTO suggestionDTO;
    private User user;

    @BeforeEach
    public void init() {
        suggestionDTO = new SuggestionDTO();
        suggestionDTO.setTitle("Título");
        suggestionDTO.setAuthor("Autor");
        suggestionDTO.setPublicationYear(2025);
        suggestionDTO.setActive(true);

        user = new User();
    }

    @Test
    public void SuggestionService_SendSuggestion_ReturnCreated() {
        Suggestion suggestion = new Suggestion();
        suggestion.setId(1L);

        when(suggestionRepository.save(any())).thenReturn(suggestion);
        when(suggestionMapper.toDTO(any())).thenReturn(suggestionDTO);

        SuggestionDTO result = suggestionService.sendSuggestion(suggestionDTO, user);

        assertEquals(suggestionDTO, result);
        verify(suggestionRepository).save(any(Suggestion.class));
    }

    @Test
    public void SuggestionService_UpdateStatusSuggestion_AcceptAndReturn() {
        Long suggestionId = 1L;
        int newStatus = 1;

        Suggestion suggestion = new Suggestion();
        suggestion.setStatus(0);
        suggestion.setActive(true);

        user.setRole(2);

        when(suggestionRepository.findById(suggestionId)).thenReturn(Optional.of(suggestion));
        when(suggestionRepository.save(any())).thenReturn(suggestion);
        when(suggestionMapper.toDTO(any())).thenReturn(new SuggestionDTO());

        SuggestionDTO result = suggestionService.updateStatusSuggestion(suggestionId, newStatus, user);

        assertNotNull(result);
        assertEquals(newStatus, suggestion.getStatus());
        assertTrue(suggestion.isActive());
    }

    @Test
    public void SuggestionService_UpdateStatusSuggestion_RejectAndReturn() {
        Long suggestionId = 1L;
        int newStatus = 3;

        Suggestion suggestion = new Suggestion();
        suggestion.setStatus(0);

        user.setRole(2);

        when(suggestionRepository.findById(suggestionId)).thenReturn(Optional.of(suggestion));
        when(suggestionRepository.save(any())).thenReturn(suggestion);
        when(suggestionMapper.toDTO(any())).thenReturn(new SuggestionDTO());

        SuggestionDTO result = suggestionService.updateStatusSuggestion(suggestionId, newStatus, user);

        assertNotNull(result);
        assertEquals(newStatus, suggestion.getStatus());
        assertFalse(suggestion.isActive());
    }

    @Test
    public void SuggestionService_UpdateStatusSuggestion_InvalidStatus() {
        Long suggestionId = 1L;
        int newStatus = 100;

        Suggestion suggestion = new Suggestion();
        suggestion.setStatus(0);

        user.setRole(2);

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> suggestionService.updateStatusSuggestion(suggestionId, newStatus, user)
        );

        assertEquals("El nuevo estado de la sugerencia es inválido.", exception.getMessage());
    }

    @Test
    public void SuggestionService_UpdateStatusSuggestion_UserIsNotAdmin() {
        Long suggestionId = 1L;
        int newStatus = 1;

        Suggestion suggestion = new Suggestion();
        suggestion.setStatus(0);

        user.setRole(0);

        AccessDeniedException exception = assertThrows(
                AccessDeniedException.class,
                () -> suggestionService.updateStatusSuggestion(suggestionId, newStatus, user)
        );

        assertEquals("Solo los admins pueden realizar esta acción.", exception.getMessage());
    }

    @Test
    public void SuggestionService_UpdateStatusSuggestion_UnexistentSuggestion() {
        Long suggestionId = 1L;
        int newStatus = 1;

        Suggestion suggestion = new Suggestion();
        suggestion.setStatus(0);

        user.setRole(2);

        when(suggestionRepository.findById(suggestionId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> suggestionService.updateStatusSuggestion(suggestionId, newStatus, user)
        );

        assertEquals("La sugerencia con ID 1 no existe.", exception.getMessage());
    }

    @Test
    public void SuggestionService_GetAllSuggestions_ReturnSuggestions() {
        user.setRole(2);

        Page<Suggestion> page = new PageImpl<>(List.of(new Suggestion()));
        when(suggestionRepository.findAll(any(Pageable.class))).thenReturn(page);
        when(suggestionMapper.toDTO(any())).thenReturn(new SuggestionDTO());

        Page<SuggestionDTO> result = suggestionService.getAllSuggestions(0, 5, user);

        assertEquals(1, result.getContent().size());
    }

    @Test
    public void SuggestionService_GetAllSuggestions_UserIsNotAdmin() {
        user.setRole(0);

        AccessDeniedException exception = assertThrows(
                AccessDeniedException.class,
                () -> suggestionService.getAllSuggestions(0, 5, user)
        );

        assertEquals("Solo los admins pueden realizar esta acción.", exception.getMessage());
    }

    @Test
    public void SuggestionService_GetSuggestionsWithStatus_ReturnSuggestions() {
        user.setRole(2);
        int status = 0;
        Page<Suggestion> page = new PageImpl<>(List.of(new Suggestion()));

        when(suggestionRepository.findByStatus(anyInt(), any(Pageable.class))).thenReturn(page);
        when(suggestionMapper.toDTO(any())).thenReturn(new SuggestionDTO());

        Page<SuggestionDTO> result = suggestionService.getSuggestionsWithStatus(0, 5, status, user);

        assertEquals(1, result.getContent().size());
    }

    @Test
    public void SuggestionService_GetSuggestionsWithStatus_InvalidStatus() {
        user.setRole(2);
        int status = 99;

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> suggestionService.getSuggestionsWithStatus(0, 5, status, user)
        );

        assertEquals("El estado es inválido.", exception.getMessage());
    }

    @Test
    public void SuggestionService_GetSuggestionsWithStatus_UserIsNotAdmin() {
        user.setRole(0);
        int status = 1;

        AccessDeniedException exception = assertThrows(
                AccessDeniedException.class,
                () -> suggestionService.getSuggestionsWithStatus(0, 5, status, user)
        );

        assertEquals("Solo los admins pueden realizar esta acción.", exception.getMessage());
    }

    @Test
    public void SuggestionStatus_GetSuggestion_ReturnSuggestion() {
        user.setRole(2);

        Suggestion suggestion = new Suggestion();
        when(suggestionRepository.findById(1L)).thenReturn(Optional.of(suggestion));
        when(suggestionMapper.toDTO(any())).thenReturn(new SuggestionDTO());

        SuggestionDTO result = suggestionService.getSuggestion(1L, user);

        assertNotNull(result);
    }

    @Test
    public void SuggestionStatus_GetSuggestion_UserIsNotAdmin() {
        user.setRole(0);

        AccessDeniedException exception = assertThrows(
                AccessDeniedException.class,
                () -> suggestionService.getSuggestion(1L, user)
        );

        assertEquals("Solo los admins pueden realizar esta acción.", exception.getMessage());
    }

    @Test
    public void SuggestionStatus_GetSuggestion_UnexistentSuggestion() {
        user.setRole(2);

        when(suggestionRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> suggestionService.getSuggestion(1L, user)
        );

        assertEquals("La sugerencia con ID 1 no existe.", exception.getMessage());
    }
}
