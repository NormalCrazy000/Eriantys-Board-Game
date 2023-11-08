package it.polimi.ingsw.network.messages.sentByClient.CharacterCard;

import it.polimi.ingsw.model.CharacterCard;
import it.polimi.ingsw.model.characterCard.Exchange2StudentsCharacterCard;
import it.polimi.ingsw.network.messages.sentByClient.ClientMessage;
import it.polimi.ingsw.network.server.ServerMessageHandler;

import java.io.IOException;

public class Exchange2StudentsMessage extends ClientMessage {

    Exchange2StudentsCharacterCard card;

    public Exchange2StudentsMessage(String message,Exchange2StudentsCharacterCard card) {
        super(message);
        this.card = card;
    }

    public Exchange2StudentsCharacterCard getCard() {
        return card;
    }

    @Override
    public void handle(ServerMessageHandler messageHandler) throws IOException {
        messageHandler.handleMessage(this);
    }
}
