package it.polimi.ingsw.network.client.cli.TurnPhases;

import it.polimi.ingsw.model.GameMode;
import it.polimi.ingsw.model.PawnType;
import it.polimi.ingsw.network.client.cli.CLI;
import it.polimi.ingsw.network.client.cli.componentPrinter.GameBoardPrinter;
import it.polimi.ingsw.network.client.cli.componentPrinter.PlayerBoardPrinter;
import it.polimi.ingsw.network.messages.sentByClient.StudentToDiningRoomMessage;
import it.polimi.ingsw.network.messages.sentByClient.StudentToIslandMessage;
import it.polimi.ingsw.utils.Constants;

import java.util.HashMap;
import java.util.Scanner;


/**
 * This class handles the Phase where the player specify the student to move in dining room or to islands
 */
public class MoveStudentsPhase extends Phase {


    int numOfStudentToMove;
    int numOfStudToDiningRoom;
    int numOfStudToIsland;
    int red;
    int blue;
    int pink;
    int yellow;
    int green;
    HashMap<PawnType, Integer> studToDiningRoom;
    HashMap<PawnType, Integer> studToIsland;
    int islandIndex;
    int action;


    @Override
    public void makeAction(CLI cli) {

        numOfStudentToMove = 3;
        numOfStudToDiningRoom = 0;
        numOfStudToIsland = 0;
        studToDiningRoom = new HashMap<>();
        studToIsland = new HashMap<>();
        red = 0;
        blue = 0;
        pink = 0;
        yellow = 0;
        green = 0;
        action = 1;

        Scanner scanner = new Scanner(System.in);


        PlayerBoardPrinter printer = new PlayerBoardPrinter();
        printer.printEntrance(cli.getClientSocket().getView().getMyPlayerBoard());

        if (cli.getClientSocket().getView().getGameBoard().getGameMode() == GameMode.EXPERT && !cli.isCharacterCardPlayed()) {
            action = 0;
            do {
                System.out.println(Constants.ANSI_GREEN + "ACTION FASE: You can choose to play a character card of to start to move students" + Constants.ANSI_RESET);

                System.out.println("[1] move students");
                System.out.println("[2] use character card");
                System.out.print("> ");
                action = scanner.nextInt();

                if (action < 1 || action > 2) {
                    System.out.println(Constants.ANSI_RED + "Invalid input" + Constants.ANSI_RESET);
                }

            } while (action < 1 || action > 2);

        }

        if (action == 2) {
            cli.setGamePhase(new SelectCharacterCardPhase());
            new Thread(cli).start();
        } else {


            System.out.println("=================================== MOVE STUDENTS ===========================================\n\n\"" + Constants.ANSI_GREEN +
                    "MOVE STUDENTS: You can move 3 Students from your Entrance to either your Dining Room or to an Island\n" + Constants.ANSI_RESET);

            int x ;

            if(cli.getClientSocket().getView().getGameBoard().getClouds().length == 3){
                x=4;
            }else{
                x=3;
            }


            for (int i = 0; i < x; i++) {

                int choice;

                do {


                    System.out.println("\nWhere do you want to move the student?");
                    System.out.println("[1] DiningRoom");
                    System.out.println("[2] Island");
                    System.out.print(">");
                    choice = scanner.nextInt();


                    if (choice < 1 || choice > 2) {
                        System.out.println(Constants.ANSI_RED + "Invalid input" + Constants.ANSI_RESET);
                    }

                } while (choice < 1 || choice > 2);


                if (choice == 1) {
                    // DINING ROOM

                    PawnType colorChosen = null;

                    do{
                        for (PawnType color : PawnType.values()) {
                            if (cli.getClientSocket().getView().getMyPlayerBoard().getEntrance().get(color) > 0) {
                                String confirm;

                                do {

                                    System.out.println("Do you want to move a " + color.name() + " student? [y/n]");
                                    System.out.print(">");
                                    confirm = scanner.next();

                                    if (!confirm.equals("y") && !confirm.equals("n")) {
                                        System.out.println(Constants.ANSI_RED + "Invalid input" + Constants.ANSI_RESET);
                                    }
                                } while (!confirm.equals("y") && !confirm.equals("n"));

                                if (confirm.equals("y")) {
                                    colorChosen = color;
                                    break;
                                }
                            }
                        }

                        if(cli.getClientSocket().getView().getMyPlayerBoard().getDiningRoom().get(colorChosen)+1 > 10 ){
                            System.out.println(Constants.ANSI_RED+"Invalid color choice. Your dining room is full"+Constants.ANSI_RESET);
                        }

                    }while(cli.getClientSocket().getView().getMyPlayerBoard().getDiningRoom().get(colorChosen)+1 > 10 );




                    HashMap<PawnType, Integer> studToDiningRoom = new HashMap<>();
                    studToDiningRoom.put(colorChosen, 1);


                    cli.getClientSocket().send(new StudentToDiningRoomMessage("Move to DiningRoom", studToDiningRoom));

                    try {
                        synchronized (this) {
                            wait();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }


                } else {
                    //ISLAND
                    PawnType colorChosen = null;

                    for (PawnType color : PawnType.values()) {
                        if (cli.getClientSocket().getView().getMyPlayerBoard().getEntrance().get(color) > 0) {
                            String confirm;
                            do {
                                System.out.println("Do you want to move a " + color.name() + " student? [y/n]");
                                System.out.print(">");
                                confirm = scanner.next();

                                if (!confirm.equals("y") && !confirm.equals("n")) {
                                    System.out.println(Constants.ANSI_RED + "Invalid input" + Constants.ANSI_RESET);
                                }
                            } while (!confirm.equals("y") && !confirm.equals("n"));

                            if (confirm.equals("y")) {
                                colorChosen = color;
                                break;
                            }
                        }
                    }

                    HashMap<PawnType, Integer> studToIsland = new HashMap<>();
                    studToIsland.put(colorChosen, 1);

                    do {
                        System.out.println("Select an island: ");
                        GameBoardPrinter gameBoardPrinter = new GameBoardPrinter();
                        gameBoardPrinter.printIslands(cli.getClientSocket().getView().getGameBoard());
                        System.out.print(">");

                        islandIndex = scanner.nextInt();

                        //if invalid number typed
                        if (islandIndex < 0 || islandIndex > cli.getClientSocket().getView().getGameBoard().getRegions().size()) {
                            System.out.println(Constants.ANSI_RED + "Invalid number" + Constants.ANSI_RESET);
                        }

                    } while (islandIndex < 0 || islandIndex > cli.getClientSocket().getView().getGameBoard().getRegions().size());

                    cli.getClientSocket().send(new StudentToIslandMessage("move to island", studToIsland, islandIndex - 1));

                    try {
                        synchronized (this) {
                            wait();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }


            System.out.println("DONE");
            cli.setMoveStudentsDone(true);

            cli.setGamePhase(new MoveMotherNaturePhase());
            new Thread(cli).start();

        }
    }
}
