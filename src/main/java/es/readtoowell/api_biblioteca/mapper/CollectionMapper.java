package es.readtoowell.api_biblioteca.mapper;

import es.readtoowell.api_biblioteca.model.DTO.book.CollectionDTO;
import es.readtoowell.api_biblioteca.model.entity.Collection;
import org.springframework.stereotype.Component;

/**
 * Mapeador encargado de gestionar las conversiones de colecciones entre entidades y DTOs.
 */
@Component
public class CollectionMapper {
    /**
     * Convierte una instancia de {@code Collection} en {@code CollectionDTO}.
     *
     * @param col La entidad {@code Collection} a convertir.
     * @return Una instancia de {@code CollectionDTO} con los datos del libro.
     */
    public CollectionDTO toDTO(Collection col) {
        CollectionDTO dto = new CollectionDTO();

        dto.setId(col.getId());
        dto.setName(col.getName());

        return dto;
    }

    /**
     * Convierte una instancia de {@code CollectionDTO} en {@code Collection}.
     *
     * @param dto El {@code CollectionDTO} a convertir.
     * @return Una instancia de {@code Collection} con los datos del DTO.
     */
    public Collection toEntity(CollectionDTO dto) {
        Collection col = new Collection();

        col.setId(dto.getId());
        col.setName(dto.getName());

        return col;
    }
}
