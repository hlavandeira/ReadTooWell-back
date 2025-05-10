package es.readtoowell.api_biblioteca.model.DTO.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * DTO que representa los detalles de un registro de usuario.
 */
public class RegisterDTO {
    @NotBlank(message = "El nombre de usuario no puede ser nulo")
    @Size(min=5, max=20, message="El nombre de usuario debe tener entre 5 y 20 caracteres")
    private String username;
    @NotBlank(message = "El correo no puede ser nulo")
    @Pattern(regexp="^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
            message="El correo electrónico no tiene un formato válido")
    private String email;
    @NotBlank(message = "La contraseña no puede ser nula")
    @Pattern(regexp="^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$",
            message="La contraseña no tiene un formato válido")
    private String password;
    @NotBlank(message = "Se debe confirmar la contraseña")
    private String confirmPassword;
    private String token;

    // Métodos Getters y Setters

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
     * Devuelve la confirmación de contraseña del usuario.
     *
     * @return Confirmación de contraseña del usuario
     */
    public String getConfirmPassword() {
        return confirmPassword;
    }

    /**
     * Establece un valor para la confirmación de contraseña del usuario.
     *
     * @param confirmPassword Nueva confirmación de contraseña del usuario
     */
    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    /**
     * Devuelve el token de sesión del usuario.
     *
     * @return Token de sesión
     */
    public String getToken() {
        return token;
    }

    /**
     * Establece un valor para el token de sesión del usuario.
     *
     * @param token Nuevo token de sesión
     */
    public void setToken(String token) {
        this.token = token;
    }
}
