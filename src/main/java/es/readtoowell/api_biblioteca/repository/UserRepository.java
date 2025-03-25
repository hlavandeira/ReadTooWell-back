package es.readtoowell.api_biblioteca.repository;

import es.readtoowell.api_biblioteca.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findById(Long id);

    Optional<User> findByCorreo(String correo);

    @Query("""
            select u from User u
            where (:searchString is null or 
                lower(u.nombreUsuario) like lower(concat('%', :searchString, '%'))
                or lower(u.nombrePerfil) like lower(concat('%', :searchString, '%')))
            """)
    Page<User> searchUsers(
            @Param("searchString") String searchString,
            Pageable pageable
    );
}
