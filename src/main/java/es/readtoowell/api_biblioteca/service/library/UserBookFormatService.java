package es.readtoowell.api_biblioteca.service.library;

import es.readtoowell.api_biblioteca.mapper.FormatMapper;
import es.readtoowell.api_biblioteca.model.DTO.book.FormatDTO;
import es.readtoowell.api_biblioteca.model.entity.*;
import es.readtoowell.api_biblioteca.model.entity.id.UserBookFormatId;
import es.readtoowell.api_biblioteca.model.entity.id.UserLibraryBookId;
import es.readtoowell.api_biblioteca.repository.book.BookRepository;
import es.readtoowell.api_biblioteca.repository.book.FormatRepository;
import es.readtoowell.api_biblioteca.repository.library.UserBookFormatRepository;
import es.readtoowell.api_biblioteca.repository.library.UserLibraryBookRepository;
import es.readtoowell.api_biblioteca.repository.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserBookFormatService {
    @Autowired
    private UserBookFormatRepository userFormatRepository;
    @Autowired
    private FormatMapper formatMapper;
    @Autowired
    private FormatRepository formatRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private UserLibraryBookRepository libraryRepository;

    /**
     * Devuelve los formatos de un libro de un usuario.
     *
     * @param idBook ID del libro
     * @param idUser ID del usuario
     * @return Lista con los formatos que un usuario tiene de un libro
     */
    public List<FormatDTO> getFormatsForUserBook(Long idBook, Long idUser) {

        return userFormatRepository.findFormatsByUserAndBook(idUser, idBook)
                .stream()
                .map(UserBookFormat::getFormat)
                .map(formatMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Añade un formato a un libro de la biblioteca de un usuario.
     *
     * @param idBook ID del libro
     * @param idUser ID del usuario
     * @param idFormat ID del formato a añadir
     * @return DTO con el formato añadido al libro
     * @throws EntityNotFoundException El usuario, libro o formato no existen
     * @throws IllegalStateException El usuario no tiene el libro en su biblioteca o el formato ya está asociado
     */
    public FormatDTO addFormatToBook(Long idBook, Long idUser, Long idFormat) {
        User user = userRepository.findById(idUser)
                .orElseThrow(() -> new EntityNotFoundException("El usuario con ID " + idUser + " no existe."));
        Book book = bookRepository.findById(idBook)
                .orElseThrow(() -> new EntityNotFoundException("El libro con ID " + idBook + " no existe."));
        Format format = formatRepository.findById(idFormat)
                .orElseThrow(() -> new EntityNotFoundException("El formato con ID " + idFormat + " no existe."));
        // El libro debe pertenecer a la biblioteca del usuario
        UserLibraryBook userLibraryBook = libraryRepository.findByUserAndBook(user, book)
                .orElseThrow(() -> new IllegalStateException("El libro no pertenece a la biblioteca del usuario."));

        // Comprobar que el formato no está ya asociado
        boolean alreadyExists = userFormatRepository.existsByLibraryBookAndFormat(userLibraryBook, format);
        if (alreadyExists) {
            throw new IllegalStateException("El formato ya está asociado a este libro para el usuario.");
        }

        UserBookFormat userBookFormat = new UserBookFormat();
        userBookFormat.setLibraryBook(userLibraryBook);
        userBookFormat.setFormat(format);
        UserBookFormatId id = new UserBookFormatId(new UserLibraryBookId(idUser, idBook), idFormat);
        userBookFormat.setId(id);
        userFormatRepository.save(userBookFormat);

        return formatMapper.toDTO(format);
    }

    /**
     * Elimina un formato a un libro de la biblioteca de un usuario.
     *
     * @param idBook ID del libro
     * @param idUser ID del usuario
     * @param idFormat ID del formato a eliminar
     * @throws EntityNotFoundException El usuario, libro o formato no existen
     * @throws IllegalStateException El usuario no tiene el libro en su biblioteca o el formato no está asociado
     */
    public void removeFormatFromBook(Long idBook, Long idUser, Long idFormat) {
        User user = userRepository.findById(idUser)
                .orElseThrow(() -> new EntityNotFoundException("El usuario con ID " + idUser + " no existe."));
        Book book = bookRepository.findById(idBook)
                .orElseThrow(() -> new EntityNotFoundException("El libro con ID " + idBook + " no existe."));
        Format format = formatRepository.findById(idFormat)
                .orElseThrow(() -> new EntityNotFoundException("El formato con ID " + idFormat + " no existe."));
        // El libro debe pertenecer a la biblioteca del usuario
        UserLibraryBook userLibraryBook = libraryRepository.findByUserAndBook(user, book)
                .orElseThrow(() -> new IllegalStateException("El libro no pertenece a la biblioteca del usuario."));

        UserBookFormat userBookFormat = userFormatRepository.findByLibraryBookAndFormat(userLibraryBook, format)
                .orElseThrow(() ->
                        new IllegalStateException("El formato no está asociado a este libro para el usuario."));

        userFormatRepository.delete(userBookFormat);

        //return formatMapper.toDTO(format);
    }
}
