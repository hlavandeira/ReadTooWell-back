package es.readtoowell.api_biblioteca.controller.book;

import es.readtoowell.api_biblioteca.model.DTO.book.BookListDTO;
import es.readtoowell.api_biblioteca.model.DTO.book.BookListDetailsDTO;
import es.readtoowell.api_biblioteca.model.entity.User;
import es.readtoowell.api_biblioteca.service.book.BookListService;
import es.readtoowell.api_biblioteca.service.user.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador que gestiona las peticiones HTTP relativas a las listas de libros.
 */
@RestController
@RequestMapping("/listas")
public class BookListController {
    @Autowired
    private BookListService listService;
    @Autowired
    private UserService userService;

    /**
     * Devuelve las listas de un usuario.
     *
     * @param page Número de la página que se quiere devolver
     * @param size Tamaño de la página
     * @return Página con las listas resultantes como DTOs
     */
    @GetMapping
    public ResponseEntity<Page<BookListDTO>> getListsByUser(
                                                    @RequestParam(value = "page", defaultValue = "0") int page,
                                                    @RequestParam(value = "size", defaultValue = "10") int size) {

        User user = userService.getAuthenticatedUser();
        if (user == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Page<BookListDTO> listas = listService.getListsByUser(user.getId(), page, size);
        return ResponseEntity.ok(listas);
    }

    /**
     * Devuelve los detalles de una lista, con paginación para los libros incluidos en esta.
     *
     * @param idList ID de la lista
     * @param page Número de la página que se quiere devolver
     * @param size Tamaño de la página
     * @return DTO con los detalles de la lista incluyendo paginación de libros
     */
    @GetMapping("/{idList}")
    public ResponseEntity<BookListDetailsDTO> getListDetails(
                                                        @PathVariable Long idList,
                                                        @RequestParam(value = "page", defaultValue = "0") int page,
                                                        @RequestParam(value = "size", defaultValue = "10") int size) {
        User user = userService.getAuthenticatedUser();
        if (user == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        BookListDetailsDTO lista = listService.getListDetails(user.getId(), idList, page, size);
        return ResponseEntity.ok(lista);
    }

    /**
     * Crea una nueva lista para un usuario.
     *
     * @param list DTO con los datos de la lista a crear
     * @param genreIds Lista con los IDs de los géneros asociados a la lista
     * @return DTO con los detalles de la lista creada
     */
    @PostMapping
    public ResponseEntity<BookListDTO> createList(@Valid @RequestBody BookListDTO list,
                                                  @RequestParam List<Long> genreIds) {
        User user = userService.getAuthenticatedUser();
        if (user == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        BookListDTO listaCreada = listService.createList(user, list, genreIds);

        return ResponseEntity.status(HttpStatus.CREATED).body(listaCreada);
    }

    /**
     * Actualiza los datos de una lista de un usuario.
     *
     * @param idList ID de la lista a actualizar
     * @param list DTO con los datos a actualizar en la lista
     * @param genreIds Lista con los IDs de los géneros asociados a la lista
     * @return DTO con los detalles de la lista actualizada
     */
    @PutMapping("/{idList}")
    public ResponseEntity<BookListDTO> updateList(@PathVariable Long idList,
                                                  @Valid @RequestBody BookListDTO list,
                                                  @RequestParam List<Long> genreIds) {
        User user = userService.getAuthenticatedUser();
        if (user == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        BookListDTO lista = listService.updateList(user.getId(), idList, list, genreIds);

        return ResponseEntity.ok(lista);
    }

    /**
     * Borra una lista de un usuario.
     *
     * @param idList ID de la lista a borrar
     * @return DTO con los datos de la lista borrada
     */
    @DeleteMapping("/{idList}")
    public ResponseEntity<BookListDTO> deleteList(@PathVariable Long idList) {
        User user = userService.getAuthenticatedUser();
        if (user == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        BookListDTO listaEliminada = listService.deleteList(user.getId(), idList);

        return ResponseEntity.ok(listaEliminada);
    }

    /**
     * Añade un libro a una lista de un usuario.
     *
     * @param idList ID de la lista
     * @param idBook ID del libro que se va a añadir
     * @return DTO con los datos de la lista actualizada
     */
    @PostMapping("/{idList}/añadir-libro/{idBook}")
    public ResponseEntity<BookListDTO> addBookToList(@PathVariable Long idList,
                                                     @PathVariable Long idBook) {
        User user = userService.getAuthenticatedUser();
        if (user == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        BookListDTO updatedList = listService.addBookToList(user.getId(), idList, idBook);

        return ResponseEntity.ok(updatedList);
    }

    /**
     * Elimina un libro de una lista de un usuario.
     *
     * @param idList ID de la lista
     * @param idBook ID del libro que se va a eliminar
     * @return DTO con los datos de la lista actualizada
     */
    @DeleteMapping("/{idList}/eliminar-libro/{idBook}")
    public ResponseEntity<BookListDTO> deleteBookFromList(@PathVariable Long idList,
                                                          @PathVariable Long idBook) {
        User user = userService.getAuthenticatedUser();
        if (user == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        BookListDTO updatedList = listService.deleteBookFromList(user.getId(), idList, idBook);

        return ResponseEntity.ok(updatedList);
    }

    /**
     * Devuelve todas las listas de un usuario que no contienen un libro específico.
     *
     * @param idBook ID del libro a excluir
     * @param page Número de la página
     * @param size Tamaño de la página
     * @return Página con las listas resultantes de la búsqueda
     */
    @GetMapping("/{idBook}/otras-listas")
    public ResponseEntity<Page<BookListDTO>> getListsWithoutBook(
                                                        @PathVariable Long idBook,
                                                        @RequestParam(value = "page", defaultValue = "0") int page,
                                                        @RequestParam(value = "size", defaultValue = "100") int size) {
        User user = userService.getAuthenticatedUser();
        if (user == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Page<BookListDTO> lists = listService.getListsWithoutBook(idBook, user.getId(), page, size);

        return ResponseEntity.ok(lists);
    }

    /**
     * Devuelve todas las listas de un usuario que tienen al menos un libro o un género asociado.
     *
     * @return Listado con las listas resultantes
     */
    @GetMapping("/todas-no-vacias")
    public ResponseEntity<List<BookListDTO>> getAllListsExcludingEmpty() {
        User user = userService.getAuthenticatedUser();
        if (user == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        List<BookListDTO> lists = listService.getAllListsExcludingEmpty(user.getId());

        return ResponseEntity.ok(lists);
    }
}
