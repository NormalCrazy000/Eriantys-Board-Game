package it.polimi.ingsw.network.messages.sentByClient;

import it.polimi.ingsw.network.server.ServerMessageHandler;

public class AmountOfMotherNatureMovementMessage extends ClientMessage{

    int movement;

    public AmountOfMotherNatureMovementMessage(String message, int movement) {
        super(message);
        this.movement = movement;
    }

    public int getMovement() {
        return movement;
    }

    @Override
    public void handle(ServerMessageHandler messageHandler) {
        messageHandler.handleMessage(this);
    }
}
