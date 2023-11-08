package it.polimi.ingsw.network.client.gui.update;

import it.polimi.ingsw.model.PawnType;
import it.polimi.ingsw.network.client.gui.GUI;
import it.polimi.ingsw.network.client.gui.GeneratorPositionRegions;
import it.polimi.ingsw.network.client.gui.items.*;
import it.polimi.ingsw.serializableModel.SerializableGameBoard;
import it.polimi.ingsw.serializableModel.SerializablePlayerBoard;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.Map;

/**
 * This class is used to update graphics item into Game
 */
public class Update {
    /**
     * This methods return dining room col according to color
     *
     * @param color type {@link PawnType}: student's color
     * @return type int: dining room's col
     */
    public static int colDiningRoomWithColor(PawnType color) {
        int column = 0;
        switch (color) {
            case YELLOW:
                column = 2;
                break;
            case RED:
                column = 3;
                break;
            case GREEN:
                column = 4;
                break;
            case BLUE:
                break;
            case PINK:
                column = 1;
                break;
        }
        return column;
    }

    /**
     * This method is used to update number of island and item on it
     *
     * @param gameBoard          type {@link SerializableGameBoard}: SerializableGameBoard object
     * @param anchorGroupRegions type {@link AnchorPane}: anchorPane when island will be placed
     * @param anchorFather       type {@link AnchorPane}: this represents the anchorPane father of the scene.
     * @param gui                type {@link GUI}: GUI object
     */
    public static void updateIslands(SerializableGameBoard gameBoard, AnchorPane anchorGroupRegions, AnchorPane anchorFather, GUI gui) {
        Platform.runLater(
                () -> {
                    int numberOfRegions = gameBoard.getRegions().size();
                    //-1 because there is cloud pane on anchorGroupRegions
                    if (numberOfRegions != (anchorGroupRegions.getChildren().size()) - 1) {
                        //Remove island
                        anchorGroupRegions.getChildren().removeIf(n -> n instanceof IslandItem);
                        //Generate posizion
                        GeneratorPositionRegions complex = new GeneratorPositionRegions();
                        Map<Integer, ArrayList<Double>> result = complex.complexSquare(gameBoard.getRegions().size());
                        //Add new island
                        result.forEach((key, value) -> {
                            anchorGroupRegions.getChildren().add(new IslandItem(key, anchorFather, gui));
                            anchorGroupRegions.getChildren().get(key + 1).setTranslateX(value.get(0) * 450 + anchorGroupRegions.getPrefWidth() / 2 - 50);
                            anchorGroupRegions.getChildren().get(key + 1).setTranslateY(value.get(1) * 350 + anchorGroupRegions.getPrefHeight() / 2 - 50);
                        });
                    }
                    //Update every islands
                    //<=!!!There is pane to clouds on anchorGroupRegions!!!!
                    for (int i = 1; i <= numberOfRegions; i++) {
                        ((IslandItem) anchorGroupRegions.getChildren().get(i)).getChildren().removeIf(e -> e instanceof Label);
                        ((IslandItem) anchorGroupRegions.getChildren().get(i)).updateStudents(gameBoard.getRegions().get(i - 1).getAllStudentsOrder());
                        ((IslandItem) anchorGroupRegions.getChildren().get(i)).updateTowers(gameBoard.getRegions().get(i - 1).numberOfTowers(), gameBoard.getRegions().get(i - 1).getColorTower());
                        ((IslandItem) anchorGroupRegions.getChildren().get(i)).updateMothers(gameBoard.getRegions().get(i - 1).isMother());
                        ((IslandItem) anchorGroupRegions.getChildren().get(i)).updateNoEntry(gameBoard.getRegions().get(i - 1).getNoEntry());
                    }
                }
        );
    }

    /**
     * This method is used to update number of cloud and item on it
     *
     * @param gameBoard      type {@link SerializableGameBoard}: SerializableGameBoard object
     * @param gridPaneClouds type {@link AnchorPane}: anchorPane when cloud will be placed
     * @param anchorFather   type {@link AnchorPane}: this represents the anchorPane father of the scene.
     * @param gui            type {@link GUI}: GUI object
     */
    public static void updateClouds(SerializableGameBoard gameBoard, GridPane gridPaneClouds, AnchorPane anchorFather, GUI gui) {
        //Check if clouds aren't on grid pane and if it's true create them
        if (gridPaneClouds.getChildren().size() == 0) {
            int row = 0;
            int col = 0;
            for (int i = 0; i < gameBoard.getClouds().length; i++) {
                gridPaneClouds.add(new CloudItem(i, anchorFather, gui), col, row);
                col++;
                if (col == 2) {
                    col = 0;
                    row++;
                }
            }

        }
        //Update every clouds
        for (int i = 0; i < gameBoard.getClouds().length; i++) {
            ((CloudItem) gridPaneClouds.getChildren().get(i)).updateStudents(gameBoard.getClouds()[i].getAllStudents());
        }
    }

