package es.readtoowell.api_biblioteca.model.DTO;

/**
 * DTO que representa los detalles de un usuario registrado.
 */
public class RegisteredDTO {
    private UserDTO user;
    private String token;

    // Métodos Getters y Setters

    /**
     * Devuelve el usuario registrado.
     *
     * @return Usuario registrado
     */
    public UserDTO getUser() {
        return user;
    }

    /**
     * Establece un valor para el usuario registrado.
     *
     * @param user Nuevo usuario registrado
     */
    public void setUser(UserDTO user) {
        this.user = user;
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
