package shmax.rickandmortyapp.model;

public enum Status {
    ALIVE("Alive"), DEAD("Dead"), UNKNOWN("unknown");

    private final String value;

    Status(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
