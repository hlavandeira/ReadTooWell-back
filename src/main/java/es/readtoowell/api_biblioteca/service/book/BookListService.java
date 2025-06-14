package es.readtoowell.api_biblioteca.service.book;

import es.readtoowell.api_biblioteca.mapper.BookListItemMapper;
import es.readtoowell.api_biblioteca.mapper.BookListMapper;
import es.readtoowell.api_biblioteca.mapper.BookMapper;
import es.readtoowell.api_biblioteca.mapper.GenreMapper;
import es.readtoowell.api_biblioteca.model.DTO.book.BookListDTO;
import es.readtoowell.api_biblioteca.model.DTO.book.BookListDetailsDTO;
import es.readtoowell.api_biblioteca.model.DTO.book.GenreDTO;
import es.readtoowell.api_biblioteca.model.entity.*;
import es.readtoowell.api_biblioteca.model.entity.id.BookListItemId;
import es.readtoowell.api_biblioteca.repository.book.BookListItemRepository;
import es.readtoowell.api_biblioteca.repository.book.BookListRepository;
import es.readtoowell.api_biblioteca.repository.book.BookRepository;
import es.readtoowell.api_biblioteca.repository.book.GenreRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio encargado de gestionar la lógica relacionada con las listas de libros.
 */
@Service
public class BookListService {
    @Autowired
    private BookListRepository listRepository;
    @Autowired
    private BookListMapper listMapper;
    @Autowired
    private GenreRepository genreRepository;
    @Autowired
    private GenreMapper genreMapper;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private BookListItemRepository bookItemRepository;
    @Autowired
    private BookListItemMapper bookItemMapper;

    /**
     * Devuelve las listas de un usuario.
     *
     * @param idUser ID del usuario
     * @param page Número de la página que se quiere devolver
     * @param size Tamaño de la página
     * @return Página con las listas resultantes como DTOs
     */
    public Page<BookListDTO> getListsByUser(Long idUser, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<BookList> lists = listRepository.findByUserId(idUser, pageable);

        return lists.map(listMapper::toDTO);
    }

