package es.readtoowell.api_biblioteca.repository;

import es.readtoowell.api_biblioteca.model.Suggestion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SuggestionRepository extends JpaRepository<Suggestion, Long> {
}
