package it.polimi.ingsw.network.messages.sentByClient.CharacterCard;

import it.polimi.ingsw.model.characterCard.Replace3StudentsInEntranceCharacterCard;
import it.polimi.ingsw.network.messages.sentByClient.ClientMessage;
import it.polimi.ingsw.network.server.ServerMessageHandler;

import java.io.IOException;

public class Replace3StudentsInEntranceCardMessage extends ClientMessage {

    Replace3StudentsInEntranceCharacterCard card;

    public Replace3StudentsInEntranceCardMessage(String message, Replace3StudentsInEntranceCharacterCard card) {
        super(message);
        this.card = card;
    }

    public Replace3StudentsInEntranceCharacterCard getCard() {
        return card;
    }

    @Override
    public void handle(ServerMessageHandler messageHandler) throws IOException {
        messageHandler.handleMessage(this);
    }
}
