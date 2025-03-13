package es.readtoowell.api_biblioteca.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "usuario")
public class User {
    @Id
    @Column(name = "id_usuario", unique = true, updatable = false)
    private Long id;
    @Column(name = "nombre_usuario")
    private String nombreUsuario;
    @Column(name = "nombre_perfil")
    private String nombrePerfil;
    private String correo;
    private String contraseña;
    private int rol;
    @Column(name = "foto_perfil")
    private String fotoPerfil;
    private String biografia;

    public Long getId() {
        return id;
    }
    public String getNombreUsuario() {
        return nombreUsuario;
    }
    public String getNombrePerfil() {
        return nombrePerfil;
    }
    public String getCorreo() {
        return correo;
    }
    public String getContraseña() {
        return contraseña;
    }
    public int getRol() {
        return rol;
    }
    public String getFotoPerfil() {
        return fotoPerfil;
    }
    public String getBiografia() {
        return biografia;
    }
}
