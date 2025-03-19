package es.readtoowell.api_biblioteca.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "formato_usuario")
public class UserBookFormat {
    @EmbeddedId
    private UserBookFormatId id = new UserBookFormatId();
    @ManyToOne
    @MapsId("userLibraryBookId")
    @JoinColumns({
            @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario"),
            @JoinColumn(name = "id_libro", referencedColumnName = "id_libro")
    })
    private UserLibraryBook libroBiblioteca;
    @ManyToOne
    @MapsId("formatId")
    @JoinColumn(name = "id_formato", referencedColumnName = "id_formato", nullable = false)
    private Format formato;

    // Métodos Getters y Setters
    public UserBookFormatId getId() {
        return id;
    }
    public void setId(UserBookFormatId id) {
        this.id = id;
    }

    public UserLibraryBook getLibroBiblioteca() {
        return libroBiblioteca;
    }
    public void setLibroBiblioteca(UserLibraryBook libroBiblioteca) {
        this.libroBiblioteca = libroBiblioteca;
    }

    public Format getFormato() {
        return formato;
    }
    public void setFormato(Format formato) {
        this.formato = formato;
    }
}
