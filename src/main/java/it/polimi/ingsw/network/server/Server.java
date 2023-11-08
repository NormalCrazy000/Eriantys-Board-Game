package it.polimi.ingsw.network.server;

import it.polimi.ingsw.model.GameMode;
import it.polimi.ingsw.utils.Constants;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    private final SocketServer socketServer;
    private static int port = 2222; //default value

    //the add method for the following lists needs to be synchronized
    private List<ServerClientConnection> lobby2playersEASY;
    private List<ServerClientConnection> lobby3playersEASY;
    private List<ServerClientConnection> lobby2playersEXPERT;
    private List<ServerClientConnection> lobby3playersEXPERT;

    private HashMap<ServerClientConnection,GameHandler> games;
    private List<String> listOfTakenNicknames;

    public Server() {
        this.socketServer = new SocketServer(this);
        lobby2playersEASY = new ArrayList<>();
        lobby3playersEASY = new ArrayList<>();
        lobby2playersEXPERT = new ArrayList<>();
        lobby3playersEXPERT = new ArrayList<>();
        games = new HashMap<>();
        listOfTakenNicknames = new ArrayList<>();
    }


    /**
     * This method checks if a nickname is available.
     *
     * @param nickname is the nickname to be checked
     * @return false if is already taken, true if is available
     */
    public boolean checkNickname(String nickname){

        synchronized(listOfTakenNicknames){
            return !listOfTakenNicknames.contains(nickname);
        }

    }


    /**
     * This Method adds the new player to the right lobby (for EASY or EXPERT game) where he will wait for another player.
     * If the arrayList reaches size()==2 the method will instantiate a gameHandler object to start the game.
     *
     * @param scc is the new player ServerClientConnection
     * @param gameMode the gameMode requested by the user
     */
    public synchronized void addToLobby2Players(ServerClientConnection scc, GameMode gameMode){

        if(gameMode == GameMode.EASY){
            lobby2playersEASY.add(scc);

            //check if the lobby is ready to start the game
            if(lobby2playersEASY.size()==2){

                ArrayList<ServerClientConnection> twoPlayer = new ArrayList<>();

                for(int i=0; i < 2 ; i++){
                    twoPlayer.add(lobby2playersEASY.remove(0));
                }

                GameHandler gameHandler = new GameHandler(2,gameMode,twoPlayer);

                for(int i =0;i<2;i++){
                    games.put(twoPlayer.get(i),gameHandler);
                }
            }
            System.out.println("["+ lobby2playersEASY.size()+"/2] in lobby");

        }else if (gameMode == GameMode.EXPERT){
            lobby2playersEXPERT.add(scc);

            //check if the lobby is ready to start the game
            if(lobby2playersEXPERT.size()==2){

                ArrayList<ServerClientConnection> twoPlayer = new ArrayList<>();

                for(int i=0; i < 2 ; i++){
                    twoPlayer.add(lobby2playersEXPERT.remove(0));
                }

                GameHandler gameHandler = new GameHandler(2,gameMode,twoPlayer);

                System.out.println("============================");
                for(int i =0;i<2;i++){
                    games.put(twoPlayer.get(i),gameHandler);
                }
            }
            System.out.println("["+ lobby2playersEXPERT.size()+"/2] in lobby");
        }

    }



    /**
     * This Method adds the new player to the Lobby where he will wait for another player.
     * If the arrayList reaches size()==3 the method will instantiate a gameHandler object to start the game.
     *
     * @param scc is the new player ServerClientConnection
     * @param gameMode the gameMode requested by the user
     */
    public synchronized void addToLobby3Players(ServerClientConnection scc,GameMode gameMode){
        if(gameMode == GameMode.EASY){
            lobby3playersEASY.add(scc);

            //check if the lobby is ready to start the game
            if (lobby3playersEASY.size()==3){
                ArrayList<ServerClientConnection> threePlayers = new ArrayList<>();

                for (int i=0; i<3; i++) {
                    threePlayers.add(lobby3playersEASY.remove(0));
                }
                GameHandler gameHandler = new GameHandler(3, gameMode,threePlayers);
                for (int i=0; i<3; i++) {
                    games.put(threePlayers.get(i), gameHandler);
                }

            }
            System.out.println("["+ lobby2playersEASY.size()+"/3] in lobby");

        } else if (gameMode == GameMode.EXPERT){
            lobby3playersEXPERT.add(scc);

            //check if the lobby is ready to start the game
            if (lobby3playersEXPERT.size()==3){
                ArrayList<ServerClientConnection> threePlayers = new ArrayList<>();

                for (int i=0; i<3; i++) {
                    threePlayers.add(lobby3playersEXPERT.remove(0));
                }
                GameHandler gameHandler = new GameHandler(3, gameMode,threePlayers);
                for (int i=0; i<3; i++) {
                    games.put(threePlayers.get(i), gameHandler);
                }

            }
            System.out.println("["+ lobby2playersEASY.size()+"/3] in lobby");
        }
    }

    /**
     * remove players from the corresponding lobby
     * @param scc is the connection with the player to be removed
     * @param gameMode the gameMode requested by the user
     */
    public synchronized void removeToLobby2Players(ServerClientConnection scc, GameMode gameMode){

        if(gameMode == GameMode.EASY){
            lobby2playersEASY.remove(scc);
        }else {
            lobby2playersEXPERT.remove(scc);
        }
    }

    /**
     * remove players from the corresponding lobby
     * @param scc is the connection with the player to be removed
     */
    public synchronized void removeToLobby3Players(ServerClientConnection scc,GameMode gameMode){
        if(gameMode == GameMode.EASY){
            lobby2playersEASY.remove(scc);
        }else {
            lobby2playersEXPERT.remove(scc);
        }
    }




    public List<ServerClientConnection> getLobby2playersEASY() {
        return lobby2playersEASY;
    }

    public List<ServerClientConnection> getLobby3playersEASY() {
        return lobby3playersEASY;
    }

    public List<ServerClientConnection> getLobby2playersEXPERT() {
        return lobby2playersEXPERT;
    }

    public List<ServerClientConnection> getLobby3playersEXPERT() {
        return lobby3playersEXPERT;
    }

    /**
     * add the nickname to the list of taken nicknames
     * @param nickname is the nickname to be added
     */
    public void addTakenNickname(String nickname) {
        synchronized (listOfTakenNicknames){
            listOfTakenNicknames.add(nickname);
        }
    }

    /**
     * remove the nickname to the list of taken nicknames
     * @param nickname is the nickname to be removed
     */
    public void removeTakeNickname(String nickname){
        synchronized (listOfTakenNicknames){
            listOfTakenNicknames.remove(nickname);
        }
    }

    public HashMap<ServerClientConnection, GameHandler> getGames() {
        return games;
    }


    public int getPort() {
        return port;
    }


    public static void main(String[] args) {
        System.out.println(Constants.ANSI_RED + "Eriantys | Server" + Constants.ANSI_RESET);
        Scanner scanner = new Scanner(System.in);

        do {
            System.out.println(Constants.ANSI_CYAN + "> Insert the port which server will listen on." + Constants.ANSI_RESET);
            System.out.print(Constants.ANSI_CYAN + "> " +Constants.ANSI_RESET);
            try {
                port = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.err.println("Numeric format requested, retry...");
            }
            if(port < 1025 || port > 65535){
                System.err.println("Error: ports available only from 1025 to 65534");
            }
        }while(port < 1025 || port > 65534);

        System.out.println(Constants.ANSI_GREEN + "Starting the server" + Constants.ANSI_RESET);
        Server server = new Server();

        ExecutorService ex = Executors.newCachedThreadPool();
        ex.submit(server.socketServer);
    }
}
