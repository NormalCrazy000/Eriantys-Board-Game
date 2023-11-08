package it.polimi.ingsw.network.client.gui.controllerGUI;

import it.polimi.ingsw.model.GameMode;
import it.polimi.ingsw.network.client.gui.GUI;
import it.polimi.ingsw.network.messages.sentByClient.JoinGameMessage;
import it.polimi.ingsw.network.messages.sentByClient.NewGameMessage;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

/**
 * This class represents the controller to manage the Game's creation
 */
public class JoinOrHostController extends Controller {
    @FXML
    private RadioButton radioJoin, radioHost, radio2, radio3, radioEasy, radioExpert;
    private ToggleGroup groupJoinOrHost, groupNumber, groupMode;

    /**
     * This method is used to set up the JoinOrHost scene.
     *
     * @param gui type {@link GUI}: GUI object
     */
    @Override
    public void setup(GUI gui) {
        setGui(gui);
        groupJoinOrHost = new ToggleGroup();
        radioJoin.setToggleGroup(groupJoinOrHost);
        radioHost.setToggleGroup(groupJoinOrHost);
        groupNumber = new ToggleGroup();
        radio2.setToggleGroup(groupNumber);
        radio3.setToggleGroup(groupNumber);
        groupMode = new ToggleGroup();
        radioEasy.setToggleGroup(groupMode);
        radioExpert.setToggleGroup(groupMode);
    }

    /**
     * This method is used to: create game(Easy/Expert and 2/3 players) or to join into game that is created
     */
    public void start() {
        getGui().setIsAckArrived(false);
        GameMode gameMode;
        int numberOfPlayers;
        //Get values for mode and number of players from radioButtons
        numberOfPlayers = (groupNumber.getSelectedToggle().equals(radio2)) ? 2 : 3;
        gameMode = (groupMode.getSelectedToggle().equals(radioEasy)) ? GameMode.EASY : GameMode.EXPERT;
        if (groupJoinOrHost.getSelectedToggle().equals(radioJoin)) {
            getGui().getClientSocket().send(new JoinGameMessage("Request to join Game", getGui().getNickname(), numberOfPlayers, gameMode));
            try {
                synchronized (this) {
                    wait();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (getGui().getMessageFromServer() != null && getGui().getMessageFromServer().equals("Error! You cant join a game that has not been created.")) {
                Alert alert = new Alert(Alert.AlertType.ERROR, getGui().getMessageFromServer(), ButtonType.OK);
                alert.showAndWait();
                getGui().setMessageFromServer(null);
            }


        } else {
            NewGameMessage newGameMessage = new NewGameMessage("NewGame", getGui().getNickname(), numberOfPlayers, gameMode);
            getGui().getClientSocket().send(newGameMessage);
            try {
                synchronized (this) {
                    wait();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (getGui().isAckArrived()) {
                getGui().setIsAckArrived(false);

            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, getGui().getMessageFromServer(), ButtonType.OK);
                alert.showAndWait();
            }

        }


    }
}
