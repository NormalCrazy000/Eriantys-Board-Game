package it.polimi.ingsw.network.client.gui.controllerGUI;

import it.polimi.ingsw.network.messages.sentByClient.NicknameMessage;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;

import java.util.Optional;

/**
 * This class represents the controller to manage the nickname creation
 */
public class CreatePlayerController extends Controller {
    @FXML
    private TextField textFieldNickname;

    /**
     * This method handles the nickname's creation.
     */
    public void createPlayer() {
        if (textFieldNickname.getText().equals("")) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Insert valid Nickname ", ButtonType.OK);
            alert.showAndWait();
            return;
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("You chose: " + textFieldNickname.getText());
            alert.setContentText("Is it ok?");
            Optional<ButtonType> result = alert.showAndWait();
            //Ask if nickname is correct
            if (result.get() == ButtonType.OK) {
            } else if (result.get() == ButtonType.CANCEL) {
                return;
            }
        }
        NicknameMessage nicknameMessage = new NicknameMessage(textFieldNickname.getText());
        //send to server
        getGui().getClientSocket().send(nicknameMessage);
        try {
            synchronized (this) {
                wait();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (getGui().isAckArrived()) {
            getGui().setNickname(textFieldNickname.getText());
            getGui().setIsAckArrived(false);
            getGui().switchScene("/fxml/JoinOrHost.fxml");
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, getGui().getMessageFromServer(), ButtonType.OK);
            alert.showAndWait();
        }

    }
}
