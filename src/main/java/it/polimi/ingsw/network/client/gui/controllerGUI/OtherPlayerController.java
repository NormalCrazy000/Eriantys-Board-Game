package it.polimi.ingsw.network.client.gui.controllerGUI;

import it.polimi.ingsw.network.client.gui.GUI;
import it.polimi.ingsw.network.client.gui.update.Update;
import it.polimi.ingsw.serializableModel.SerializablePlayerBoard;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.util.Map;

/**
 * This class represents the controller to manage the playerboard display of other players
 */
public class OtherPlayerController extends Controller {
    @FXML
    private AnchorPane anchorFather;

    /**
     * This method is used to set up the OtherPlayer scene.
     *
     * @param gui type {@link GUI}: GUI object
     */
    @Override
    public void setup(GUI gui) {
        setGui(gui);
        //Check number of player to remove, in case, the item player board
        if (getGui().getClientSocket().getView().getEnemyPlayerBoards().size() == 1) {
            anchorFather.getChildren().remove(1);
            anchorFather.setPrefWidth(anchorFather.getPrefWidth() / 2);
        }
        update();
    }

    /**
     * This method is called to update all enemy playerBoards
     */
    public void update() {
        int i = 0;
        for (Map.Entry<String, SerializablePlayerBoard> entry : getGui().getClientSocket().getView().getEnemyPlayerBoards().entrySet()) {
            updatePlayerBoard(entry.getValue(), (AnchorPane) anchorFather.getChildren().get(i));
            i++;
        }
    }

    /**
     * This method is called to update playerBoard
     *
     * @param playerBoard type {@link SerializablePlayerBoard}: SerializablePlayerBoard object
     * @param player      type {@link AnchorPane}: this object is used to update playerBoard
     */
    private void updatePlayerBoard(SerializablePlayerBoard playerBoard, AnchorPane player) {
        Platform.runLater(
                () -> {
                    Update.updateEntrance(playerBoard, anchorFather, ((GridPane) ((AnchorPane) player.getChildren().get(0)).getChildren().get(0)), false, getGui());
                    Update.updateDiningRoom(playerBoard, anchorFather, ((GridPane) ((AnchorPane) player.getChildren().get(0)).getChildren().get(1)));
                    Update.updateProfessors(playerBoard, anchorFather, ((GridPane) ((AnchorPane) player.getChildren().get(0)).getChildren().get(2)));
                    Update.updateTowers(playerBoard, anchorFather, ((GridPane) ((AnchorPane) player.getChildren().get(0)).getChildren().get(3)));
                    Update.updateMoneyPlayer(playerBoard, (AnchorPane) player.getChildren().get(1));
                    Update.updatePlayerAssistant((AnchorPane) player.getChildren().get(2), playerBoard);
                }
        );
    }
}
