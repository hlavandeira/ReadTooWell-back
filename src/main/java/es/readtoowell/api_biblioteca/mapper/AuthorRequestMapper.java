package es.readtoowell.api_biblioteca.mapper;

import es.readtoowell.api_biblioteca.model.AuthorRequest;
import es.readtoowell.api_biblioteca.model.DTO.AuthorRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class AuthorRequestMapper {
    @Autowired
    private RequestBookMapper requestBookMapper;

    public AuthorRequestDTO toDTO(AuthorRequest req) {
        AuthorRequestDTO dto = new AuthorRequestDTO();

        dto.setId(req.getId());
        dto.setNombre(req.getNombre());
        dto.setBiografia(req.getBiografia());
        dto.setActivo(req.isActivo());
        dto.setUsuario(req.getUsuario());
        dto.setFechaEnviada(req.getFechaEnviada());
        dto.setEstado(req.getEstado());
        dto.setLibros(req.getLibros().stream()
                .map(requestBookMapper::toDTO)
                .collect(Collectors.toSet()));

        return dto;
    }

    public AuthorRequest toEntity(AuthorRequestDTO dto) {
        AuthorRequest req = new AuthorRequest();

        req.setId(dto.getId());
        req.setNombre(dto.getNombre());
        req.setBiografia(dto.getBiografia());
        req.setActivo(dto.isActivo());
        req.setUsuario(dto.getUsuario());
        req.setFechaEnviada(dto.getFechaEnviada());
        req.setEstado(dto.getEstado());
        req.setLibros(dto.getLibros().stream()
                .map(requestBookMapper::toEntity)
                .collect(Collectors.toSet()));

        return req;
    }
}
