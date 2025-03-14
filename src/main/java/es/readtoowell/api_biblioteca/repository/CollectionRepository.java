package es.readtoowell.api_biblioteca.repository;

import es.readtoowell.api_biblioteca.model.Collection;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CollectionRepository extends JpaRepository<Collection, Long> {
}
