package es.readtoowell.api_biblioteca.mapper;

import es.readtoowell.api_biblioteca.model.DTO.SuggestionDTO;
import es.readtoowell.api_biblioteca.model.Suggestion;
import org.springframework.stereotype.Component;

@Component
public class SuggestionMapper {
    public SuggestionDTO toDTO(Suggestion sug) {
        SuggestionDTO dto = new SuggestionDTO();

        dto.setId(sug.getId());
        dto.setTitulo(sug.getTitulo());
        dto.setAutor(sug.getAutor());
        dto.setUsuario(sug.getUsuario());
        dto.setAñoPublicacion(sug.getAñoPublicacion());
        dto.setEstado(sug.getEstado());
        dto.setActivo(sug.isActivo());
        dto.setFechaEnviada(sug.getFechaEnviada());

        return dto;
    }

    public Suggestion toDTO(SuggestionDTO dto) {
        Suggestion sug = new Suggestion();

        sug.setId(dto.getId());
        sug.setTitulo(dto.getTitulo());
        sug.setAutor(dto.getAutor());
        sug.setUsuario(dto.getUsuario());
        sug.setAñoPublicacion(dto.getAñoPublicacion());
        sug.setEstado(dto.getEstado());
        sug.setActivo(dto.isActivo());
        sug.setFechaEnviada(dto.getFechaEnviada());

        return sug;
    }
}
