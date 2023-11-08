package it.polimi.ingsw.network.messages.sentByClient.CharacterCard;

import it.polimi.ingsw.model.characterCard.OneStudentToAnIslandCharacterCard;
import it.polimi.ingsw.network.messages.sentByClient.ClientMessage;
import it.polimi.ingsw.network.server.ServerMessageHandler;

import java.io.IOException;

public class OneStudentToAnIslandCardMessage extends ClientMessage {

    OneStudentToAnIslandCharacterCard card;

    public OneStudentToAnIslandCardMessage(String message, OneStudentToAnIslandCharacterCard card) {
        super(message);
        this.card = card;
    }

    public OneStudentToAnIslandCharacterCard getCard() {
        return card;
    }

    @Override
    public void handle(ServerMessageHandler messageHandler) throws IOException {
        messageHandler.handleMessage(this);
    }
}
