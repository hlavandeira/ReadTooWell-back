package es.readtoowell.api_biblioteca.controller;

import es.readtoowell.api_biblioteca.model.DTO.UserLibraryBookDTO;
import es.readtoowell.api_biblioteca.model.DTO.YearRecapDTO;
import es.readtoowell.api_biblioteca.model.User;
import es.readtoowell.api_biblioteca.service.UserLibraryBookService;
import es.readtoowell.api_biblioteca.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/biblioteca")
public class UserLibraryBookController {
    @Autowired
    private UserLibraryBookService libraryService;
    @Autowired
    private UserService userService;

    @GetMapping("/todos")
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

    @GetMapping
    public ResponseEntity<Page<UserLibraryBookDTO>> getLibraryBooksByStatus(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam int status) {

        User user = userService.getAuthenticatedUser();
        if (user == null) {
            throw new AccessDeniedException("Usuario no autenticado.");
        }

        Page<UserLibraryBookDTO> libros = libraryService.getLibraryByStatus(user, status, page, size);

        return ResponseEntity.ok(libros);
    }

    @PostMapping("/{idBook}")
    public ResponseEntity<UserLibraryBookDTO> addBookToLibrary(@PathVariable Long idBook) {
        User user = userService.getAuthenticatedUser();
        if (user == null) {
            throw new AccessDeniedException("Usuario no autenticado.");
        }

        UserLibraryBookDTO addedBook = libraryService.addBookToLibrary(idBook, user);

        return ResponseEntity.status(HttpStatus.CREATED).body(addedBook);
    }

    @DeleteMapping("/{idBook}")
    public ResponseEntity<UserLibraryBookDTO> deleteBookFromLibrary(@PathVariable Long idBook) {
        User user = userService.getAuthenticatedUser();
        if (user == null) {
            throw new AccessDeniedException("Usuario no autenticado.");
        }

        UserLibraryBookDTO deletedBook = libraryService.deleteBookFromLibrary(idBook, user);

        return ResponseEntity.ok(deletedBook);
    }

    @PutMapping("/{idBook}/calificar")
    public ResponseEntity<UserLibraryBookDTO> rateBook(@PathVariable Long idBook,
                                                       @RequestParam double calificacion) {
        User user = userService.getAuthenticatedUser();
        if (user == null) {
            throw new AccessDeniedException("Usuario no autenticado.");
        }

        UserLibraryBookDTO ratedBook = libraryService.rateBook(idBook, user, calificacion);

        return ResponseEntity.ok(ratedBook);
    }

    @PutMapping("/{idBook}/escribir-reseña")
    public ResponseEntity<UserLibraryBookDTO> reviewBook(@PathVariable Long idBook,
                                                         @RequestParam String reseña) {
        User user = userService.getAuthenticatedUser();
        if (user == null) {
            throw new AccessDeniedException("Usuario no autenticado.");
        }

        UserLibraryBookDTO ratedBook = libraryService.reviewBook(idBook, user, reseña);

        return ResponseEntity.ok(ratedBook);
    }

    @PutMapping("/{idBook}/estado")
    public ResponseEntity<UserLibraryBookDTO> updateReadingStatus(@PathVariable Long idBook,
                                                                  @RequestParam int estado) {
        User user = userService.getAuthenticatedUser();
        if (user == null) {
            throw new AccessDeniedException("Usuario no autenticado.");
        }

        UserLibraryBookDTO ratedBook = libraryService.updateReadingStatus(idBook, user, estado);

        return ResponseEntity.ok(ratedBook);
    }

    @PutMapping("/{idBook}/progreso")
    public ResponseEntity<UserLibraryBookDTO> updateProgress(@PathVariable Long idBook,
                                                             @RequestParam int progreso,
                                                             @RequestParam String tipoProgreso) {
        User user = userService.getAuthenticatedUser();
        if (user == null) {
            throw new AccessDeniedException("Usuario no autenticado.");
        }

        UserLibraryBookDTO updatedBook = libraryService.updateProgress(idBook, user, progreso, tipoProgreso);

        return ResponseEntity.ok(updatedBook);
    }

    @GetMapping("/resumen-anual")
    public ResponseEntity<YearRecapDTO> getYearRecap() {
        User user = userService.getAuthenticatedUser();
        if (user == null) {
            throw new AccessDeniedException("Usuario no autenticado.");
        }

        YearRecapDTO recap = libraryService.getYearRecap(user);

        return ResponseEntity.ok(recap);
    }
}
