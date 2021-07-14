package battleship.ui;

import battleship.logic.field.CellStatus;
import battleship.logic.Errors;
import battleship.logic.ships.ShipTypes;
import battleship.logic.field.ShootStatus;

public interface UI {
    public int[] getCoordinates(ShipTypes type);
    public void receiveMessage(String message);
    public void receiveError(Errors shipCreationStatus);
    public void printField(CellStatus[][] field, boolean hideShips);
    public int[] getShotCoordinates();
    public void receiveShotStatus(ShootStatus status);
}
