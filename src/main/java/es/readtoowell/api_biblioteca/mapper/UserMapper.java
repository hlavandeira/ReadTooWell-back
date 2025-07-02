package es.readtoowell.api_biblioteca.mapper;

import es.readtoowell.api_biblioteca.model.DTO.user.UserDTO;
import es.readtoowell.api_biblioteca.model.entity.User;
import org.springframework.stereotype.Component;

/**
 * Mapeador encargado de gestionar las conversiones de usuarios entre entidades y DTOs.
 */
@Component
public class UserMapper {
    /**
     * Convierte una instancia de {@code User} en {@code UserDTO}.
     *
     * @param user La entidad {@code User} a convertir.
     * @return Una instancia de {@code UserDTO} con los datos de .
     */
    public UserDTO toDTO(User user) {
        UserDTO dto = new UserDTO();

        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setProfileName(user.getProfileName());
        dto.setPassword(user.getPassword());
        dto.setEmail(user.getEmail());
        dto.setBiography(user.getBiography());
        dto.setRole(user.getRole());
        dto.setProfilePic(user.getProfilePic());

        return dto;
    }

    /**
     * Convierte una instancia de {@code UserDTO} en {@code User}.
     *
     * @param dto El {@code User} a convertir.
     * @return Una instancia de {@code UserDTO} con los datos del DTO.
     */
    public User toEntity(UserDTO dto) {
        User user = new User();

        user.setId(dto.getId());
        user.setUsername(dto.getUsername());
        user.setProfileName(dto.getProfileName());
        user.setPassword(dto.getPassword());
        user.setEmail(dto.getEmail());
        user.setBiography(dto.getBiography());
        user.setRole(dto.getRole());
        user.setProfilePic(dto.getProfilePic());

        return user;
    }
}
