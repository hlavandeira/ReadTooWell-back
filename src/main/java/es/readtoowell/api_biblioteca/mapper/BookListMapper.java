package es.readtoowell.api_biblioteca.mapper;

import es.readtoowell.api_biblioteca.model.BookList;
import es.readtoowell.api_biblioteca.model.DTO.BookListDTO;
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

    public BookListDTO toDTO(BookList list) {
        BookListDTO dto = new BookListDTO();
        dto.setId(list.getId());
        dto.setNombre(list.getNombre());
        dto.setDescripcion(list.getDescripcion());
        dto.setUsuario(userMapper.toDTO(list.getUsuario()));
        dto.setLibros(list.getLibros().stream()
                .map(listItemMapper::toDTO)
                .collect(Collectors.toSet()));
        dto.setGeneros(list.getGeneros().stream()
                .map(genreMapper::toDTO)
                .collect(Collectors.toSet()));
        return dto;
    }

    public BookList toEntity(BookListDTO dto) {
        BookList list = new BookList();
        list.setId(dto.getId());
        list.setNombre(dto.getNombre());
        list.setDescripcion(dto.getDescripcion());
        list.setUsuario(userMapper.toEntity(dto.getUsuario()));
        list.setLibros(dto.getLibros().stream()
                .map(listItemMapper::toEntity)
                .collect(Collectors.toSet()));
        list.setGeneros(dto.getGeneros().stream()
                .map(genreMapper::toEntity)
                .collect(Collectors.toSet()));
        return list;
    }
}
