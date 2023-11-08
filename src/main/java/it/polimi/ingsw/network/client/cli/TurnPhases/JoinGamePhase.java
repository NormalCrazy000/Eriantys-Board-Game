package it.polimi.ingsw.network.client.cli.TurnPhases;

import it.polimi.ingsw.model.GameMode;
import it.polimi.ingsw.network.client.cli.CLI;
import it.polimi.ingsw.network.messages.sentByClient.JoinGameMessage;
import java.util.Scanner;


/**
 * This class handles the Phase where the player specify the game infos to join
 */
public class JoinGamePhase extends Phase{
    @Override
    public void makeAction(CLI cli) {


        int numPlayer;
        int gameMode_selected;
        GameMode gameMode;


        Scanner scanner = new Scanner(System.in);

        do {
            System.out.println(" What type of game you want to join ?");
            System.out.println(" Max num of player [2 or 3]: ");
            System.out.print("> ");
            numPlayer = scanner.nextInt();

        }while (numPlayer < 2 || numPlayer > 3);


        do {
            System.out.println(" game mode [1 for EASY / 2 for EXPERT]: ");
            System.out.print("> ");
            gameMode_selected= scanner.nextInt();

        }while(gameMode_selected < 1 || gameMode_selected > 2);

        gameMode = (gameMode_selected == 1) ? GameMode.EASY : GameMode.EXPERT;



        cli.getClientSocket().send(new JoinGameMessage("Request to join Game",cli.getNickname(),numPlayer,gameMode));

        try{
            synchronized (this){
                wait();
            }
        }catch(InterruptedException e){
                e.printStackTrace();
        }


        cli.setGamePhase(new LobbyPhase());
        cli.setIsAckArrived(false);
        new Thread(cli).start();

    }
}
