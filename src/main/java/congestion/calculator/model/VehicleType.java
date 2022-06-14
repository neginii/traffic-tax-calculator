package congestion.calculator.model;

public enum VehicleType {

    MOTORCYCLE("MOTORCYCLE"),
    BUS("BUS"),
    EMERGENCY("EMERGENCY"),
    DIPLOMAT("DIPLOMAT"),
    MILITARY("MILITARY"),
    FOREIGN("FOREIGN");
    private final String name;

    VehicleType(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
