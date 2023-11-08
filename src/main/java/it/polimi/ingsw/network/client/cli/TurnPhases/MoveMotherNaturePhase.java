package it.polimi.ingsw.network.client.cli.TurnPhases;

import it.polimi.ingsw.model.GameMode;
import it.polimi.ingsw.network.client.cli.CLI;
import it.polimi.ingsw.network.client.cli.componentPrinter.GameBoardPrinter;
import it.polimi.ingsw.network.messages.sentByClient.AmountOfMotherNatureMovementMessage;
import it.polimi.ingsw.utils.Constants;

import java.util.Scanner;

/**
 * This class handles the Phase where the player specify the amount of movement for mother nature
 */
public class MoveMotherNaturePhase extends Phase{


    @Override
    public void makeAction(CLI cli) {

        int movement;
        int action = 1;
        Scanner scanner = new Scanner(System.in);



        if(cli.getClientSocket().getView().getGameBoard().getGameMode() == GameMode.EXPERT && !cli.isCharacterCardPlayed()) {
            do {
                System.out.println(Constants.ANSI_GREEN+"ACTION FASE: You can choose to play a character card of to start to move mother nature" +Constants.ANSI_RESET);

                System.out.println("[1] move mother nature");
                System.out.println("[2] use character card");
                System.out.print("> ");
                action = scanner.nextInt();

                if(action<1 || action > 2){
                    System.out.println(Constants.ANSI_RED+"Invalid input"+Constants.ANSI_RESET);
                }

            }while (action<1 || action > 2);

        }

        if (action == 2){
            cli.setGamePhase(new SelectCharacterCardPhase());
            new Thread(cli).start();
        }else{



            System.out.println(Constants.ANSI_GREEN+"\n================================ ACTION MOVE MOTHER NATURE =================================\n"+Constants.ANSI_RESET);

//            System.out.println("><"+cli.getMotherMovement());
//            System.out.println("><"+cli.getAdditionalMovement());

            do {
                System.out.println("You can move your mother nature.How many steps must she takes [between 1 and "+(cli.getMotherMovement()+cli.getAdditionalMovement())+"]");
                System.out.println("> ");

                movement = scanner.nextInt();

            }while (movement<1 || movement> cli.getMotherMovement()+cli.getAdditionalMovement());

            cli.getClientSocket().send(new AmountOfMotherNatureMovementMessage("mother movement",movement));

            try {
                synchronized (this){
                    wait();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            GameBoardPrinter printer = new GameBoardPrinter();
            printer.printIslands(cli.getClientSocket().getView().getGameBoard());

            cli.setMoveMotherNature(true);
            cli.setAdditionalMovement(0);

            if(cli.getClientSocket().getView().getGameBoard().getRegions().size()>3) {

                cli.setGamePhase(new GetStudFromCloudPhase());
                new Thread(cli).start();
            }
        }

    }
}
