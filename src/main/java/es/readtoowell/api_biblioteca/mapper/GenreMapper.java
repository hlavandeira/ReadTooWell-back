package es.readtoowell.api_biblioteca.mapper;

import es.readtoowell.api_biblioteca.DTO.GenreDTO;
import es.readtoowell.api_biblioteca.model.Genre;
import org.springframework.stereotype.Component;

@Component
public class GenreMapper {
    public GenreDTO toDTO(Genre genre) {
        GenreDTO dto = new GenreDTO();

        dto.setId(genre.getId());
        dto.setNombre(genre.getNombre());

        return dto;
    }

    public Genre toEntity(GenreDTO dto) {
        Genre genre = new Genre();

        genre.setId(dto.getId());
        genre.setNombre(dto.getNombre());

        return genre;
    }
}
