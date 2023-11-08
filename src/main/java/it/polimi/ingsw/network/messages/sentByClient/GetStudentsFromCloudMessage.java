package it.polimi.ingsw.network.messages.sentByClient;

import it.polimi.ingsw.network.server.ServerMessageHandler;

public class GetStudentsFromCloudMessage extends ClientMessage{

    int cloudIndex;

    public GetStudentsFromCloudMessage(String message, int cloudIndex) {
        super(message);
        this.cloudIndex = cloudIndex;
    }

    public int getCloudIndex() {
        return cloudIndex;
    }

    @Override
    public void handle(ServerMessageHandler messageHandler) {
        messageHandler.handleMessage(this);
    }
}
