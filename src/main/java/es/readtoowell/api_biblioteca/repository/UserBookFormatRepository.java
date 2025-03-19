package es.readtoowell.api_biblioteca.repository;

import es.readtoowell.api_biblioteca.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserBookFormatRepository extends JpaRepository<UserBookFormat, UserBookFormatId> {
    @Query("""
    SELECT ubf 
    FROM UserBookFormat ubf 
    WHERE ubf.libroBiblioteca.libro.id = :libro 
    AND ubf.libroBiblioteca.usuario.id = :usuario 
    """)
    List<UserBookFormat> findFormatsByUserAndBook(@Param("usuario") Long usuario, @Param("libro") Long libro);

    boolean existsByLibroBibliotecaAndFormato(UserLibraryBook userLibraryBook, Format format);

    Optional<UserBookFormat> findByLibroBibliotecaAndFormato(UserLibraryBook libroBiblioteca, Format formato);
}
