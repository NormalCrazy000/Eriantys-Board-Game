package it.polimi.ingsw.network.messages.sentByClient;

import it.polimi.ingsw.model.PawnType;
import it.polimi.ingsw.network.server.ServerMessageHandler;

import java.util.HashMap;

public class StudentToDiningRoomMessage extends ClientMessage{

    HashMap<PawnType,Integer> studToDiningRoom = new HashMap<>();

    public StudentToDiningRoomMessage(String message, HashMap<PawnType,Integer> studToDiningRoom) {
        super(message);
        this.studToDiningRoom =studToDiningRoom;
    }


    public HashMap<PawnType, Integer> getStudToDiningRoom() {
        return studToDiningRoom;
    }

    @Override
    public void handle(ServerMessageHandler messageHandler) {
        messageHandler.handleMessage(this);
    }
}
