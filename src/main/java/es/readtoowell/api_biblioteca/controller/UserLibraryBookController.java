package es.readtoowell.api_biblioteca.controller;

import es.readtoowell.api_biblioteca.model.DTO.UserLibraryBookDTO;
import es.readtoowell.api_biblioteca.model.User;
import es.readtoowell.api_biblioteca.service.UserLibraryBookService;
import es.readtoowell.api_biblioteca.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/biblioteca")
public class UserLibraryBookController {
    @Autowired
    private UserLibraryBookService libraryService;
    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<Page<UserLibraryBookDTO>> getLibraryBooks(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        User user = userService.getAuthenticatedUser();
        if (user == null) {
            throw new AccessDeniedException("Usuario no autenticado.");
        }

        Page<UserLibraryBookDTO> libros = libraryService.getLibraryFromUser(user, page, size);

        return ResponseEntity.ok(libros);
    }
}
