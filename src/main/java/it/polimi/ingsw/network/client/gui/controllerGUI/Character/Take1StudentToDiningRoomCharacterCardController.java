package it.polimi.ingsw.network.client.gui.controllerGUI.Character;

import it.polimi.ingsw.model.PawnType;
import it.polimi.ingsw.model.characterCard.Take1StudentToDiningRoomCharacterCard;
import it.polimi.ingsw.network.client.gui.GUI;
import it.polimi.ingsw.network.messages.sentByClient.CharacterCard.Take1StudentToDiningRoomCardMessage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

/**
 * This class represents controller to manage Take1StudentToDiningRoomCharacterCardController characterCard
 */
public class Take1StudentToDiningRoomCharacterCardController extends ControllerCharacter {
    @FXML
    private GridPane gridPaneCard;
    @FXML
    private ToggleGroup redCard, blueCard, pinkCard, yellowCard, greenCard;
    @FXML
    private Text textStudetnOnCard;
    @FXML
    private Button buttonPlay;

    /**
     * This method is called to setup Scene
     *
     * @param gui type {@link GUI}: GUI object
     */
    public void setup(GUI gui) {
        setGui(gui);
        Map<PawnType, Integer> studentsCard = ((Take1StudentToDiningRoomCharacterCard) gui.getCardPlayed()).getStudentsCard();
        setNumberOfStudent(gridPaneCard, studentsCard);
        textStudetnOnCard.setText("blue: " + studentsCard.get(PawnType.BLUE) +
                "\npink: " + studentsCard.get(PawnType.PINK) +
                "\nyellow: " + studentsCard.get(PawnType.YELLOW) +
                "\nred: " + studentsCard.get(PawnType.RED) +
                "\ngreen: " + studentsCard.get(PawnType.GREEN));
    }

    /**
     * This method is used to set student on GridPane
     *
     * @param gridPane type {@link GridPane}: destination GridPane
     * @param students type Map with key {@link PawnType} and values {@link Integer}: student to add on gridPane
     */
    private void setNumberOfStudent(GridPane gridPane, Map<PawnType, Integer> students) {
        for (Map.Entry<PawnType, Integer> entry : students.entrySet()) {
            int rowGridPaneColor = 0;
            switch (entry.getKey()) {
                case RED:
                    rowGridPaneColor = 3;
                    break;
                case GREEN:
                    rowGridPaneColor = 4;
                    break;
                case BLUE:
                    rowGridPaneColor = 0;
                    break;
                case PINK:
                    rowGridPaneColor = 1;
                    break;
                case YELLOW:
                    rowGridPaneColor = 2;
                    break;
            }
            //Remove radio button if number of students is less than prefixed number
            GridPane gridPaneColor = (GridPane) getObjectFromGridPane(gridPane, 0, rowGridPaneColor);
            if (entry.getValue() < 1) {
                Node removeNode = getObjectFromGridPane(gridPaneColor, 2, 0);
                gridPaneColor.getChildren().remove(removeNode);
            }
        }
    }

    /**
     * This method returns object from GridPane
     *
     * @param gridPane type {@link GridPane}: destination GridPane
     * @param col      type int: object's column
     * @param row      object's column
     * @return type {@link Node}: Obecjt in GridPane(null if the object is not in the gridPane)
     */
    private Node getObjectFromGridPane(GridPane gridPane, int col, int row) {
        for (Node object : gridPane.getChildren()) {
            Integer columnIndex = GridPane.getColumnIndex(object);
            Integer rowIndex = GridPane.getRowIndex(object);

            if (columnIndex == null)
                columnIndex = 0;
            if (rowIndex == null)
                rowIndex = 0;

            if (columnIndex == col && rowIndex == row) {
                return object;
            }
        }
        return null;
    }

    /**
     * This method is called when buttonCard is clicked
     */
    public void playCharacterCard(ActionEvent actionEvent) {
        Map<PawnType, Integer> setStudentOnIsland;
        setStudentOnIsland = setStudents(redCard, greenCard, yellowCard, pinkCard, blueCard);
        //Check if there are students on bag
        if (getGui().getClientSocket().getView().getGameBoard().isBagEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "You cannot play this card. The bag of student's is empty", ButtonType.OK);
            alert.showAndWait();
            return;
        }
        //Check number of student selected
        if (setStudentOnIsland.values().stream().mapToInt(e -> e).sum() == 1) {

            PawnType colorToMove = null;
            for (Map.Entry<PawnType, Integer> entry : setStudentOnIsland.entrySet()) {
                if (entry.getValue() == 1) {
                    colorToMove = entry.getKey();
                    break;
                }
            }
            if (getGui().getClientSocket().getView().getMyPlayerBoard().getDiningRoom().get(colorToMove) == 10) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Error!!!!", ButtonType.OK);
                alert.showAndWait();
                return;
            }
            ((Take1StudentToDiningRoomCharacterCard) getGui().getCardPlayed()).setParameterToEffect(colorToMove);
            getGui().getClientSocket().send(new Take1StudentToDiningRoomCardMessage("One stud to island card", (Take1StudentToDiningRoomCharacterCard) ((Take1StudentToDiningRoomCharacterCard) getGui().getCardPlayed())));
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
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error!!!!", ButtonType.OK);
            alert.showAndWait();
            return;
        }

    }

    /**
     * This method se on Map value of ToggleGroup
     *
     * @param red    type {@link ToggleGroup}: ToggleGroup red
     * @param green  type {@link ToggleGroup}: ToggleGroup green
     * @param yellow type {@link ToggleGroup}: ToggleGroup yellow
     * @param pink   type {@link ToggleGroup}: ToggleGroup pink
     * @param blue   type {@link ToggleGroup}: ToggleGroup blue
     * @return type Map with key {@link PawnType} and values {@link Integer}
     */
    private Map<PawnType, Integer> setStudents(ToggleGroup red, ToggleGroup green, ToggleGroup yellow, ToggleGroup pink, ToggleGroup blue) {
        Map<PawnType, Integer> changeStudents = new HashMap<>();
        changeStudents.put(PawnType.RED, Integer.parseInt(((RadioButton) red.getSelectedToggle()).getText()));
        changeStudents.put(PawnType.GREEN, Integer.parseInt(((RadioButton) green.getSelectedToggle()).getText()));
        changeStudents.put(PawnType.YELLOW, Integer.parseInt(((RadioButton) yellow.getSelectedToggle()).getText()));
        changeStudents.put(PawnType.BLUE, Integer.parseInt(((RadioButton) blue.getSelectedToggle()).getText()));
        changeStudents.put(PawnType.PINK, Integer.parseInt(((RadioButton) pink.getSelectedToggle()).getText()));
        return changeStudents;
    }
}
