package it.polimi.ingsw.network.messages.sentByClient;


import it.polimi.ingsw.network.server.ServerMessageHandler;

import java.io.IOException;

public class JoinOrHostMessage extends ClientMessage {

    public JoinOrHostMessage(String message) {
        super(message);
    }

    @Override
    public void handle(ServerMessageHandler messageHandler) throws IOException {

    }
}
