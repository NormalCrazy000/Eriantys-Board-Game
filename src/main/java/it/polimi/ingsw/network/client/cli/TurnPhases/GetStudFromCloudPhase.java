package it.polimi.ingsw.network.client.cli.TurnPhases;

import it.polimi.ingsw.model.GameMode;
import it.polimi.ingsw.network.client.cli.CLI;
import it.polimi.ingsw.network.client.cli.componentPrinter.GameBoardPrinter;
import it.polimi.ingsw.network.client.cli.componentPrinter.PlayerBoardPrinter;
import it.polimi.ingsw.network.messages.sentByClient.EndTurnMessage;
import it.polimi.ingsw.network.messages.sentByClient.GetStudentsFromCloudMessage;
import it.polimi.ingsw.utils.Constants;
import java.util.Scanner;


/**
 * This class handles the Phase where the player choose a cloud from which get all the students
 */
public class GetStudFromCloudPhase extends Phase{

    @Override
    public void makeAction(CLI cli) {

        int cloudIndex;
        int action =1;
        Scanner scanner = new Scanner(System.in);



        if(cli.getClientSocket().getView().getGameBoard().getGameMode() == GameMode.EXPERT && !cli.isCharacterCardPlayed()) {
            do {
                System.out.println(Constants.ANSI_GREEN+"ACTION FASE: You can choose to play a character card of to start to move students" +Constants.ANSI_RESET);

                System.out.println("[1] get students from cloud");
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


            System.out.println(Constants.ANSI_GREEN+"\nACTION GET STUDENTS FROM A CLOUD\n"+Constants.ANSI_RESET);


            do {
                System.out.println("Choose the cloud from which you want to take the 3 students");

                GameBoardPrinter boardPrinter = new GameBoardPrinter();
                boardPrinter.printClouds(cli.getClientSocket().getView().getGameBoard());

                System.out.print("> ");
                cloudIndex = scanner.nextInt() -1;

                // check player input
                if(cloudIndex<0 || cloudIndex > cli.getClientSocket().getView().getGameBoard().getClouds().length-1){
                    System.out.println(Constants.ANSI_RED+"Invalid number"+Constants.ANSI_RESET);
                } else if(cli.getClientSocket().getView().getGameBoard().getClouds()[cloudIndex].isEmpty()){
                    // check if the cloud has been already chosen by the another player
                    System.out.println(Constants.ANSI_RED+"You cannot choose an empty cloud"+Constants.ANSI_RESET);
                }


            }while (cloudIndex<0 || cloudIndex > cli.getClientSocket().getView().getGameBoard().getClouds().length-1 ||
                    cli.getClientSocket().getView().getGameBoard().getClouds()[cloudIndex].isEmpty() );


            //sending message
            cli.getClientSocket().send(new GetStudentsFromCloudMessage("get stud from cloud", cloudIndex));

            try {
                synchronized (this){
                    wait();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // For user-side readability print entrance and cloud
            PlayerBoardPrinter printer = new PlayerBoardPrinter();
            printer.printEntrance(cli.getClientSocket().getView().getMyPlayerBoard());
            GameBoardPrinter boardPrinter = new GameBoardPrinter();
            boardPrinter.printCloud(cli.getClientSocket().getView().getGameBoard().getClouds()[cloudIndex],cloudIndex+1 );

            //The player has done his action
            cli.setGetStudFromCloudDone(true);
            cli.setActionDone(true);
            cli.setCharacterCardPlayed(false);

            //requesting server for new player
            cli.getClientSocket().send(new EndTurnMessage("end turn"));

            // put this player in waiting phase
            cli.setGamePhase(new OtherPlayersTurnPhase());
            new Thread(cli).start();



        }
    }
}
