package es.readtoowell.api_biblioteca.model.DTO.user;

/**
 * DTO que representa los detalles de un usuario autenticado.
 */
public class AuthenticatedUserDTO {
    private UserDTO user;
    private String token;

    // Métodos Getters y Setters

    /**
     * Devuelve el usuario autenticado.
     *
     * @return Usuario autenticado
     */
    public UserDTO getUser() {
        return user;
    }

    /**
     * Establece un valor para el usuario autenticado.
     *
     * @param user Nuevo usuario autenticado
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
