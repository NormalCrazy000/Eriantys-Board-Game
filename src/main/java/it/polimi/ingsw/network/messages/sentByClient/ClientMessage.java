package it.polimi.ingsw.network.messages.sentByClient;

import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.server.ServerMessageHandler;

import java.io.IOException;

public abstract class ClientMessage extends Message {

    public ClientMessage(String message) {
        super(message);
    }

    public abstract void handle (ServerMessageHandler messageHandler) throws IOException;
}
