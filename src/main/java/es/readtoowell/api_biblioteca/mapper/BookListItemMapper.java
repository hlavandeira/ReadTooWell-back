package es.readtoowell.api_biblioteca.mapper;

import es.readtoowell.api_biblioteca.model.BookListItem;
import es.readtoowell.api_biblioteca.model.BookListItemId;
import es.readtoowell.api_biblioteca.model.DTO.BookListItemDTO;
import org.springframework.stereotype.Component;

@Component
public class BookListItemMapper {
    public BookListItemDTO toDTO(BookListItem item) {
        BookListItemDTO dto = new BookListItemDTO();

        dto.setId(item.getId());
        dto.setLibro(item.getLibro());
        dto.setFechaAñadido(item.getFechaAñadido());

        return dto;
    }

    public BookListItem toEntity(BookListItemDTO dto) {
        BookListItem item = new BookListItem();

        item.setId(dto.getId());
        item.setLibro(dto.getLibro());
        item.setFechaAñadido(dto.getFechaAñadido());

        return item;
    }
}
