package it.polimi.ingsw.network.messages.sentByClient;


import it.polimi.ingsw.network.server.ServerMessageHandler;



public class NicknameMessage extends ClientMessage{

    public NicknameMessage(String message) {
        super(message);
    }

    @Override
    public void handle(ServerMessageHandler messageHandler) {
        messageHandler.handleMessage(this);
    }

}
