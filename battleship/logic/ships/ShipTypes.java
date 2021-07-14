package battleship.logic.ships;

public enum ShipTypes {
    AircraftCarrier(5, "Aircraft Carrier"),
    Battleship(4, "Battleship"),
    Cruiser(3, "Cruiser"),
    Submarine(3, "Submarine"),
    Destroyer(2, "Destroyer");

    private final int length;
    private final String name;

    ShipTypes(int length, String name) {
        this.length = length;
        this.name = name;
    }

    public int getLength() {
        return this.length;
    }

    public String getName() {
        return this.name;
    }
}
