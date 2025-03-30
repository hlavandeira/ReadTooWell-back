package es.readtoowell.api_biblioteca.model.enums;

/**
 * Enumeración que representa los posibles estados de lectura de un libro.
 */
public enum ReadingStatus {
    PENDING(0), READING(1), READ(2),
    PAUSED(3), ABANDONED(4);

    private final int value;

    ReadingStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static ReadingStatus fromValue(int value) {
        for (ReadingStatus status : ReadingStatus.values()) {
            if (status.getValue() == value) {
                return status;
            }
        }
        throw new IllegalArgumentException("Estado inválido: " + value);
    }
}
