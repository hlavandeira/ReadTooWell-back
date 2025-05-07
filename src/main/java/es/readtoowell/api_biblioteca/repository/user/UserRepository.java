package es.readtoowell.api_biblioteca.repository.user;

import es.readtoowell.api_biblioteca.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Busca un usuario por su correo electrónico.
     *
     * @param email Correo electrónico del usuario
     * @return Un {@code Optional} con el usuario. Si no se encuentra, estará vacío.
     */
    Optional<User> findByEmail(String email);

    /**
     * Busca usuarios por nombre de usuario o nombre de perfil.
     *
     * @param searchString Cadena que se compara con el nombre de usuario o de perfil
     * @param pageable Información de paginación
     * @return Página con los usuarios resultantes, paginada según {@code pageable}
     */
    @Query("""
    select u from User u
    where (:searchString is null or
        lower(u.username) like lower(concat('%', :searchString, '%'))
        or lower(u.profileName) like lower(concat('%', :searchString, '%')))
        and u.role <> 2
    """)
    Page<User> searchUsers(@Param("searchString") String searchString, Pageable pageable);

    @Query("select u from User u where u.role = 1")
    Page<User> findAuthors(Pageable pageable);
}
