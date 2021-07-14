package battleship.logic.field;

import battleship.logic.Errors;
import battleship.logic.ships.Ship;
import battleship.logic.ships.ShipTypes;
import battleship.ui.UI;

import static battleship.logic.ships.ShipTypes.*;

public class Field {

    private final UI ui;
    private CellStatus[][] field;
    private Ship[] ships;
    private int totalHP = 0;
    private final UI enemyUI;

    public int getTotalHP() {
        return totalHP;
    }

    //battleship with your field size and ship types
    public Field(int x, int y, UI ui, UI enemyUI, ShipTypes[] types) {
        this.ui = ui;
        this.enemyUI = enemyUI;
        field = new CellStatus[x][y];
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                field[i][j] = CellStatus.EMPTY;
            }
        }

        for (ShipTypes type : types) {
            totalHP += type.getLength();
        }

        ui.printField(field, false);
        placeShips(types);
    }

    //Classic battleship game
    public Field(UI ui, UI enemyUI) {
        this(10, 10, ui, enemyUI, new ShipTypes[]{AircraftCarrier, Battleship, Submarine, Cruiser, Destroyer});
    }

    private void placeShips(ShipTypes[] types) {

        int[] coordinates;
        int shipCount = types.length;
        int currentShipCount = 0;
        ships = new Ship[shipCount];

        while (currentShipCount != shipCount) {
            coordinates = ui.getCoordinates(types[currentShipCount]);

            Ship newShip = Ship.shipFactory(types[currentShipCount], ships, coordinates[0], coordinates[1], coordinates[2], coordinates[3]);

            if (coordinates[0] < 0 || coordinates[0] > field.length
                    || coordinates[1] < 0 || coordinates[1] > field[0].length
                    || coordinates[2] < 0 || coordinates[2] > field.length
                    || coordinates[3] < 0 || coordinates[3] > field[0].length) {

                ui.receiveError(Errors.WRONG_COORDINATES);

            } else if (newShip.getCreationStatus() == Errors.DONE) {
                ships[currentShipCount] = newShip;
                currentShipCount++;
                printField();
            } else {
                ui.receiveError(newShip.getCreationStatus());
                newShip = null;
            }
        }
    }

    public boolean getShot() {
        int[] coordinates = ui.getShotCoordinates();

        if (coordinates[0] < 0 || coordinates[0] >= field.length
                || coordinates[1] < 0 || coordinates[1] >= field[0].length) {
            enemyUI.receiveError(Errors.WRONG_COORDINATES);
            return false;
        }

        if (field[coordinates[0]][coordinates[1]] == CellStatus.DEAD
            || field[coordinates[0]][coordinates[1]] == CellStatus.MISS) {

            enemyUI.receiveError(Errors.DOUBLE_SHOOT);
            return true;
        }

        ShootStatus status;

        for (Ship ship : ships) {
            status = ship.shoot(coordinates[0], coordinates[1]);
            if (status != ShootStatus.MISS) {
                totalHP--;
                if (totalHP == 0) {
                    enemyUI.receiveShotStatus(ShootStatus.ENDGAME);
                } else {
                    enemyUI.receiveShotStatus(status);
                }

                return true;
            }
        }
        field[coordinates[0]][coordinates[1]] = CellStatus.MISS;
        enemyUI.receiveShotStatus(ShootStatus.MISS);
        return true;
    }

    public void printField() {
        fillCells();
        ui.printField(field, false);
    }

    public void sendFieldToEnemy() {
        fillCells();
        enemyUI.printField(field, true);
    }

    private void fillCells() {
        if (ships == null) {
            return;
        }
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[0].length; j++) {
                if (field[i][j] == CellStatus.MISS || field[i][j] == CellStatus.DEAD) {
                    continue;
                }
                for (Ship ship : ships) {
                    if (ship == null) {
                        continue;
                    }
                    if (ship.checkCell(i, j) == CellStatus.DEAD) {
                        field[i][j] = CellStatus.DEAD;
                        break;
                    }
                    if (ship.checkCell(i, j) == CellStatus.ALIVE) {
                        field[i][j] = CellStatus.ALIVE;
                        break;
                    }
                }
            }
        }
    }
}