    /**
     * Devuelve los libros paginados de una lista, junto con los detalles de esta.
     *
     * @param idUser ID del usuario
     * @param idList ID de la lista
     * @param page Número de la página que se quiere devolver
     * @param size Tamaño de la página
     * @return DTO con los datos de la lista y los libros paginados
     * @throws AccessDeniedException El usuario no es el propietario de la lista
     */
    public BookListDetailsDTO getListDetails(Long idUser, Long idList, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "dateAdded"));
        Page<BookListItem> booksInList = bookItemRepository.findByListId(idList, pageable);

        BookList list = listRepository.findByIdWithRelations(idList)
                .orElseThrow(() -> new EntityNotFoundException("La lista con ID " + idList + " no existe."));

        if (!list.getUser().getId().equals(idUser)) {
            throw new AccessDeniedException("No tienes permiso para consultar esta lista.");
        }

        BookListDetailsDTO listDetails = new BookListDetailsDTO();
        listDetails.setBooks(booksInList.map(bookItemMapper::toDTO));
        listDetails.setId(idList);
        listDetails.setName(list.getName());
        listDetails.setDescription(list.getDescription());
        List<GenreDTO> genres = list.getGenres().stream().map(genreMapper::toDTO).collect(Collectors.toList());
        listDetails.setGenres(genres);

        return listDetails;
    }

    /**
     * Crea una nueva lista para un usuario.
     *
     * @param user Usuario que crea la lista
     * @param dto DTO con los datos de la lista a crear
     * @param genreIds Lista con los IDs de los géneros asociados a la lista
     * @return DTO con los detalles de la lista creada
     */
    public BookListDTO createList(User user, BookListDTO dto, List<Long> genreIds) {
        BookList lista = new BookList();

        lista.setName(dto.getName().trim());
        lista.setDescription(dto.getDescription().trim());
        lista.setUser(user);

        List<Genre> genres = new ArrayList<>(genreRepository.findAllById(genreIds));

        lista.setGenres(genres.stream().collect(Collectors.toSet()));

        lista = listRepository.save(lista);

        return listMapper.toDTO(lista);
    }

    /**
     * Actualiza los datos de una lista de un usuario.
     *
     * @param idUser ID del usuario propietario de la lista
     * @param idList ID de la lista a actualizar
     * @param dto DTO con los datos a actualizar en la lista
     * @param genreIds Lista con los IDs de los géneros asociados a la lista
     * @return DTO con los detalles de la lista actualizada
     * @throws EntityNotFoundException La lista no existe
     * @throws AccessDeniedException Otro usuario intenta acceder a la lista
     */
    public BookListDTO updateList(Long idUser, Long idList, BookListDTO dto, List<Long> genreIds) {
        BookList lista = listRepository.findByIdWithRelations(idList)
                .orElseThrow(() -> new EntityNotFoundException("La lista con ID " + idList + " no existe."));

        if (!lista.getUser().getId().equals(idUser)) {
            throw new AccessDeniedException("No tienes permiso para editar esta lista.");
        }

        lista.setName(dto.getName().trim());
        lista.setDescription(dto.getDescription().trim());

        List<Genre> genres = new ArrayList<>(genreRepository.findAllById(genreIds));
        lista.setGenres(genres.stream().collect(Collectors.toSet()));

        lista = listRepository.save(lista);

        return listMapper.toDTO(lista);
    }

    /**
     * Borra una lista de un usuario.
     *
     * @param idUser ID del usuario propietario de la lista
     * @param idList ID de la lista a borrar
     * @return DTO con los datos de la lista borrada
     * @throws EntityNotFoundException La lista no existe
     * @throws AccessDeniedException El usuario no es el propietario de la lista
     */
    public BookListDTO deleteList(Long idUser, Long idList) {
        BookList list = listRepository.findByIdWithRelations(idList)
                .orElseThrow(() -> new EntityNotFoundException("Lista con ID " + idList + " no encontrada."));

        if (!list.getUser().getId().equals(idUser)) {
            throw new AccessDeniedException("No tienes permiso para borrar esta lista.");
        }

        listRepository.delete(list);

        return listMapper.toDTO(list);
    }

    /**
     * Añade un libro a una lista de un usuario.
     *
     * @param idUser ID del usuario propietario de la lista
     * @param idList ID de la lista
     * @param idBook ID del libro que se va a añadir
     * @return DTO con los datos de la lista actualizada
     * @throws EntityNotFoundException La lista o el libro no existen
     * @throws AccessDeniedException El usuario no es el propietario de la lista
     */
    public BookListDTO addBookToList(Long idUser, Long idList, Long idBook) {
        BookList list = listRepository.findByIdWithRelations(idList)
                .orElseThrow(() -> new EntityNotFoundException("Lista con ID " + idList + " no encontrada."));

        if (!list.getUser().getId().equals(idUser)) {
            throw new AccessDeniedException("No tienes permiso para acceder a esta lista");
        }

        Book book = bookRepository.findById(idBook)
                .orElseThrow(() -> new EntityNotFoundException("Libro con ID " + idList + " no encontrado."));

        if (!list.getBooks().contains(book)) {
            BookListItem addedBook = new BookListItem();
            addedBook.setList(list);
            addedBook.setBook(book);
            addedBook.setDateAdded(Date.valueOf(LocalDate.now()));
            addedBook.setId(new BookListItemId(list.getId(), book.getId()));

            list.getBooks().add(addedBook);
            list = listRepository.save(list);
        }

        return listMapper.toDTO(list);
    }

    /**
     * Elimina un libro de una lista de un usuario.
     *
     * @param idUser ID del usuario propietario de la lista
     * @param idList ID de la lista
     * @param idBook ID del libro que se va a eliminar
     * @return DTO con los datos de la lista actualizada
     * @throws EntityNotFoundException La lista o el libro no existen
     * @throws AccessDeniedException El usuario no es el propietario de la lista
     */
    public BookListDTO deleteBookFromList(Long idUser, Long idList, Long idBook) {
        BookList list = listRepository.findByIdWithRelations(idList)
                .orElseThrow(() -> new EntityNotFoundException("Lista con ID " + idList + " no encontrada."));

        if (!list.getUser().getId().equals(idUser)) {
            throw new AccessDeniedException("No tienes permiso para acceder a esta lista");
        }

        Book book = bookRepository.findById(idBook)
                .orElseThrow(() -> new EntityNotFoundException("Libro con ID " + idBook + " no encontrado."));

        BookListItem item = new BookListItem();
        item.setId(new BookListItemId(list.getId(), book.getId()));
        item.setList(list);
        item.setBook(book);
        item.setDateAdded(Date.valueOf(LocalDate.now()));

        if (list.getBooks().contains(item)) {
            list.getBooks().remove(item);
            list = listRepository.save(list);
        }

        return listMapper.toDTO(list);
    }

    /**
     * Devuelve todas las listas de un usuario que no contienen un libro específico.
     *
     * @param idBook ID del libro a excluir
     * @param idUser ID del usuario
     * @param page Número de la página
     * @param size Tamaño de la página
     * @return Página con las listas resultantes de la búsqueda
     */
    public Page<BookListDTO> getListsWithoutBook(Long idBook, Long idUser, int page, int size) {
        Book book = bookRepository.findById(idBook)
                .orElseThrow(() -> new EntityNotFoundException("Libro con ID " + idBook + " no encontrado."));

        Pageable pageable = PageRequest.of(page, size);
        Page<BookList> lists = bookItemRepository.findAllListsByUserIdAndBookIdNotIn(idUser, idBook, pageable);

        return lists.map(listMapper::toDTO);
    }

    /**
     * Devuelve todas las listas de un usuario que tienen al menos un libro o un género asociado.
     *
     * @param idUser ID del usuario
     * @return Listado con las listas resultantes
     */
    public List<BookListDTO> getAllListsExcludingEmpty(Long idUser) {
        List<BookList> listas = listRepository.findAllByUserId(idUser);

        List<BookList> listasFiltradas = listas.stream()
                .filter(bookList -> bookList.getBooks().size() > 0 || bookList.getGenres().size() > 0).toList();

        return listasFiltradas.stream().map(listMapper::toDTO).toList();
    }
}
