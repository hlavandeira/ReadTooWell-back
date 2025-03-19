package es.readtoowell.api_biblioteca.mapper;

import es.readtoowell.api_biblioteca.model.DTO.FormatDTO;
import es.readtoowell.api_biblioteca.model.Format;
import org.springframework.stereotype.Component;

@Component
public class FormatMapper {
    public FormatDTO toDTO(Format format) {
        FormatDTO dto = new FormatDTO();

        dto.setId(format.getId());
        dto.setNombre(format.getNombre());

        return dto;
    }
}
