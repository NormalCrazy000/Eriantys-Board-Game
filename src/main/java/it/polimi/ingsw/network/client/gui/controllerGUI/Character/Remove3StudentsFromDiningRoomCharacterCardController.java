package it.polimi.ingsw.network.client.gui.controllerGUI.Character;

import it.polimi.ingsw.model.PawnType;
import it.polimi.ingsw.model.characterCard.Remove3StudentsFromDiningRoomCharacterCard;
import it.polimi.ingsw.network.client.gui.GUI;
import it.polimi.ingsw.network.messages.sentByClient.CharacterCard.Remove3StudentsFromDiningRoomCardMessage;
import it.polimi.ingsw.serializableModel.SerializablePlayerBoard;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.Map;

/**
 * This class represents controller to manage Remove3StudentsFromDiningRoomCharacterCardController characterCard
 */
public class Remove3StudentsFromDiningRoomCharacterCardController extends ControllerCharacter {
    @FXML
    private RadioButton blueColor, pinkColor, greenColor, yellowColor, redColor;
    @FXML
    private ToggleGroup groupColor;
    @FXML
    private Button buttonPlay;

    /**
     * This method is called to setup Scene
     *
     * @param gui type {@link GUI}: GUI object
     */
    @Override
    public void setup(GUI gui) {
        setGui(gui);
    }

    /**
     * This method is called when buttonCard is clicked
     */
    public void playCharacter(ActionEvent actionEvent) {
        PawnType color = null;
        if (groupColor.getSelectedToggle().equals(pinkColor)) {
            color = PawnType.PINK;
        } else if (groupColor.getSelectedToggle().equals(greenColor)) {
            color = PawnType.GREEN;
        } else if (groupColor.getSelectedToggle().equals(yellowColor)) {
            color = PawnType.YELLOW;
        } else if (groupColor.getSelectedToggle().equals(blueColor)) {
            color = PawnType.BLUE;
        }
        if (groupColor.getSelectedToggle().equals(redColor)) {
            color = PawnType.RED;
        }
        boolean notPlay = true;
        //Check if there is player with the student of the chosen color
        for (Map.Entry<String, SerializablePlayerBoard> entry : getGui().getClientSocket().getView().getEnemyPlayerBoards().entrySet()) {
            if (entry.getValue().getDiningRoom().get(color) > 0) {
                notPlay = false;
                break;
            }
        }
        if (getGui().getClientSocket().getView().getMyPlayerBoard().getDiningRoom().get(color) > 0) {
            notPlay = false;
        }

        if (notPlay) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "You can't play character", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        ((Remove3StudentsFromDiningRoomCharacterCard) getGui().getCardPlayed()).setParameterToEffect(color);
        getGui().getClientSocket().send(new Remove3StudentsFromDiningRoomCardMessage("get students for character card ", ((Remove3StudentsFromDiningRoomCharacterCard) getGui().getCardPlayed())));
        synchronized (getGui().getController()) {
            try {
                getGui().getController().wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Stage stage = (Stage) buttonPlay.getScene().getWindow();
        getGui().setCanPlay(true);
        stage.close();
    }
}
