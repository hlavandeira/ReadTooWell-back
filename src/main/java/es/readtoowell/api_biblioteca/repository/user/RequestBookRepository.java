package es.readtoowell.api_biblioteca.repository.user;

import es.readtoowell.api_biblioteca.model.entity.RequestBook;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestBookRepository extends JpaRepository<RequestBook, Long> {
}
