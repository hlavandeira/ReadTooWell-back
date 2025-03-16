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
    @Column(name = "nombre_usuario")
    private String nombreUsuario;
    @Column(name = "nombre_perfil")
    private String nombrePerfil;
    private String correo;
    private String contraseña;
    private int rol;
    @Column(name = "foto_perfil")
    private String fotoPerfil;
    @Column(name = "biografia", length = 2000)
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
