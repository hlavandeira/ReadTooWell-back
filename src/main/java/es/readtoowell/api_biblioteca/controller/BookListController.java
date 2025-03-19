package es.readtoowell.api_biblioteca.controller;

import es.readtoowell.api_biblioteca.model.DTO.BookListDTO;
import es.readtoowell.api_biblioteca.model.User;
import es.readtoowell.api_biblioteca.service.BookListService;
import es.readtoowell.api_biblioteca.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/listas")
public class BookListController {
    @Autowired
    private BookListService listService;
    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<Page<BookListDTO>> getListsByUser(
                                                    @RequestParam(value = "page", defaultValue = "0") int page,
                                                    @RequestParam(value = "size", defaultValue = "10") int size) {

        User user = userService.getAuthenticatedUser();
        if (user == null) {
            throw new AccessDeniedException("Usuario no autenticado.");
        }

        Page<BookListDTO> listas = listService.getListsByUser(user.getId(), page, size);
        return ResponseEntity.ok(listas);
    }

    @PostMapping
    public ResponseEntity<BookListDTO> createList(@Valid @RequestBody BookListDTO list,
                                                  @RequestParam Set<Long> genreIds) {
        User user = userService.getAuthenticatedUser();
        if (user == null) {
            throw new AccessDeniedException("Usuario no autenticado.");
        }

        BookListDTO listaCreada = listService.createList(user, list, genreIds);

        return ResponseEntity.status(HttpStatus.CREATED).body(listaCreada);
    }

    @PutMapping("/{idList}")
    public ResponseEntity<BookListDTO> updateList(@PathVariable Long idList,
                                                  @Valid @RequestBody BookListDTO list,
                                                  @RequestParam Set<Long> genreIds) {
        User user = userService.getAuthenticatedUser();
        if (user == null) {
            throw new AccessDeniedException("Usuario no autenticado.");
        }

        BookListDTO lista = listService.updateList(user.getId(), idList, list, genreIds);

        return ResponseEntity.ok(lista);
    }

    @DeleteMapping("/{idList}")
    public ResponseEntity<BookListDTO> deleteList(@PathVariable Long idList) {
        User user = userService.getAuthenticatedUser();
        if (user == null) {
            throw new AccessDeniedException("Usuario no autenticado.");
        }

        BookListDTO listaEliminada = listService.deleteList(user.getId(), idList);

        return ResponseEntity.ok(listaEliminada);
    }

    @PostMapping("/{idList}/añadir-libro/{idBook}")
    public ResponseEntity<BookListDTO> addBookToList(@PathVariable Long idList,
                                                     @PathVariable Long idBook) {
        User user = userService.getAuthenticatedUser();
        if (user == null) {
            throw new AccessDeniedException("Usuario no autenticado.");
        }

        BookListDTO updatedList = listService.addBookToList(user.getId(), idList, idBook);

        return ResponseEntity.ok(updatedList);
    }

    @DeleteMapping("/{idList}/eliminar-libro/{idBook}")
    public ResponseEntity<BookListDTO> deleteBookFromList(@PathVariable Long idList,
                                                          @PathVariable Long idBook) {
        User user = userService.getAuthenticatedUser();
        if (user == null) {
            throw new AccessDeniedException("Usuario no autenticado.");
        }

        BookListDTO updatedList = listService.deleteBookFromList(user.getId(), idList, idBook);

        return ResponseEntity.ok(updatedList);
    }
}
