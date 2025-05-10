package es.readtoowell.api_biblioteca.model.DTO.user;

import jakarta.validation.constraints.*;

/**
 *  DTO que representa los detalles de un usuario.
 */
public class UserDTO {
    private Long id;
    @NotBlank(message = "El nombre de usuario no puede ser nulo")
    @Size(min=5, max=20, message="El nombre de usuario debe tener entre 5 y 20 caracteres")
    private String username;
    @Size(max=35, message="El nombre de perfil debe tener como máximo 35 caracteres")
    private String profileName;
    @Pattern(regexp="^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
            message="El correo electrónico no tiene un formato válido")
    private String email;
    @Pattern(regexp="^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$",
            message="La contraseña no tiene un formato válido")
    private String password;
    @Min(value=0, message="El valor mínimo es 0")
    @Max(value=2, message="El valor máximo es 2")
    private int role;
    private String profilePic;
    @Size(max=2000, message="La biografía no puede superar los 2000 caracteres")
    private String biography;

    // Métodos Getters y Setters

    /**
     * Devuelve el identificador del usuario.
     *
     * @return ID del usuario
     */
    public Long getId() {
        return id;
    }

    /**
     * Establece un valor para el identificador del usuario.
     *
     * @param id Nuevo ID del usuario
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Devuelve el nombre de usuario del usuario.
     *
     * @return Nombre de usuario
     */
    public String getUsername() {
        return username;
    }

    /**
     * Establece un valor para el nombre de usuario del usuario.
     *
     * @param username Nuevo nombre de usuario
     */
    public void setUsername(String username) {
        this.username = username;
    }

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
     * Devuelve el correo electrónico del usuario.
     *
     * @return Correo electrónico
     */
    public String getEmail() {
        return email;
    }

    /**
     * Establece un valor para el correo electrónico del usuario.
     *
     * @param email Nuevo correo electrónico
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Devuelve la contraseña del usuario.
     *
     * @return Contraseña del usuario
     */
    public String getPassword() {
        return password;
    }

    /**
     * Establece un valor para la contraseña del usuario.
     *
     * @param password Nueva contraseña del usuario
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Devuelve el rol del usuario.
     *
     * @return Rol del usuario
     */
    public int getRole() {
        return role;
    }

    /**
     * Establece un valor para el rol del usuario.
     *
     * @param role Nuevo rol del usuario
     */
    public void setRole(int role) {
        this.role = role;
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
