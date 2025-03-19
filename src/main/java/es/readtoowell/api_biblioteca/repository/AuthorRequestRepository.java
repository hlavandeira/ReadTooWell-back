package es.readtoowell.api_biblioteca.repository;

import es.readtoowell.api_biblioteca.model.AuthorRequest;
import es.readtoowell.api_biblioteca.model.enums.RequestStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface AuthorRequestRepository extends JpaRepository<AuthorRequest, Long> {
    boolean existsByUsuarioIdAndEstadoIn(Long usuarioId, List<Integer> estados);

    @Query("SELECT ar FROM AuthorRequest ar " +
            "LEFT JOIN FETCH ar.libros WHERE ar.id = :id")
    Optional<AuthorRequest> findByIdWithBooks(@Param("id") Long id);

    Page<AuthorRequest> findByEstado(int estado, Pageable pageable);

    @Query("SELECT a FROM AuthorRequest a " +
            "WHERE a.usuario.id = :userId AND a.estado IN :estados " +
            "AND a.activo = true ORDER BY a.fechaEnviada DESC")
    Optional<AuthorRequest> findLatestRequestByUserAndStatus(
            @Param("userId") Long userId, @Param("estados") List<Integer> estados);
}
