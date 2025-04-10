package es.readtoowell.api_biblioteca.controller.book;

import es.readtoowell.api_biblioteca.model.DTO.AuthorDTO;
import es.readtoowell.api_biblioteca.model.DTO.BookDTO;
import es.readtoowell.api_biblioteca.model.DTO.BookDetailsDTO;
import es.readtoowell.api_biblioteca.model.entity.User;
import es.readtoowell.api_biblioteca.service.user.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import es.readtoowell.api_biblioteca.service.book.BookService;

import java.util.Set;

@RestController
@RequestMapping("/libros")
public class BookController {
    @Autowired
    private BookService bookService;
    @Autowired
    private UserService userService;

    /**
     * Devuelve todos los libros.
     *
     * @param page Número de la página que se quiere devolver
     * @param size Tamaño de la página
     * @return Página con los libros resultantes como DTOs
     */
    @GetMapping
    public ResponseEntity<Page<BookDTO>> getBooks(@RequestParam(value = "page", defaultValue = "0") int page,
                                                  @RequestParam(value = "size", defaultValue = "10") int size) {

        Page<BookDTO> bookPage = bookService.getAllBooks(page, size);

        return ResponseEntity.ok(bookPage);
    }

    /**
     * Devuelve un libro según su ID.
     *
     * @param idBook ID del libro a devolver
     * @return DTO con los datos del libro
     */
    @GetMapping("/{idBook}")
    public ResponseEntity<BookDTO> getBook(@PathVariable Long idBook) {
        BookDTO libro = bookService.getBook(idBook);

        return ResponseEntity.ok(libro);
    }

    /**
     * Crea un nuevo libro.
     *
     * @param book DTO con los datos del libro a añadir
     * @param genreIds Lista con los IDs de los géneros asociados al libro
     * @return DTO con los datos del libro creado
     */
    @PostMapping
    public ResponseEntity<BookDTO> createBook(@Valid @RequestBody BookDTO book,
                                              @RequestParam Set<Long> genreIds) {
        BookDTO newBook = bookService.createBook(book, genreIds);

        return ResponseEntity.status(HttpStatus.CREATED).body(newBook);
    }

    /**
     * Actualiza los datos de un libro.
     *
     * @param idBook ID del libro a actualizar
     * @param book DTO con los datos a actualizar del libro
     * @param genreIds Lista con los IDs de los géneros asociados al libro
     * @return DTO con los datos del libro actualizado
     */
    @PutMapping("/{idBook}")
    public ResponseEntity<BookDTO> updateBook(@PathVariable Long idBook,
                                              @Valid @RequestBody BookDTO book,
                                              @RequestParam Set<Long> genreIds) {
        BookDTO libro = bookService.updateBook(idBook, book, genreIds);

        return ResponseEntity.ok(libro);
    }

    /**
     * Elimina un libro.
     *
     * @param idBook ID del libro
     * @return DTO con los datos del libro borrado
     */
    @DeleteMapping("/{idBook}")
    public ResponseEntity<BookDTO> deleteBook(@PathVariable Long idBook) {
        BookDTO libro = bookService.getBook(idBook);

        BookDTO dto = bookService.deleteBook(libro);

        return ResponseEntity.ok(dto);
    }

    /**
     * Busca libros por su título, autor o colección, y permite filtrar por número de páginas y año de publicación.
     *
     * @param searchString Cadena que se compara con el título, autor o colección
     * @param minPages Mínimo de páginas para filtrar
     * @param maxPages Máximo de páginas para filtrar
     * @param minYear Año de publicación mínimo para filtrar
     * @param maxYear Año de publicación máximo para filtrar
     * @param page Número de la página que se quiere devolver
     * @param size Tamaño de la página
     * @return Página con los libros resultantes como DTOs
     */
    @GetMapping("/buscar")
    public ResponseEntity<Page<BookDTO>> searchBooks(
            @RequestParam(required = false) String searchString,
            @RequestParam(required = false) Integer minPages,
            @RequestParam(required = false) Integer maxPages,
            @RequestParam(required = false) Integer minYear,
            @RequestParam(required = false) Integer maxYear,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size)
    {
        Page<BookDTO> libros = bookService.filterBooks(searchString, minPages, maxPages, minYear,
                maxYear, page, size);

        return ResponseEntity.ok(libros);
    }

    /**
     * Busca los libros de un género específico.
     *
     * @param idGenre ID del género que se busca
     * @param page Número de la página que se quiere devolver
     * @param size Tamaño de la página
     * @return Página con los libros resultantes como DTOs
     */
    @GetMapping("/buscar-genero")
    public ResponseEntity<Page<BookDTO>> searchBooksByGenre(@RequestParam Long idGenre,
                                                            @RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "10") int size) {
        Page<BookDTO> libros = bookService.filterBooksByGenre(idGenre, page, size);

        return ResponseEntity.ok(libros);
    }

    /**
     * Devuelve los detalles completos de un libro para un usuario.
     * Incluye la calificación, reseña y listas del usuario.
     *
     * @param idBook ID del libro
     * @return DTO con los detalles completos del libro
     * @throws AccessDeniedException Usuario no autenticado
     */
    @GetMapping("/{idBook}/detalles")
    public ResponseEntity<BookDetailsDTO> getBookDetails(@PathVariable Long idBook) {

        User user = userService.getAuthenticatedUser();
        if (user == null) {
            throw new AccessDeniedException("Usuario no autenticado.");
        }

        BookDetailsDTO details = bookService.getBookDetails(idBook, user);

        return ResponseEntity.ok(details);
    }

    /**
     * Devuelve los libros escritos por un autor que tiene cuenta de usuario.
     *
     * @param idAuthor ID de usuario del autor
     * @return DTO con los datos del autor y sus libros escritos
     */
    @GetMapping("/{idAuthor}/autor")
    public ResponseEntity<AuthorDTO> getBooksByAuthor(@PathVariable Long idAuthor) {
        AuthorDTO author = bookService.getBooksByAuthor(idAuthor);

        return ResponseEntity.ok(author);
    }

    /**
     * Devuelve los libros escritos por un autor.
     *
     * @param authorName Nombre del autor
     * @return Lista con los libros escritos por el autor
     */
    @GetMapping("/libros-autor")
    public ResponseEntity<Page<BookDTO>> getAllBooksByAuthor(@RequestParam String authorName,
                                                             @RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "10") int size) {
        Page<BookDTO> books = bookService.getAllBooksByAuthor(authorName, page, size);

        return ResponseEntity.ok(books);
    }

    /**
     * Devuelve el resto de libros de la colección a la que pertenece un libro.
     *
     * @param idBook ID del libro de la colección
     * @return Lista de libros de la colección, exceptuando el libro indicado
     */
    @GetMapping("/coleccion/{idBook}")
    public ResponseEntity<Set<BookDTO>> getOtherBooksFromCollection(@PathVariable Long idBook) {
        Set<BookDTO> books = bookService.getOtherBooksFromCollection(idBook);

        return ResponseEntity.ok(books);
    }
}
