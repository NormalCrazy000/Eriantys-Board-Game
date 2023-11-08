package it.polimi.ingsw.network.messages.sentByClient.CharacterCard;

import it.polimi.ingsw.model.CharacterCard;
import it.polimi.ingsw.network.messages.sentByClient.ClientMessage;
import it.polimi.ingsw.network.server.ServerMessageHandler;

public class UseCharacterCardMessage extends ClientMessage {

    CharacterCard card;

    public UseCharacterCardMessage(String message,CharacterCard card) {
        super(message);
        this.card = card;
    }

    public CharacterCard getCard() {
        return card;
    }

    @Override
    public void handle(ServerMessageHandler messageHandler) {
        messageHandler.handleMessage(this);
    }
}
