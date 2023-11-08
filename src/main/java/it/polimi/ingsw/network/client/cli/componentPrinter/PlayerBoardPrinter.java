package it.polimi.ingsw.network.client.cli.componentPrinter;

import it.polimi.ingsw.model.PawnType;
import it.polimi.ingsw.serializableModel.SerializablePlayerBoard;
import it.polimi.ingsw.utils.Constants;

/**
 * Utility class for printing the player board
 */
public class PlayerBoardPrinter {


    /**
     * This method prints the entire Player board
     *
     * @param playerBoard
     */
    public void print(SerializablePlayerBoard playerBoard) {
        System.out.println("PlayerBoard");

        System.out.println("════════════════════════");
        System.out.println("-- Entrance --");
        System.out.println(Constants.ANSI_RED + "RED: " + playerBoard.getEntrance().get(PawnType.RED) + Constants.ANSI_RESET);
        System.out.println(Constants.ANSI_GREEN + "GREEN: " + playerBoard.getEntrance().get(PawnType.GREEN) + Constants.ANSI_RESET);
        System.out.println(Constants.ANSI_YELLOW + "YELLOW: " + playerBoard.getEntrance().get(PawnType.YELLOW) + Constants.ANSI_RESET);
        System.out.println(Constants.ANSI_CYAN + "PINK: " + playerBoard.getEntrance().get(PawnType.PINK) + Constants.ANSI_RESET);
        System.out.println(Constants.ANSI_BLUE + "BLUE: " + playerBoard.getEntrance().get(PawnType.BLUE) + Constants.ANSI_RESET);

        System.out.println("-- Dining Room -- ");
        System.out.println(Constants.ANSI_RED + "RED: " + playerBoard.getDiningRoom().get(PawnType.RED) + Constants.ANSI_RESET);
        System.out.println(Constants.ANSI_GREEN + "GREEN: " + playerBoard.getDiningRoom().get(PawnType.GREEN) + Constants.ANSI_RESET);
        System.out.println(Constants.ANSI_YELLOW + "YELLOW: " + playerBoard.getDiningRoom().get(PawnType.YELLOW) + Constants.ANSI_RESET);
        System.out.println(Constants.ANSI_CYAN + "PINK: " + playerBoard.getDiningRoom().get(PawnType.PINK) + Constants.ANSI_RESET);
        System.out.println(Constants.ANSI_BLUE + "BLUE: " + playerBoard.getDiningRoom().get(PawnType.BLUE) + Constants.ANSI_RESET);

        System.out.println("-- Professor -- ");
        System.out.println(Constants.ANSI_RED + "RED: " + ((playerBoard.getProfessors().get(PawnType.RED)) ? "X" : "-") + Constants.ANSI_RESET);
        System.out.println(Constants.ANSI_GREEN + "GREEN: " + ((playerBoard.getProfessors().get(PawnType.GREEN)) ? "X" : "-") + Constants.ANSI_RESET);
        System.out.println(Constants.ANSI_YELLOW + "YELLOW: " + ((playerBoard.getProfessors().get(PawnType.YELLOW)) ? "X" : "-") + Constants.ANSI_RESET);
        System.out.println(Constants.ANSI_CYAN + "PINK: " + ((playerBoard.getProfessors().get(PawnType.PINK)) ? "X" : "-") + Constants.ANSI_RESET);
        System.out.println(Constants.ANSI_BLUE + "BLUE: " + ((playerBoard.getProfessors().get(PawnType.BLUE)) ? "X" : "-") + Constants.ANSI_RESET);

        System.out.println("-- Towers --\n" + playerBoard.getTowers() + " of color " + playerBoard.getTowerColor().name());
        System.out.println("-- Coins --\n" + playerBoard.getCoins());
        System.out.println("════════════════════════");

        System.out.println("Assistant card played: value[" + playerBoard.getAssistantCard().getValue() + "], mother nature movement[" + playerBoard.getAssistantCard().getMovementMotherNature() + "]\n\n");
    }


    /**
     * This method prints only the entrance section of the player board
     *
     * @param playerBoard
     */
    public void printEntrance(SerializablePlayerBoard playerBoard) {

        System.out.println("════════════Entrance════════════");
        System.out.println(Constants.ANSI_RED + "RED: " + playerBoard.getEntrance().get(PawnType.RED) + Constants.ANSI_RESET);
        System.out.println(Constants.ANSI_GREEN + "GREEN: " + playerBoard.getEntrance().get(PawnType.GREEN) + Constants.ANSI_RESET);
        System.out.println(Constants.ANSI_YELLOW + "YELLOW: " + playerBoard.getEntrance().get(PawnType.YELLOW) + Constants.ANSI_RESET);
        System.out.println(Constants.ANSI_CYAN + "PINK: " + playerBoard.getEntrance().get(PawnType.PINK) + Constants.ANSI_RESET);
        System.out.println(Constants.ANSI_BLUE + "BLUE: " + playerBoard.getEntrance().get(PawnType.BLUE) + Constants.ANSI_RESET);
        System.out.println("════════════════════════════════");
    }

    /**
     * This method prints only the dining room section of the player board
     *
     * @param playerBoard
     */
    public void printDiningRoom(SerializablePlayerBoard playerBoard) {

        System.out.println("════════════ Dining Room ════════════");
        System.out.println(Constants.ANSI_RED + "RED: " + playerBoard.getDiningRoom().get(PawnType.RED) + Constants.ANSI_RESET);
        System.out.println(Constants.ANSI_GREEN + "GREEN: " + playerBoard.getDiningRoom().get(PawnType.GREEN) + Constants.ANSI_RESET);
        System.out.println(Constants.ANSI_YELLOW + "YELLOW: " + playerBoard.getDiningRoom().get(PawnType.YELLOW) + Constants.ANSI_RESET);
        System.out.println(Constants.ANSI_CYAN + "PINK: " + playerBoard.getDiningRoom().get(PawnType.PINK) + Constants.ANSI_RESET);
        System.out.println(Constants.ANSI_BLUE + "BLUE: " + playerBoard.getDiningRoom().get(PawnType.BLUE) + Constants.ANSI_RESET);
        System.out.println("════════════════════════════════");
    }


}
