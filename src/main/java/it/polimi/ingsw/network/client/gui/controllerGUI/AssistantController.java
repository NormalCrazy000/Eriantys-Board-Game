package it.polimi.ingsw.network.client.gui.controllerGUI;

import it.polimi.ingsw.model.AssistantCard;
import it.polimi.ingsw.network.client.gui.GUI;
import it.polimi.ingsw.network.client.gui.Phase;
import it.polimi.ingsw.network.client.gui.items.AssistantCardItem;
import it.polimi.ingsw.network.messages.sentByClient.AssistantCardMessage;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * This class represents the controller to manage the choice of the assistant
 */
public class AssistantController extends Controller {
    @FXML
    private GridPane gridPaneGroupAssistant;
    @FXML
    private Button buttonChooseAssistant;
    private AssistantCardItem chooseCard;

    /**
     * This method is used to set up the Assistant scene.
     *
     * @param gui type {@link GUI}: GUI object
     */
    @Override
    public void setup(GUI gui) {
        setGui(gui);
        int row = 0;
        int col = 0;
        int i = 0;
        for (AssistantCard card : getGui().getClientSocket().getView().getAssistantCards()) {
            AssistantCardItem c = new AssistantCardItem(card.getValue(), i);
            //add choice functionality to assistant card
            c.setOnMouseClicked(mouseEvent -> {
                //Reset border
                for (Node n : gridPaneGroupAssistant.getChildren()) {
                    ((AssistantCardItem) n).setBorder(null);
                }
                //set new border
                ((AssistantCardItem) mouseEvent.getSource()).setBorder(new Border(new BorderStroke(Color.GREEN,
                        BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
                chooseCard = ((AssistantCardItem) mouseEvent.getSource());
            });
            gridPaneGroupAssistant.add(c, col, row);
            i++;
            col++;
            if (col == 5) {
                col = 0;
                row++;
            }
        }
    }

    /**
     * This method handles the choice of the assistant.
     * This method is called on the click of the button buttonChooseAssistant
     */
    public void chooseAssistantCard() {
        if (chooseCard == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Choose card assistant!!!!!", ButtonType.OK);
            alert.showAndWait();
        } else {
            //Check if action is allowed
            if (getGui().isCanPlay() && getGui().getPhase().equals(Phase.ASSISTANT)) {
                getGui().setActionDone(false);
                getGui().setCanPlay(false);
                AssistantCard assistantCardChoose = getGui().getClientSocket().getView().getAssistantCards().get(chooseCard.getIndex());
                //send to server assistantCard selected
                getGui().getClientSocket().send(new AssistantCardMessage("assistantCard to play", assistantCardChoose));
                try {
                    synchronized (getGui().getController()) {
                        getGui().getController().wait();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (getGui().isError()) {
                    chooseCard.setOnMouseClicked(null);
                    chooseCard.setOpacity(0.5);
                    getGui().setCanPlay(true);
                    getGui().setError(false);
                } else {
                    Stage stage = (Stage) buttonChooseAssistant.getScene().getWindow();
                    getGui().setAssistantController(null);
                    getGui().setPhase(Phase.OTHER);
                    getGui().setCanPlay(true);
                    getGui().getClientSocket().getView().getAssistantCards().remove(chooseCard.getIndex());
                    getGui().setMotherMovement(assistantCardChoose.getMovementMotherNature());
                    stage.close();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "it's not your round !", ButtonType.OK);
                alert.showAndWait();
            }
        }
    }

}
