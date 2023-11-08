package it.polimi.ingsw.network.messages.sentByServer.updateMessages;

import it.polimi.ingsw.network.client.messageHandler.ClientMessageHandler;
import it.polimi.ingsw.network.messages.sentByServer.ServerMessage;
import it.polimi.ingsw.serializableModel.SerializableGameBoard;

public class UpdateGameBoardMessage extends ServerMessage {

    private SerializableGameBoard gameBoard;

    public UpdateGameBoardMessage(String message,SerializableGameBoard gameBoard) {
        super(message);
        this.gameBoard = gameBoard;

    }

    public SerializableGameBoard getGameBoard() {
        return gameBoard;
    }

    @Override
    public void handle(ClientMessageHandler messageHandler) {
        messageHandler.handleUpdateMessage(this);
    }
}
