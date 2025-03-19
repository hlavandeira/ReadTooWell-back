package es.readtoowell.api_biblioteca.controller;

import es.readtoowell.api_biblioteca.model.DTO.FormatDTO;
import es.readtoowell.api_biblioteca.model.Format;
import es.readtoowell.api_biblioteca.model.User;
import es.readtoowell.api_biblioteca.service.UserBookFormatService;
import es.readtoowell.api_biblioteca.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/biblioteca")
public class UserBookFormatController {
    @Autowired
    private UserBookFormatService bookFormatService;
    @Autowired
    private UserService userService;

    @GetMapping("/{idBook}/formatos")
    public ResponseEntity<List<FormatDTO>> getFormatsForUserBook(@PathVariable Long idBook) {
        User user = userService.getAuthenticatedUser();
        if (user == null) {
            throw new AccessDeniedException("Usuario no autenticado.");
        }

        List<FormatDTO> formats = bookFormatService.getFormatsForUserBook(idBook, user.getId());
        if (formats.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(formats);
    }

    @PostMapping("/{idBook}/formatos/{idFormat}")
    public ResponseEntity<List<FormatDTO>> addFormatToBook(@PathVariable Long idBook,
                                                           @PathVariable Long idFormat) {
        User user = userService.getAuthenticatedUser();
        if (user == null) {
            throw new AccessDeniedException("Usuario no autenticado.");
        }

        List<FormatDTO> formats = bookFormatService.getFormatsForUserBook(idBook, user.getId());
        FormatDTO newFormat = bookFormatService.addFormatToBook(idBook, user.getId(), idFormat);
        formats.add(newFormat);

        return ResponseEntity.ok(formats);
    }

    @DeleteMapping("/{idBook}/formatos/{idFormat}")
    public ResponseEntity<List<FormatDTO>> removeFormatFromBook(@PathVariable Long idBook,
                                                                @PathVariable Long idFormat) {
        User user = userService.getAuthenticatedUser();
        if (user == null) {
            throw new AccessDeniedException("Usuario no autenticado.");
        }

        bookFormatService.removeFormatFromBook(idBook, user.getId(), idFormat);
        List<FormatDTO> formats = bookFormatService.getFormatsForUserBook(idBook, user.getId());

        return ResponseEntity.ok(formats);
    }
}
