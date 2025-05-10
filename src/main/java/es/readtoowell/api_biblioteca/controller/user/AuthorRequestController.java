package es.readtoowell.api_biblioteca.controller.user;

import es.readtoowell.api_biblioteca.model.DTO.AuthorRequestDTO;
import es.readtoowell.api_biblioteca.model.entity.User;
import es.readtoowell.api_biblioteca.service.user.AuthorRequestService;
import es.readtoowell.api_biblioteca.service.user.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/solicitud-autor")
public class AuthorRequestController {
    @Autowired
    private AuthorRequestService requestService;
    @Autowired
    private UserService userService;

    /**
     * Envía una solicitud de verificación de autor.
     *
     * @param dto DTO con los datos de la solicitud
     * @return DTO con los datos de la solicitud enviada
     * @throws AccessDeniedException Usuario no autenticado
     */
    @PostMapping
    public ResponseEntity<AuthorRequestDTO> sendAuthorRequest(@Valid @RequestBody AuthorRequestDTO dto) {
        User user = userService.getAuthenticatedUser();
        if (user == null) {
            throw new AccessDeniedException("Usuario no autenticado.");
        }

        AuthorRequestDTO request = requestService.sendAuthorRequest(user, dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(request);
    }

    /**
     * Cambia el estado de una solicitud de verificación de autor.
     *
     * @param idRequest ID de la solicitud a actualizar
     * @param newStatus Nuevo estado de la solicitud
     * @return DTO con los datos de la solicitud actualizada
     * @throws AccessDeniedException Usuario no autenticado
     */
    @PutMapping("/{idRequest}")
    public ResponseEntity<AuthorRequestDTO> updateStatusRequest(@PathVariable Long idRequest,
                                                                @RequestParam int newStatus) {
        User user = userService.getAuthenticatedUser();
        if (user == null) {
            throw new AccessDeniedException("Usuario no autenticado.");
        }

        AuthorRequestDTO dto = requestService.updateStatusRequest(idRequest, newStatus, user);

        return ResponseEntity.ok(dto);
    }

    /**
     * Devuelve todas las solicitudes de verificación de autor.
     *
     * @param page Número de la página que se quiere devolver
     * @param size Tamaño de la página
     * @return Página con las solicitudes como DTOs
     * @throws AccessDeniedException Usuario no autenticado
     */
    @GetMapping
    public ResponseEntity<Page<AuthorRequestDTO>> getAllRequests(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        User user = userService.getAuthenticatedUser();
        if (user == null) {
            throw new AccessDeniedException("Usuario no autenticado.");
        }

        Page<AuthorRequestDTO> requests = requestService.getAllRequests(page, size, user);

        return ResponseEntity.ok(requests);
    }

    /**
     * Devuelve todas las solicitudes de verificación de autor con un estado específico.
     *
     * @param page Número de la página que se quiere devolver
     * @param size Tamaño de la página
     * @param status Estado por el que se quieren filtrar
     * @return Página con las solicitudes filtradas como DTOs
     * @throws AccessDeniedException Usuario no autenticado
     */
    @GetMapping("/estado")
    public ResponseEntity<Page<AuthorRequestDTO>> getRequestsWithStatus(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "status", defaultValue = "0") int status) {

        User user = userService.getAuthenticatedUser();
        if (user == null) {
            throw new AccessDeniedException("Usuario no autenticado.");
        }

        Page<AuthorRequestDTO> requests = requestService.getRequestsWithStatus(page, size, status, user);

        return ResponseEntity.ok(requests);
    }

    /**
     * Devuelve una solicitud de verificación de autor.
     *
     * @param idRequest ID de la solicitud
     * @return DTO con los datos de la solicitud
     * @throws AccessDeniedException Usuario no autenticado
     */
    @GetMapping("/{idRequest}")
    public ResponseEntity<AuthorRequestDTO> getRequest(@PathVariable Long idRequest) {
        User user = userService.getAuthenticatedUser();
        if (user == null) {
            throw new AccessDeniedException("Usuario no autenticado.");
        }

        AuthorRequestDTO request = requestService.getRequest(idRequest, user);

        return ResponseEntity.ok(request);
    }

    /**
     * Comprueba si existen solicitudes pendientes de un usuario concreto.
     *
     * @return 'true' si tiene alguna solicitud pendiente, 'false' en caso contrario
     */
    @GetMapping("/comprobar-pendiente")
    public ResponseEntity<Boolean> checkIfPendingRequest() {
        User user = userService.getAuthenticatedUser();
        if (user == null) {
            throw new AccessDeniedException("Usuario no autenticado.");
        }

        return ResponseEntity.ok(requestService.checkIfPendingRequest(user));
    }
}
