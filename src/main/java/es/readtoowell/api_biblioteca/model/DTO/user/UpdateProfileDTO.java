package es.readtoowell.api_biblioteca.model.DTO.user;

import jakarta.validation.constraints.Size;

public class UpdateProfileDTO {
    @Size(max=35, message="El nombre de perfil debe tener como máximo 35 caracteres")
    private String profileName;
    private String profilePic;
    @Size(max=2000, message="La biografía no puede superar los 2000 caracteres")
    private String biography;

    // Métodos Getters y Setters

    /**
     * Devuelve el nombre de perfil del usuario.
     *
     * @return Nombre de perfil
     */
    public String getProfileName() {
        return profileName;
    }

    /**
     * Establece un valor para el nombre de perfil del usuario.
     *
     * @param profileName Nuevo nombre de perfil
     */
    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    /**
     * Devuelve la URL de la foto de perfil del usuario.
     *
     * @return URL de la foto de perfil
     */
    public String getProfilePic() {
        return profilePic;
    }

    /**
     * Establece un valor para la URL de la foto de perfil del usuario.
     *
     * @param profilePic Nueva URL de la foto de perfil
     */
    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    /**
     * Devuelve la biografía del usuario.
     *
     * @return Biografía del usuario
     */
    public String getBiography() {
        return biography;
    }

    /**
     * Establece un valor para la biografía del usuario.
     *
     * @param biography Nueva biografía del usuario
     */
    public void setBiography(String biography) {
        this.biography = biography;
    }
}
