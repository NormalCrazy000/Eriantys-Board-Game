package it.polimi.ingsw.network.client;


import it.polimi.ingsw.network.client.cli.CLI;
import it.polimi.ingsw.network.client.gui.GUI;
import it.polimi.ingsw.network.client.messageHandler.ClientMessageHandler;
import it.polimi.ingsw.network.client.messageHandler.ClientMessageHandlerCLI;
import it.polimi.ingsw.network.client.messageHandler.ClientMessageHandlerGUI;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.PingMessage;
import it.polimi.ingsw.network.messages.sentByServer.ServerMessage;
import it.polimi.ingsw.utils.Constants;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.net.Socket;

public class ClientSocket implements Runnable {

    private final GUI gui;
    private final ObjectInputStream inputStream;
    private final ObjectOutputStream outputStream;
    private final ClientMessageHandler messageHandler;
    boolean isActive;
    private View view;

    /**
     * Class constructor for the CLI client that extract the streams from the socket and instantiates the correct messageHandler
     *
     * @param cli    is the client's cli
     * @param socket is the socket connection established with the server
     */
    public ClientSocket(CLI cli, Socket socket) throws IOException {
        this.gui = null;
        outputStream = new ObjectOutputStream(socket.getOutputStream());
        inputStream = new ObjectInputStream(socket.getInputStream());
        messageHandler = new ClientMessageHandlerCLI(cli, this);
        isActive = true;
    }

    /**
     * Class constructor for the GUI client that extract the streams from the socket and instantiates the correct messageHandler
     *
     * @param gui    is the client's GUI
     * @param socket is the socket connection established with the server
     */
    public ClientSocket(GUI gui, Socket socket) throws IOException {
        this.gui = gui;
        outputStream = new ObjectOutputStream(socket.getOutputStream());
        inputStream = new ObjectInputStream(socket.getInputStream());
        messageHandler = new ClientMessageHandlerGUI(gui, this);
        isActive = true;
    }


    /**
     * This method sends the message to the server
     *
     * @param message is the message that has to be sent
     */
    public synchronized void send(Message message) {
        try {
            outputStream.reset();
            outputStream.writeObject(message);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
            isActive = false;
        }
    }

    /**
     * This method returns Game's view
     *
     * @return type {@link View}: view object
     */
    public View getView() {
        return view;
    }

    /**
     * This method set Game's view
     *
     * @param view type {@link View}: view object
     */
    public void setView(View view) {
        this.view = view;
    }


    /**
     * This method is used to manage message from server
     */
    @Override
    public void run() {
        while (isActive) {
            //read messages
            try {
                Message input = (Message) inputStream.readObject();

                if (!(input instanceof PingMessage)) {
                    ((ServerMessage) input).handle(messageHandler);
                } else {
                    send(new PingMessage("Ping response"));
                }

            } catch (StreamCorruptedException e) {
                System.out.println("CORRUPTED");
                e.printStackTrace();
                return;

            } catch (IOException e) {

                System.out.println(Constants.ANSI_RED + "\n\nError: Server disconnected. Close the app and retry" + Constants.ANSI_RESET);
                System.exit(1);


                System.out.println("IOException");
                e.printStackTrace();
                return;
            } catch (ClassNotFoundException e) {

                System.out.println("Class not found");
                e.printStackTrace();
                return;
            }
        }
    }
}
