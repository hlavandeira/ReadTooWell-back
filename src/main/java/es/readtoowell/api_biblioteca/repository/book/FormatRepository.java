package es.readtoowell.api_biblioteca.repository.book;

import es.readtoowell.api_biblioteca.model.entity.Format;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FormatRepository extends JpaRepository<Format, Long> {
}
