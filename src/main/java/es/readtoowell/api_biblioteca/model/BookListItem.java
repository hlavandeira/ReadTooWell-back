package es.readtoowell.api_biblioteca.model;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "libro_lista")
public class BookListItem {
    @EmbeddedId
    private BookListItemId id;
    @ManyToOne
    @MapsId("listaId")
    @JoinColumn(name = "id_lista")
    private BookList lista;
    @ManyToOne
    @MapsId("libroId")
    @JoinColumn(name = "id_libro")
    private Book libro;
    @Column(name = "fecha_añadido")
    private Date fechaAñadido;

    // Métodos Getters y Setters
    public BookListItemId getId() {
        return id;
    }
    public void setId(BookListItemId id) {
        this.id = id;
    }

    public BookList getLista() {
        return lista;
    }
    public void setLista(BookList lista) {
        this.lista = lista;
    }

    public Book getLibro() {
        return libro;
    }
    public void setLibro(Book libro) {
        this.libro = libro;
    }

    public Date getFechaAñadido() {
        return fechaAñadido;
    }
    public void setFechaAñadido(Date fechaAñadido) {
        this.fechaAñadido = fechaAñadido;
    }
}