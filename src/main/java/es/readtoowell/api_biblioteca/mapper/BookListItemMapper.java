package es.readtoowell.api_biblioteca.mapper;

import es.readtoowell.api_biblioteca.model.entity.BookListItem;
import es.readtoowell.api_biblioteca.model.DTO.book.BookListItemDTO;
import org.springframework.stereotype.Component;

/**
 * Mapeador encargado de gestionar las conversiones de libros añadidos a listas entre entidades y DTOs.
 */
@Component
public class BookListItemMapper {
    /**
     * Convierte una instancia de {@code BookListItem} en {@code BookListItemDTO}.
     *
     * @param item La entidad {@code BookListItem} a convertir.
     * @return Una instancia de {@code BookListItemDTO} con los datos del libro.
     */
    public BookListItemDTO toDTO(BookListItem item) {
        BookListItemDTO dto = new BookListItemDTO();

        dto.setId(item.getId());
        dto.setBook(item.getBook());
        dto.setDateAdded(item.getDateAdded());

        return dto;
    }

    /**
     * Convierte una instancia de {@code BookListItemDTO} en {@code BookListItem}.
     *
     * @param dto El {@code BookListItemDTO} a convertir.
     * @return Una instancia de {@code BookListItem} con los datos del DTO.
     */
    public BookListItem toEntity(BookListItemDTO dto) {
        BookListItem item = new BookListItem();

        item.setId(dto.getId());
        item.setBook(dto.getBook());
        item.setDateAdded(dto.getDateAdded());

        return item;
    }
}
