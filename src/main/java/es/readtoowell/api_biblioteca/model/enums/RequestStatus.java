package es.readtoowell.api_biblioteca.model.enums;

/**
 * Enumeración que representa los posibles estados de las solicitudes de autor.
 */
public enum RequestStatus {
    PENDING(0), ACCEPETD(1), REJECTED(2);

    private final int value;

    RequestStatus(int value) {
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
