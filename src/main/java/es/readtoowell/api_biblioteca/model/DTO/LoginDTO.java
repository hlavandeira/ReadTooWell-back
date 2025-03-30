package es.readtoowell.api_biblioteca.model.DTO;

import jakarta.validation.constraints.NotBlank;

/**
 *  DTO que representa los detalles de un inicio de sesión.
 */
public class LoginDTO {
    @NotBlank(message = "El correo no puede ser nulo")
    private String email;
    @NotBlank(message = "La contraseña no puede ser nula")
    private String password;
    private String token;

    // Métodos Getters y Setters

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
