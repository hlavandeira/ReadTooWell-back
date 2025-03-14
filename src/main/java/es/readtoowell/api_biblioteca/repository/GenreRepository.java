package es.readtoowell.api_biblioteca.repository;

import es.readtoowell.api_biblioteca.model.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {
}
