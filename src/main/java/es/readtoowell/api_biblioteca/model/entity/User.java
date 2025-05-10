package es.readtoowell.api_biblioteca.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import es.readtoowell.api_biblioteca.model.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Entidad que representa los usuarios.
 */
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
    private String username;
    @Column(name = "nombre_perfil")
    private String profileName;
    @Column(name = "correo")
    private String email;
    @Column(name = "contraseña")
    private String password;
    @Column(name = "rol")
    private int role;
    @Column(name = "foto_perfil")
    private String profilePic;
    @Column(name = "biografia", length = 2000)
    private String biography;
    /**
     * Lista de usuarios a los que sigue el propio usuario.
     */
    @ManyToMany
    @JoinTable(
            name="seguimiento",
            joinColumns = @JoinColumn(name= "id_seguidor"),
            inverseJoinColumns = @JoinColumn(name = "id_seguido")
    )
    @JsonIgnore
    private List<User> followedUsers = new ArrayList<>();
    /**
     * Lista de usuarios que siguen al propio usuario.
     */
    @ManyToMany(mappedBy = "followedUsers")
    @JsonIgnore
    private List<User> followers = new ArrayList<>();
    /**
     * Lista de géneros favoritos del usuario.
     */
    @ManyToMany
    @JoinTable(name = "genero_favorito",
            joinColumns = @JoinColumn(name = "id_usuario"),
            inverseJoinColumns = @JoinColumn(name = "id_genero"))
    @JsonIgnore
    private List<Genre> favoriteGenres = new ArrayList<>();
    /**
     * Lista de libros favoritos del usuario.
     */
    @ManyToMany
    @JoinTable(name = "libro_favorito",
            joinColumns = @JoinColumn(name = "id_usuario"),
            inverseJoinColumns = @JoinColumn(name = "id_libro"))
    @JsonIgnore
    private List<Book> favoriteBooks = new ArrayList<>();

    /**
     * Devuelve el rol del usuario como Enum.
     *
     * @return Enum con el rol del usuario
     */
    @Transient
    public Role getRoleEnum() {
        return Role.fromValue(this.role);
    }

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

    /**
     * Devuelve los usuarios a los que sigue el propio usuario.
     *
     * @return Listado con los usuarios a los que sigue el propio usuario
     */
    public List<User> getFollowedUsers() {
        return followedUsers;
    }

    /**
     * Devuelve los usuarios seguidores del propio usuario.
     *
     * @return Listado con los usuarios seguidores del propio usuario
     */
    public List<User> getFollowers() {
        return followers;
    }

    /**
     * Devuelve los géneros favoritos del usuario.
     *
     * @return Listado con los géneros favoritos del usuario
     */
    public List<Genre> getFavoriteGenres() { return favoriteGenres; }

    /**
     * Establece los géneros favoritos del usuario.
     *
     * @param favoriteGenres Listado con los nuevos géneros favoritos
     */
    public void setFavoriteGenres(List<Genre> favoriteGenres) { this.favoriteGenres = favoriteGenres; }

    /**
     * Devuelve los libros favoritos del usuario.
     *
     * @return Listado con los libros favoritos del usuario
     */
    public List<Book> getFavoriteBooks() { return favoriteBooks; }

    /**
     * Establece los libros favoritos del usuario.
     *
     * @param favoriteBooks Listado con los nuevos libros favoritos
     */
    public void setFavoriteBooks(List<Book> favoriteBooks) { this.favoriteBooks = favoriteBooks; }
}
