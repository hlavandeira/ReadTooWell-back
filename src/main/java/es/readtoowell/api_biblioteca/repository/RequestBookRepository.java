package es.readtoowell.api_biblioteca.repository;

import es.readtoowell.api_biblioteca.model.RequestBook;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestBookRepository extends JpaRepository<RequestBook, Long> {
}
