package it.polimi.ingsw.network.client.gui.controllerGUI.Character;

import it.polimi.ingsw.model.characterCard.ResolveIslandCharacterCard;
import it.polimi.ingsw.network.client.gui.GUI;
import it.polimi.ingsw.network.messages.sentByClient.CharacterCard.ResolveIslandCardMessage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * This class represents controller to manage ResolveIslandCharacterCardController characterCard
 */
public class ResolveIslandCharacterCardController extends ControllerCharacter {
    @FXML
    private TextField textFieldIsland;
    @FXML
    private Button buttonPlay;

    /**
     * This method is called to setup Scene
     *
     * @param gui type {@link GUI}: GUI object
     */
    public void setup(GUI gui) {
        setGui(gui);
    }

    /**
     * This method is called when buttonCard is clicked
     */
    public void playCharacterCard(ActionEvent actionEvent) {
        //Check number of region
        int numberIsland;
        try {
            numberIsland = Integer.parseInt(textFieldIsland.getText());
            if (numberIsland < 0) throw new NumberFormatException();
            if (numberIsland > getGui().getClientSocket().getView().getGameBoard().getRegions().size() - 1)
                throw new NumberFormatException();
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Select island", ButtonType.OK);
            alert.showAndWait();
            return;
        }
        ((ResolveIslandCharacterCard) getGui().getCardPlayed()).setParameterToEffect(numberIsland);

        getGui().getClientSocket().send(new ResolveIslandCardMessage("Resolve island card message", (ResolveIslandCharacterCard) getGui().getCardPlayed()));
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
