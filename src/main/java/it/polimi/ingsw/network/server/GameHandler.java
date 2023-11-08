package it.polimi.ingsw.network.server;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameMode;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.network.client.View;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.sentByServer.SendViewMessage;
import it.polimi.ingsw.network.messages.sentByServer.StartGameMessage;
import it.polimi.ingsw.serializableModel.SerializableGameBoard;
import it.polimi.ingsw.serializableModel.SerializablePlayerBoard;
import it.polimi.ingsw.utils.Constants;


import java.util.ArrayList;
import java.util.HashMap;

public class GameHandler {

    private int numberOfPlayers;
    private Game game;
    private final GameController gameController;
    private int started;
    private String activePlayer;
    private HashMap<Player,VirtualView> playerScokets;
    private HashMap<ServerClientConnection,Player> playersInGame;
    private HashMap<Player,ServerClientConnection> sccRelatedToPlayer;



    public GameHandler(int numberOfPlayers, GameMode gameMode, ArrayList<ServerClientConnection> playerConnected) {
        gameController = new GameController();                              //instantiate a gameController
        gameController.createGame(numberOfPlayers,gameMode);                //create a new game with the gameController
        game = gameController.getGame();
        started = 0;                                                         //game hasn't started yet
        this.numberOfPlayers = numberOfPlayers;
        playerScokets = new HashMap<>();
        playersInGame = new HashMap<>();
        sccRelatedToPlayer = new HashMap<>();


        for (ServerClientConnection scc : playerConnected) {

            scc.setGameHandler(this);

            System.out.println(Constants.ANSI_GREEN + "registering " + scc.getNickname() + " in game handler" + Constants.ANSI_RESET);

            // create a new player
            gameController.registration(scc.getNickname());

            Player player = game.getPlayerByNickname(scc.getNickname());

            playersInGame.put(scc, player);
            sccRelatedToPlayer.put(player, scc);

        }

        //setup player board and populate cloud tile
        gameController.setupPlayerBoard();

        gameController.populateCloudTile();

        setActivePlayer(game.getCurrentPlayer().getNickname());


        for (ServerClientConnection x : playerConnected) {
            System.out.println("setting view for " + x.getNickname());
            x.getGameHandler().initializationView();
            x.send(new StartGameMessage("game is starting", getActivePlayer()));
        }

    }


    /**
     * This method creates a view from a virtual view.
     * It is also responsible to create "enemyPlayersView"
     * @param virtualView is the virtual view from which create the view
     * @return the View to be sent
     */
    public View createView(VirtualView virtualView) {

        SerializableGameBoard serializableGameBoard = new SerializableGameBoard(virtualView.getGameBoard());
        SerializablePlayerBoard serializablePlayerBoard = new SerializablePlayerBoard(virtualView.getPersonalPlayerBoard());


        HashMap<String, SerializablePlayerBoard> serializableEnemyBoard = new HashMap<>();

        for (Player player : virtualView.getEnemyPlayerViews().keySet()) {
            SerializablePlayerBoard serializablePlayerBoardEnemy = new SerializablePlayerBoard(virtualView.getEnemyPlayerViews().get(player).getPersonalPlayerBoard());
            serializableEnemyBoard.put(player.getNickname(), serializablePlayerBoardEnemy);
        }

        View view = new View(game.getCurrentPlayer().getNickname(), virtualView.getScc().getNickname(), virtualView.getPlayerAssistantDeck(), serializablePlayerBoard, serializableGameBoard, serializableEnemyBoard/*,virtualView.getGameMode()*/);
        return view;
    }


    /**
     * This method creates the virtual view during the game initialization and send it to the
     * correct player
     */
    public void initializationView() {
        int i = 1;
        for (Player player : playersInGame.values()) {
            VirtualView virtualView = new VirtualView(sccRelatedToPlayer.get(player), game.getGameBoard(), player.getPlayerboard(), player.getAssistantDeck().getAvailableDeckList());
            playerScokets.put(player, virtualView);
        }

        //create the hashmap with the view of the other player
        for (Player player : playersInGame.values()) {
            HashMap<Player, VirtualView> otherPlayer = (HashMap<Player, VirtualView>) playerScokets.clone();
            VirtualView myVirtualView = otherPlayer.remove(player);

            playerScokets.get(player).setEnemyPlayerViews(otherPlayer);
            View view = createView(playerScokets.get(player));
            System.out.println(i);
            i++;
            //TODO
            sccRelatedToPlayer.get(player).send(new SendViewMessage("View", view));
            sccRelatedToPlayer.get(player).setGamePhase(GamePhases.GAME);

        }

        //set the new game phase for every player
        for (ServerClientConnection scc : playersInGame.keySet()) {
            scc.setGamePhase(GamePhases.GAME);
        }

    }


    /**
     * This method is used for sending a message to all the player in the game
     * @param message the message to send
     */
    public void sendBroadcast(Message message) {
        for (ServerClientConnection scc : playersInGame.keySet()) {
            scc.send(message);
        }
    }

    /**
     * This method is used for sending a message to all the player in the game except the one specified.
     * For example, it's used for notifying all the other client of an enemy player board update.
     * @param message          the message to send
     * @param nicknameToExcept (of type String) specify the username of the player omitted.
     */

    public void sendBroadcastExcept(Message message, String nicknameToExcept) {
        for (ServerClientConnection scc : playersInGame.keySet()) {
            if (!scc.getNickname().equals(nicknameToExcept)) {
                scc.send(message);
            }
        }
    }


    /**
     * Method isStarted returns if the game has started (the started attribute becomes true after every player has joined the lobby).
     * @return int - the current game phase.
     */
    public int isStarted() {
        return started;
    }

    public HashMap<ServerClientConnection, Player> getPlayerInGame() {
        return playersInGame;
    }

    public HashMap<Player, VirtualView> getPlayerSockets() {
        return playerScokets;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public Game getGame() {
        return game;
    }

    public GameController getGameController() {
        return gameController;
    }


    public void setActivePlayer(String activePlayer) {
        this.activePlayer = activePlayer;
    }


    public String getActivePlayer() {
        return activePlayer;
    }

    public HashMap<Player, ServerClientConnection> getSccRelatedToPlayer() {
        return sccRelatedToPlayer;
    }
}
