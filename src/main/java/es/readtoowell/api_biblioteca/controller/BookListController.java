package es.readtoowell.api_biblioteca.controller;

import es.readtoowell.api_biblioteca.model.DTO.BookListDTO;
import es.readtoowell.api_biblioteca.service.BookListService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

        return ResponseEntity.ok(listaCreada);
    }

    @DeleteMapping("/{idUser}/{idList}")
    public ResponseEntity<BookListDTO> deleteList(@PathVariable Long idUser,
                                                  @PathVariable Long idList) {
        BookListDTO listaEliminada = listService.deleteList(idUser, idList);

        return ResponseEntity.ok(listaEliminada);
    }
}
