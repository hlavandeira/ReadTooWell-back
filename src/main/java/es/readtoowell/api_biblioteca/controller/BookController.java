package es.readtoowell.api_biblioteca.controller;

import es.readtoowell.api_biblioteca.DTO.BookDTO;
import es.readtoowell.api_biblioteca.mapper.BookMapper;
import es.readtoowell.api_biblioteca.model.Book;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
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
    @Autowired
    private BookMapper bookMapper;

    @GetMapping
    public ResponseEntity<Page<BookDTO>> getBooks(@RequestParam(value = "page", defaultValue = "0") int page,
                                               @RequestParam(value = "size", defaultValue = "10") int size) {

        Page<Book> bookPage = bookService.getAllBooks(page, size);
        Page<BookDTO> bookDTOPage = bookPage.map(bookMapper::toDTO);

        return ResponseEntity.ok(bookDTOPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDTO> getBook(@PathVariable(value = "id") Long id) {
        Optional<Book> libro = bookService.getBook(id);

        if (libro.isPresent()) {
            return ResponseEntity.ok(bookMapper.toDTO(libro.get()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<BookDTO> createBook(@Valid @RequestBody BookDTO book,
                                           @RequestParam Set<Long> genreIds) {
        Book newBook = bookService.createBook(book, genreIds);
        System.out.println("Géneros en el controlador: " + genreIds);
        return ResponseEntity.status(HttpStatus.CREATED).body(bookMapper.toDTO(newBook));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookDTO> updateBook(@PathVariable Long id,
                                         @Valid @RequestBody BookDTO book,
                                         @RequestParam Set<Long> genreIds) {
        try {
            Book libro = bookService.updateBook(id, book, genreIds);
            return ResponseEntity.ok(bookMapper.toDTO(libro));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BookDTO> deleteBook(@PathVariable Long id) {
        Optional<Book> libro = bookService.getBook(id);

        if (libro.isPresent()) {
            libro.get().delete();
            bookService.deleteBook(bookMapper.toDTO(libro.get()));
            return ResponseEntity.ok(bookMapper.toDTO(libro.get()));
        } else {
            return ResponseEntity.notFound().build();
        }
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
        Page<Book> libros = bookService.filterBooks(searchString, minPags, maxPags, minAño,
                maxAño, page, size);
        Page<BookDTO> bookDTOPage = libros.map(bookMapper::toDTO);

        return ResponseEntity.ok(bookDTOPage);
    }
}
