package it.polimi.ingsw.network.client.messageHandler;

import it.polimi.ingsw.network.client.ClientSocket;
import it.polimi.ingsw.network.client.cli.TurnPhases.JoinOrHostPhase;
import it.polimi.ingsw.network.client.gui.GUI;
import it.polimi.ingsw.network.client.gui.Phase;
import it.polimi.ingsw.network.client.gui.controllerGUI.JoinOrHostController;
import it.polimi.ingsw.network.client.gui.controllerGUI.LobbyController;
import it.polimi.ingsw.network.messages.sentByServer.*;
import it.polimi.ingsw.network.messages.sentByServer.updateMessages.UpdateActivePlayerMessage;
import it.polimi.ingsw.network.messages.sentByServer.updateMessages.UpdateEnemyPlayerBoard;
import it.polimi.ingsw.network.messages.sentByServer.updateMessages.UpdateGameBoardMessage;
import it.polimi.ingsw.network.messages.sentByServer.updateMessages.UpdatePlayerBoardMessage;
import it.polimi.ingsw.utils.Constants;
import javafx.application.Platform;

public class ClientMessageHandlerGUI extends ClientMessageHandler {
    private GUI gui;

    public ClientMessageHandlerGUI(GUI gui, ClientSocket clientSocket) {
        this.gui = gui;
        this.clientSocket = clientSocket;
    }

    @Override
    public void handleMessage(ACKMessage message) {
        gui.setIsAckArrived(true);
        synchronized (gui.getController()) {
            if(gui.getController() instanceof JoinOrHostController){
                Thread thread= new Thread(() ->Platform.runLater(()->gui.switchScene("/fxml/Wait.fxml")));
                thread.start();
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            gui.getController().notifyAll();
        }
    }

    @Override
    public void handleMessage(NACKMessage message) {
        synchronized (gui.getController()) {
            //TODO
            gui.setMessageFromServer(message.getMessage());
            gui.getController().notifyAll();
        }
    }

    @Override
    public void handleMessage(ClientJoinedMesage message) {
        synchronized (gui.getController()) {
            Thread thread= new Thread(() ->Platform.runLater(()->gui.switchScene("/fxml/Wait.fxml")));
            thread.start();
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            gui.getController().notifyAll();
        }
    }

    @Override
    public void handleMessage(GameCreatedMessage message) {
        gui.setIsAckArrived(true);
        synchronized (gui.getController()) {
            gui.getController().notifyAll();
        }
    }

    @Override
    public void handleMessage(StartGameMessage message) {
        if (gui.getNickname().equals(message.getPlayer())) {
            gui.setPhase(Phase.ASSISTANT);

        } else {
            gui.setPhase(Phase.OTHER);
        }
        Platform.runLater(
                () -> gui.switchScene("/fxml/LobbyGame.fxml")
        );

    }

    @Override
    public void handleMessage(ExitGameMessage message) {
        System.out.println(Constants.ANSI_RED + message.getMessage() + Constants.ANSI_RESET);
        gui.setMessageFromServer(message.getMessage());
        //TODO
        System.exit(0);
    }


    @Override
    public void handleMessage(MoveErrorMessage message) {
        gui.setError(true);
        synchronized (gui.getController()) {
            gui.getController().notifyAll();
        }
    }

    @Override
    public void handleMessage(SelectMageMessage message) {

    }

    @Override
    public void handleMessage(StartTurnMessage message) {

        //TODO
        gui.getClientSocket().getView().setActivePlayer(message.getPlayer());
        if (gui.getNickname().equals(message.getPlayer())) {
            if (gui.isActionDone()) {
                gui.setPhase(Phase.ASSISTANT);
            } else {
                gui.setPhase(Phase.MOVE1);
            }
        }
    }

    @Override
    public void handleMessage(SendViewMessage message) {
        if(clientSocket.getView()==null){
            clientSocket.setView(message.getView());

        }
    }


    @Override
    public void handleMessage(AssistantCardPlayed message) {
        /*cli.getClientSocket().getView().setActivePlayer(message.getNextActivePlayer());
        cli.setIsAckArrived(true);
        synchronized (cli.getGamePhase()){
            cli.getGamePhase().notifyAll();
        }
         */
    }

    @Override
    public void handleMessage(UpdateBoards message) {
        gui.getClientSocket().getView().setMyPlayerBoard(message.getPlayerBoard());
        gui.getClientSocket().getView().setGameBoard(message.getGameBoard());
        Thread thread1 = new Thread(() -> ((LobbyController) gui.getController()).updatePlayerBoard(gui.getClientSocket().getView().getMyPlayerBoard()));
        thread1.start();
        Thread thread2 = new Thread(() -> ((LobbyController) gui.getController()).updateGameBoard(gui.getClientSocket().getView().getGameBoard()));
        thread2.start();
        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handleMessage(StartPlanningMessage message) {
        gui.getClientSocket().getView().setActivePlayer(message.getPlayer());
        gui.setPhase(Phase.ASSISTANT);
    }

    @Override
    public void handleMessage(UpdateEnemyPlayerBoard message) {
        gui.getClientSocket().getView().updateEnemyPlayerBoard(message.getPlayerBoard(), message.getNickname());
        if (gui.getOtherController() != null) {
            gui.getOtherController().update();
        }
    }

    @Override
    public void handleMessage(WaitTurnMessage message) {
        //cli.setGamePhase(new OtherPlayersTurnPhase());
        //new Thread(cli).start();
    }

    @Override
    public void handleMessage(NotJoinedMessage message) {
        synchronized (gui.getController()) {
            gui.setMessageFromServer(message.getMessage());
            gui.getController().notifyAll();
        }
    }

    @Override
    public void handleUpdateMessage(UpdateActivePlayerMessage message) {
        gui.getClientSocket().getView().setActivePlayer(message.getNickname());
    }

    @Override
    public void handleUpdateMessage(UpdatePlayerBoardMessage message) {
        gui.getClientSocket().getView().setMyPlayerBoard(message.getPlayerBoard());
        Thread thread = new Thread(() -> ((LobbyController) gui.getController()).updatePlayerBoard(gui.getClientSocket().getView().getMyPlayerBoard()));
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handleUpdateMessage(UpdateGameBoardMessage message) {
        gui.getClientSocket().getView().setGameBoard(message.getGameBoard());
        Thread thread1 = new Thread(() -> ((LobbyController) gui.getController()).updateGameBoard(gui.getClientSocket().getView().getGameBoard()));
        thread1.start();
        try {
            thread1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void handleMessage(WinMessage message) {
        if(message.getWinnerNickname()==null){
            System.out.println(Constants.ANSI_GREEN+ "\n\n GAME ENDED. IT WAS A TIE!\n\n ");
            System.exit(0);
        }else if(message.getWinnerNickname().equals(gui.getNickname())){
            System.out.println(Constants.ANSI_GREEN+ "\n\n CONGRATULATION. YOU WIN!\n\n ");
            System.exit(0);
        }else{
            System.out.println(Constants.ANSI_RED + " \n\nYOU LOST!\n\n");
            System.exit(0);
        }
    }
}
