package it.polimi.ingsw.network.messages.sentByServer;

import it.polimi.ingsw.network.client.messageHandler.ClientMessageHandler;

public class MoveErrorMessage extends ServerMessage{

    public MoveErrorMessage(String message) {
        super(message);
    }

    @Override
    public void handle(ClientMessageHandler messageHandler) {
        messageHandler.handleMessage(this);
    }
}
