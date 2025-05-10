package es.readtoowell.api_biblioteca.service.user;

import es.readtoowell.api_biblioteca.mapper.AuthorRequestMapper;
import es.readtoowell.api_biblioteca.model.entity.AuthorRequest;
import es.readtoowell.api_biblioteca.model.DTO.AuthorRequestDTO;
import es.readtoowell.api_biblioteca.model.entity.RequestBook;
import es.readtoowell.api_biblioteca.model.entity.User;
import es.readtoowell.api_biblioteca.model.enums.RequestStatus;
import es.readtoowell.api_biblioteca.model.enums.Role;
import es.readtoowell.api_biblioteca.repository.user.AuthorRequestRepository;
import es.readtoowell.api_biblioteca.repository.user.RequestBookRepository;
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
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthorRequestService {
    @Autowired
    private AuthorRequestRepository requestRepository;
    @Autowired
    private AuthorRequestMapper requestMapper;
    @Autowired
    private RequestBookRepository requestBookRepository;
    @Autowired
    private UserService userService;

    /**
     * Envía una solicitud de verificación de autor.
     *
     * @param user Usuario que envía la solicitud
     * @param dto DTO con los datos de la solicitud
     * @return DTO con los datos de la solicitud enviada
     * @throws IllegalStateException El usuario tiene una solicitud pendiente o aceptada
     */
    public AuthorRequestDTO sendAuthorRequest(User user, AuthorRequestDTO dto) {
        if (requestRepository.existsByUserIdAndStatusIn(user.getId(),
                List.of(RequestStatus.PENDING.getValue(), RequestStatus.ACCEPETD.getValue()))) {
            throw new IllegalStateException("El usuario ya tiene una solicitud en proceso o aceptada.");
        }

        AuthorRequest request = new AuthorRequest();
        request.setName(dto.getName());
        request.setBiography(dto.getBiography());
        request.setDateSent(Date.valueOf(LocalDate.now()));
        request.setActive(true);
        request.setStatus(RequestStatus.PENDING.getValue());
        request.setUser(user);

        final AuthorRequest savedRequest = requestRepository.save(request);

        // Libros asociados a la solicitud
        List<RequestBook> books = dto.getBooks().stream().map(req -> {
            RequestBook book = new RequestBook();
            book.setTitle(req.getTitle());
            book.setPublicationYear(req.getPublicationYear());
            book.setRequest(savedRequest);
            return book;
        }).collect(Collectors.toList());

        savedRequest.setBooks(books);

        requestBookRepository.saveAll(books);

        AuthorRequest savedRequest2 = requestRepository.save(savedRequest);

        AuthorRequest requestWithBooks = requestRepository.findByIdWithBooks(savedRequest2.getId()).get();

        return requestMapper.toDTO(requestWithBooks);
    }

    /**
     * Cambia el estado de una solicitud de verificación de autor.
     *
     * @param idRequest ID de la solicitud a actualizar
     * @param newStatus Nuevo estado de la solicitud
     * @return DTO con los datos de la solicitud actualizada
     * @throws ValidationException Estado de solicitud inválido
     * @throws EntityNotFoundException La solicitud no existe
     * @throws AccessDeniedException El usuario autenticado no tiene rol de admin
     */
    public AuthorRequestDTO updateStatusRequest(Long idRequest, int newStatus, User user) {
        if (newStatus < 0 || newStatus > 2) {
            throw new ValidationException("El nuevo estado de la solicitud es inválido.");
        }
        if (user.getRole() != Role.ADMIN.getValue()) {
            throw new AccessDeniedException("Solo los admins pueden realizar esta acción.");
        }

        AuthorRequest request = requestRepository.findByIdWithBooks(idRequest)
                .orElseThrow(() -> new EntityNotFoundException("La solicitud con ID " + idRequest + " no existe."));

        if (newStatus == RequestStatus.REJECTED.getValue()) {
            request.setActive(false);
        } else if (newStatus == RequestStatus.ACCEPETD.getValue()) { // Si se acepta, actualizar los datos del usuario
            userService.promoteToAuthor(request.getUser().getId());
        }

        request.setStatus(newStatus);

        request = requestRepository.save(request);

        return requestMapper.toDTO(request);
    }

    /**
     * Devuelve todas las solicitudes de verificación de autor.
     * Las solicitudes se devuelven ordenadas de más a menos reciente.
     *
     * @param page Número de la página que se quiere devolver
     * @param size Tamaño de la página
     * @return Página con las solicitudes como DTOs
     * @throws AccessDeniedException El usuario autenticado no tiene rol de admin
     */
    public Page<AuthorRequestDTO> getAllRequests(int page, int size, User user) {
        if (user.getRole() != Role.ADMIN.getValue()) {
            throw new AccessDeniedException("Solo los admins pueden realizar esta acción.");
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "dateSent"));

        return requestRepository.findAll(pageable).map(requestMapper::toDTO);
    }

    /**
     * Devuelve todas las solicitudes de verificación de autor con un estado específico.
     * Las solicitudes se devuelven ordeandas de más a menos reciente.
     *
     * @param page Número de la página que se quiere devolver
     * @param size Tamaño de la página
     * @param status Estado por el que se quieren filtrar
     * @return Página con las solicitudes filtradas como DTOs
     * @throws ValidationException Estado de solicitud inválido
     * @throws AccessDeniedException El usuario autenticado no tiene rol de admin
     */
    public Page<AuthorRequestDTO> getRequestsWithStatus(int page, int size, int status, User user) {
        if (status < 0 || status > 2) {
            throw new ValidationException("El estado de la solicitud es inválido.");
        }
        if (user.getRole() != Role.ADMIN.getValue()) {
            throw new AccessDeniedException("Solo los admins pueden realizar esta acción.");
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "dateSent"));

        return requestRepository.findByStatus(status, pageable).map(requestMapper::toDTO);
    }

    /**
     * Devuelve una solicitud de verificación de autor.
     *
     * @param idRequest ID de la solicitud
     * @return DTO con los datos de la solicitud
     * @throws EntityNotFoundException La solicitud no existe
     * @throws AccessDeniedException El usuario autenticado no tiene rol de admin
     */
    public AuthorRequestDTO getRequest(Long idRequest, User user) {
        if (user.getRole() != Role.ADMIN.getValue()) {
            throw new AccessDeniedException("Solo los admins pueden realizar esta acción.");
        }

        AuthorRequest request = requestRepository.findByIdWithBooks(idRequest)
                .orElseThrow(() -> new EntityNotFoundException("La solicitud con ID " + idRequest + " no existe."));

        return requestMapper.toDTO(request);
    }

    /**
     * Comprueba si existen solicitudes pendientes de un usuario concreto.
     *
     * @param user Usuario del que se comprueban las solicitudes
     * @return 'true' si tiene alguna solicitud pendiente, 'false' en caso contrario
     */
    public boolean checkIfPendingRequest(User user) {
        return requestRepository.existsByUserIdAndStatusIn(user.getId(), List.of(RequestStatus.PENDING.getValue()));
    }
}
