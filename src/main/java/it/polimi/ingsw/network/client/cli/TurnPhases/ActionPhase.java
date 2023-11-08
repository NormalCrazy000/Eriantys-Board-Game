package it.polimi.ingsw.network.client.cli.TurnPhases;


import it.polimi.ingsw.network.client.cli.CLI;
import it.polimi.ingsw.utils.Constants;

import java.util.Scanner;


/**
 * This class handles the Phase where the player is able to start his action phase or checking the views ( his player board, enemy player boards and game-board)
 */
public class ActionPhase extends Phase{
    @Override
    public void makeAction(CLI cli) {

        System.out.println("action for "+cli.getClientSocket().getView().getActivePlayer());
        Scanner scanner = new Scanner(System.in);

        int choice;


        if(cli.getClientSocket().getView().getActivePlayer().equals(cli.getNickname())) {

            System.out.println("It's your turn\n");
            System.out.println("[1] Start ActionPhase");
            System.out.println("[2] Show my PlayerBoard ");
            System.out.println("[3] Show GameBoard ");
            System.out.println("[4] Show enemies PlayerBoards ");

            System.out.print("> ");
            choice = scanner.nextInt();

            switch(choice){
                case 1:
                    cli.setGamePhase(new MoveStudentsPhase());
                    new Thread(cli).start();
                    break;
                case 2:
                    cli.printPlayerBoard();
                    new Thread(cli).start();
                    break;
                case 3:
                    cli.printGameBoard();
                    new Thread(cli).start();
                    break;
                case 4:
                    cli.printEnemyPlayerBoard();
                    new Thread(cli).start();
                    break;

                default:
                    System.out.println("This action doesn't exist");
                    new Thread(cli).start();
                    break;
            }

        }else {
            System.out.println(Constants.ANSI_YELLOW + "Waiting for other player to finish their turn!" +Constants.ANSI_RESET);
            //new Thread(cli).start();
        }

    }
}
