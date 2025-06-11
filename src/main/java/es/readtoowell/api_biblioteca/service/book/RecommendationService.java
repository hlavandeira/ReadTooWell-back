package es.readtoowell.api_biblioteca.service.book;

import es.readtoowell.api_biblioteca.mapper.BookMapper;
import es.readtoowell.api_biblioteca.model.DTO.book.RatedBookDTO;
import es.readtoowell.api_biblioteca.model.entity.Book;
import es.readtoowell.api_biblioteca.model.entity.BookList;
import es.readtoowell.api_biblioteca.model.entity.BookListItem;
import es.readtoowell.api_biblioteca.model.entity.Genre;
import es.readtoowell.api_biblioteca.repository.book.BookListRepository;
import es.readtoowell.api_biblioteca.repository.book.BookRepository;
import es.readtoowell.api_biblioteca.repository.library.UserLibraryBookRepository;
import es.readtoowell.api_biblioteca.service.user.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Servicio encargado de gestionar la lógica relacionada con las recomendaciones.
 */
@Service
public class RecommendationService {
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private UserLibraryBookRepository libraryRepository;
    @Autowired
    private BookListRepository listRepository;
    @Autowired
    private BookMapper bookMapper;

    /**
     * Busca posibles recomendaciones a partir de los libros favoritos de un usuario.
     *
     * @param idUser ID del usuario
     * @return Lista con libros recomendados
     */
    public List<RatedBookDTO> getRecommendationsByFavoriteBooks(Long idUser) {
        List<Long> favBooksIds = userService.getFavorites(idUser).getFavoriteBooks()
                .stream().map(book -> book.getId()).toList();

        List<Book> libros = bookRepository.findSimilarBooksByFavoriteBooks(favBooksIds, idUser);

        return fromBookToRatedBookDTO(libros);
    }

    /**
     * Busca posibles recomendaciones a partir de los géneros favoritos de un usuario.
     *
     * @param idUser ID del usuario
     * @return Lista con libros recomendados
     */
    public List<RatedBookDTO> getRecommendationsByFavoriteGenres(Long idUser) {
        List<Long> favGenresIds = userService.getFavorites(idUser).getFavoriteGenres()
                .stream().map(genre -> genre.getId()).toList();

        List<Book> libros = bookRepository.findBooksWithSimilarGenres(favGenresIds, idUser);

        return fromBookToRatedBookDTO(libros);
    }

    /**
     * Busca posibles recomendaciones a partir de los libros leídos de un usuario que tengan
     * una calificación superior o igual a 3 sobre 5.
     *
     * @param idUser ID del usuario
     * @return Lista con libros recomendados
     */
    public List<RatedBookDTO> getRecommendationsByReadBooks(Long idUser) {
        List<Book> libros = bookRepository.findBooksSimilarToReadOnes(idUser);

        return fromBookToRatedBookDTO(libros);
    }

    /**
     * Busca posibles recomendaciones a partir de una lista de un usuario, utilizando los libros de la lista
     * y los géneros asociados a esta.
     *
     * @param idUser ID del usuario
     * @param idList ID de la lista
     * @return Lista con libros recomendados
     * @throws EntityNotFoundException La lista no existe
     * @throws AccessDeniedException El usuario no es propietario de la lista
     */
    public List<RatedBookDTO> getRecommendationsByList(Long idUser, Long idList) {
        BookList lista = listRepository.findById(idList)
                .orElseThrow(() -> new EntityNotFoundException("La lista con ID " + idList + " no existe."));
        if (lista.getUser().getId() != idUser) {
            throw new AccessDeniedException("Solo el propietario de la lista puede acceder a esta.");
        }

        List<Book> libros;

        if (lista.getBooks().size() > 0) {
            if (lista.getBooks().size() > 3) { // Si la lista tiene más de 3 libros, usarlos para buscar recomendaciones

                List<Long> bookIds = lista.getBooks().stream().map(book -> book.getId().getBookId()).toList();
                libros = bookRepository.findSimilarBooksByFavoriteBooks(bookIds, idUser);

            } else { // Si tiene entre 1 y 3 libros, usar los libros y los géneros de la lista

                Set<Long> allGenresIds = new HashSet<>();

                for (BookListItem b : lista.getBooks()) {
                    Book book = b.getBook();
                    for (Genre g : book.getGenres()) {
                        allGenresIds.add(g.getId());
                    }
                }
                for (Genre g : lista.getGenres()) {
                    allGenresIds.add(g.getId());
                }

                List<Long> genresIds = new ArrayList<>(allGenresIds);
                libros = bookRepository.findBooksWithSimilarGenres(genresIds, idUser);
            }
        } else {
            if (lista.getGenres().size() > 0) { // Si tiene géneros, buscar según estos

                List<Long> genreIds = lista.getGenres().stream().map(genre -> genre.getId()).toList();
                libros = bookRepository.findBooksWithSimilarGenres(genreIds, idUser);

            } else { // Si no tiene ni libros ni géneros, no hay información para buscar recomendaciones
                return null;
            }
        }

        return fromBookToRatedBookDTO(libros);
    }

    /**
     * Busca recomendaciones generales para cualquier usuario, en función de los libros más populares
     * publicados en los últimos años.
     *
     * @param idUser ID del usuario
     * @return Lista con libros recomendados
     */
    public List<RatedBookDTO> getGeneralRecommendations(Long idUser) {
        int minYear = LocalDate.now().getYear() - 5;
        List<Book> libros = bookRepository.findGeneralRecommendations(idUser, minYear);

        return fromBookToRatedBookDTO(libros);
    }

    /**
     * Convierte una lista de {@code Book} en una lista de {@code RatedBookDTO}, calculando la calificación
     * media de cada libro.
     *
     * @param libros Lista de libros
     * @return Lista de libros con calificación media de cada uno
     */
    private List<RatedBookDTO> fromBookToRatedBookDTO(List<Book> libros) {
        return libros.stream().map(book -> {
            RatedBookDTO dto = new RatedBookDTO();
            dto.setBook(bookMapper.toDTO(book));
            dto.setAverageRating(libraryRepository.findAverageRatingByBookId(book.getId()));
            return dto;
        }).toList();
    }
}