    /**
     * This method is used to update student's entrance
     *
     * @param playerBoard    type {@link SerializablePlayerBoard}: SerializablePlayerBoard object
     * @param anchorFather   type {@link AnchorPane}: this represents the anchorPane father of the scene.
     * @param anchorEntrance type {@link AnchorPane}: anchorPane when students will be placed
     * @param move           type bool: if it is true student can be moved, else student cannot be moved
     * @param gui            type {@link GUI}: GUI object
     */
    public static void updateEntrance(SerializablePlayerBoard playerBoard, AnchorPane anchorFather, GridPane anchorEntrance, boolean move, GUI gui) {
        Platform.runLater(
                () -> {
                    int row = 0;
                    int col = 0;
                    //Remove Student
                    if (move) {
                        anchorEntrance.getChildren().removeIf(n -> n instanceof StudentMoveItem);
                    } else {
                        anchorEntrance.getChildren().removeIf(n -> n instanceof StudentItem);
                    }
                    for (Map.Entry<PawnType, Integer> entry : playerBoard.getEntrance().entrySet()) {
                        for (int i = 0; i < entry.getValue(); i++) {
                            //Check if student can be moved
                            if (move) {
                                anchorEntrance.add(new StudentMoveItem(entry.getKey(), anchorEntrance, col, row, anchorFather, gui), col, row);
                            } else {
                                anchorEntrance.add(new StudentItem(entry.getKey(), anchorEntrance, col, row, anchorFather), col, row);
                            }
                            col++;
                            if (col == 5) {
                                col = 0;
                                row++;
                            }
                        }
                    }
                }
        );
    }

    /**
     * This method is used to update student's diningRoom
     *
     * @param playerBoard  type {@link SerializablePlayerBoard}: SerializablePlayerBoard object
     * @param anchorFather type {@link AnchorPane}: this represents the anchorPane father of the scene.
     * @param anchorDining type {@link AnchorPane}: anchorPane when students will be placed
     */
    public static void updateDiningRoom(SerializablePlayerBoard playerBoard, AnchorPane anchorFather, GridPane anchorDining) {
        Platform.runLater(
                () -> {
                    //Remove students
                    anchorDining.getChildren().removeIf(n -> n instanceof StudentItem);
                    int col;
                    for (Map.Entry<PawnType, Integer> entry : playerBoard.getDiningRoom().entrySet()) {
                        col = colDiningRoomWithColor(entry.getKey());
                        for (int i = 0; i < entry.getValue(); i++) {
                            anchorDining.add(new StudentItem(entry.getKey(), anchorDining, col, i, anchorFather), col, i);
                        }
                    }

                }
        );
    }

    /**
     * This method is used to update professors
     *
     * @param playerBoard      type {@link SerializablePlayerBoard}: SerializablePlayerBoard object
     * @param anchorFather     type {@link AnchorPane}: this represents the anchorPane father of the scene.
     * @param anchorProfessors type {@link AnchorPane}: anchorPane when professors will be placed
     */
    public static void updateProfessors(SerializablePlayerBoard playerBoard, AnchorPane anchorFather, GridPane anchorProfessors) {
        Platform.runLater(
                () -> {
                    //Remove professors
                    anchorProfessors.getChildren().removeIf(n -> n instanceof ProfessorItem);
                    int col;
                    for (Map.Entry<PawnType, Boolean> entry : playerBoard.getProfessors().entrySet()) {
                        col = colDiningRoomWithColor(entry.getKey());
                        if (entry.getValue())
                            anchorProfessors.add(new ProfessorItem(entry.getKey(), anchorProfessors, anchorFather), col, 0);
                    }
                }
        );
    }

    /**
     * This method is used to update towers
     *
     * @param playerBoard  type {@link SerializablePlayerBoard}: SerializablePlayerBoard object
     * @param anchorFather type {@link AnchorPane}: this represents the anchorPane father of the scene.
     * @param anchorTowers type {@link AnchorPane}: anchorPane when towers will be placed
     */
    public static void updateTowers(SerializablePlayerBoard playerBoard, AnchorPane anchorFather, GridPane anchorTowers) {
        Platform.runLater(
                () -> {
                    //Remove towers
                    anchorTowers.getChildren().removeIf(n -> n instanceof TowerItem);
                    int row = 0;
                    int col = 0;
                    for (int i = 0; i < playerBoard.getTowers(); i++) {
                        anchorTowers.add(new TowerItem(playerBoard.getTowerColor(), anchorTowers, anchorFather), col, row);

                        col++;
                        if (col == 5) {
                            col = 0;
                            row++;
                        }
                    }
                }
        );
    }

    /**
     * This method is used to update player's money
     *
     * @param playerBoard       type {@link SerializablePlayerBoard}: SerializablePlayerBoard object
     * @param anchorCoinsPlayer type {@link AnchorPane}: anchorPane when money will be placed
     */
    public static void updateMoneyPlayer(SerializablePlayerBoard playerBoard, AnchorPane anchorCoinsPlayer) {
        Platform.runLater(
                () -> ((Text) anchorCoinsPlayer.getChildren().get(0)).setText(String.valueOf(playerBoard.getCoins()))
        );
    }

    /**
     * This method is used to update player's assistantCard played
     *
     * @param chosen_character type {@link AnchorPane}: anchorPane when Assistant will be placed
     * @param playerBoard      type {@link SerializablePlayerBoard}: SerializablePlayerBoard object
     */
    public static void updatePlayerAssistant(AnchorPane chosen_character, SerializablePlayerBoard playerBoard) {
        Platform.runLater(
                () -> {
                    chosen_character.getChildren().removeAll();
                    if (playerBoard.getAssistantCard() != null) {
                        chosen_character.getChildren().add(new AssistantCardItem(playerBoard.getAssistantCard().getValue(), 0));

                    }
                }
        );
    }


}
