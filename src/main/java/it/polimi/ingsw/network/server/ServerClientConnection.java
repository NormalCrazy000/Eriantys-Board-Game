package it.polimi.ingsw.network.server;

import it.polimi.ingsw.model.GameMode;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.PingMessage;
import it.polimi.ingsw.network.messages.sentByClient.ClientMessage;
import it.polimi.ingsw.network.messages.sentByServer.ExitGameMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * This class handles the communication between the server and the client
 */
public class ServerClientConnection implements Runnable {

    private Socket socket;
    private Server server;
    private String nickname;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private boolean isActive = false;
    private PingSender ps;
    private ExecutorService executorService;
    private ServerMessageHandler messageHandler;
    private GamePhases gamePhase;
    private GameHandler gameHandler;
    private Boolean hasDisconnectionBeenCalled;


    /**
     * class constructor that creates the streams, the ping sender and the message handler
     * @param server is the instance of the server
     * @param socket is the socket with the client
     */
    public ServerClientConnection(Server server, Socket socket) throws IOException {
        this.server=server;
        this.socket=socket;
        isActive=true;
        hasDisconnectionBeenCalled=false;
        executorService = Executors.newCachedThreadPool();
        nickname=null;

        messageHandler = new ServerMessageHandler(this.server,this);


        outputStream = new ObjectOutputStream(socket.getOutputStream());
        inputStream = new ObjectInputStream(socket.getInputStream());
        //isActive=true;
        ps= new PingSender(this);
        gamePhase = GamePhases.CONFIGURATION;
    }

    /**
     * This method sends a message to the client
     * @param message is the message to be sent
     */
    public synchronized void send(Message message){
        if (!isActive){
            return;
        }
        try{
            outputStream.reset();
            outputStream.writeObject(message);
            outputStream.flush();
        }catch (IOException e){
            e.printStackTrace();
        }
    }



    /**
     * This method waits for messages from the client and invokes the method to handle it. It also handles the
     * disconnection when an IOException is thrown
     */
    @Override
    public void run() {

        try{

            ps.setActive(true);
            new Thread(ps).start();
            System.out.println("Start with socket: "+socket);
            Message input = null;

            while(isActive){
                //read input messages and execute them
                input= (Message) inputStream.readObject();

                if(!(input instanceof PingMessage)){
                    System.out.println("Message read: " + input.getMessage() + " From: " + nickname);

//                    if(gameHandler!=null && gameHandler.getPlayerInGame().get(this)!=null){
//                        System.err.println(nickname+" is playing: "+gameHandler.getPlayerInGame().get(this).isPlaying());
//                    }

                    ((ClientMessage)input).handle(messageHandler);

                }else{
                    ps.pingReceived();
                }
            }

        } catch (IOException e) {
            System.out.println(nickname + ": readObject disconnection");
            isActive = false;
            ps.setActive(false);

            disconnect();


        } catch (ClassNotFoundException e) {
            System.out.println("message sent was not correct");
        }
    }


    /**
     * This method handles the disconnection of a player. It notifies all the players that someone has been disconnected
     * and removes all the player nicknames from the server.
     */
    public  void disconnect(){

        gameHandler.getPlayerInGame().remove(this);

        System.out.println("sending mex");
        for(ServerClientConnection serverClientConnection : gameHandler.getPlayerInGame().keySet()){
            System.out.println("sending to "+serverClientConnection.getNickname());
            serverClientConnection.send(new ExitGameMessage("EXIT"));
        }

        System.out.println("remove nick");
        //remove the taken nicknames from the arrayList
        for (Player nickname : gameHandler.getPlayerInGame().values()){
            server.removeTakeNickname(nickname.getNickname());
        }

    }

    /**
     * check if the player disconnected while he was waiting in lobby, in that case just removes him
     */
    private void checkLobbyDisconnection() {
        for (ServerClientConnection serverClientConnection : server.getLobby2playersEASY()){
            if (serverClientConnection.equals(this)){
                server.removeToLobby2Players(this, GameMode.EASY);
                return;
            }
        }

        for (ServerClientConnection serverClientConnection : server.getLobby2playersEXPERT()){
            if (serverClientConnection.equals(this)){
                server.removeToLobby2Players(this,GameMode.EXPERT);
                return;
            }
        }
        for (ServerClientConnection serverClientConnection : server.getLobby3playersEASY()){
            if (serverClientConnection.equals(this)){
                server.removeToLobby3Players(this,GameMode.EASY);
                return;
            }
        }
        for (ServerClientConnection serverClientConnection : server.getLobby3playersEXPERT()){
            if (serverClientConnection.equals(this)){
                server.removeToLobby3Players(this,GameMode.EXPERT);
                return;
            }
        }


    }




    public void setServer(Server server) {
        this.server = server;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void setGamePhase(GamePhases gamePhase) {
        this.gamePhase = gamePhase;
    }

    public void setGameHandler(GameHandler gameHandler) {
        this.gameHandler = gameHandler;
    }


    public Socket getSocket() {
        return socket;
    }

    public Server getServer() {
        return server;
    }

    public String getNickname() {
        return nickname;
    }

    public ObjectInputStream getInputStream() {
        return inputStream;
    }

    public ObjectOutputStream getOutputStream() {
        return outputStream;
    }

    public boolean isActive() {
        return isActive;
    }

    public GamePhases getGamePhase() {
        return gamePhase;
    }

    public GameHandler getGameHandler() {
        return gameHandler;
    }

}
