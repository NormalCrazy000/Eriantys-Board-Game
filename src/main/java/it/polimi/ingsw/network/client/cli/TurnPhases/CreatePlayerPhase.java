package it.polimi.ingsw.network.client.cli.TurnPhases;


import it.polimi.ingsw.network.client.cli.CLI;
import it.polimi.ingsw.network.messages.sentByClient.NicknameMessage;
import it.polimi.ingsw.utils.Constants;

import java.util.Scanner;


/**
 * This class handles the Phase where the client choose a new nickname for his player
 */

public class CreatePlayerPhase extends Phase{

    @Override
    public void makeAction(CLI cli) {

        Scanner input = new Scanner(System.in);

        String nickname = null;
        boolean confirmation = false;

        do {
            while (!confirmation) {
                do {
                    System.out.println("Insert your nickname: ");
                    System.out.print("> ");

                    nickname = input.nextLine();

                } while (nickname == null);

                System.out.println("You chose: " + nickname);
                System.out.println("Is it ok? [y/n] ");
                System.out.print("> ");
                if (input.nextLine().equalsIgnoreCase("y")) {
                    confirmation = true;
                } else {
                    nickname = null;
                }
            }

            NicknameMessage nicknameMessage = new NicknameMessage(nickname);
            cli.getClientSocket().send(nicknameMessage);
            confirmation=false;

            try {
                synchronized (this){
                    wait();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }while(!cli.isAckArrived());


        //if client received ack , register nickname on the client
        if(cli.isAckArrived()){

            cli.setNickname(nickname);
            System.out.println(Constants.ANSI_GREEN +"\n ... player created successfully ...\n"+Constants.ANSI_RESET);
            cli.setGamePhase(new JoinOrHostPhase());
            cli.setIsAckArrived(false);
            new Thread(cli).start();
        }

    }
}
