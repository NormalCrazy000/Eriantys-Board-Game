package it.polimi.ingsw.serializableModel;

import it.polimi.ingsw.model.AssistantCard;
import it.polimi.ingsw.model.Player;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Serializable class that contains the information needed by the view.
 * Light copy of the {@link Player}
 */
public class SerializablePlayer implements Serializable {

    private final String nickname;
    private SerializablePlayerBoard playerBoard;
    private ArrayList<SerializableAssistantCard> assistantDeck;



    public SerializablePlayer(Player player) {
        this.nickname = player.getNickname();
        this.playerBoard = new SerializablePlayerBoard(player.getPlayerboard());

        for(AssistantCard x : player.getAssistantDeck().getAvailableDeckList()){
            assistantDeck.add(new SerializableAssistantCard(x));
        }
    }


    public String getNickname() {
        return nickname;
    }

    public SerializablePlayerBoard getPlayerBoard() {
        return playerBoard;
    }

    public ArrayList<SerializableAssistantCard> getAssistantDeck() {
        return assistantDeck;
    }

    public void setPlayerboard(SerializablePlayerBoard playerBoard) {
        this.playerBoard = playerBoard;
    }

    public void setAssistantDeck(ArrayList<SerializableAssistantCard> assistantDeck) {
        this.assistantDeck = assistantDeck;
    }
}
