package it.polimi.ingsw.network.messages.sentByServer;

import it.polimi.ingsw.network.client.messageHandler.ClientMessageHandler;

public class NotJoinedMessage extends ServerMessage{

    public NotJoinedMessage(String message) {
        super(message);
    }

    @Override
    public void handle(ClientMessageHandler messageHandler) {
        messageHandler.handleMessage(this);
    }
}
