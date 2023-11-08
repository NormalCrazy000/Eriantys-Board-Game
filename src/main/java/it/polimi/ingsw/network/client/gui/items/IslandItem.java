package it.polimi.ingsw.network.client.gui.items;


import it.polimi.ingsw.model.PawnType;
import it.polimi.ingsw.model.TowerColor;
import it.polimi.ingsw.network.client.gui.GUI;
import it.polimi.ingsw.network.client.gui.Phase;
import it.polimi.ingsw.network.messages.sentByClient.AmountOfMotherNatureMovementMessage;
import it.polimi.ingsw.network.messages.sentByClient.StudentToIslandMessage;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.HashMap;
import java.util.Map;


/**
 * This class represents Island card item
 */
public class IslandItem extends GridPane {
    int index;
    private AnchorPane anchorFather;
    private GUI gui;

    /**
     * This method creates the graphical item
     *
     * @param index        type int: island's index
     * @param anchorFather type {@link AnchorPane}: this represents the anchorPane father of the scene.
     * @param gui          type {@link GUI}: GUI object
     */
    public IslandItem(int index, AnchorPane anchorFather, GUI gui) {
        this.index = index;
        this.anchorFather = anchorFather;
        this.gui = gui;
        getStylesheets().add("/css/Island.css");
        getStyleClass().add("gridPaneIsland");
        //Add dragEventRelease on diningRoom
        //To manage move student into island
        setOnMouseDragReleased(event -> {
            //Check if move student is allowed
            if ((gui.getPhase().equals(Phase.MOVE1) ||gui.getPhase().equals(Phase.MOVE4) || gui.getPhase().equals(Phase.MOVE2) || gui.getPhase().equals(Phase.MOVE3)) && event.getGestureSource() instanceof StudentMoveItem) {
                StudentMoveItem student = (StudentMoveItem) event.getGestureSource();
                HashMap<PawnType, Integer> students = new HashMap<>();
                students.put(student.getColor(), 1);
                gui.setCanPlay(false);
                anchorFather.getChildren().remove(student);
                gui.getClientSocket().send(new StudentToIslandMessage("move to island", students, index));
                synchronized (gui.getController()) {
                    try {
                        gui.getController().wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                //Change phase
                switch (gui.getPhase()) {
                    case MOVE1:
                        gui.setPhase(Phase.MOVE2);
                        break;
                    case MOVE2:
                        gui.setPhase(Phase.MOVE3);
                        break;
                    case MOVE3:
                        if(gui.getClientSocket().getView().getGameBoard().getClouds().length== 3){
                            gui.setPhase(Phase.MOVE4);

                        }else{
                           gui.setPhase(Phase.MOTHER);
                        }
                        break;
                    case MOVE4:
                        gui.setPhase(Phase.MOTHER);
                        break;
                }
                gui.setCanPlay(true);
            }
            //Check if move mother is allowed
            if (gui.getClientSocket().getView().getGameBoard().getMotherIndex() != index && gui.getPhase().equals(Phase.MOTHER) && event.getGestureSource() instanceof MotherItem) {
                MotherItem mother = ((MotherItem) event.getGestureSource());
                gui.setCanPlay(false);
                int oldIndex = ((IslandItem) mother.getGrid()).getIndex();
                int movement = 0;
                //generate from movement to index
                while (oldIndex != index) {
                    movement++;
                    oldIndex++;
                    if (oldIndex == gui.getClientSocket().getView().getGameBoard().getRegions().size()) {
                        oldIndex = 0;
                    }
                }
                //Check if movement is less than movement on Assistant card
                if (movement <= gui.getMotherMovement() + gui.getAdditionalMovement()) {
                    anchorFather.getChildren().remove(mother);
                    gui.getClientSocket().send(new AmountOfMotherNatureMovementMessage("mother movement", movement));
                    synchronized (gui.getController()) {
                        try {
                            gui.getController().wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    gui.setPhase(Phase.CLOUD);
                    gui.setCanPlay(true);
                } else {
                    gui.setCanPlay(true);
                    mother.resetPosition();
                    mother.setMouseTransparent(false);
                    Alert alert = new Alert(Alert.AlertType.ERROR, "MotherItem error", ButtonType.OK);
                    alert.showAndWait();
                }
            }
            event.consume();
        });
    }

    /**
     * This method is used to update student on island
     *
     * @param students type {@link Map} with keys {@link PawnType} and values {@link  Integer}: students to add on island
     */
    public void updateStudents(Map<PawnType, Integer> students) {
        Platform.runLater(
                () -> {
                    //remove old items
                    getChildren().removeIf(e -> e instanceof StudentMoveItem);
                    int[] cord;
                    for (Map.Entry<PawnType, Integer> entry : students.entrySet()) {
                        cord = colAndRowWithStudentColor(entry.getKey());
                        for (int i = 0; i < entry.getValue(); i++) {
                            add(new StudentItem(entry.getKey(), this, cord[0], cord[1], anchorFather), cord[0], cord[1]);
                        }
                        if (entry.getValue() != 0) {
                            Label text = new Label(entry.getValue().toString());
                            text.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
                            add(text, cord[0], cord[1]);
                            GridPane.setHalignment(text, HPos.CENTER);
                        }
                    }
                }
        );
    }

    /**
     * This method is called to update number of towers on island
     *
     * @param numberOfTowers type int: number of towers
     * @param color          type {@link TowerColor}: tower's color
     */
    public void updateTowers(int numberOfTowers, TowerColor color) {
        Platform.runLater(() -> {
            getChildren().removeIf(e -> e instanceof TowerItem);
            for (int j = 0; j < numberOfTowers; j++) {
                add(new TowerItem(color, this, anchorFather), 0, 3);
            }
            if (numberOfTowers > 0) {
                Label text = new Label(String.valueOf(numberOfTowers));
                text.setFont(Font.font("Verdana", FontWeight.BOLD, 20));

                add(text, 0, 3);
                GridPane.setHalignment(text, HPos.CENTER);
            }
        });
    }

    /**
     * This method is called to update mother on island
     *
     * @param isMother type bool: true if mother is on island, else false
     */
    public void updateMothers(boolean isMother) {
        Platform.runLater(() -> {
            int i = (int) getChildren().stream().filter(e -> e instanceof MotherItem).count();
            if (i == 0 && isMother) {
                add(new MotherItem(this, 1, 2, anchorFather, gui), 1, 2);
            } else if (i == 1 && !isMother) {
                getChildren().removeIf(e -> e instanceof MotherItem);
            }
        });
    }

    /**
     * This method is called to update noEntry tiles on island
     *
     * @param numberOfTile type int: number of noEntry Tile
     */
    public void updateNoEntry(int numberOfTile) {
        Platform.runLater(() -> {
            //remove old items
            getChildren().removeIf(e -> e instanceof NoEntryTileItem);
            for (int i = 0; i < numberOfTile; i++) {
                add(new NoEntryTileItem(this, 1, 3, anchorFather), 1, 3);
            }
            if (numberOfTile > 0) {
                Label text = new Label(String.valueOf(numberOfTile));
                text.setFont(Font.font("Verdana", FontWeight.BOLD, 20));

                add(text, 1, 3);
                GridPane.setHalignment(text, HPos.CENTER);
            }
        });
    }

    /**
     * This methdo is used to return col and row according to student's color
     *
     * @param color type {@link PawnType}: student's color
     * @return type int[]: array[0] is col and array[1] is row
     */
    private int[] colAndRowWithStudentColor(PawnType color) {
        int row = 0;
        int column = 0;
        switch (color) {
            case YELLOW:
                break;
            case RED:
                column = 1;
                break;
            case GREEN:
                row = 1;
                break;
            case BLUE:
                row = 1;
                column = 1;
                break;
            case PINK:
                row = 2;
                break;
        }
        return new int[]{column, row};
    }

    /**
     * This methods returns island's index
     *
     * @return type int: island's index
     */
    public int getIndex() {
        return index;
    }
}
