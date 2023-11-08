package it.polimi.ingsw.network.messages.sentByClient.CharacterCard;

import it.polimi.ingsw.model.characterCard.Remove3StudentsFromDiningRoomCharacterCard;
import it.polimi.ingsw.network.messages.sentByClient.ClientMessage;
import it.polimi.ingsw.network.server.ServerMessageHandler;

import java.io.IOException;

public class Remove3StudentsFromDiningRoomCardMessage extends ClientMessage {


    Remove3StudentsFromDiningRoomCharacterCard card;

    public Remove3StudentsFromDiningRoomCardMessage(String message, Remove3StudentsFromDiningRoomCharacterCard card) {
        super(message);
        this.card = card;
    }

    public Remove3StudentsFromDiningRoomCharacterCard getCard() {
        return card;
    }

    @Override
    public void handle(ServerMessageHandler messageHandler) throws IOException {
        messageHandler.handleMessage(this);
    }
}
