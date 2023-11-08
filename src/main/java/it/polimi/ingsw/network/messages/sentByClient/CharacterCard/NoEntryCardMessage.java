package it.polimi.ingsw.network.messages.sentByClient.CharacterCard;

import it.polimi.ingsw.model.characterCard.NoEntryCardCharacterCard;
import it.polimi.ingsw.network.messages.sentByClient.ClientMessage;
import it.polimi.ingsw.network.server.ServerMessageHandler;

import java.io.IOException;

public class NoEntryCardMessage extends ClientMessage {

    NoEntryCardCharacterCard card;

    public NoEntryCardMessage(String message, NoEntryCardCharacterCard card) {
        super(message);
        this.card = card;
    }

    public NoEntryCardCharacterCard getCard() {
        return card;
    }

    @Override
    public void handle(ServerMessageHandler messageHandler) throws IOException {
        messageHandler.handleMessage(this);
    }
}
