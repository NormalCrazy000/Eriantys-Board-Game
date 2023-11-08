package it.polimi.ingsw.network.server;


import it.polimi.ingsw.model.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class represents the clients view in the server. It is listening for changes on the gameBoard and playerBoard
 */
public class VirtualView{

    private ServerClientConnection scc;
    private GameBoard gameBoard;
    private PlayerBoard personalPlayerBoard;
    private ArrayList<AssistantCard> playerAssistantDeck;
    private HashMap<Player,VirtualView> enemyPlayerViews;


    public VirtualView(ServerClientConnection scc, GameBoard gameBoard, PlayerBoard personalPlayerBoard, ArrayList<AssistantCard> playerAssistantDeck) {
        this.scc = scc;
        this.gameBoard = gameBoard;
        this.personalPlayerBoard = personalPlayerBoard;
        this.playerAssistantDeck= playerAssistantDeck;
    }


    public ServerClientConnection getScc() {
        return scc;
    }

    public GameBoard getGameBoard() {
        return gameBoard;
    }

    public PlayerBoard getPersonalPlayerBoard() {
        return personalPlayerBoard;
    }

    public HashMap<Player, VirtualView> getEnemyPlayerViews() {
        return enemyPlayerViews;
    }


    public void setScc(ServerClientConnection scc) {
        this.scc = scc;
    }

    public void setGameBoard(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
    }

    public void setPersonalPlayerBoard(PlayerBoard personalPlayerBoard) {
        this.personalPlayerBoard = personalPlayerBoard;
    }

    public void setEnemyPlayerViews(HashMap<Player, VirtualView> enemyPlayerViews) {
        this.enemyPlayerViews = enemyPlayerViews;
    }


    public void setPlayerAssistantDeck(ArrayList<AssistantCard> playerAssistantDeck) {
        this.playerAssistantDeck=playerAssistantDeck;
    }

    public ArrayList<AssistantCard> getPlayerAssistantDeck() {
        return playerAssistantDeck;
    }




}
