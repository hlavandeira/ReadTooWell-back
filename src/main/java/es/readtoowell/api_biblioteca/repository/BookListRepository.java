package es.readtoowell.api_biblioteca.repository;

import es.readtoowell.api_biblioteca.model.BookList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.Set;

public interface BookListRepository extends JpaRepository<BookList, Long> {
    Page<BookList> findByUsuarioId(Long id, Pageable pageable);

    @Query("SELECT l FROM BookList l LEFT JOIN FETCH l.libros LEFT JOIN FETCH l.generos WHERE l.id = :id")
    Optional<BookList> findByIdWithRelations(@Param("id") Long id);
}
