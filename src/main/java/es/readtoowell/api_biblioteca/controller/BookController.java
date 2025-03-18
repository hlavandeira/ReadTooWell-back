package es.readtoowell.api_biblioteca.controller;

import es.readtoowell.api_biblioteca.model.DTO.BookDTO;
import es.readtoowell.api_biblioteca.model.DTO.SuggestionDTO;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import es.readtoowell.api_biblioteca.service.BookService;

import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/libros")
public class BookController {
    @Autowired
    private BookService bookService;

    @GetMapping
    public ResponseEntity<Page<BookDTO>> getBooks(@RequestParam(value = "page", defaultValue = "0") int page,
                                               @RequestParam(value = "size", defaultValue = "10") int size) {

        Page<BookDTO> bookPage = bookService.getAllBooks(page, size);

        return ResponseEntity.ok(bookPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDTO> getBook(@PathVariable(value = "id") Long id) {
        BookDTO libro = bookService.getBook(id);

        return ResponseEntity.ok(libro);
    }

    @PostMapping
    public ResponseEntity<BookDTO> createBook(@Valid @RequestBody BookDTO book,
                                           @RequestParam Set<Long> genreIds) {
        BookDTO newBook = bookService.createBook(book, genreIds);
        return ResponseEntity.status(HttpStatus.CREATED).body(newBook);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookDTO> updateBook(@PathVariable Long id,
                                         @Valid @RequestBody BookDTO book,
                                         @RequestParam Set<Long> genreIds) {
        try {
            BookDTO libro = bookService.updateBook(id, book, genreIds);
            return ResponseEntity.ok(libro);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BookDTO> deleteBook(@PathVariable Long id) {
        BookDTO libro = bookService.getBook(id);

        BookDTO dto = bookService.deleteBook(libro);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/buscar")
    public ResponseEntity<Page<BookDTO>> searchBooks(
            @RequestParam(required = false) String searchString,
            @RequestParam(required = false) Integer minPags,
            @RequestParam(required = false) Integer maxPags,
            @RequestParam(required = false) Integer minAño,
            @RequestParam(required = false) Integer maxAño,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size)
    {
        Page<BookDTO> libros = bookService.filterBooks(searchString, minPags, maxPags, minAño,
                maxAño, page, size);

        return ResponseEntity.ok(libros);
    }
}
