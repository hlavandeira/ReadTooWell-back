package es.readtoowell.api_biblioteca.service;

import es.readtoowell.api_biblioteca.mapper.AuthorRequestMapper;
import es.readtoowell.api_biblioteca.model.DTO.AuthorRequestDTO;
import es.readtoowell.api_biblioteca.model.entity.AuthorRequest;
import es.readtoowell.api_biblioteca.model.entity.User;
import es.readtoowell.api_biblioteca.repository.user.AuthorRequestRepository;
import es.readtoowell.api_biblioteca.repository.user.RequestBookRepository;
import es.readtoowell.api_biblioteca.service.user.AuthorRequestService;
import es.readtoowell.api_biblioteca.service.user.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.Assertions;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

/**
 * Clase de pruebas para el servicio de solicitudes de autor.
 */
@ExtendWith(MockitoExtension.class)
public class AuthorRequestServiceTests {
    @Mock
    private AuthorRequestRepository requestRepository;
    @Mock
    private AuthorRequestMapper requestMapper;
    @Mock
    private RequestBookRepository requestBookRepository;
    @Mock
    private UserService userService;
    @InjectMocks
    private AuthorRequestService requestService;

    private AuthorRequestDTO requestDTO;
    private AuthorRequest request;
    private User user;

    @BeforeEach
    public void init() {
        requestDTO = new AuthorRequestDTO();
        requestDTO.setName("Nombre");
        requestDTO.setBiography("Biografía");
        requestDTO.setBooks(new ArrayList<>());

        request = new AuthorRequest();
        request.setName("Nombre");
        request.setBiography("Biografia");
        request.setBooks(new ArrayList<>());

        user = new User();
    }

