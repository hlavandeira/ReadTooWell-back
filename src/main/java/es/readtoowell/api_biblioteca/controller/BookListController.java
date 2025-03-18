package es.readtoowell.api_biblioteca.controller;

import es.readtoowell.api_biblioteca.model.DTO.BookListDTO;
import es.readtoowell.api_biblioteca.service.BookListService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/listas")
public class BookListController {
    @Autowired
    private BookListService listService;

    @GetMapping("/{idUser}")
    public ResponseEntity<Page<BookListDTO>> getListsByUser(@PathVariable Long idUser,
                                                        @RequestParam(value = "page", defaultValue = "0") int page,
                                                        @RequestParam(value = "size", defaultValue = "10") int size) {

        Page<BookListDTO> listas = listService.getListsByUser(idUser, page, size);
        return ResponseEntity.ok(listas);
    }

    @PostMapping("/{idUser}")
    public ResponseEntity<BookListDTO> createList(@PathVariable Long idUser,
                                                  @Valid @RequestBody BookListDTO list,
                                                  @RequestParam Set<Long> genreIds) {
        BookListDTO listaCreada = listService.createList(idUser, list, genreIds);

        return ResponseEntity.status(HttpStatus.CREATED).body(listaCreada);
    }

    @PutMapping("/{idUser}/{idList}")
    public ResponseEntity<BookListDTO> updateList(@PathVariable Long idUser,
                                                  @PathVariable Long idList,
                                                  @Valid @RequestBody BookListDTO list,
                                                  @RequestParam Set<Long> genreIds) {
        BookListDTO lista = listService.updateList(idUser, idList, list, genreIds);

        return ResponseEntity.ok(lista);
    }

    @DeleteMapping("/{idUser}/{idList}")
    public ResponseEntity<BookListDTO> deleteList(@PathVariable Long idUser,
                                                  @PathVariable Long idList) {
        BookListDTO listaEliminada = listService.deleteList(idUser, idList);

        return ResponseEntity.ok(listaEliminada);
    }

    @PostMapping("/{idUser}/{idList}/añadir-libro/{idBook}")
    public ResponseEntity<BookListDTO> addBookToList(@PathVariable Long idUser,
                                                     @PathVariable Long idList,
                                                     @PathVariable Long idBook) {
        BookListDTO updatedList = listService.addBookToList(idUser, idList, idBook);

        return ResponseEntity.ok(updatedList);
    }

    @DeleteMapping("/{idUser}/{idList}/eliminar-libro/{idBook}")
    public ResponseEntity<BookListDTO> deleteBookFromList(@PathVariable Long idUser,
                                                          @PathVariable Long idList,
                                                          @PathVariable Long idBook) {
        BookListDTO updatedList = listService.deleteBookFromList(idUser, idList, idBook);

        return ResponseEntity.ok(updatedList);
    }
}
