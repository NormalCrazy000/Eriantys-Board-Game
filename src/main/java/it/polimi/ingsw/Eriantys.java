package it.polimi.ingsw;

import it.polimi.ingsw.network.client.cli.CLI;
import it.polimi.ingsw.network.client.gui.GUI;
import it.polimi.ingsw.network.server.Server;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Hello world!
 *
 */
public class Eriantys
{
    public static void main( String[] args ){

        System.out.println("Hi! Welcome to Eriantys!\nWhat do you want to launch?");
        System.out.println("0. SERVER\n1. CLIENT (CLI INTERFACE)\n2. CLIENT (GUI INTERFACE)");
        System.out.println("\n> Type the number of the desired option!");
        System.out.print("> ");
        Scanner scanner = new Scanner(System.in);

        int input = 0;
        try {
            input = scanner.nextInt();
        } catch (InputMismatchException e) {
            System.err.println("Numeric format requested, application will now close...");
            System.exit(-1);
        }

        switch (input) {
            case 0 :
                Server.main(null);
                break;
            case 1 :
                new CLI();
                break;
            case 2 :
                System.out.println("You selected the GUI interface, have fun!\nStarting...");
                GUI.main(null);
                break;
            default :
                System.err.println("Invalid argument, please run the executable again.");
        }
    }
}
