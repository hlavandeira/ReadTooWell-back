package es.readtoowell.api_biblioteca.service;

import es.readtoowell.api_biblioteca.mapper.FormatMapper;
import es.readtoowell.api_biblioteca.model.*;
import es.readtoowell.api_biblioteca.model.DTO.FormatDTO;
import es.readtoowell.api_biblioteca.repository.*;
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

    public List<FormatDTO> getFormatsForUserBook(Long idBook, Long idUser) {

        return userFormatRepository.findFormatsByUserAndBook(idUser, idBook)
                .stream()
                .map(UserBookFormat::getFormato)
                .map(formatMapper::toDTO)
                .collect(Collectors.toList());
    }

    public FormatDTO addFormatToBook(Long idBook, Long idUser, Long idFormat) {
        User user = userRepository.findById(idUser)
                .orElseThrow(() -> new EntityNotFoundException("El usuario con ID " + idUser + " no existe."));
        Book book = bookRepository.findById(idBook)
                .orElseThrow(() -> new EntityNotFoundException("El libro con ID " + idBook + " no existe."));
        Format format = formatRepository.findById(idFormat)
                .orElseThrow(() -> new EntityNotFoundException("El formato con ID " + idFormat + " no existe."));
        // El libro debe pertenecer a la biblioteca del usuario
        UserLibraryBook userLibraryBook = libraryRepository.findByUsuarioAndLibro(user, book)
                .orElseThrow(() -> new EntityNotFoundException("El libro no pertenece a la biblioteca del usuario."));

        // Comprobar que el formato no está ya asociado
        boolean alreadyExists = userFormatRepository.existsByLibroBibliotecaAndFormato(userLibraryBook, format);

        if (alreadyExists) {
            throw new IllegalStateException("El formato ya está asociado a este libro para el usuario.");
        }

        UserBookFormat userBookFormat = new UserBookFormat();
        userBookFormat.setLibroBiblioteca(userLibraryBook);
        userBookFormat.setFormato(format);
        UserBookFormatId id = new UserBookFormatId(new UserLibraryBookId(idUser, idBook), idFormat);
        userBookFormat.setId(id);
        userFormatRepository.save(userBookFormat);

        return formatMapper.toDTO(format);
    }

    public void removeFormatFromBook(Long idBook, Long idUser, Long idFormat) {
        User user = userRepository.findById(idUser)
                .orElseThrow(() -> new EntityNotFoundException("El usuario con ID " + idUser + " no existe."));
        Book book = bookRepository.findById(idBook)
                .orElseThrow(() -> new EntityNotFoundException("El libro con ID " + idBook + " no existe."));
        Format format = formatRepository.findById(idFormat)
                .orElseThrow(() -> new EntityNotFoundException("El formato con ID " + idFormat + " no existe."));
        // El libro debe pertenecer a la biblioteca del usuario
        UserLibraryBook userLibraryBook = libraryRepository.findByUsuarioAndLibro(user, book)
                .orElseThrow(() -> new EntityNotFoundException("El libro no pertenece a la biblioteca del usuario."));

        UserBookFormat userBookFormat = userFormatRepository.findByLibroBibliotecaAndFormato(userLibraryBook, format)
                .orElseThrow(() ->
                        new EntityNotFoundException("El formato no está asociado a este libro para el usuario."));

        userFormatRepository.delete(userBookFormat);

        //return formatMapper.toDTO(format);
    }
}
