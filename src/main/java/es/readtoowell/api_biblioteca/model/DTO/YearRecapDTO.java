package es.readtoowell.api_biblioteca.model.DTO;

import es.readtoowell.api_biblioteca.model.Book;
import es.readtoowell.api_biblioteca.model.Genre;

import java.util.List;

public class YearRecapDTO {
    private List<GoalDTO> objetivosAnuales;
    private long totalLibros;
    private long totalPaginas;
    private List<GenreDTO> generosMasLeidos;
    private List<SimpleBookDTO> librosMejorValorados;

    public List<GoalDTO> getObjetivoAnual() {
        return objetivosAnuales;
    }
    public void setObjetivoAnual(List<GoalDTO> objetivosAnuales) {
        this.objetivosAnuales = objetivosAnuales;
    }

    public long getTotalLibros() {
        return totalLibros;
    }
    public void setTotalLibros(long totalLibros) {
        this.totalLibros = totalLibros;
    }

    public long getTotalPaginas() {
        return totalPaginas;
    }
    public void setTotalPaginas(long totalPaginas) {
        this.totalPaginas = totalPaginas;
    }

    public List<GenreDTO> getGenerosMasLeidos() {
        return generosMasLeidos;
    }
    public void setGenerosMasLeidos(List<GenreDTO> generosMasLeidos) {
        this.generosMasLeidos = generosMasLeidos;
    }

    public List<SimpleBookDTO> getLibrosMejorValorados() {
        return librosMejorValorados;
    }
    public void setLibrosMejorValorados(List<SimpleBookDTO> librosMejorValorados) {
        this.librosMejorValorados = librosMejorValorados;
    }
}
