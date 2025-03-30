package es.readtoowell.api_biblioteca.model.enums;

/**
 * Enumeración que representa los posibles estados de las sugerencias de libros.
 */
public enum SuggestionStatus {
    PENDING(0), ACCEPTED(1), ADDED(2), REJECTED(3);

    private final int value;

    SuggestionStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static SuggestionStatus fromValue(int value) {
        for (SuggestionStatus status : SuggestionStatus.values()) {
            if (status.getValue() == value) {
                return status;
            }
        }
        throw new IllegalArgumentException("Estado inválido: " + value);
    }
}
