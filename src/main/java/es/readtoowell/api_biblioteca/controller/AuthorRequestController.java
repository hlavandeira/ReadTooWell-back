package es.readtoowell.api_biblioteca.controller;

import es.readtoowell.api_biblioteca.model.DTO.AuthorRequestDTO;
import es.readtoowell.api_biblioteca.service.AuthorRequestService;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/solicitud-autor")
public class AuthorRequestController {
    @Autowired
    private AuthorRequestService requestService;

    @PostMapping("/{idUser}")
    public ResponseEntity<AuthorRequestDTO> sendAuthorRequest(@PathVariable Long idUser,
                                                              @Valid @RequestBody AuthorRequestDTO dto) {
        AuthorRequestDTO request = requestService.sendAuthorRequest(idUser, dto);

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
