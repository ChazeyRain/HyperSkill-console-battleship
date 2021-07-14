package battleship;

import battleship.logic.field.Field;
import battleship.logic.ships.ShipTypes;
import battleship.ui.CLI;

import java.io.IOException;
import java.util.Scanner;

import static battleship.logic.ships.ShipTypes.*;

public class Main {

    public static void main(String[] args) {
        //BEHOLD, MY OVER ENGINEERING GENIUS!

        //I know that there a LOT more simple ways to write logic for this game,
        // but I can create different amount of ship just rewriting just one row)
        //and another row just to change size of the field
        //and also I can create different UI's without fear to destroy everything

        CLI cli = new CLI();

        Scanner scanner = new Scanner(System.in);

        cli.receiveMessage("Player 1, place your ships on the game field");
        Field field1 = new Field(cli, cli);

        cli.receiveMessage("Press Enter and pass the move to another player");
        cli.promptEnterKey();

        cli.receiveMessage("Player 2, place your ships on the game field");
        Field field2 = new Field(cli, cli);

        cli.receiveMessage("Press Enter and pass the move to another player");
        cli.promptEnterKey();

        cli.receiveMessage("The game starts!");

        boolean firstPlayer = true;

        while (field1.getTotalHP() != 0 || field2.getTotalHP() != 0) {
            if (firstPlayer) {
                do {
                    firstPlayer = false;
                    field2.sendFieldToEnemy();
                    cli.receiveMessage("---------------------");
                    field1.printField();
                    cli.receiveMessage("Player 1, it's your turn:");
                } while (!field2.getShot());

            } else {
                do {
                    firstPlayer = true;
                    field1.sendFieldToEnemy();
                    cli.receiveMessage("---------------------");
                    field2.printField();
                    cli.receiveMessage("Player 2, it's your turn:");
                } while (!field1.getShot());
            }
        }

        cli.receiveMessage("You sank the last ship. You won. Congratulations!");
    }
}
