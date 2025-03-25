package es.readtoowell.api_biblioteca.model;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class UserBookFormatId implements Serializable {
    private UserLibraryBookId userLibraryBookId;
    private Long formatId;

    public UserBookFormatId() {}
    public UserBookFormatId(UserLibraryBookId userLibraryBookId, Long formatId) {
        this.userLibraryBookId = userLibraryBookId;
        this.formatId = formatId;
    }

    // Métodos Getters y Setters
    public UserLibraryBookId getUserLibraryBookId() {
        return userLibraryBookId;
    }
    public void setUserLibraryBookId(UserLibraryBookId userLibraryBookId) {
        this.userLibraryBookId = userLibraryBookId;
    }

    public Long getFormatId() {
        return formatId;
    }
    public void setFormatId(Long formatId) {
        this.formatId = formatId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserBookFormatId that = (UserBookFormatId) o;
        return Objects.equals(userLibraryBookId, that.userLibraryBookId) &&
                Objects.equals(formatId, that.formatId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userLibraryBookId, formatId);
    }
}
