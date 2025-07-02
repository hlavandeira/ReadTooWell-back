package es.readtoowell.api_biblioteca.mapper;

import es.readtoowell.api_biblioteca.model.DTO.book.FormatDTO;
import es.readtoowell.api_biblioteca.model.entity.Format;
import org.springframework.stereotype.Component;

/**
 * Mapeador encargado de gestionar las conversiones de formatos entre entidades y DTOs.
 */
@Component
public class FormatMapper {
    /**
     * Convierte una instancia de {@code Format} en {@code FormatDTO}.
     *
     * @param format La entidad {@code Format} a convertir.
     * @return Una instancia de {@code FormatDTO} con los datos del formato.
     */
    public FormatDTO toDTO(Format format) {
        FormatDTO dto = new FormatDTO();

        dto.setId(format.getId());
        dto.setName(format.getName());

        return dto;
    }
}
