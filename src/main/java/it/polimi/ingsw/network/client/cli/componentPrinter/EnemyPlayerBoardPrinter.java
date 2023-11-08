package it.polimi.ingsw.network.client.cli.componentPrinter;

import it.polimi.ingsw.serializableModel.SerializablePlayerBoard;

import java.util.HashMap;

/**
 * Utility Class for printing all the Enemy player board
 */
public class EnemyPlayerBoardPrinter {
    /**
     * This method print enemy playerBoard
     * @param enemyBoards
     */
    public void print(HashMap<String, SerializablePlayerBoard> enemyBoards){
        PlayerBoardPrinter printer = new PlayerBoardPrinter();
        enemyBoards.forEach((K,V) -> printer.print(V));
    }
}
