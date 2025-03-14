package es.readtoowell.api_biblioteca.mapper;

import es.readtoowell.api_biblioteca.DTO.BookDTO;
import es.readtoowell.api_biblioteca.DTO.GenreDTO;
import es.readtoowell.api_biblioteca.model.Book;
import es.readtoowell.api_biblioteca.model.Genre;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class BookMapper {
    @Autowired
    private GenreMapper genreMapper;

    public BookDTO toDTO(Book book) {
        BookDTO dto = new BookDTO();

        dto.setId(book.getId());
        dto.setTitulo(book.getTitulo());
        dto.setAutor(book.getAutor());
        dto.setAñoPublicacion(book.getAñoPublicacion());
        dto.setNumeroPaginas(book.getNumeroPaginas());
        dto.setEditorial(book.getEditorial());
        dto.setSinopsis(book.getSinopsis());
        dto.setPortada(book.getPortada());
        dto.setIsbn(book.getIsbn());
        dto.setActivo(book.isActivo());
        dto.setNumColeccion(book.getNumColeccion());
        dto.setIdColeccion(book.getIdColeccion());

        Set<GenreDTO> genreDTOs = book.getGeneros()
                .stream()
                .map(genreMapper::toDTO)
                .collect(Collectors.toSet());
        dto.setGeneros(genreDTOs);

        return dto;
    }

    public Book toEntity(BookDTO dto) {
        Book book = new Book();

        book.setId(dto.getId());
        book.setTitulo(dto.getTitulo());
        book.setAutor(dto.getAutor());
        book.setAñoPublicacion(dto.getAñoPublicacion());
        book.setNumeroPaginas(dto.getNumeroPaginas());
        book.setEditorial(dto.getEditorial());
        book.setSinopsis(dto.getSinopsis());
        book.setPortada(dto.getPortada());
        book.setIsbn(dto.getIsbn());
        book.setActivo(dto.isActivo());
        book.setNumColeccion(dto.getNumColeccion());

        Set<Genre> genres = dto.getGeneros()
                .stream()
                .map(genreMapper::toEntity)
                .collect(Collectors.toSet());
        book.setGeneros(genres);

        return book;
    }
}