    @Test
    public void AuthorRequestService_SendAuthorRequest_ReturnAuthorRequestDto() {
        when(requestRepository.existsByUserIdAndStatusIn(any(), anyList())).thenReturn(false);
        when(requestRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(requestBookRepository.saveAll(any())).thenReturn(null);
        when(requestRepository.findByIdWithBooks(any())).thenReturn(Optional.of(request));
        when(requestMapper.toDTO(any())).thenReturn(requestDTO);

        AuthorRequestDTO result = requestService.sendAuthorRequest(user, requestDTO);

        assertNotNull(result);
        assertEquals(requestDTO.getName(), result.getName());
    }

    @Test
    public void AuthorRequestService_SendAuthorRequest_PendingRequestExists() {
        when(requestRepository.existsByUserIdAndStatusIn(any(), anyList())).thenReturn(true);

        IllegalStateException exception = Assertions.assertThrows(
                IllegalStateException.class,
                () -> requestService.sendAuthorRequest(user, requestDTO)
        );

        assertEquals("El usuario ya tiene una solicitud en proceso o aceptada.", exception.getMessage());
    }

    @Test
    public void AuthorRequestService_UpdateStatusRequest_ReturnAuthorRequestDto() {
        user.setRole(2);
        request.setStatus(0);
        when(requestRepository.findByIdWithBooks(any())).thenReturn(Optional.of(request));
        when(requestRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        requestDTO.setStatus(2);
        when(requestMapper.toDTO(any())).thenReturn(requestDTO);

        AuthorRequestDTO result = requestService.updateStatusRequest(1L, 2, user);

        assertNotNull(result);
        assertNotEquals(result.getStatus(), 0);
        assertEquals(result.getStatus(), 2);
    }

    @Test
    public void AuthorRequestService_UpdateStatusRequest_InvalidStatus() {
        user.setRole(2);

        ValidationException exception = Assertions.assertThrows(
                ValidationException.class,
                () -> requestService.updateStatusRequest(1L, 100, user)
        );

        assertEquals("El nuevo estado de la solicitud es inválido.", exception.getMessage());
    }

    @Test
    public void AuthorRequestService_UpdateStatusRequest_UserIsNotAdmin() {
        user.setRole(0);

        AccessDeniedException exception = Assertions.assertThrows(
                AccessDeniedException.class,
                () -> requestService.updateStatusRequest(1L, 1, user)
        );

        assertEquals("Solo los admins pueden realizar esta acción.", exception.getMessage());
    }

    @Test
    public void AuthorRequestService_UpdateStatusRequest_UnexistentRequest() {
        user.setRole(2);
        when(requestRepository.findByIdWithBooks(any())).thenReturn(Optional.empty());

        EntityNotFoundException exception = Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> requestService.updateStatusRequest(1L, 1, user)
        );

        assertEquals("La solicitud con ID 1 no existe.", exception.getMessage());
    }

    @Test
    public void AuthorRequestService_GetAllRequests_ReturnRequests() {
        user.setRole(2);
        Page<AuthorRequest> page = new PageImpl<>(List.of(request));
        when(requestRepository.findAll(any(Pageable.class))).thenReturn(page);
        when(requestMapper.toDTO(any())).thenReturn(requestDTO);

        Page<AuthorRequestDTO> result = requestService.getAllRequests(0, 5, user);

        assertFalse(result.isEmpty());
        assertEquals(1, result.getContent().size());
    }

    @Test
    public void AuthorRequestService_GetAllRequests_UserIsNotAdmin() {
        user.setRole(0);

        AccessDeniedException exception = Assertions.assertThrows(
                AccessDeniedException.class,
                () -> requestService.getAllRequests(0, 10, user)
        );

        assertEquals("Solo los admins pueden realizar esta acción.", exception.getMessage());
    }

    @Test
    public void AuthorRequestService_GetRequestsWithStatus_ReturnRequests() {
        user.setRole(2);
        Page<AuthorRequest> page = new PageImpl<>(List.of(request));
        when(requestRepository.findByStatus(anyInt(), any(Pageable.class))).thenReturn(page);
        when(requestMapper.toDTO(any())).thenReturn(requestDTO);

        Page<AuthorRequestDTO> result = requestService.getRequestsWithStatus(0, 5, 0, user);

        assertFalse(result.isEmpty());
        assertEquals(1, result.getContent().size());
    }

    @Test
    public void AuthorRequestService_GetRequestsWithStatus_InvalidStatus() {
        user.setRole(2);

        ValidationException exception = Assertions.assertThrows(
                ValidationException.class,
                () -> requestService.getRequestsWithStatus(0, 10, 20, user)
        );

        assertEquals("El estado de la solicitud es inválido.", exception.getMessage());
    }

    @Test
    public void AuthorRequestService_GetRequestsWithStatus_UserIsNotAdmin() {
        user.setRole(0);

        AccessDeniedException exception = Assertions.assertThrows(
                AccessDeniedException.class,
                () -> requestService.getRequestsWithStatus(0, 10, 2, user)
        );

        assertEquals("Solo los admins pueden realizar esta acción.", exception.getMessage());
    }

    @Test
    public void AuthorRequestService_GetRequest_ReturnAuthorRequestDto() {
        user.setRole(2);
        when(requestRepository.findByIdWithBooks(any())).thenReturn(Optional.of(request));
        when(requestMapper.toDTO(any())).thenReturn(requestDTO);

        AuthorRequestDTO result = requestService.getRequest(1L, user);

        assertNotNull(result);
        assertEquals(requestDTO.getName(), result.getName());
    }

    @Test
    public void AuthorRequestService_GetRequest_UserIsNotAdmin() {
        user.setRole(0);

        AccessDeniedException exception = Assertions.assertThrows(
                AccessDeniedException.class,
                () -> requestService.getRequest(1L, user)
        );

        assertEquals("Solo los admins pueden realizar esta acción.", exception.getMessage());
    }

    @Test
    public void AuthorRequestService_GetRequest_UnexistentRequest() {
        user.setRole(2);
        when(requestRepository.findByIdWithBooks(any())).thenReturn(Optional.empty());

        EntityNotFoundException exception = Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> requestService.getRequest(1L, user)
        );

        assertEquals("La solicitud con ID 1 no existe.", exception.getMessage());
    }
}
