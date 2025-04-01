package es.readtoowell.api_biblioteca.model.enums;

/**
 * Enumeración que representa los posibles roles de los usuarios.
 */
public enum Role {
    USER(0), AUTHOR(1), ADMIN(2);

    private final int value;

    Role(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static Role fromValue(int value) {
        for (Role role : Role.values()) {
            if (role.getValue() == value) {
                return role;
            }
        }
        throw new IllegalArgumentException("Rol inválido: " + value);
    }
}
