package it.polimi.ingsw.network.client.gui.controllerGUI.Character;

import it.polimi.ingsw.model.characterCard.NoEntryCardCharacterCard;
import it.polimi.ingsw.network.client.gui.GUI;
import it.polimi.ingsw.network.messages.sentByClient.CharacterCard.NoEntryCardMessage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * This class represents controller to manage NoEntryCardCharacterCardController characterCard
 */
public class NoEntryCardCharacterCardController extends ControllerCharacter {
    @FXML
    private TextField textIsland;
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
    public void playCharacter(ActionEvent actionEvent) {

        if (((NoEntryCardCharacterCard) getGui().getCardPlayed()).getNoEntry() == 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "You cannot play this card", ButtonType.OK);
            alert.showAndWait();
            return;
        }
        int numberIsland;
        try {
            numberIsland = Integer.parseInt(textIsland.getText());
            if (numberIsland < 0) throw new NumberFormatException();
            if (numberIsland > getGui().getClientSocket().getView().getGameBoard().getRegions().size() - 1)
                throw new NumberFormatException();
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Select island", ButtonType.OK);
            alert.showAndWait();
            return;
        }
        ((NoEntryCardCharacterCard) getGui().getCardPlayed()).setParameterToEffect(numberIsland);
        getGui().getClientSocket().send(new NoEntryCardMessage("No entry character card", (NoEntryCardCharacterCard) getGui().getCardPlayed()));
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
