package es.readtoowell.api_biblioteca.mapper;

import es.readtoowell.api_biblioteca.model.entity.AuthorRequest;
import es.readtoowell.api_biblioteca.model.DTO.AuthorRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class AuthorRequestMapper {
    @Autowired
    private RequestBookMapper requestBookMapper;

    /**
     * Convierte una instancia de {@code AuthorRequest} en {@code AuthorRequestDTO}.
     *
     * @param req La entidad {@code AuthorRequest} a convertir.
     * @return Una instancia de {@code AuthorRequestDTO} con los datos de la solicitud de autor.
     */
    public AuthorRequestDTO toDTO(AuthorRequest req) {
        AuthorRequestDTO dto = new AuthorRequestDTO();

        dto.setId(req.getId());
        dto.setName(req.getName());
        dto.setBiography(req.getBiography());
        dto.setActive(req.isActive());
        dto.setUser(req.getUser());
        dto.setDateSent(req.getDateSent());
        dto.setStatus(req.getStatus());
        dto.setBooks(req.getBooks().stream()
                .map(requestBookMapper::toDTO)
                .collect(Collectors.toList()));

        return dto;
    }

    /**
     * Convierte una instancia de {@code AuthorRequestDTO} en {@code AuthorRequest}.
     *
     * @param dto El {@code AuthorRequestDTO} a convertir.
     * @return Una instancia de {@code AuthorRequest} con los datos del DTO.
     */
    public AuthorRequest toEntity(AuthorRequestDTO dto) {
        AuthorRequest req = new AuthorRequest();

        req.setId(dto.getId());
        req.setName(dto.getName());
        req.setBiography(dto.getBiography());
        req.setActive(dto.isActive());
        req.setUser(dto.getUser());
        req.setDateSent(dto.getDateSent());
        req.setStatus(dto.getStatus());
        req.setBooks(dto.getBooks().stream()
                .map(requestBookMapper::toEntity)
                .collect(Collectors.toList()));

        return req;
    }
}
