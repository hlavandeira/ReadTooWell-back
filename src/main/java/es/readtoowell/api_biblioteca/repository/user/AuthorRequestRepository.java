package es.readtoowell.api_biblioteca.repository.user;

import es.readtoowell.api_biblioteca.model.entity.AuthorRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AuthorRequestRepository extends JpaRepository<AuthorRequest, Long> {
    /**
     * Comprueba si existe una solicitud de autor para un usuario con unos estados específicos.
     *
     * @param userId Usuario del que se comprueba si existe solicitud
     * @param statusList Estados para buscar la solicitud
     * @return 'true' si existe, 'false' en caso contrario
     */
    boolean existsByUserIdAndStatusIn(Long userId, List<Integer> statusList);

    /**
     * Busca una solicitud de autor por ID y la devuelve junto con los libros asociados.
     *
     * @param id ID de la solicitud
     * @return Un {@code Optional} con la solicitud de autor. Si no se encuentra, estará vacío.
     */
    @Query("""
    SELECT ar FROM AuthorRequest ar
    LEFT JOIN FETCH ar.books WHERE ar.id = :id
    """)
    Optional<AuthorRequest> findByIdWithBooks(@Param("id") Long id);

    /**
     * Filtra las solicitudes de autor por estado.
     *
     * @param status Estado con el que se buscan las solicitudes
     * @param pageable Información de paginación
     * @return Página con las solicitudes resultantes, paginada según {@code pageable}
     */
    Page<AuthorRequest> findByStatus(int status, Pageable pageable);

    /**
     * Busca la última solicitud de autor enviada por un usuario con unos estados específicos.
     *
     * @param userId ID del usuario que envió la solicitud
     * @param statusList Estados que puede tener la solicitud
     * @return Un {@code Optional} con la última solicitud del usuario. Si no se encuentra, estará vacío.
     */
    @Query("""
    SELECT a FROM AuthorRequest a
    WHERE a.user.id = :userId AND a.status IN :statusList
    ORDER BY a.dateSent DESC
    """)
    Optional<AuthorRequest> findLatestRequestByUserAndStatus(
            @Param("userId") Long userId, @Param("statusList") List<Integer> statusList);
}
