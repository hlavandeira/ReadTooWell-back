package es.readtoowell.api_biblioteca.repository;

import es.readtoowell.api_biblioteca.model.Suggestion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface SuggestionRepository extends JpaRepository<Suggestion, Long> {
    Page<Suggestion> findByEstado(int estado, Pageable pageable);
}
