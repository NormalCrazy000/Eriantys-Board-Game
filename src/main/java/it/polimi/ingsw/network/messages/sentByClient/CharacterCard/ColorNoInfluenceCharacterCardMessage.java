package it.polimi.ingsw.network.messages.sentByClient.CharacterCard;

import it.polimi.ingsw.model.CharacterCard;
import it.polimi.ingsw.model.PawnType;
import it.polimi.ingsw.network.messages.sentByClient.ClientMessage;
import it.polimi.ingsw.network.server.ServerMessageHandler;

import java.io.IOException;

public class ColorNoInfluenceCharacterCardMessage extends ClientMessage {

    private PawnType color;
    private CharacterCard card;

    public ColorNoInfluenceCharacterCardMessage(String message, PawnType color, CharacterCard card) {
        super(message);
        this.color = color;
        this.card = card;
    }

    public CharacterCard getCard() {
        return card;
    }

    public PawnType getColor() {
        return color;
    }

    @Override
    public void handle(ServerMessageHandler messageHandler) {
        messageHandler.handleMessage(this);
    }
}
