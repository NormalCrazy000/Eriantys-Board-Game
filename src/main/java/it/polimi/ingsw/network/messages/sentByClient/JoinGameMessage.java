package it.polimi.ingsw.network.messages.sentByClient;

import it.polimi.ingsw.model.GameMode;
import it.polimi.ingsw.network.server.ServerMessageHandler;

public class JoinGameMessage extends ClientMessage{

    private final int numOfPlayer;
    private final GameMode gameMode;
    private final String playerNickname;

    public JoinGameMessage(String message,String playerNickname, int numOfPlayer,GameMode gameMode) {
        super(message);
        this.numOfPlayer = numOfPlayer;
        this.gameMode = gameMode;
        this.playerNickname =playerNickname;
    }

    public String getPlayerNickname(){return playerNickname;}

    public int getNumOfPlayer(){
        return numOfPlayer;
    }
    public GameMode getGameMode(){
        return gameMode;
    }


    @Override
    public void handle(ServerMessageHandler messageHandler) {
        messageHandler.handleMessage(this);
    }
}
