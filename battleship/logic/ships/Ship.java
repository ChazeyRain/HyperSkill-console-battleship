package battleship.logic.ships;

import battleship.logic.field.CellStatus;
import battleship.logic.Errors;
import battleship.logic.field.ShootStatus;

public class Ship {

    private final int x1;
    private final int x2;
    private final int y1;
    private final int y2;
    private Errors creationStatus;

    private boolean[] cellDead;
    private int hp;

    int getX1() {
        return x1;
    }

    int getX2() {
        return x2;
    }

    int getY1() {
        return y1;
    }

    int getY2() {
        return y2;
    }

    public Errors getCreationStatus() {
        return creationStatus;
    }

    private void setCreationStatus(Errors creationStatus) {
        this.creationStatus = creationStatus;
    }

    private Ship(ShipTypes type, int x1, int y1, int x2, int y2) {

        this.x1 = Math.min(x1, x2);
        this.x2 = Math.max(x1, x2);
        this.y1 = Math.min(y1, y2);
        this.y2 = Math.max(y1, y2);
        this.hp = type.getLength();
        this.cellDead = new boolean[type.getLength()];
        this.creationStatus = Errors.DONE;
    }

    private boolean canPlace(Ship ship) {

        int x1 = ship.getX1();
        int x2 = ship.getX2();
        int y1 = ship.getY1();
        int y2 = ship.getY2();

        boolean canPlace = ((this.x1 - 1) <= x1 && (this.x2 + 1) >= x1) && ((this.y1 - 1) <= y1 && (this.y2 + 1) >= y1);
        canPlace = canPlace || ((this.x1 - 1) <= x2 && (this.x2 + 1) >= x2) && ((this.y1 - 1) <= y2 && (this.y2 + 1) >= y2);
        return !canPlace;
    }

    public CellStatus checkCell(int x, int y) {
        if (x >= this.x1 && x <= this.x2 && y >= this.y1 && y <= this.y2) {
            int cellId = x - x1 + y - y1;
            if (cellDead[cellId]) {
                return CellStatus.DEAD;
            }
            return CellStatus.ALIVE;
        }

        return CellStatus.EMPTY;
    }

    public ShootStatus shoot(int x, int y) {

        int cellId = x - x1 + y - y1;

        if (cellId > cellDead.length || cellId < 0
                || !(x >= this.x1 && x <= this.x2 && y >= this.y1 && y <= this.y2)) {
            return ShootStatus.MISS;
        }

        if (cellDead[cellId]) {
            return ShootStatus.MISS;
        } else {
            cellDead[cellId] = true;
            hp--;
            if (hp == 0) {
                return ShootStatus.DEAD;
            }
            return ShootStatus.HIT;
        }
    }

    public static Ship shipFactory(ShipTypes type, Ship[] otherShips, int x1, int y1, int x2, int y2) {

        Ship newShip = new Ship(type, x1, y1, x2, y2);

        if (x1 != x2 && y1 != y2) {
            newShip.setCreationStatus(Errors.WRONG_POSITION);
            return newShip;
        }

        int sqrLength = (x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2);

        if (sqrLength != (type.getLength() - 1) * (type.getLength() - 1)) {
            newShip.setCreationStatus(Errors.WRONG_SIZE);
            return newShip;
        }

        for (Ship ship : otherShips) {
            if (ship == null) {
                break;
            }
            if (!ship.canPlace(newShip)) {
                newShip.setCreationStatus(Errors.INTERFERENCE);
                return newShip;
            }
        }

        return newShip;
    }

    /*
    You may ask why in the name of god I've done this?
    So there is my answer. I wanted to create field with different sizes, so I needed flexible logic.
    Maybe there are better ways to implement this, but I don't see them
     */
}