package it.polimi.ingsw.network.messages.sentByClient;

import it.polimi.ingsw.network.server.ServerMessageHandler;

public class PingACKMessage extends ClientMessage{
    public PingACKMessage(String message) {
        super(message);
    }

    @Override
    public void handle(ServerMessageHandler messageHandler) {
        messageHandler.handleMessage(this);
    }
}
