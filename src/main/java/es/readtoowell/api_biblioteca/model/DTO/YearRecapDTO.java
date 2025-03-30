package es.readtoowell.api_biblioteca.model.DTO;

import java.util.List;

/**
 * DTO que representa los detalles del resumen anual de un usuario.
 */
public class YearRecapDTO {
    private List<GoalDTO> annualGoals;
    private long totalBooksRead;
    private long totalPagesRead;
    private List<GenreDTO> mostReadGenres;
    private List<SimpleBookDTO> topRatedBooks;

    // Métodos Getters y Setters

    /**
     * Devuelve los objetivos anuales en curso del usuario.
     *
     * @return Objetivos anuales en curso
     */
    public List<GoalDTO> getAnnualGoals() {
        return annualGoals;
    }

    /**
     * Establece un valor para los objetivos anuales en curso del usuario.
     *
     * @param annualGoals Lista con los nuevos objetivos anuales en curso
     */
    public void setAnnualGoals(List<GoalDTO> annualGoals) {
        this.annualGoals = annualGoals;
    }

    /**
     * Devuelve el total de libros leídos en el año actual.
     *
     * @return Total de libros leídos en el año
     */
    public long getTotalBooksRead() {
        return totalBooksRead;
    }

    /**
     * Establece un valor para el total de libros leídos en el año actual.
     *
     * @param totalBooksRead Nuevo total de libros leídos en el año
     */
    public void setTotalBooksRead(long totalBooksRead) {
        this.totalBooksRead = totalBooksRead;
    }

    /**
     * Devuelve el total de páginas leídas en el año actual.
     *
     * @return Total de páginas leídas en el año
     */
    public long getTotalPagesRead() {
        return totalPagesRead;
    }

    /**
     * Establece un valor para el total de páginas leídas en el año actual.
     *
     * @param totalPagesRead Nuevo total de páginas leídas en el año
     */
    public void setTotalPagesRead(long totalPagesRead) {
        this.totalPagesRead = totalPagesRead;
    }

    /**
     * Devuelve los géneros más leídos en el año actual.
     *
     * @return Lista con los géneros más leídos
     */
    public List<GenreDTO> getMostReadGenres() {
        return mostReadGenres;
    }

    /**
     * Establece un valor para los géneros más leídos en el año actual.
     *
     * @param mostReadGenres Lista con los nuevos géneros más leídos
     */
    public void setMostReadGenres(List<GenreDTO> mostReadGenres) {
        this.mostReadGenres = mostReadGenres;
    }

    /**
     * Devuelve los libros mejor valorados en el año actual.
     *
     * @return Lista con los libros mejor valorados
     */
    public List<SimpleBookDTO> getTopRatedBooks() {
        return topRatedBooks;
    }

    /**
     * Establece un valor para los libros mejor valorados en el año actual.
     *
     * @param topRatedBooks Lista con los nuevos libros mejor valorados
     */
    public void setTopRatedBooks(List<SimpleBookDTO> topRatedBooks) {
        this.topRatedBooks = topRatedBooks;
    }
}
