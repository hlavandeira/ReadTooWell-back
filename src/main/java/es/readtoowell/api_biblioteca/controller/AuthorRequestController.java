package es.readtoowell.api_biblioteca.controller;

import es.readtoowell.api_biblioteca.model.DTO.AuthorRequestDTO;
import es.readtoowell.api_biblioteca.model.User;
import es.readtoowell.api_biblioteca.service.AuthorRequestService;
import es.readtoowell.api_biblioteca.service.UserService;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
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

    @PostMapping
    public ResponseEntity<AuthorRequestDTO> sendAuthorRequest(@Valid @RequestBody AuthorRequestDTO dto) {
        User user = userService.getAuthenticatedUser();
        if (user == null) {
            throw new AccessDeniedException("Usuario no autenticado.");
        }

        AuthorRequestDTO request = requestService.sendAuthorRequest(user, dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(request);
    }

    @PutMapping("/{idRequest}")
    public ResponseEntity<AuthorRequestDTO> updateStatusRequest(@PathVariable Long idRequest,
                                                                @RequestParam int newStatus) {
        AuthorRequestDTO dto = requestService.updateStatusRequest(idRequest, newStatus);

        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public ResponseEntity<Page<AuthorRequestDTO>> getAllRequests(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        Page<AuthorRequestDTO> requests = requestService.getAllRequests(page, size);

        return ResponseEntity.ok(requests);
    }

    @GetMapping("/estado")
    public ResponseEntity<Page<AuthorRequestDTO>> getRequestsWithStatus(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "status", defaultValue = "0") int status) {

        Page<AuthorRequestDTO> requests = requestService.getRequestsWithStatus(page, size, status);

        return ResponseEntity.ok(requests);
    }

    @GetMapping("/{idRequest}")
    public ResponseEntity<AuthorRequestDTO> getRequest(@PathVariable Long idRequest) {
        AuthorRequestDTO request = requestService.getRequest(idRequest);

        return ResponseEntity.ok(request);
    }
}
