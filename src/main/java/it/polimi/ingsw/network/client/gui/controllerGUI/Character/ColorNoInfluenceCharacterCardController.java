package it.polimi.ingsw.network.client.gui.controllerGUI.Character;

import it.polimi.ingsw.model.PawnType;
import it.polimi.ingsw.network.client.gui.GUI;
import it.polimi.ingsw.network.messages.sentByClient.CharacterCard.ColorNoInfluenceCharacterCardMessage;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

/**
 * This class represents controller to manage ColorNoInfluenceCharacterCardController characterCard
 */
public class ColorNoInfluenceCharacterCardController extends ControllerCharacter {
    @FXML
    private RadioButton blueColor, pinkColor, greenColor, yellowColor, redColor;
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
        groupColor = new ToggleGroup();
        blueColor.setToggleGroup(groupColor);
        pinkColor.setToggleGroup(groupColor);
        greenColor.setToggleGroup(groupColor);
        yellowColor.setToggleGroup(groupColor);
        redColor.setToggleGroup(groupColor);
    }

    /**
     * This method is called when buttonCard is clicked
     */
    public void playCharacter() {
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
        getGui().getClientSocket().send(new ColorNoInfluenceCharacterCardMessage("Color no influence", color, getGui().getCardPlayed()));
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
