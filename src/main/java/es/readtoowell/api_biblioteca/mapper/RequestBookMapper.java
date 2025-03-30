package es.readtoowell.api_biblioteca.mapper;

import es.readtoowell.api_biblioteca.model.DTO.RequestBookDTO;
import es.readtoowell.api_biblioteca.model.entity.RequestBook;
import org.springframework.stereotype.Component;

@Component
public class RequestBookMapper {
    /**
     * Convierte una instancia de {@code RequestBook} en {@code RequestBookDTO}.
     *
     * @param book La entidad {@code RequestBook} a convertir.
     * @return Una instancia de {@code RequestBookDTO} con los datos del libro.
     */
    public RequestBookDTO toDTO(RequestBook book) {
        RequestBookDTO dto = new RequestBookDTO();

        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        dto.setPublicationYear(book.getPublicationYear());

        return dto;
    }

    /**
     * Convierte una instancia de {@code RequestBookDTO} en {@code RequestBook}.
     *
     * @param dto El {@code RequestBookDTO} a convertir.
     * @return Una instancia de {@code RequestBook} con los datos del DTO.
     */
    public RequestBook toEntity(RequestBookDTO dto) {
        RequestBook book = new RequestBook();

        book.setId(dto.getId());
        book.setTitle(dto.getTitle());
        book.setPublicationYear(dto.getPublicationYear());

        return book;
    }
}
