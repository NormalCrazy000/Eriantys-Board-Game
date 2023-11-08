package it.polimi.ingsw.network.server;


import it.polimi.ingsw.utils.Constants;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This class execute the binding between the client socket and the server socket
 *
 * @author Christian Lisi
 */
public class SocketServer implements Runnable {

    private int port;
    private Server server;
    private ExecutorService executorService;
    private boolean isActive;

    public SocketServer(Server server){
        this.server=server;
        this.port=server.getPort();
        executorService = Executors.newCachedThreadPool();
        isActive=true;
    }


    public void setActive(boolean value) {
        isActive = value;
    }


    /**
     * This method handles the creation of a socket connection. When a socket is accepted a new Server client connection
     * object is created
     */
    @Override
    public void run() {
        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(Constants.ANSI_CYAN + "ServerSocket listening at port: "+ Constants.ANSI_RESET + Constants.ANSI_GREEN + port + Constants.ANSI_RESET);

        while (isActive){
            Socket s = null;
            try {
                assert serverSocket != null;
                s=serverSocket.accept();
            } catch (IOException e) {
                System.err.println("Error while accepting the socket, server still listening");
                continue;
            }

            System.out.println(Constants.ANSI_GREEN + "New client detected: " + Constants.ANSI_RESET + Constants.ANSI_PURPLE + s + Constants.ANSI_RESET);

            ServerClientConnection scc = null;
            try {
                scc = new ServerClientConnection(this.server,s);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            executorService.submit(scc);

        }

    }
}
