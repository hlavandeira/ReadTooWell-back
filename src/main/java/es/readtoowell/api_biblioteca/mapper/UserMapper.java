package es.readtoowell.api_biblioteca.mapper;

import es.readtoowell.api_biblioteca.model.DTO.UserDTO;
import es.readtoowell.api_biblioteca.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserDTO toDTO(User user) {
        UserDTO dto = new UserDTO();

        dto.setId(user.getId());
        dto.setNombreUsuario(user.getNombreUsuario());
        dto.setNombrePerfil(user.getNombrePerfil());
        dto.setContraseña(user.getContraseña());
        dto.setCorreo(user.getCorreo());
        dto.setBiografia(user.getBiografia());
        dto.setRol(user.getRol());
        dto.setFotoPerfil(user.getFotoPerfil());
        dto.setActivo(user.isActivo());

        return dto;
    }

    public User toEntity(UserDTO dto) {
        User user = new User();

        user.setId(dto.getId());
        user.setNombreUsuario(dto.getNombreUsuario());
        user.setNombrePerfil(dto.getNombrePerfil());
        user.setContraseña(dto.getContraseña());
        user.setCorreo(dto.getCorreo());
        user.setBiografia(dto.getBiografia());
        user.setRol(dto.getRol());
        user.setFotoPerfil(dto.getFotoPerfil());
        user.setActivo(dto.isActivo());

        return user;
    }
}
