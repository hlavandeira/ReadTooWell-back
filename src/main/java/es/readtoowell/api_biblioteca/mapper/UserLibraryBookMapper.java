package es.readtoowell.api_biblioteca.mapper;

import es.readtoowell.api_biblioteca.model.DTO.UserLibraryBookDTO;
import es.readtoowell.api_biblioteca.model.UserLibraryBook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserLibraryBookMapper {
    @Autowired
    private BookMapper bookMapper;

    public UserLibraryBookDTO toDTO(UserLibraryBook lib) {
        UserLibraryBookDTO dto = new UserLibraryBookDTO();

        dto.setId(lib.getId());
        dto.setLibro(bookMapper.toDTO(lib.getLibro()));
        dto.setFechaInicio(lib.getFechaInicio());
        dto.setFechaFin(lib.getFechaFin());
        dto.setCalificacion(lib.getCalificacion());
        dto.setReseña(lib.getReseña());
        dto.setEstadoLectura(lib.getEstadoLectura());
        dto.setProgreso(lib.getProgreso());
        dto.setTipoProgreso(lib.getTipoProgreso());

        return dto;
    }

    public UserLibraryBook toEntity(UserLibraryBookDTO dto) {
        UserLibraryBook lib = new UserLibraryBook();

        lib.setId(dto.getId());
        lib.setLibro(bookMapper.toEntity(dto.getLibro()));
        lib.setFechaInicio(dto.getFechaInicio());
        lib.setFechaFin(dto.getFechaFin());
        lib.setCalificacion(dto.getCalificacion());
        lib.setReseña(dto.getReseña());
        lib.setEstadoLectura(dto.getEstadoLectura());
        lib.setProgreso(dto.getProgreso());
        lib.setTipoProgreso(dto.getTipoProgreso());

        return lib;
    }
}
