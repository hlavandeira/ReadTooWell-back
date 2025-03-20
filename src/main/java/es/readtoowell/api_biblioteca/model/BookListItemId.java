package es.readtoowell.api_biblioteca.model;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class BookListItemId implements Serializable {
    private Long listaId;
    private Long libroId;

    public BookListItemId() {}
    public BookListItemId(Long listaId, Long libroId) {
        this.libroId = libroId;
        this.listaId = listaId;
    }

    public Long getListaId() {
        return listaId;
    }
    public void setListaId(Long listaId) {
        this.listaId = listaId;
    }

    public Long getLibroId() {
        return libroId;
    }
    public void setLibroId(Long libroId) {
        this.libroId = libroId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookListItemId that = (BookListItemId) o;
        return Objects.equals(listaId, that.listaId) && Objects.equals(libroId, that.libroId);
    }
    @Override
    public int hashCode() {
        return Objects.hash(listaId, libroId);
    }
}
