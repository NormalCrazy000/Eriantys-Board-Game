package it.polimi.ingsw.network.client.messageHandler;

import it.polimi.ingsw.network.client.ClientSocket;
import it.polimi.ingsw.network.client.cli.CLI;
import it.polimi.ingsw.network.client.cli.TurnPhases.*;
import it.polimi.ingsw.network.messages.sentByServer.*;
import it.polimi.ingsw.network.messages.sentByServer.updateMessages.UpdateActivePlayerMessage;
import it.polimi.ingsw.network.messages.sentByServer.updateMessages.UpdateEnemyPlayerBoard;
import it.polimi.ingsw.network.messages.sentByServer.updateMessages.UpdateGameBoardMessage;
import it.polimi.ingsw.network.messages.sentByServer.updateMessages.UpdatePlayerBoardMessage;
import it.polimi.ingsw.utils.Constants;


/**
 * This class handles all the messages sent by the server implemented for the cli
 */
public class ClientMessageHandlerCLI extends ClientMessageHandler {

    private final CLI cli;

    public ClientMessageHandlerCLI(CLI cli, ClientSocket clientSocket) {
        this.cli = cli;
        this.clientSocket = clientSocket ;
    }


    @Override
    public void handleMessage(ACKMessage message) {
        cli.setIsAckArrived(true);
        synchronized (cli.getGamePhase()){
            cli.getGamePhase().notifyAll();
        }
    }

    @Override
    public void handleMessage(NACKMessage message) {
        System.out.println(Constants.ANSI_RED + message.getMessage() + Constants.ANSI_RESET);

        synchronized (cli.getGamePhase()){
            cli.getGamePhase().notifyAll();
        }
    }

    @Override
    public void handleMessage(ClientJoinedMesage message) {
        System.out.println(Constants.ANSI_GREEN+"... Successfully joined the game ..."+Constants.ANSI_RESET);
    }

    @Override
    public void handleMessage(GameCreatedMessage message) {
        cli.setIsAckArrived(true);
        synchronized (cli.getGamePhase()){
            cli.getGamePhase().notifyAll();
        }
    }

    @Override
    public void handleMessage(MoveErrorMessage message) {
        System.out.println(Constants.ANSI_RED+message.getMessage()+Constants.ANSI_RESET);
        cli.setGamePhase(new SelectAssistantCardPhase());
        new Thread(cli).start();
    }

    @Override
    public void handleMessage(SelectMageMessage message) {

    }

    @Override
    public void handleMessage(StartGameMessage message) {

        if(cli.getNickname().equals(message.getPlayer())){
            cli.setGamePhase(new SelectAssistantCardPhase());
        }else {
            cli.setGamePhase(new OtherPlayersTurnPhase());
        }
        new Thread(cli).start();
    }


    @Override
    public void handleMessage(StartTurnMessage message) {
        cli.getClientSocket().getView().setActivePlayer(message.getPlayer());

        System.out.println(Constants.ANSI_YELLOW+"active player: "+message.getPlayer());

        System.out.println(Constants.ANSI_RED+"Action DONE ?"+cli.isActionDone()+Constants.ANSI_RESET);


        if(cli.isActionDone()){
            cli.setMoveStudentsDone(false);
            cli.setMoveMotherNature(false);
            cli.setGetStudFromCloudDone(false);
            cli.setGamePhase(new SelectAssistantCardPhase());
            new Thread(cli).start();
        }else{
            cli.setGamePhase(new ActionPhase());
            new Thread(cli).start();
        }


    }

    @Override
    public void handleMessage(ExitGameMessage message) {
        cli.setGamePhase(new DisconnectionPhase());
        new Thread(cli).start();

    }

    @Override
    public void handleMessage(SendViewMessage message) {
        clientSocket.setView(message.getView());
    }

    @Override
    public void handleMessage(AssistantCardPlayed message) {
        cli.getClientSocket().getView().setActivePlayer(message.getNextActivePlayer());
        cli.setIsAckArrived(true);
        synchronized (cli.getGamePhase()){
            cli.getGamePhase().notifyAll();
        }
    }

    @Override
    public void handleMessage(UpdateBoards message) {
        cli.getClientSocket().getView().setMyPlayerBoard(message.getPlayerBoard());
        cli.getClientSocket().getView().setGameBoard(message.getGameBoard());
    }

    @Override
    public void handleMessage(StartPlanningMessage message) {
        cli.getClientSocket().getView().setActivePlayer(message.getPlayer());

        cli.setGamePhase(new SelectAssistantCardPhase());
        new Thread(cli).start();

    }

    @Override
    public void handleMessage(UpdateEnemyPlayerBoard message) {
        cli.getClientSocket().getView().updateEnemyPlayerBoard(message.getPlayerBoard(), message.getNickname());
    }

    @Override
    public void handleMessage(WaitTurnMessage message) {
        cli.setGamePhase(new OtherPlayersTurnPhase());
        new Thread(cli).start();
    }

    @Override
    public void handleMessage(NotJoinedMessage message) {
        System.out.println(Constants.ANSI_RED + message.getMessage() + Constants.ANSI_RESET);
        cli.setGamePhase(new JoinOrHostPhase());
        new Thread(cli).start();
    }

    @Override
    public void handleUpdateMessage(UpdateActivePlayerMessage message) {
        cli.getClientSocket().getView().setActivePlayer(message.getNickname());
    }

    @Override
    public void handleUpdateMessage(UpdatePlayerBoardMessage message) {
        cli.getClientSocket().getView().setMyPlayerBoard(message.getPlayerBoard());
    }

    @Override
    public void handleUpdateMessage(UpdateGameBoardMessage message) {
        cli.getClientSocket().getView().setGameBoard(message.getGameBoard());
    }

    @Override
    public void handleMessage(WinMessage message) {

        System.out.println("IN CLI HANDLER");

        if(message.getWinnerNickname() == null){
            System.out.println("IN CLI HANDLER-> TIE");

            cli.setGamePhase(new TiePhase());
            new Thread(cli).start();
        }else if(cli.getNickname().equals(message.getWinnerNickname())){
            System.out.println("IN CLI HANDLER->WIN");

            cli.setGamePhase(new WinnerPhase());
            new Thread(cli).start();
        }else {
            System.out.println("IN CLI HANDLER->LOSE");

            cli.setGamePhase(new LosePhase());
            new Thread(cli).start();
        }
    }
}
