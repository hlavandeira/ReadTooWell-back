package es.readtoowell.api_biblioteca.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.validation.constraints.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "usuario")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario", unique = true, updatable = false)
    private Long id;
    @NotBlank(message = "El nombre de usuario no puede ser nulo")
    @Size(min=5, max=20, message="El nombre de usuario debe tener entre 5 y 20 caracteres")
    @Column(name = "nombre_usuario")
    private String nombreUsuario;
    @Size(max=35, message="El nombre de perfil debe tener como máximo 35 caracteres")
    @Column(name = "nombre_perfil")
    private String nombrePerfil;
    @Pattern(regexp="^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
            message="El correo electrónico no tiene un formato válido")
    private String correo;
    @Pattern(regexp="^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$",
            message="La contraseña no tiene un formato válido")
    private String contraseña;
    @Min(value=0, message="El valor mínimo es 0")
    @Max(value=2, message="El valor máximo es 2")
    private int rol;
    @Column(name = "foto_perfil")
    private String fotoPerfil;
    @Size(max=2000, message="La biografía no puede superar los 2000 caracteres")
    private String biografia;
    private boolean activo;

    @ManyToMany
    @JoinTable(
            name="seguimiento",
            joinColumns = @JoinColumn(name= "id_seguidor"),
            inverseJoinColumns = @JoinColumn(name = "id_seguido")
    )
    @JsonIgnore
    private Set<User> seguidos = new HashSet<>();
    @ManyToMany(mappedBy = "seguidos")
    @JsonIgnore
    private Set<User> seguidores = new HashSet<>();

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Goal> objetivos = new HashSet<>();


    public void delete() {
        this.activo = false;
    }

    // Métodos Getters
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
    public Set<User> getSeguidos() {
        return seguidos;
    }
    public Set<User> getSeguidores() {
        return seguidores;
    }
    public boolean isActivo() {
        return activo;
    }

    // Métodos Setters
    public void setId(Long id) {
        this.id = id;
    }
    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }
    public void setNombrePerfil(String nombrePerfil) {
        this.nombrePerfil = nombrePerfil;
    }
    public void setCorreo(String correo) {
        this.correo = correo;
    }
    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }
    public void setRol(int rol) {
        this.rol = rol;
    }
    public void setFotoPerfil(String fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }
    public void setBiografia(String biografia) {
        this.biografia = biografia;
    }
    public void setActivo(boolean activo) {
        this.activo = activo;
    }

}
