package es.readtoowell.api_biblioteca.model.enums;

public enum ReadingStatus {

    PENDIENTE(0), LEYENDO(1), LEIDO(2),
    PAUSADO(3), ABANDONADO(4);

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
