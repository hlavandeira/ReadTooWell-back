package es.readtoowell.api_biblioteca.mapper;

import es.readtoowell.api_biblioteca.model.DTO.RequestBookDTO;
import es.readtoowell.api_biblioteca.model.RequestBook;
import org.springframework.stereotype.Component;

@Component
public class RequestBookMapper {

    public RequestBookDTO toDTO(RequestBook book) {
        RequestBookDTO dto = new RequestBookDTO();

        dto.setId(book.getId());
        dto.setTitulo(book.getTitulo());
        dto.setAñoPublicacion(book.getAñoPublicacion());

        return dto;
    }

    public RequestBook toEntity(RequestBookDTO dto) {
        RequestBook book = new RequestBook();

        book.setId(dto.getId());
        book.setTitulo(dto.getTitulo());
        book.setAñoPublicacion(dto.getAñoPublicacion());

        return book;
    }
}
