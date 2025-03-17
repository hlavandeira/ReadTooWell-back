package es.readtoowell.api_biblioteca.model.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterDTO {
    @NotBlank(message = "El nombre de usuario no puede ser nulo")
    @Size(min=5, max=20, message="El nombre de usuario debe tener entre 5 y 20 caracteres")
    private String nombreUsuario;
    @NotBlank(message = "El correo no puede ser nulo")
    @Pattern(regexp="^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
            message="El correo electrónico no tiene un formato válido")
    private String correo;
    @NotBlank(message = "La contraseña no puede ser nula")
    @Pattern(regexp="^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$",
            message="La contraseña no tiene un formato válido")
    private String contraseña;
    @NotBlank(message = "Se debe confirmar la contraseña")
    private String confirmarContraseña;

    private String token;

    public String getNombreUsuario() {
        return nombreUsuario;
    }
    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getCorreo() {
        return correo;
    }
    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContraseña() {
        return contraseña;
    }
    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public String getConfirmarContraseña() {
        return confirmarContraseña;
    }
    public void setConfirmarContraseña(String confirmarContraseña) {
        this.confirmarContraseña = confirmarContraseña;
    }

    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
}
