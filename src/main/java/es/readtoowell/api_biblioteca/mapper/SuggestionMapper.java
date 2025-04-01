package es.readtoowell.api_biblioteca.mapper;

import es.readtoowell.api_biblioteca.model.DTO.SuggestionDTO;
import es.readtoowell.api_biblioteca.model.entity.Suggestion;
import org.springframework.stereotype.Component;

@Component
public class SuggestionMapper {
    /**
     * Convierte una instancia de {@code Suggestion} en {@code SuggestionDTO}.
     *
     * @param sug La entidad {@code Suggestion} a convertir.
     * @return Una instancia de {@code SuggestionDTO} con los datos de la sugerencia.
     */
    public SuggestionDTO toDTO(Suggestion sug) {
        SuggestionDTO dto = new SuggestionDTO();

        dto.setId(sug.getId());
        dto.setTitle(sug.getTitle());
        dto.setAuthor(sug.getAuthor());
        dto.setUser(sug.getUser());
        dto.setPublicationYear(sug.getPublicationYear());
        dto.setStatus(sug.getStatus());
        dto.setActive(sug.isActive());
        dto.setDateSent(sug.getDateSent());

        return dto;
    }

    /**
     * Convierte una instancia de {@code SuggestionDTO} en {@code Suggestion}.
     *
     * @param dto El {@code SuggestionDTO} a convertir.
     * @return Una instancia de {@code Suggestion} con los datos del DTO.
     */
    public Suggestion toEntity(SuggestionDTO dto) {
        Suggestion sug = new Suggestion();

        sug.setId(dto.getId());
        sug.setTitle(dto.getTitle());
        sug.setAuthor(dto.getAuthor());
        sug.setUser(dto.getUser());
        sug.setPublicationYear(dto.getPublicationYear());
        sug.setStatus(dto.getStatus());
        sug.setActive(dto.isActive());
        sug.setDateSent(dto.getDateSent());

        return sug;
    }
}
