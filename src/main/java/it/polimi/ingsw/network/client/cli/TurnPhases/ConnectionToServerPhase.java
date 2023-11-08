package it.polimi.ingsw.network.client.cli.TurnPhases;

import it.polimi.ingsw.network.client.ClientSocket;
import it.polimi.ingsw.network.client.cli.CLI;
import it.polimi.ingsw.utils.Constants;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;


/**
 * This class handles the Phase where the player set up a connection to the server
 */
public class ConnectionToServerPhase extends Phase{


    @Override
    public void makeAction(CLI cli) {

        cli.printTitle();

        Scanner scanner = new Scanner(System.in);

        System.out.println("Insert the server IP address");
        System.out.print("> ");
        String address = scanner.nextLine();

        System.out.println("Insert the server port");
        System.out.print("> ");
        int port = scanner.nextInt();

        try {
            //connect
            cli.setSocket(new Socket(address,port));
            cli.setClientSocket(new ClientSocket(cli,cli.getSocket()));

        } catch (IOException e) {

            cli.setClientSocket(null);
            System.out.println(Constants.ANSI_RED + "There was a problem with the server. Please check if the ip address and port number" +
                    "are correct and if the server is up and running " + Constants.ANSI_RESET);
        }

        if(!(cli.getClientSocket()==null)){
            cli.setGamePhase(new CreatePlayerPhase());
            new Thread(cli.getClientSocket()).start();
            new Thread(cli).start();
        }

    }
}
