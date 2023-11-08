package it.polimi.ingsw.network.client.cli.TurnPhases;

import it.polimi.ingsw.network.client.cli.CLI;
import it.polimi.ingsw.network.messages.sentByClient.JoinOrHostMessage;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * This class handles the Phase where the player choose if he wants to create a new game or join one.
 */
public class JoinOrHostPhase extends Phase{


    @Override
    public void makeAction(CLI cli) {

        int playerChoice=0;

        Scanner scanner = new Scanner(System.in);


        do{

            System.out.println(" What do you want to do ? \n\t"+
                    "1. Join a game\n\t"+
                    "2. Host a new game\n\n");

            System.out.print("> ");

            try{
                playerChoice = scanner.nextInt();
            }catch(InputMismatchException e){
                System.out.println("Please enter an integer value");
                scanner.nextInt();
            }

        }while(playerChoice < 1 || playerChoice > 2);



        if(playerChoice == 1){
            cli.setGamePhase(new JoinGamePhase());
        }
        else {
            cli.setGamePhase(new HostGamePhase());
        }
        cli.setIsAckArrived(false);
        new Thread(cli).start();


    }

}
