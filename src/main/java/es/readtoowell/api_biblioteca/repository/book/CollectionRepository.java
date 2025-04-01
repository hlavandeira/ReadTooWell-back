package es.readtoowell.api_biblioteca.repository.book;

import es.readtoowell.api_biblioteca.model.entity.Collection;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CollectionRepository extends JpaRepository<Collection, Long> {
}
