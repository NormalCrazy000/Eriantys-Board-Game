package it.polimi.ingsw.network.client.gui.items;

import it.polimi.ingsw.model.PawnType;
import it.polimi.ingsw.network.client.gui.GUI;
import it.polimi.ingsw.network.client.gui.Phase;
import it.polimi.ingsw.network.messages.sentByClient.EndTurnMessage;
import it.polimi.ingsw.network.messages.sentByClient.GetStudentsFromCloudMessage;
import javafx.application.Platform;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.util.Map;

/**
 * This class represents Cloud item
 */
public class CloudItem extends GridPane {
    int index;
    private AnchorPane anchorFather;
    private GUI gui;

    /**
     * This method creates the graphical item
     *
     * @param index        type int: cloud's index
     * @param anchorFather type {@link AnchorPane}: this represents the anchorPane father of the scene.
     * @param gui          type {@link GUI}: GUI object
     */
    public CloudItem(int index, AnchorPane anchorFather, GUI gui) {
        this.index = index;
        this.anchorFather = anchorFather;
        this.gui = gui;
        getStylesheets().add("/css/Cloud.css");
        getStyleClass().add("gridPaneClouds");
        //Add mouseClick event
        //To manage click on cloud
        setOnMouseClicked(event -> {
            //Check if move student from cloud to entrance is allowed
            if (gui.getPhase().equals(Phase.CLOUD) && !gui.getClientSocket().getView().getGameBoard().getClouds()[index].isEmpty()) {
                gui.getClientSocket().send(new GetStudentsFromCloudMessage("get stud from cloud", index));
                synchronized (gui.getController()) {
                    try {
                        gui.getController().wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                gui.setActionDone(true);
                gui.setPhase(Phase.OTHER);
                //TODO
                gui.setCardPlayed(null);
                gui.setCanPlayCharacter(true);
                gui.setAdditionalMovement(0);
                gui.getClientSocket().send(new EndTurnMessage("end turn"));
            }
            event.consume();
        });
    }

    /**
     * This method is used to update student on cloud
     *
     * @param students type {@link Map} with keys {@link PawnType} and values {@link  Integer}: students to add on Cloud
     */
    public void updateStudents(Map<PawnType, Integer> students) {
        Platform.runLater(
                () -> {
                    getChildren().removeIf(n -> n instanceof StudentItem);
                    int col = 0;
                    int row = 0;
                    for (Map.Entry<PawnType, Integer> entry : students.entrySet()) {
                        for (int i = 0; i < entry.getValue(); i++) {
                            //Add student into cloud
                            add(new StudentItem(entry.getKey(), this, col, row, anchorFather), col, row);
                            col++;
                            if (col == 2) {
                                col = 0;
                                row++;
                            }
                        }
                    }
                }
        );
    }
}
