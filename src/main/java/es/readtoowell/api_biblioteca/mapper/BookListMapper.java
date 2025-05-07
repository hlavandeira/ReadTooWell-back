package es.readtoowell.api_biblioteca.mapper;

import es.readtoowell.api_biblioteca.model.entity.BookList;
import es.readtoowell.api_biblioteca.model.DTO.book.BookListDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class BookListMapper {
    @Autowired
    private BookListItemMapper listItemMapper;
    @Autowired
    private GenreMapper genreMapper;
    @Autowired
    private UserMapper userMapper;

    /**
     * Convierte una instancia de {@code BookList} en {@code BookListDTO}.
     *
     * @param list La entidad {@code BookList} a convertir.
     * @return Una instancia de {@code BookListDTO} con los datos de la lista.
     */
    public BookListDTO toDTO(BookList list) {
        BookListDTO dto = new BookListDTO();
        dto.setId(list.getId());
        dto.setName(list.getName());
        dto.setDescription(list.getDescription());
        dto.setUser(userMapper.toDTO(list.getUser()));
        dto.setBooks(list.getBooks().stream()
                .map(listItemMapper::toDTO)
                .collect(Collectors.toList()));
        dto.setGenres(list.getGenres().stream()
                .map(genreMapper::toDTO)
                .collect(Collectors.toList()));
        return dto;
    }

    /**
     * Convierte una instancia de {@code BookListDTO} en {@code BookList}.
     *
     * @param dto El {@code BookListDTO} a convertir.
     * @return Una instancia de {@code BookList} con los datos del DTO.
     */
    public BookList toEntity(BookListDTO dto) {
        BookList list = new BookList();
        list.setId(dto.getId());
        list.setName(dto.getName());
        list.setDescription(dto.getDescription());
        list.setUser(userMapper.toEntity(dto.getUser()));
        list.setBooks(dto.getBooks().stream()
                .map(listItemMapper::toEntity)
                .collect(Collectors.toList()));
        list.setGenres(dto.getGenres().stream()
                .map(genreMapper::toEntity)
                .collect(Collectors.toList()));
        return list;
    }
}
