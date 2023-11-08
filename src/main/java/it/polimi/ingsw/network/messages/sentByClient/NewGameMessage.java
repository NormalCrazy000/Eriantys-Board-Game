package it.polimi.ingsw.network.messages.sentByClient;


import it.polimi.ingsw.model.GameMode;
import it.polimi.ingsw.network.server.ServerMessageHandler;

import java.io.IOException;

public class NewGameMessage extends ClientMessage{



    private final int numOfPlayer;
    private final GameMode gameMode;
    private final String playerNickname;


    public NewGameMessage(String message,String playerNickname, int numOfPlayer, GameMode gameMode) {
        super(message);
        this.playerNickname =playerNickname;
        this.numOfPlayer = numOfPlayer;
        this.gameMode = gameMode;
    }

    public String getPlayerNickname(){return playerNickname;}

    public int getNumOfPlayer(){
        return numOfPlayer;
    }


    public GameMode getGameMode(){
        return gameMode;
    }

    @Override
    public void handle(ServerMessageHandler messageHandler) throws IOException {
        messageHandler.handleMessage(this);
    }
}
