package it.polimi.ingsw.network.client.messageHandler;

import it.polimi.ingsw.network.client.ClientSocket;
import it.polimi.ingsw.network.messages.sentByServer.*;
import it.polimi.ingsw.network.messages.sentByServer.updateMessages.UpdateActivePlayerMessage;
import it.polimi.ingsw.network.messages.sentByServer.updateMessages.UpdateEnemyPlayerBoard;
import it.polimi.ingsw.network.messages.sentByServer.updateMessages.UpdateGameBoardMessage;
import it.polimi.ingsw.network.messages.sentByServer.updateMessages.UpdatePlayerBoardMessage;

public abstract class ClientMessageHandler {

    protected ClientSocket clientSocket;

    public abstract void handleMessage(ACKMessage message);

    public abstract void handleMessage(NACKMessage message);

    public abstract void handleMessage(ClientJoinedMesage message);

    public abstract void handleMessage(GameCreatedMessage message);

    public abstract void handleMessage(MoveErrorMessage message);

    public abstract void handleMessage(SelectMageMessage message);

    public abstract void handleMessage(StartGameMessage message);

    public abstract void handleMessage(StartTurnMessage message);

    public abstract void handleMessage(ExitGameMessage message);

    public abstract void handleMessage(SendViewMessage message);

    public abstract void handleMessage(AssistantCardPlayed message);

    public abstract void handleMessage(UpdateBoards message);

    public abstract void handleMessage(StartPlanningMessage message);

    public abstract void handleMessage(UpdateEnemyPlayerBoard message);

    public abstract void handleMessage(WaitTurnMessage message);

    public abstract void handleMessage(NotJoinedMessage message);


    public abstract void handleUpdateMessage(UpdateActivePlayerMessage message);

    public abstract void handleUpdateMessage(UpdatePlayerBoardMessage message);

    public abstract void handleUpdateMessage(UpdateGameBoardMessage message);


    public abstract void handleMessage(WinMessage abortGameMessage);

}
