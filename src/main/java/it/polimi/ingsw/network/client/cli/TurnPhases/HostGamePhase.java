package it.polimi.ingsw.network.client.cli.TurnPhases;

import it.polimi.ingsw.model.GameMode;
import it.polimi.ingsw.network.client.cli.CLI;
import it.polimi.ingsw.network.messages.sentByClient.NewGameMessage;
import it.polimi.ingsw.utils.Constants;

import java.util.InputMismatchException;
import java.util.Scanner;


/**
 * This class handles the Phase where the player specify the game infos to create
 */
public class HostGamePhase extends Phase{


    @Override
    public void makeAction(CLI cli) {

        int numOfPlayer = 0;
        int gameMode_input = 0;
        GameMode gameMode;
        Scanner scanner = new Scanner(System.in);


        do{
            System.out.println("You want to create a new game! Please provide the number of player and the game mode:\n\t");

            do{
                System.out.println("n° of player [2 or 3]:");
                System.out.print("> ");
                try{
                    numOfPlayer = scanner.nextInt();
                }catch (InputMismatchException e){
                    System.out.println("Invalid input try again");
                }

            }while(numOfPlayer < 2 || numOfPlayer > 3);


            do{

                System.out.println("game mode [1 for EASY][2 for EXPERT]:");
                System.out.print("> ");

                try{
                    gameMode_input = scanner.nextInt();
                }catch (InputMismatchException e){
                    System.out.println("Invalid input try again");
                }

            }while(gameMode_input <1 || gameMode_input > 2 );


            gameMode = (gameMode_input==1) ? GameMode.EASY : GameMode.EXPERT;

            NewGameMessage newGameMessage = new NewGameMessage("NewGame",cli.getNickname(),numOfPlayer,gameMode);
            cli.getClientSocket().send(newGameMessage);

            try {
                synchronized (this){
                    wait();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }while(!cli.isAckArrived());

        if (cli.isAckArrived()){
            System.out.println(Constants.ANSI_GREEN +"\n... game created ...\n"+Constants.ANSI_RESET);
            cli.setGamePhase(new LobbyPhase());
            cli.setIsAckArrived(false);
            new Thread(cli).start();
        }


    }
}
