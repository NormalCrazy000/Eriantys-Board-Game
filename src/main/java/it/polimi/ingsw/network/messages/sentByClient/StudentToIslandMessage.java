package it.polimi.ingsw.network.messages.sentByClient;

import it.polimi.ingsw.model.IslandRegion;
import it.polimi.ingsw.model.PawnType;
import it.polimi.ingsw.network.server.ServerMessageHandler;

import java.util.HashMap;

public class StudentToIslandMessage extends ClientMessage{


    HashMap<PawnType,Integer> studToIsland = new HashMap<>();
    int islandIndex ;

    public StudentToIslandMessage(String message, HashMap<PawnType,Integer> studToIsland, int islandIndex) {
        super(message);
        this.studToIsland = studToIsland;
        this.islandIndex = islandIndex;

    }


    public HashMap<PawnType, Integer> getStudToIsland() {
        return studToIsland;
    }

    public int getIslandIndex() {
        return islandIndex;
    }

    @Override
    public void handle(ServerMessageHandler messageHandler) {
        messageHandler.handleMessage(this);
    }
}
