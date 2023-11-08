package it.polimi.ingsw.network.client.cli.componentPrinter;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.serializableModel.SerializableGameBoard;
import it.polimi.ingsw.utils.Constants;


/**
 * Utility Class for printing the game board player board
 */
public class GameBoardPrinter {

    /**
     * This method prints the entire gameboard
     *
     * @param gameBoard (of type {@link SerializableGameBoard}) the actual gameBoard state
     */
    public void printGameBoard(SerializableGameBoard gameBoard) {

        System.out.println("Islands");
        int i = 1;
        for (IslandRegion x : gameBoard.getRegions()) {
            printRegion(x, i);
            i++;
        }

        System.out.println("Clouds");
        i = 1;
        for (CloudTile x : gameBoard.getClouds()) {
            printCloud(x, i);
            i++;
        }

        if (gameBoard.getGameMode() == GameMode.EXPERT) {


            for (CharacterCard c : gameBoard.getCharacterDeck().getDeckList()) {
                printCharacterCards(c);
            }
        }

    }


    /**
     * This method prints only the islands section of the gameboard
     *
     * @param gameBoard (of type {@link SerializableGameBoard}) the actual gameBoard state
     */
    public void printIslands(SerializableGameBoard gameBoard) {
        int i = 1;
        for (IslandRegion x : gameBoard.getRegions()) {
            printRegion(x, i);
            i++;
        }
    }


    /**
     * This method prints only the clouds section of the gameboard
     *
     * @param gameBoard (of type {@link SerializableGameBoard}) the actual gameBoard state
     */
    public void printClouds(SerializableGameBoard gameBoard) {
        int i = 1;
        for (CloudTile x : gameBoard.getClouds()) {
            printCloud(x, i);
            i++;
        }
    }


    /**
     * This methods print a specified {@link IslandRegion}
     *
     * @param region  the region to print
     * @param counter the index to print
     */
    public void printRegion(IslandRegion region, int counter) {

        System.out.println("╔═Island " + counter + "═╗");
        System.out.println(Constants.ANSI_RED + "RED: " + region.getAllStudentsOrder().get(PawnType.RED) + Constants.ANSI_RESET);
        System.out.println(Constants.ANSI_GREEN + "GREEN: " + region.getAllStudentsOrder().get(PawnType.GREEN) + Constants.ANSI_RESET);
        System.out.println(Constants.ANSI_YELLOW + "YELLOW: " + region.getAllStudentsOrder().get(PawnType.YELLOW) + Constants.ANSI_RESET);
        System.out.println(Constants.ANSI_PURPLE + "PINK: " + region.getAllStudentsOrder().get(PawnType.PINK) + Constants.ANSI_RESET);
        System.out.println(Constants.ANSI_BLUE + "BLUE: " + region.getAllStudentsOrder().get(PawnType.BLUE) + Constants.ANSI_RESET + "\n");

        System.out.println((region.isTower()) ? String.valueOf(region.getColorTower()) : "-");
        System.out.println((region.isMother()) ? "HAS MOTHER" : "-");
        System.out.println("╚═══════════╝");

    }


    /**
     * This method print a specified {@link CloudTile}
     *
     * @param cloudTile the cloud tile to print
     * @param counter   the index to print
     */
    public void printCloud(CloudTile cloudTile, int counter) {

        System.out.println(Constants.ANSI_BLUE + "╔═Cloud " + counter + "═╗" + Constants.ANSI_RESET);
        System.out.println(Constants.ANSI_RED + "RED: " + cloudTile.getAllStudents().get(PawnType.RED) + Constants.ANSI_RESET);
        System.out.println(Constants.ANSI_GREEN + "GREEN: " + cloudTile.getAllStudents().get(PawnType.GREEN) + Constants.ANSI_RESET);
        System.out.println(Constants.ANSI_YELLOW + "YELLOW: " + cloudTile.getAllStudents().get(PawnType.YELLOW) + Constants.ANSI_RESET);
        System.out.println(Constants.ANSI_PURPLE + "PINK: " + cloudTile.getAllStudents().get(PawnType.PINK) + Constants.ANSI_RESET);
        System.out.println(Constants.ANSI_BLUE + "BLUE: " + cloudTile.getAllStudents().get(PawnType.BLUE) + Constants.ANSI_RESET);
        System.out.println(Constants.ANSI_BLUE + "╚══════════╝" + Constants.ANSI_RESET);

    }


    /**
     * this method print character card
     *
     * @param c type {@link CharacterCard}: character card to print
     */
    public void printCharacterCards(CharacterCard c) {
        System.out.println("═══════════════Character card id: " + c.getID() + "══════════════════");
        System.out.println("- cost : " + c.getCost());
        System.out.println("- description : " + c.getEffectDescription());
        System.out.println("════════════════════════════════════════════════════════════");
    }

    /**
     * this method print character cards
     *
     * @param gameBoard type {@link SerializableGameBoard}: SerializableGameBoard object
     */
    public void printCard(SerializableGameBoard gameBoard) {
        for (int i = 0; i < gameBoard.getCharacterDeck().getDeckList().length; i++) {
            System.out.println("[" + (i + 1) + "]\n\t" +
                    "ID:" + gameBoard.getCharacterDeck().getDeckList()[i].getID() + " \n\t" +
                    "Cost:" + gameBoard.getCharacterDeck().getDeckList()[i].getCost() + " \n\t" +
                    "Effect: " + gameBoard.getCharacterDeck().getDeckList()[i].getEffectDescription() + "\n\t");
        }
    }
}
