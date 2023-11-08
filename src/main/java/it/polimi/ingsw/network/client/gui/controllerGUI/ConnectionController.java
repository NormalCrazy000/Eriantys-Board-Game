package it.polimi.ingsw.network.client.gui.controllerGUI;

import it.polimi.ingsw.network.client.ClientSocket;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.Socket;

/**
 * This class represents the controller to manage the connection to the server
 */
public class ConnectionController extends Controller {
    @FXML
    private TextField textFieldIp;
    @FXML
    private TextField textFieldPort;


    /**
     * This method is called to connect the client to the server
     */
    public void connectToServer() {
        Platform.runLater(() -> {
            try {
                getGui().setSocket(new Socket(textFieldIp.getText(), Integer.parseInt(textFieldPort.getText())));
                getGui().setClientSocket(new ClientSocket(getGui(), getGui().getSocket()));
            } catch (IOException e) {
                getGui().setClientSocket(null);
                Alert alert = new Alert(Alert.AlertType.ERROR, "There was a problem with the server. Please check if the ip address and port number are correct and if the server is up and running ", ButtonType.OK);
                alert.showAndWait();
            }
            if (!(getGui().getClientSocket() == null)) {
                //Start clientSocket to receive serverMessage
                new Thread(getGui().getClientSocket()).start();
                getGui().switchScene("/fxml/CreatePlayer.fxml");
            }
        });
    }
}
