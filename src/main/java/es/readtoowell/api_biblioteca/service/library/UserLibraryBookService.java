package es.readtoowell.api_biblioteca.service.library;

import es.readtoowell.api_biblioteca.mapper.GenreMapper;
import es.readtoowell.api_biblioteca.mapper.GoalMapper;
import es.readtoowell.api_biblioteca.mapper.UserLibraryBookMapper;
import es.readtoowell.api_biblioteca.model.DTO.GoalDTO;
import es.readtoowell.api_biblioteca.model.DTO.SimpleBookDTO;
import es.readtoowell.api_biblioteca.model.DTO.UserLibraryBookDTO;
import es.readtoowell.api_biblioteca.model.DTO.YearRecapDTO;
import es.readtoowell.api_biblioteca.model.entity.*;
import es.readtoowell.api_biblioteca.model.entity.id.UserLibraryBookId;
import es.readtoowell.api_biblioteca.model.enums.ReadingStatus;
import es.readtoowell.api_biblioteca.repository.book.BookRepository;
import es.readtoowell.api_biblioteca.repository.goal.GoalRepository;
import es.readtoowell.api_biblioteca.repository.library.UserLibraryBookRepository;
import es.readtoowell.api_biblioteca.service.goal.GoalService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserLibraryBookService {
    @Autowired
    private UserLibraryBookRepository libraryRepository;
    @Autowired
    private UserLibraryBookMapper libraryMapper;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private GoalRepository goalRepository;
    @Autowired
    private GoalMapper goalMapper;
    @Autowired
    private GenreMapper genreMapper;
    @Autowired
    private GoalService goalService;

    /**
     * Devuelve los libros de la biblioteca de un usuario.
     *
     * @param user Usuario propietario de la biblioteca
     * @param page Número de la página que se quiere devolver
     * @param size Tamaño de la página
     * @return Página con los libros de la biblioteca del usuario como DTOs
     */
    public Page<UserLibraryBookDTO> getLibraryFromUser(User user, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<UserLibraryBook> libros = libraryRepository.findByUser(user, pageable);

        return libros.map(libraryMapper::toDTO);
    }

    /**
     * Devuelve los libros de la biblioteca de un usuario con un estado de lectura concreto.
     *
     * @param user Usuario propietario de la biblioteca
     * @param status Estado por el que se quiere filtrar
     * @param page Número de la página que se quiere devolver
     * @param size Tamaño de la página
     * @return Página con los libros de la biblioteca del usuario filtrados como DTOs
     * @throws ValidationException El estado de lectura es inválido
     */
    public Page<UserLibraryBookDTO> getLibraryByStatus(User user, int status, int page, int size) {
        if (status < 0 || status > 4) {
            throw new ValidationException("El estado de lectura indicado es inválido.");
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<UserLibraryBook> libros = libraryRepository.findByUserAndReadingStatus(user, status, pageable);

        return libros.map(libraryMapper::toDTO);
    }

    /**
     * Añade un libro a la biblioteca de un usuario.
     *
     * @param idBook ID del libro a añadir
     * @param user Usuario que añade el libro a su biblioteca
     * @return DTO con los datos del libro añadido
     * @throws EntityNotFoundException El libro no existe
     * @throws IllegalStateException El libro ya está en la biblioteca
     */
    public UserLibraryBookDTO addBookToLibrary(Long idBook, User user) {
        Book book = bookRepository.findById(idBook)
                .orElseThrow(() -> new EntityNotFoundException("Libro con ID " + idBook + " no encontrado."));

        Optional<UserLibraryBook> libroBiblio = libraryRepository.findByUserAndBook(user, book);
        libroBiblio.ifPresent(lib -> {
                    throw new IllegalStateException("El libro ya pertenece a la biblioteca del usuario.");
                });

        UserLibraryBook libro = new UserLibraryBook();
        libro.setId(new UserLibraryBookId(user.getId(), book.getId()));
        libro.setUser(user);
        libro.setBook(book);
        libro.setProgress(0);
        libro.setProgressType("porcentaje");
        libro.setReadingStatus(ReadingStatus.PENDING.getValue());
        libro.setRating(0);

        libro = libraryRepository.save(libro);

        return libraryMapper.toDTO(libro);
    }

    /**
     * Elimina un libro de la bilbioteca de un usuario.
     *
     * @param idBook ID del libro a eliminar
     * @param user Usuario que elimina el libro de su biblioteca
     * @return DTO con los datos del libro eliminado
     * @throws EntityNotFoundException El libro no existe
     * @throws IllegalStateException El libro no pertenece a la biblioteca del usuario
     */
    public UserLibraryBookDTO deleteBookFromLibrary(Long idBook, User user) {
        Book book = bookRepository.findById(idBook)
                .orElseThrow(() -> new EntityNotFoundException("Libro con ID " + idBook + " no encontrado."));

        UserLibraryBook libroBiblio = libraryRepository.findByUserAndBook(user, book)
                .orElseThrow(() -> new IllegalStateException("El libro no pertenece a la biblioteca del usuario."));

        libraryRepository.delete(libroBiblio);

        return libraryMapper.toDTO(libroBiblio);
    }

    /**
     * Califica un libro.
     *
     * @param idBook ID del libro
     * @param user Usuario que califica el libro
     * @param calificacion Calificación
     * @return DTO con los datos del libro actualizado
     * @throws ValidationException La calificación es inválida
     */
    public UserLibraryBookDTO rateBook(Long idBook, User user, double calificacion) {
        if (calificacion < 0.5 || calificacion > 5 || calificacion % 0.5 != 0) { // Comprobar que la nota sea válida
            throw new ValidationException("La calificación debe estar entre 0.5 y 5, en incrementos de 0.5.");
        }

        Book book = bookRepository.findById(idBook)
                .orElseThrow(() -> new EntityNotFoundException("Libro con ID " + idBook + " no encontrado."));

        // Comprobar si está en la biblioteca: si no lo está, se añade
        UserLibraryBook libro = libraryRepository.findByUserAndBook(user, book)
                .orElseGet(() -> {
                    UserLibraryBookDTO dto = addBookToLibrary(book.getId(), user);
                    return libraryMapper.toEntity(dto);
                });

        libro.setRating(calificacion);
        libro.setReadingStatus(ReadingStatus.READ.getValue());

        libro = libraryRepository.save(libro);

        return libraryMapper.toDTO(libro);
    }

    /**
     * Añade una reseña a un libro.
     *
     * @param idBook ID del libro
     * @param user Usuario que escribe la reseña
     * @param review Reseña
     * @return DTO con los datos del libro actualizado
     * @throws ValidationException Reseña demasiado larga
     * @throws EntityNotFoundException El libro no existe
     * @throws IllegalStateException El libro no pertenece a la biblioteca del usuario
     */
    public UserLibraryBookDTO reviewBook(Long idBook, User user, String review) {
        if (review.length() > 2000) {
            throw new ValidationException("La reseña es demasiado larga.");
        }

        Book book = bookRepository.findById(idBook)
                .orElseThrow(() -> new EntityNotFoundException("Libro con ID " + idBook + " no encontrado."));

        UserLibraryBook libro = libraryRepository.findByUserAndBook(user, book)
                .orElseThrow(() -> new IllegalStateException("El libro no pertenece a la biblioteca del usuario."));

        libro.setReview(review);

        libro = libraryRepository.save(libro);

        return libraryMapper.toDTO(libro);
    }

    /**
     * Actualiza el estado de lectura de un libro.
     *
     * @param idBook ID del libro
     * @param user Usuario que actualiza el estado
     * @param status Nuevo estado de lectura
     * @return DTO con los datos del libro actualizado
     * @throws ValidationException Estado de lectura inválido
     * @throws EntityNotFoundException El libro no existe
     * @throws IllegalStateException El libro no pertenece a la biblioteca del usuario
     */
    public UserLibraryBookDTO updateReadingStatus(Long idBook, User user, int status) {
        if (status < 0 || status > 4) {
            throw new ValidationException("El estado de lectura proporcionado es inválido.");
        }

        Book book = bookRepository.findById(idBook)
                .orElseThrow(() -> new EntityNotFoundException("Libro con ID " + idBook + " no encontrado."));

        UserLibraryBook libro = libraryRepository.findByUserAndBook(user, book)
                .orElseThrow(() -> new IllegalStateException("El libro no pertenece a la biblioteca del usuario."));

        int lastStatus = libro.getReadingStatus();

        libro.setReadingStatus(status);

        if (status == ReadingStatus.READING.getValue()) {
            libro.setDateStart(Date.valueOf(LocalDate.now())); // Si pasa a "Leyendo", actualizar fecha inicio
        } else if (status == ReadingStatus.READ.getValue() && lastStatus == ReadingStatus.READING.getValue()) {
            libro.setDateFinish(Date.valueOf(LocalDate.now())); // Si pasa de "Leyendo" a "Leído", actualizar fecha fin
            goalService.updateGoals(user.getId(), 0);
        } else if (status == ReadingStatus.PENDING.getValue() && (lastStatus == ReadingStatus.PAUSED.getValue()
                    || lastStatus == ReadingStatus.ABANDONED.getValue())) {
            libro.setDateStart(null); // Si pasa de "Pausado" o "Abandonado" a "Pendiente", quitar fecha inicio
        }

        libro = libraryRepository.save(libro);

        return libraryMapper.toDTO(libro);
    }

    /**
     * Actualiza el progreso de lectura de un libro.
     *
     * @param idBook ID del libro
     * @param user Usuario que actualiza el progreso
     * @param progreso Nuevo progreso del libro
     * @param tipoProgreso Tipo de progreso
     * @return DTO con los datos del libro actualizado
     * @throws ValidationException El tipo de progreso o la cantidad del progreso son inválidos
     * @throws EntityNotFoundException El libro no existe
     * @throws IllegalStateException El libro no pertenece a la biblioteca o no está en estado 'Leyendo'
     */
    public UserLibraryBookDTO updateProgress(Long idBook, User user, int progreso, String tipoProgreso) {
        if (!tipoProgreso.equals("porcentaje") && !tipoProgreso.equals("paginas")) {
            throw new ValidationException("El tipo de progreso de lectura proporcionado es inválido.");
        }
        if (progreso < 0) {
            throw new ValidationException("La cantidad del progreso es inválida.");
        }

        Book book = bookRepository.findById(idBook)
                .orElseThrow(() -> new EntityNotFoundException("Libro con ID " + idBook + " no encontrado."));

        UserLibraryBook libro = libraryRepository.findByUserAndBook(user, book)
                .orElseThrow(() -> new IllegalStateException("El libro no pertenece a la biblioteca del usuario."));

        if (libro.getReadingStatus() != ReadingStatus.READING.getValue()) {
            throw new IllegalStateException("No se puede actualizar el progreso de un libro que no esté en 'Leyendo'");
        }

        int progresoTotal = progreso;
        if (tipoProgreso.equals("porcentaje") && progreso >= 100) {
            progresoTotal = 100;
            libro.setReadingStatus(ReadingStatus.READ.getValue());
            libro.setDateStart(Date.valueOf(LocalDate.now()));

        } else if (tipoProgreso.equals("paginas") && progreso >= book.getPageNumber()) {
            progresoTotal = book.getPageNumber();
            libro.setReadingStatus(ReadingStatus.READ.getValue());
            libro.setDateFinish(Date.valueOf(LocalDate.now()));
        }

        libro.setProgress(progresoTotal);
        libro.setProgressType(tipoProgreso);

        libro = libraryRepository.save(libro);

        // Actualizar los objetivos en curso con las páginas correspondientes
        if (tipoProgreso.equals("porcentaje")) {
            int paginasLeidas = (int) Math.round((progresoTotal / 100.0) * book.getPageNumber());
            goalService.updateGoals(user.getId(), paginasLeidas);
        } else if (tipoProgreso.equals("paginas")) {
            goalService.updateGoals(user.getId(), progresoTotal);
        }

        return libraryMapper.toDTO(libro);
    }

    /**
     * Devuelve el resumen anual de un usuario.
     *
     * @param user Usuario que consulta el resumen
     * @return DTO con los datos del resumen anual
     */
    public YearRecapDTO getYearRecap(User user) {
        YearRecapDTO recap = new YearRecapDTO();

        // Objetivos en curso, en caso de que el usuario los tenga
        List<GoalDTO> goalsDTO = goalRepository.findAnnualGoalsForCurrentYear(user.getId())
                .stream().map(goalMapper::toDTO).collect(Collectors.toList());
        recap.setAnnualGoals(goalsDTO);

        // Total de libros y de páginas leídas
        long numBooks = libraryRepository.findBooksReadActualYear(user.getId()).size();
        long numPages = libraryRepository.sumPagesReadInCurrentYear(user.getId());
        recap.setTotalBooksRead(numBooks);
        recap.setTotalPagesRead(numPages);

        // Géneros más leídos (5) y libros mejor valorados (4)
        List<Genre> topGenres = libraryRepository.findTopGenresForCurrentYear(user.getId(), 5);
        List<Book> topBooks = libraryRepository.findTopRatedBooksByUserForCurrentYear(user.getId(), 4);

        recap.setMostReadGenres(topGenres.stream().map(genreMapper::toDTO).collect(Collectors.toList()));
        recap.setTopRatedBooks(topBooks.stream().map(b -> {
            SimpleBookDTO book = new SimpleBookDTO(); // SimpleBookDTO porque no se necesitan todos los datos de BookDTO

            book.setId(b.getId());
            book.setTitle(b.getTitle());
            book.setAuthor(b.getAuthor());
            book.setCover(b.getCover());

            Optional<UserLibraryBook> libBook = libraryRepository.findByUserAndBook(user, b);

            book.setRating(libBook.get().getRating());

            return book;
        }).collect(Collectors.toList()));

        return recap;
    }
}
