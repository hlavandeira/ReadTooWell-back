package es.readtoowell.api_biblioteca.service;

import es.readtoowell.api_biblioteca.mapper.GenreMapper;
import es.readtoowell.api_biblioteca.mapper.GoalMapper;
import es.readtoowell.api_biblioteca.mapper.UserLibraryBookMapper;
import es.readtoowell.api_biblioteca.model.*;
import es.readtoowell.api_biblioteca.model.DTO.GoalDTO;
import es.readtoowell.api_biblioteca.model.DTO.SimpleBookDTO;
import es.readtoowell.api_biblioteca.model.DTO.UserLibraryBookDTO;
import es.readtoowell.api_biblioteca.model.DTO.YearRecapDTO;
import es.readtoowell.api_biblioteca.model.enums.ReadingStatus;
import es.readtoowell.api_biblioteca.repository.BookRepository;
import es.readtoowell.api_biblioteca.repository.GoalRepository;
import es.readtoowell.api_biblioteca.repository.UserLibraryBookRepository;
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

    public Page<UserLibraryBookDTO> getLibraryFromUser(User user, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<UserLibraryBook> libros = libraryRepository.findByUsuario(user, pageable);

        return libros.map(libraryMapper::toDTO);
    }

    public Page<UserLibraryBookDTO> getLibraryByStatus(User user, int status, int page, int size) {
        if (status < 0 || status > 4) {
            throw new ValidationException("El estado de lectura indicado es inválido.");
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<UserLibraryBook> libros = libraryRepository.findByUsuarioAndEstadoLectura(user, status, pageable);

        return libros.map(libraryMapper::toDTO);
    }

    public UserLibraryBookDTO addBookToLibrary(Long idBook, User user) {
        Book book = bookRepository.findById(idBook)
                .orElseThrow(() -> new EntityNotFoundException("Libro con ID " + idBook + " no encontrado."));

        UserLibraryBook libro = new UserLibraryBook();
        libro.setId(new UserLibraryBookId(user.getId(), book.getId()));
        libro.setUsuario(user);
        libro.setLibro(book);
        libro.setProgreso(0);
        libro.setTipoProgreso("porcentaje");
        libro.setEstadoLectura(ReadingStatus.PENDIENTE.getValue());
        libro.setCalificacion(0);

        libro = libraryRepository.save(libro);

        return libraryMapper.toDTO(libro);
    }

    public UserLibraryBookDTO deleteBookFromLibrary(Long idBook, User user) {
        Book book = bookRepository.findById(idBook)
                .orElseThrow(() -> new EntityNotFoundException("Libro con ID " + idBook + " no encontrado."));

        UserLibraryBook libroBiblio = libraryRepository.findByUsuarioAndLibro(user, book)
                .orElseThrow(() -> new EntityNotFoundException("El libro no pertenece a la biblioteca del usuario."));

        libraryRepository.delete(libroBiblio);

        return libraryMapper.toDTO(libroBiblio);
    }

    public UserLibraryBookDTO rateBook(Long idBook, User user, double calificacion) {
        if (calificacion < 0.5 || calificacion > 5 || calificacion % 0.5 != 0) { // Comprobar que la nota sea válida
            throw new ValidationException("La calificación debe estar entre 0.5 y 5, en incrementos de 0.5.");
        }

        Book book = bookRepository.findById(idBook)
                .orElseThrow(() -> new EntityNotFoundException("Libro con ID " + idBook + " no encontrado."));

        // Comprobar si está en la biblioteca: si no lo está, se añade
        UserLibraryBook libro = libraryRepository.findByUsuarioAndLibro(user, book)
                .orElseGet(() -> {
                    UserLibraryBookDTO dto = addBookToLibrary(book.getId(), user);
                    return libraryMapper.toEntity(dto);
                });

        libro.setCalificacion(calificacion);
        libro.setEstadoLectura(ReadingStatus.LEIDO.getValue());

        libro = libraryRepository.save(libro);

        return libraryMapper.toDTO(libro);
    }

    public UserLibraryBookDTO reviewBook(Long idBook, User user, String review) {
        if (review.length() > 2000) {
            throw new ValidationException("La reseña es demasiado larga.");
        }

        Book book = bookRepository.findById(idBook)
                .orElseThrow(() -> new EntityNotFoundException("Libro con ID " + idBook + " no encontrado."));

        UserLibraryBook libro = libraryRepository.findByUsuarioAndLibro(user, book)
                .orElseThrow(() -> new EntityNotFoundException("El libro no pertenece a la biblioteca del usuario."));

        libro.setReseña(review);

        libro = libraryRepository.save(libro);

        return libraryMapper.toDTO(libro);
    }

    public UserLibraryBookDTO updateReadingStatus(Long idBook, User user, int status) {
        if (status < 0 || status > 4) {
            throw new ValidationException("El estado de lectura proporcionado es inválido.");
        }

        Book book = bookRepository.findById(idBook)
                .orElseThrow(() -> new EntityNotFoundException("Libro con ID " + idBook + " no encontrado."));

        UserLibraryBook libro = libraryRepository.findByUsuarioAndLibro(user, book)
                .orElseThrow(() -> new EntityNotFoundException("El libro no pertenece a la biblioteca del usuario."));

        int lastStatus = libro.getEstadoLectura();

        libro.setEstadoLectura(status);

        if (status == ReadingStatus.LEYENDO.getValue()) {
            libro.setFechaInicio(Date.valueOf(LocalDate.now())); // Si pasa a "Leyendo", actualizar fecha inicio
        } else if (status == ReadingStatus.LEIDO.getValue() && lastStatus == ReadingStatus.LEYENDO.getValue()) {
            libro.setFechaFin(Date.valueOf(LocalDate.now())); // Si pasa de "Leyendo" a "Leído", actualizar fecha fin
            goalService.actualizarObjetivos(user.getId(), 0);
        } else if (status == ReadingStatus.PENDIENTE.getValue() && (lastStatus == ReadingStatus.PAUSADO.getValue()
                    || lastStatus == ReadingStatus.ABANDONADO.getValue())) {
            libro.setFechaInicio(null); // Si pasa de "Pausado" o "Abandonado" a "Pendiente", quitar fecha inicio
        }

        libro = libraryRepository.save(libro);

        return libraryMapper.toDTO(libro);
    }

    public UserLibraryBookDTO updateProgress(Long idBook, User user, int progreso, String tipoProgreso) {
        if (!tipoProgreso.equals("porcentaje") && !tipoProgreso.equals("paginas")) {
            throw new ValidationException("El tipo de progreso de lectura proporcionado es inválido.");
        }
        if (progreso < 0) {
            throw new ValidationException("La cantidad del progreso es inválida.");
        }

        Book book = bookRepository.findById(idBook)
                .orElseThrow(() -> new EntityNotFoundException("Libro con ID " + idBook + " no encontrado."));

        UserLibraryBook libro = libraryRepository.findByUsuarioAndLibro(user, book)
                .orElseThrow(() -> new EntityNotFoundException("El libro no pertenece a la biblioteca del usuario."));

        if (libro.getEstadoLectura() != ReadingStatus.LEYENDO.getValue()) {
            throw new IllegalStateException("No se puede actualizar el progreso de un libro que no esté en 'Leyendo'");
        }

        int progresoTotal = progreso;
        if (tipoProgreso.equals("porcentaje") && progreso >= 100) {
            progresoTotal = 100;
            libro.setEstadoLectura(ReadingStatus.LEIDO.getValue());
            libro.setFechaFin(Date.valueOf(LocalDate.now()));

        } else if (tipoProgreso.equals("paginas") && progreso >= book.getNumeroPaginas()) {
            progresoTotal = book.getNumeroPaginas();
            libro.setEstadoLectura(ReadingStatus.LEIDO.getValue());
            libro.setFechaFin(Date.valueOf(LocalDate.now()));
        }

        libro.setProgreso(progresoTotal);
        libro.setTipoProgreso(tipoProgreso);

        libro = libraryRepository.save(libro);

        if (tipoProgreso.equals("porcentaje")) {
            int paginasLeidas = (int) Math.round((progresoTotal / 100.0) * book.getNumeroPaginas());
            goalService.actualizarObjetivos(user.getId(), paginasLeidas);
        } else if (tipoProgreso.equals("paginas")) {
            goalService.actualizarObjetivos(user.getId(), progresoTotal);
        }

        return libraryMapper.toDTO(libro);
    }

    public YearRecapDTO getYearRecap(User user) {
        List<Goal> goals = goalRepository.findAnnualGoalsForCurrentYear(user.getId());

        YearRecapDTO recap = new YearRecapDTO();
        List<GoalDTO> goalsDTO = goals.stream().map(goalMapper::toDTO).collect(Collectors.toList());
        recap.setObjetivoAnual(goalsDTO);

        long numBooks = libraryRepository.countBooksReadInCurrentYear(user.getId());
        long numPages = libraryRepository.sumPagesReadInCurrentYear(user.getId());

        recap.setTotalLibros(numBooks);
        recap.setTotalPaginas(numPages);

        List<Genre> topGenres = libraryRepository.findTopGenresForCurrentYear(user.getId());
        List<Book> topBooks = libraryRepository.findTopRatedBooksByUserForCurrentYear(user.getId(), 4);

        recap.setGenerosMasLeidos(topGenres.stream().map(genreMapper::toDTO).collect(Collectors.toList()));
        recap.setLibrosMejorValorados(topBooks.stream().map(b -> {
            SimpleBookDTO book = new SimpleBookDTO();

            book.setId(b.getId());
            book.setTitulo(b.getTitulo());
            book.setAutor(b.getAutor());
            book.setPortada(b.getPortada());

            Optional<UserLibraryBook> libBook = libraryRepository.findByUsuarioAndLibro(user, b);

            book.setCalificacion(libBook.get().getCalificacion());

            return book;
        }).collect(Collectors.toList()));

        return recap;
    }
}
