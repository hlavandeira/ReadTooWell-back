package es.readtoowell.api_biblioteca.controller.library;

import es.readtoowell.api_biblioteca.model.DTO.book.FormatDTO;
import es.readtoowell.api_biblioteca.model.entity.User;
import es.readtoowell.api_biblioteca.service.library.UserBookFormatService;
import es.readtoowell.api_biblioteca.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador que gestiona las peticiones HTTP relativas a los formatos de los libros de los usuarios.
 */
@RestController
@RequestMapping("/biblioteca")
public class UserBookFormatController {
    @Autowired
    private UserBookFormatService bookFormatService;
    @Autowired
    private UserService userService;

    /**
     * Devuelve los formatos de un libro de un usuario.
     *
     * @param idBook ID del libro
     * @return Lista con los formatos que un usuario tiene de un libro
     */
    @GetMapping("/{idBook}/formatos")
    public ResponseEntity<List<FormatDTO>> getFormatsForUserBook(@PathVariable Long idBook) {
        User user = userService.getAuthenticatedUser();
        if (user == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        List<FormatDTO> formats = bookFormatService.getFormatsForUserBook(idBook, user.getId());
        if (formats.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(formats);
    }

    /**
     * Añade un formato a un libro de la biblioteca de un usuario.
     *
     * @param idBook ID del libro
     * @param idFormat ID del formato a añadir
     * @return Lista con los formatos asociados al libro
     */
    @PostMapping("/{idBook}/formatos/{idFormat}")
    public ResponseEntity<List<FormatDTO>> addFormatToBook(@PathVariable Long idBook,
                                                           @PathVariable Long idFormat) {
        User user = userService.getAuthenticatedUser();
        if (user == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        List<FormatDTO> formats = bookFormatService.getFormatsForUserBook(idBook, user.getId());
        FormatDTO newFormat = bookFormatService.addFormatToBook(idBook, user.getId(), idFormat);
        formats.add(newFormat);

        return ResponseEntity.ok(formats);
    }

    /**
     * Elimina un formato a un libro de la biblioteca de un usuario.
     *
     * @param idBook ID del libro
     * @param idFormat ID del formato a eliminar
     * @return Lista con los formatos asociados al libro
     */
    @DeleteMapping("/{idBook}/formatos/{idFormat}")
    public ResponseEntity<List<FormatDTO>> removeFormatFromBook(@PathVariable Long idBook,
                                                                @PathVariable Long idFormat) {
        User user = userService.getAuthenticatedUser();
        if (user == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        bookFormatService.removeFormatFromBook(idBook, user.getId(), idFormat);
        List<FormatDTO> formats = bookFormatService.getFormatsForUserBook(idBook, user.getId());

        return ResponseEntity.ok(formats);
    }
}
