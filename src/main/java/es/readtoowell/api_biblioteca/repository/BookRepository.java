package es.readtoowell.api_biblioteca.repository;

import es.readtoowell.api_biblioteca.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    Optional<Book> findById(Long id);

    @Query("""
            select b from Book b left join b.coleccion c 
            where (:searchString is null or 
                lower(b.titulo) like lower(concat('%', :searchString, '%'))
                or lower(b.autor) like lower(concat('%', :searchString, '%'))
                or lower(c.nombre) like lower(concat('%', :searchString, '%'))) 
            and (:minPags is null or b.numeroPaginas >= :minPags) 
            and (:maxPags is null or b.numeroPaginas <= :maxPags) 
            and (:minAño is null or b.añoPublicacion >= :minAño) 
            and (:maxAño is null or b.añoPublicacion <= :maxAño)""")
    Page<Book> filterBooks(
            @Param("searchString") String searchString,
            @Param("minPags") Integer minPags,
            @Param("maxPags") Integer maxPags,
            @Param("minAño") Integer minAño,
            @Param("maxAño") Integer maxAño,
            Pageable pageable
    );
}
