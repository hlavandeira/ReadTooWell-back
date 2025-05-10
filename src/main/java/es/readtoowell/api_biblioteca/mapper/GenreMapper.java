package es.readtoowell.api_biblioteca.mapper;

import es.readtoowell.api_biblioteca.model.DTO.book.GenreDTO;
import es.readtoowell.api_biblioteca.model.entity.Genre;
import org.springframework.stereotype.Component;

@Component
public class GenreMapper {
    /**
     * Convierte una instancia de {@code Genre} en {@code GenreDTO}.
     *
     * @param genre La entidad {@code Genre} a convertir.
     * @return Una instancia de {@code GenreDTO} con los datos del género.
     */
    public GenreDTO toDTO(Genre genre) {
        GenreDTO dto = new GenreDTO();

        dto.setId(genre.getId());
        dto.setName(genre.getName());

        return dto;
    }

    /**
     * Convierte una instancia de {@code GenreDTO} en {@code Genre}.
     *
     * @param dto El {@code GenreDTO} a convertir.
     * @return Una instancia de {@code Genre} con los datos del DTO.
     */
    public Genre toEntity(GenreDTO dto) {
        Genre genre = new Genre();

        genre.setId(dto.getId());
        genre.setName(dto.getName());

        return genre;
    }
}
