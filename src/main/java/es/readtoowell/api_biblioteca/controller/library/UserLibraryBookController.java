package es.readtoowell.api_biblioteca.controller.library;

import es.readtoowell.api_biblioteca.model.DTO.book.RatingDTO;
import es.readtoowell.api_biblioteca.model.DTO.UserLibraryBookDTO;
import es.readtoowell.api_biblioteca.model.DTO.YearRecapDTO;
import es.readtoowell.api_biblioteca.model.entity.User;
import es.readtoowell.api_biblioteca.service.library.UserLibraryBookService;
import es.readtoowell.api_biblioteca.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador que gestiona las peticiones HTTP relativas a las bibliotecas de los usuarios.
 */
@RestController
@RequestMapping("/biblioteca")
public class UserLibraryBookController {
    @Autowired
    private UserLibraryBookService libraryService;
    @Autowired
    private UserService userService;

    /**
     * Devuelve los libros de la biblioteca de un usuario.
     *
     * @param page Número de la página que se quiere devolver
     * @param size Tamaño de la página
     * @return Página con los libros de la biblioteca del usuario como DTOs
     */
    @GetMapping("/todos")
    public ResponseEntity<Page<UserLibraryBookDTO>> getLibraryBooks(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        User user = userService.getAuthenticatedUser();
        if (user == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Page<UserLibraryBookDTO> libros = libraryService.getLibraryFromUser(user, page, size);

        return ResponseEntity.ok(libros);
    }

    /**
     * Devuelve los libros de la biblioteca de un usuario con un estado de lectura concreto.
     *
     * @param status Estado por el que se quiere filtrar
     * @param page Número de la página que se quiere devolver
     * @param size Tamaño de la página
     * @return Página con los libros de la biblioteca del usuario filtrados como DTOs
     */
    @GetMapping
    public ResponseEntity<Page<UserLibraryBookDTO>> getLibraryBooksByStatus(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam int status) {

        User user = userService.getAuthenticatedUser();
        if (user == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Page<UserLibraryBookDTO> libros = libraryService.getLibraryByStatus(user, status, page, size);

        return ResponseEntity.ok(libros);
    }

    /**
     * Añade un libro a la biblioteca de un usuario.
     *
     * @param idBook ID del libro a añadir
     * @return DTO con los datos del libro añadido
     */
    @PostMapping("/{idBook}")
    public ResponseEntity<UserLibraryBookDTO> addBookToLibrary(@PathVariable Long idBook) {
        User user = userService.getAuthenticatedUser();
        if (user == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        UserLibraryBookDTO addedBook = libraryService.addBookToLibrary(idBook, user);

        return ResponseEntity.status(HttpStatus.CREATED).body(addedBook);
    }

    /**
     * Elimina un libro de la bilbioteca de un usuario.
     *
     * @param idBook ID del libro a eliminar
     * @return DTO con los datos del libro eliminado
     */
    @DeleteMapping("/{idBook}")
    public ResponseEntity<UserLibraryBookDTO> deleteBookFromLibrary(@PathVariable Long idBook) {
        User user = userService.getAuthenticatedUser();
        if (user == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        UserLibraryBookDTO deletedBook = libraryService.deleteBookFromLibrary(idBook, user);

        return ResponseEntity.ok(deletedBook);
    }

    /**
     * Califica un libro.
     *
     * @param idBook ID del libro
     * @param calificacion Calificación
     * @return DTO con los datos del libro actualizado
     */
    @PutMapping("/{idBook}/calificar")
    public ResponseEntity<RatingDTO> rateBook(@PathVariable Long idBook,
                                              @RequestParam double calificacion) {
        User user = userService.getAuthenticatedUser();
        if (user == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        RatingDTO ratedBook = libraryService.rateBook(idBook, user, calificacion);

        return ResponseEntity.ok(ratedBook);
    }

    /**
     * Añade una reseña a un libro.
     *
     * @param idBook ID del libro
     * @param review Reseña
     * @return DTO con los datos del libro actualizado
     */
    @PutMapping("/{idBook}/escribir-reseña")
    public ResponseEntity<UserLibraryBookDTO> reviewBook(@PathVariable Long idBook,
                                                         @RequestParam String review) {
        User user = userService.getAuthenticatedUser();
        if (user == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        UserLibraryBookDTO ratedBook = libraryService.reviewBook(idBook, user, review);

        return ResponseEntity.ok(ratedBook);
    }

    /**
     * Actualiza el estado de lectura de un libro.
     *
     * @param idBook ID del libro
     * @param estado Nuevo estado de lectura
     * @return DTO con los datos del libro actualizado
     */
    @PutMapping("/{idBook}/estado")
    public ResponseEntity<UserLibraryBookDTO> updateReadingStatus(@PathVariable Long idBook,
                                                                  @RequestParam int estado) {
        User user = userService.getAuthenticatedUser();
        if (user == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        UserLibraryBookDTO ratedBook = libraryService.updateReadingStatus(idBook, user, estado);

        return ResponseEntity.ok(ratedBook);
    }

    /**
     * Actualiza el progreso de lectura de un libro.
     *
     * @param idBook ID del libro
     * @param progreso Nuevo progreso del libro
     * @param tipoProgreso Tipo de progreso
     * @return DTO con los datos del libro actualizado
     */
    @PutMapping("/{idBook}/progreso")
    public ResponseEntity<UserLibraryBookDTO> updateProgress(@PathVariable Long idBook,
                                                             @RequestParam int progreso,
                                                             @RequestParam String tipoProgreso) {
        User user = userService.getAuthenticatedUser();
        if (user == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        UserLibraryBookDTO updatedBook = libraryService.updateProgress(idBook, user, progreso, tipoProgreso);

        return ResponseEntity.ok(updatedBook);
    }

    /**
     * Devuelve el resumen anual de un usuario.
     *
     * @return DTO con los datos del resumen anual
     */
    @GetMapping("/resumen-anual")
    public ResponseEntity<YearRecapDTO> getYearRecap() {
        User user = userService.getAuthenticatedUser();
        if (user == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        YearRecapDTO recap = libraryService.getYearRecap(user);

        return ResponseEntity.ok(recap);
    }
}
