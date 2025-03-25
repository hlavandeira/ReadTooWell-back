package es.readtoowell.api_biblioteca.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class UserLibraryBookId implements Serializable {
    @Column(name = "id_usuario")
    private Long idUsuario;
    @Column(name = "id_libro")
    private Long idLibro;

    public UserLibraryBookId() {}
    public UserLibraryBookId(Long idUsuario, Long idLibro) {
        this.idUsuario = idUsuario;
        this.idLibro = idLibro;
    }

    // Getters y setters
    public Long getIdUsuario() {
        return idUsuario;
    }
    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Long getIdLibro() {
        return idLibro;
    }
    public void setIdLibro(Long idLibro) {
        this.idLibro = idLibro;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserLibraryBookId that = (UserLibraryBookId) o;
        return Objects.equals(idUsuario, that.idUsuario) &&
                Objects.equals(idLibro, that.idLibro);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idUsuario, idLibro);
    }
}
