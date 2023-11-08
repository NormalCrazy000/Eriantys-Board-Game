package it.polimi.ingsw.network.messages.sentByClient.CharacterCard;

import it.polimi.ingsw.model.characterCard.ResolveIslandCharacterCard;
import it.polimi.ingsw.network.messages.sentByClient.ClientMessage;
import it.polimi.ingsw.network.server.ServerMessageHandler;

import java.io.IOException;

public class ResolveIslandCardMessage extends ClientMessage {

    ResolveIslandCharacterCard card;

    public ResolveIslandCardMessage(String message, ResolveIslandCharacterCard card) {
        super(message);
        this.card = card;
    }

    public ResolveIslandCharacterCard getCard() {
        return card;
    }

    @Override
    public void handle(ServerMessageHandler messageHandler) throws IOException {
        messageHandler.handleMessage(this);
    }
}
