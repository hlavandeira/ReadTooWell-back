package es.readtoowell.api_biblioteca.mapper;

import es.readtoowell.api_biblioteca.model.DTO.BookDTO;
import es.readtoowell.api_biblioteca.model.DTO.GenreDTO;
import es.readtoowell.api_biblioteca.model.entity.Book;
import es.readtoowell.api_biblioteca.model.entity.Genre;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class BookMapper {
    @Autowired
    private GenreMapper genreMapper;

    /**
     * Convierte una instancia de {@code Book} en {@code BookDTO}.
     *
     * @param book La entidad {@code Book} a convertir.
     * @return Una instancia de {@code BookDTO} con los datos del libro.
     */
    public BookDTO toDTO(Book book) {
        BookDTO dto = new BookDTO();

        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        dto.setAuthor(book.getAuthor());
        dto.setPublicationYear(book.getPublicationYear());
        dto.setPageNumber(book.getPageNumber());
        dto.setPublisher(book.getPublisher());
        dto.setSynopsis(book.getSynopsis());
        dto.setCover(book.getCover());
        dto.setIsbn(book.getIsbn());
        dto.setActive(book.isActive());
        dto.setNumCollection(book.getNumCollection());
        dto.setCollectionId(book.getCollectionId());

        Set<GenreDTO> genreDTOs = book.getGenres()
                .stream()
                .map(genreMapper::toDTO)
                .collect(Collectors.toSet());
        dto.setGenres(genreDTOs);

        return dto;
    }

    /**
     * Convierte una instancia de {@code BookDTO} en {@code Book}.
     *
     * @param dto El {@code BookDTO} a convertir.
     * @return Una instancia de {@code Book} con los datos del DTO.
     */
    public Book toEntity(BookDTO dto) {
        Book book = new Book();

        book.setId(dto.getId());
        book.setTitle(dto.getTitle());
        book.setAuthor(dto.getAuthor());
        book.setPublicationYear(dto.getPublicationYear());
        book.setPageNumber(dto.getPageNumber());
        book.setPublisher(dto.getPublisher());
        book.setSynopsis(dto.getSynopsis());
        book.setCover(dto.getCover());
        book.setIsbn(dto.getIsbn());
        book.setActive(dto.isActive());
        book.setNumCollection(dto.getNumCollection());

        Set<Genre> genres = dto.getGenres()
                .stream()
                .map(genreMapper::toEntity)
                .collect(Collectors.toSet());
        book.setGenres(genres);

        return book;
    }
}
