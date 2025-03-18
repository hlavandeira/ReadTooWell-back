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

    @Query("SELECT ar FROM AuthorRequest ar LEFT JOIN FETCH ar.libros WHERE ar.id = :id")
    Optional<AuthorRequest> findByIdWithBooks(@Param("id") Long id);

    Page<AuthorRequest> findByEstado(int estado, Pageable pageable);

    Optional<AuthorRequest> findTopByUsuarioIdAndEstadoInAndActivoTrueOrderByFechaEnviadaDesc(
            Long usuarioId, List<Integer> estados);
}
