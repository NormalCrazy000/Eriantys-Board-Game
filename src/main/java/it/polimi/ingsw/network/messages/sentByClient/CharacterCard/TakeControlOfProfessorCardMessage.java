package it.polimi.ingsw.network.messages.sentByClient.CharacterCard;

import it.polimi.ingsw.model.characterCard.TakeControlOfProfessorCharacterCard;
import it.polimi.ingsw.network.messages.sentByClient.ClientMessage;
import it.polimi.ingsw.network.server.ServerMessageHandler;

import java.io.IOException;

public class TakeControlOfProfessorCardMessage extends ClientMessage {

    TakeControlOfProfessorCharacterCard card;

    public TakeControlOfProfessorCardMessage(String message, TakeControlOfProfessorCharacterCard card) {
        super(message);
        this.card = card;
    }

    public TakeControlOfProfessorCharacterCard getCard() {
        return card;
    }


    @Override
    public void handle(ServerMessageHandler messageHandler) throws IOException {
        messageHandler.handleMessage(this);
    }
}
