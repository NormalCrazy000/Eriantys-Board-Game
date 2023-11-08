package it.polimi.ingsw.network.client;

import it.polimi.ingsw.model.AssistantCard;
import it.polimi.ingsw.serializableModel.SerializableGameBoard;
import it.polimi.ingsw.serializableModel.SerializablePlayerBoard;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * This class contains all the information of the game for a player
 */
public class View implements Serializable {

    private String activePlayer;
    private final String nickname;
    private SerializablePlayerBoard myPlayerBoard;
    private SerializableGameBoard gameBoard;
    private ArrayList<AssistantCard> assistantCards;
    private HashMap<String, SerializablePlayerBoard> enemyPlayerBoards;


    /**
     * This constructor is used to setu up Game's view
     *
     * @param activePlayer      type {@link String}: active player
     * @param nickname          type {@link String}: nickname player
     * @param assistantCards    type {@link ArrayList<AssistantCard>}: Player's assistantCards
     * @param myPlayerBoard     type {@link SerializablePlayerBoard}: SerializablePlayerBoard object
     * @param gameBoard         type {@link SerializableGameBoard}: SerializableGameBoard object
     * @param enemyPlayerBoards type Map with key {@link String} and value {@link SerializablePlayerBoard}: Enemy's playerBoard
     */
    public View(String activePlayer, String nickname, ArrayList<AssistantCard> assistantCards, SerializablePlayerBoard myPlayerBoard, SerializableGameBoard gameBoard, HashMap<String, SerializablePlayerBoard> enemyPlayerBoards) {
        this.activePlayer = activePlayer;
        this.nickname = nickname;
        this.assistantCards = assistantCards;
        this.myPlayerBoard = myPlayerBoard;
        this.gameBoard = gameBoard;
        this.enemyPlayerBoards = enemyPlayerBoards;
    }

    /**
     * Thsi method returns active player
     *
     * @return type {@link String}: active player object
     */
    public String getActivePlayer() {
        return activePlayer;
    }

    /**
     * This method returns player's nickname
     *
     * @return
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * This method returns player's serializable playerBoard
     *
     * @return type {@link SerializablePlayerBoard}: player's serializable playerBoard
     */
    public SerializablePlayerBoard getMyPlayerBoard() {
        return myPlayerBoard;
    }

    /**
     * This method returns Game's serializable gameBoard
     *
     * @return type {@link SerializableGameBoard}: Game's serializable gameBoard
     */
    public SerializableGameBoard getGameBoard() {
        return gameBoard;
    }

    /**
     * This method returns enemy's serializable playerBoard
     *
     * @return type Map with key {@link String} and value {@link SerializablePlayerBoard}: Enemy's serializable playerBoard
     */
    public HashMap<String, SerializablePlayerBoard> getEnemyPlayerBoards() {
        return enemyPlayerBoards;
    }

    /**
     * This method is used to update enemy's serializable playerBoard
     *
     * @param playerBoard type {@link SerializablePlayerBoard}: enemy's serializableplayerBoard
     * @param nickname    type {@link String}: enemy's nickname
     */
    public void updateEnemyPlayerBoard(SerializablePlayerBoard playerBoard, String nickname) {

        enemyPlayerBoards.put(nickname, playerBoard);

    }

    /**
     * Thsi method is used to set up active Player
     *
     * @param activePlayer type {@link String}: active player
     */
    public void setActivePlayer(String activePlayer) {
        this.activePlayer = activePlayer;
    }

    /**
     * This method is used to set up player's serializable playerBoard
     *
     * @param myPlayerBoard type {@link SerializablePlayerBoard}: player's serializable playerBoard
     */
    public void setMyPlayerBoard(SerializablePlayerBoard myPlayerBoard) {
        this.myPlayerBoard = myPlayerBoard;
    }

    /**
     * This method is used to set up game's serializable gameBoard
     *
     * @param gameBoard type {@link SerializableGameBoard}: game's serializable gameBoard
     */
    public void setGameBoard(SerializableGameBoard gameBoard) {
        this.gameBoard = gameBoard;
    }

    /**
     * Thsi method returns player's assistantCards
     *
     * @return type {@link ArrayList<AssistantCard>}: Player's assistantCards
     */
    public ArrayList<AssistantCard> getAssistantCards() {
        return assistantCards;
    }
}
