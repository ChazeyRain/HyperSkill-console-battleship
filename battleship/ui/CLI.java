package battleship.ui;

import battleship.logic.field.CellStatus;
import battleship.logic.Errors;
import battleship.logic.ships.ShipTypes;
import battleship.logic.field.ShootStatus;

import java.io.IOException;
import java.util.Locale;
import java.util.Scanner;

public class CLI implements UI {
    Scanner scanner = new Scanner(System.in);

    @Override
    public int[] getCoordinates(ShipTypes type) {
        int[] args = new int[4];

        System.out.printf("Enter coordinates of the %s (%d cells):\n", type.getName(), type.getLength());

        String input = scanner.nextLine().toUpperCase(Locale.ROOT);

        if (!input.matches("[A-Z][1-9][0-9]? [A-Z][1-9][0-9]?")) {
            System.out.println("Error: Wrong input");
            return getCoordinates(type);
        }

        String[] inputs = input.split(" ");

        args[0] = inputs[0].charAt(0) - 65;
        args[1] = Integer.parseInt(inputs[0].substring(1)) - 1;
        args[2] = inputs[1].charAt(0) - 65;
        args[3] = Integer.parseInt(inputs[1].substring(1)) - 1;

        return args;
    }

    @Override
    public void receiveMessage(String message) {
        System.out.println(message);
    }

    @Override
    public void receiveError(Errors shipCreationStatus) {
        switch (shipCreationStatus) {
            case WRONG_SIZE:
                System.out.println("Error: wrong size!");
                break;
            case WRONG_POSITION:
                System.out.println("Error: wrong position!");
                break;
            case INTERFERENCE:
                System.out.println("Error: ships are too close");
                break;
            case WRONG_COORDINATES:
                System.out.println("Error: wrong coordinates");
                break;
            case DOUBLE_SHOOT:
                System.out.println("Error: you already shoot this one");
                break;
        }
    }

    @Override
    public void printField(CellStatus[][] field, boolean hideShips) {
        System.out.print(" ");
        for (int i = 1; i <= field.length; i++) {
            System.out.printf(" %d", i);
        }

        System.out.println();

        char rowName = 'A';

        for (CellStatus[] row : field) {
            System.out.print(rowName);
            rowName = (char) (rowName + 1);
            for (CellStatus cell : row) {
                switch (cell) {
                    case ALIVE:
                         if (hideShips) {
                             System.out.print(" ~");
                         } else {
                             System.out.print(" O");
                         }
                        break;
                    case DEAD:
                        System.out.print(" X");
                        break;
                    case EMPTY:
                        System.out.print(" ~");
                        break;
                    case MISS:
                        System.out.print(" M");
                }
            }
            System.out.println();
        }
    }

    @Override
    public int[] getShotCoordinates() {
        int[] args = new int[2];

        System.out.println("Take a shot!");

        String input = scanner.nextLine().toUpperCase(Locale.ROOT).trim();

        if (!input.matches("[A-Z][1-9][0-9]?")) {
            System.out.println("Error: Wrong input");
            return getShotCoordinates();
        }

        args[0] = input.charAt(0) - 65;
        args[1] = Integer.parseInt(input.substring(1)) - 1;

        return args;
    }

    @Override
    public void receiveShotStatus(ShootStatus status) {
        switch (status) {
            case DEAD:
                System.out.println("You sank a ship!");
                promptEnterKey();
                break;
            case HIT:
                System.out.println("You hit a ship!");
                promptEnterKey();
                break;
            case MISS:
                System.out.println("You missed!");
                promptEnterKey();
                break;
            case ENDGAME:
                System.out.println("You sank the last ship. You won. Congratulations!");
                break;
        }
    }

    public void promptEnterKey() {
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
