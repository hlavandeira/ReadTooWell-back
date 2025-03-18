package es.readtoowell.api_biblioteca.model.enums;

public enum SuggestionStatus {
    PENDIENTE(0), ACEPTADA(1), AÑADIDA(2), RECHAZADA(3);

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
