package es.readtoowell.api_biblioteca.mapper;

import es.readtoowell.api_biblioteca.model.DTO.UserLibraryBookDTO;
import es.readtoowell.api_biblioteca.model.entity.UserLibraryBook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserLibraryBookMapper {
    @Autowired
    private BookMapper bookMapper;

    /**
     * Convierte una instancia de {@code UserLibraryBook} en {@code UserLibraryBookDTO}.
     *
     * @param lib La entidad {@code UserLibraryBook} a convertir.
     * @return Una instancia de {@code UserLibraryBookDTO} con los datos del libro.
     */
    public UserLibraryBookDTO toDTO(UserLibraryBook lib) {
        UserLibraryBookDTO dto = new UserLibraryBookDTO();

        dto.setId(lib.getId());
        dto.setBook(bookMapper.toDTO(lib.getBook()));
        dto.setDateStart(lib.getDateStart());
        dto.setDateFinish(lib.getDateFinish());
        dto.setRating(lib.getRating());
        dto.setReview(lib.getReview());
        dto.setReadingStatus(lib.getReadingStatus());
        dto.setProgress(lib.getProgress());
        dto.setProgressType(lib.getProgressType());

        return dto;
    }

    /**
     * Convierte una instancia de {@code UserLibraryBookDTO} en {@code UserLibraryBook}.
     *
     * @param dto El {@code UserLibraryBookDTO} a convertir.
     * @return Una instancia de {@code UserLibraryBook} con los datos del DTO.
     */
    public UserLibraryBook toEntity(UserLibraryBookDTO dto) {
        UserLibraryBook lib = new UserLibraryBook();

        lib.setId(dto.getId());
        lib.setBook(bookMapper.toEntity(dto.getBook()));
        lib.setDateStart(dto.getDateStart());
        lib.setDateFinish(dto.getDateFinish());
        lib.setRating(dto.getRating());
        lib.setReview(dto.getReview());
        lib.setReadingStatus(dto.getReadingStatus());
        lib.setProgress(dto.getProgress());
        lib.setProgressType(dto.getProgressType());

        return lib;
    }
}
