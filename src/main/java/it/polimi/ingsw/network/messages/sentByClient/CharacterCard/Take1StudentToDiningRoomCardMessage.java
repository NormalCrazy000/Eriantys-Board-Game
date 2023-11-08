package it.polimi.ingsw.network.messages.sentByClient.CharacterCard;

import it.polimi.ingsw.model.characterCard.Take1StudentToDiningRoomCharacterCard;
import it.polimi.ingsw.network.messages.sentByClient.ClientMessage;
import it.polimi.ingsw.network.server.ServerMessageHandler;

import java.io.IOException;

public class Take1StudentToDiningRoomCardMessage extends ClientMessage {

    Take1StudentToDiningRoomCharacterCard card;


    public Take1StudentToDiningRoomCardMessage(String message, Take1StudentToDiningRoomCharacterCard card) {
        super(message);
        this.card = card;
    }

    public Take1StudentToDiningRoomCharacterCard getCard() {
        return card;
    }

    @Override
    public void handle(ServerMessageHandler messageHandler) throws IOException {
        messageHandler.handleMessage(this);
    }
}
