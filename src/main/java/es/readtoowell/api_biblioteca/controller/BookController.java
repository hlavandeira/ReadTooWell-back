package es.readtoowell.api_biblioteca.controller;

import es.readtoowell.api_biblioteca.model.DTO.AuthorDTO;
import es.readtoowell.api_biblioteca.model.DTO.BookDTO;
import es.readtoowell.api_biblioteca.model.DTO.BookDetailsDTO;
import es.readtoowell.api_biblioteca.model.DTO.SuggestionDTO;
import es.readtoowell.api_biblioteca.model.User;
import es.readtoowell.api_biblioteca.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import es.readtoowell.api_biblioteca.service.BookService;

import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/libros")
public class BookController {
    @Autowired
    private BookService bookService;
    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<Page<BookDTO>> getBooks(@RequestParam(value = "page", defaultValue = "0") int page,
                                                  @RequestParam(value = "size", defaultValue = "10") int size) {

        Page<BookDTO> bookPage = bookService.getAllBooks(page, size);

        return ResponseEntity.ok(bookPage);
    }

    @GetMapping("/{idBook}")
    public ResponseEntity<BookDTO> getBook(@PathVariable Long idBook) {
        BookDTO libro = bookService.getBook(idBook);

        return ResponseEntity.ok(libro);
    }

    @PostMapping
    public ResponseEntity<BookDTO> createBook(@Valid @RequestBody BookDTO book,
                                              @RequestParam Set<Long> genreIds) {
        BookDTO newBook = bookService.createBook(book, genreIds);

        return ResponseEntity.status(HttpStatus.CREATED).body(newBook);
    }

    @PutMapping("/{idBook}")
    public ResponseEntity<BookDTO> updateBook(@PathVariable Long idBook,
                                              @Valid @RequestBody BookDTO book,
                                              @RequestParam Set<Long> genreIds) {
        BookDTO libro = bookService.updateBook(idBook, book, genreIds);

        return ResponseEntity.ok(libro);
    }

    @DeleteMapping("/{idBook}")
    public ResponseEntity<BookDTO> deleteBook(@PathVariable Long idBook) {
        BookDTO libro = bookService.getBook(idBook);

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

    @GetMapping("/{idBook}/detalles")
    public ResponseEntity<BookDetailsDTO> getBookDetails(@PathVariable Long idBook) {

        User user = userService.getAuthenticatedUser();
        if (user == null) {
            throw new AccessDeniedException("Usuario no autenticado.");
        }

        BookDetailsDTO details = bookService.getBookDetails(idBook, user);

        return ResponseEntity.ok(details);
    }

    @GetMapping("/{idAuthor}/autor")
    public ResponseEntity<AuthorDTO> getBooksByAuthor(@PathVariable Long idAuthor) {
        AuthorDTO author = bookService.getBooksByAuthor(idAuthor);

        return ResponseEntity.ok(author);
    }
}
