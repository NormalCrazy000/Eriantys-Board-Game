package it.polimi.ingsw.serializableModel;

import it.polimi.ingsw.model.*;

import java.io.Serializable;
import java.util.ArrayList;


/**
 * Serializable class that contains the information needed by the view.
 * Light copy of the {@link Game}.
 */
public class SerializableGame implements Serializable {

    private  SerializableGameBoard gameBoard;
    private ArrayList<SerializablePlayer> players;
    private int indexCurrentPlayer;
    private final GameMode gameMode;
    private CharacterDeck characterDeck;


    public SerializableGame(Game game) {

        this.gameBoard = new SerializableGameBoard(game.getGameBoard());
        this.indexCurrentPlayer = game.getIndexCurrentPlayer();
        this.gameMode = game.getGameMode();
        this.characterDeck = game.getCharacterDeck();

        for(Player x : game.getPlayers() ){
            this.players.add(new SerializablePlayer(x));
        }
    }


    public SerializableGameBoard getGameBoard() {
        return gameBoard;
    }

    public ArrayList<SerializablePlayer> getPlayers() {
        return players;
    }

    public int getIndexCurrentPlayer() {
        return indexCurrentPlayer;
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public CharacterDeck getCharacterDeck() {
        return characterDeck;
    }

    public void setGameBoard(SerializableGameBoard gameBoard) {
        this.gameBoard = gameBoard;
    }

    public void setPlayers(ArrayList<SerializablePlayer> players) {
        this.players = players;
    }

    public void setIndexCurrentPlayer(int indexCurrentPlayer) {
        this.indexCurrentPlayer = indexCurrentPlayer;
    }

    public void setCharacterDeck(CharacterDeck characterDeck) {
        this.characterDeck = characterDeck;
    }
}
