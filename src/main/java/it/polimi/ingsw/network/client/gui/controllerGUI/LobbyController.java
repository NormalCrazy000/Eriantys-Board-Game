package it.polimi.ingsw.network.client.gui.controllerGUI;

import it.polimi.ingsw.model.CharacterCard;
import it.polimi.ingsw.model.GameMode;
import it.polimi.ingsw.model.PawnType;
import it.polimi.ingsw.network.client.gui.GUI;
import it.polimi.ingsw.network.client.gui.Phase;
import it.polimi.ingsw.network.client.gui.items.CharacterCardItem;
import it.polimi.ingsw.network.client.gui.items.MotherItem;
import it.polimi.ingsw.network.client.gui.items.StudentMoveItem;
import it.polimi.ingsw.network.client.gui.update.Update;
import it.polimi.ingsw.network.messages.sentByClient.StudentToDiningRoomMessage;
import it.polimi.ingsw.serializableModel.SerializableGameBoard;
import it.polimi.ingsw.serializableModel.SerializablePlayerBoard;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.HashMap;


/**
 * This class represents the controller to manage phases of the game
 */
public class LobbyController extends Controller {
    @FXML
    private AnchorPane anchorFather, anchorGroupRegions, anchorCoinsPlayer, anchorAssistantCardPlayed, anchorCharacterCards;
    @FXML
    private GridPane anchorProfessors, anchorTowers, anchorDining, anchorEntrance, gridPaneClouds;
    @FXML
    private Text textBagStudent, textGamemoney;

    /**
     * This method is used to set up the Lobby scene.
     *
     * @param gui type {@link GUI}: GUI object
     */
    @Override
    public void setup(GUI gui) {
        setGui(gui);
        //Update playerBoard and gameBoard items
        updatePlayerBoard(getGui().getClientSocket().getView().getMyPlayerBoard());
        updateGameBoard(getGui().getClientSocket().getView().getGameBoard());

        //Add dragEventRelease on diningRoom
        //To manage move student into diningRoom
        anchorDining.setOnMouseDragReleased(event -> {
            //Check if move is allowed
            if ((gui.getClientSocket().getView().getMyPlayerBoard().getDiningRoom().get(((StudentMoveItem) event.getGestureSource()).getColor())) < 10 && (gui.getPhase().equals(Phase.MOVE1)||gui.getPhase().equals(Phase.MOVE4) || gui.getPhase().equals(Phase.MOVE2) || gui.getPhase().equals(Phase.MOVE3)) && event.getGestureSource() instanceof StudentMoveItem) {
                StudentMoveItem student = (StudentMoveItem) event.getGestureSource();
                HashMap<PawnType, Integer> students = new HashMap<>();
                students.put(student.getColor(), 1);
                gui.setCanPlay(false);
                anchorFather.getChildren().remove(student);
                //Send student to server
                getGui().getClientSocket().send(new StudentToDiningRoomMessage("Move to DiningRoom", students));
                synchronized (getGui().getController()) {
                    try {
                        getGui().getController().wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                switch (getGui().getPhase()) {
                    case MOVE1:
                        getGui().setPhase(Phase.MOVE2);
                        break;
                    case MOVE2:
                        getGui().setPhase(Phase.MOVE3);
                        break;
                    case MOVE3:
                        if(gui.getClientSocket().getView().getGameBoard().getClouds().length== 3){
                            getGui().setPhase(Phase.MOVE4);

                        }else{
                            getGui().setPhase(Phase.MOTHER);
                        }
                        break;
                    case MOVE4:
                        getGui().setPhase(Phase.MOTHER);
                        break;
                }
                gui.setCanPlay(true);
            }
            event.consume();
        });
        //Add dragEventRelease on father
        //To manage error move: when move is not allowed items must be reset
        anchorFather.setOnMouseDragReleased(event -> {
            if (event.getGestureSource() instanceof StudentMoveItem) {
                StudentMoveItem student = ((StudentMoveItem) event.getGestureSource());
                student.setMouseTransparent(false);
                student.resetPosition();
            }
            if (event.getGestureSource() instanceof MotherItem) {
                MotherItem mother = ((MotherItem) event.getGestureSource());
                mother.setMouseTransparent(false);
                mother.resetPosition();
            }
            event.consume();
        });
    }

    /**
     * This method is called to update playerBord
     *
     * @param playerBoard type {@link SerializablePlayerBoard}: SerializablePlayerBoard object
     */
    public void updatePlayerBoard(SerializablePlayerBoard playerBoard) {
        Platform.runLater(
                () -> {
                    Update.updateEntrance(playerBoard, anchorFather, anchorEntrance, true, getGui());
                    Update.updateDiningRoom(playerBoard, anchorFather, anchorDining);
                    Update.updateProfessors(playerBoard, anchorFather, anchorProfessors);
                    Update.updateTowers(playerBoard, anchorFather, anchorTowers);
                    Update.updateMoneyPlayer(playerBoard, anchorCoinsPlayer);
                    Update.updatePlayerAssistant(anchorAssistantCardPlayed, playerBoard);

                }
        );
    }

    /**
     * This method insert characterChardItem on GameBoard
     *
     * @param characterCards type {@link CharacterCard[]}
     */
    public void setCharacterCard(CharacterCard[] characterCards) {
        Platform.runLater(
                () -> {
                    int i = 0;
                    anchorCharacterCards.getChildren().removeAll();
                    for (Node node : anchorCharacterCards.getChildren()) {
                        ((StackPane) node).getChildren().add(new CharacterCardItem(characterCards[i], getGui()));
                        Text text = new Text("Cost money: " + characterCards[i].getCost());
                        ((StackPane) node).getChildren().add(text);
                        StackPane.setAlignment(text, Pos.CENTER);
                        i++;
                    }
                }
        );
    }

    /**
     * This method is called to update gameBoard
     *
     * @param gameBoard type {@link SerializableGameBoard}: SerializableGameBoard object
     */
    public void updateGameBoard(SerializableGameBoard gameBoard) {
        Platform.runLater(
                () -> {
                    Update.updateIslands(gameBoard, anchorGroupRegions, anchorFather, getGui());
                    Update.updateClouds(gameBoard, gridPaneClouds, anchorFather, getGui());
                    if (getGui().getClientSocket().getView().getGameBoard().getCharacterDeck() != null) {
                        setCharacterCard(getGui().getClientSocket().getView().getGameBoard().getCharacterDeck().getDeckList());
                    }
                    updateMoney(gameBoard);
                    updateBag(gameBoard);
                }
        );
    }

    /**
     * This method set value of Game's money
     *
     * @param gameBoard type {@link SerializableGameBoard}: SerializableGameBoard object
     */
    public void updateMoney(SerializableGameBoard gameBoard) {
        textGamemoney.setText(String.valueOf(gameBoard.getCoins()));
    }

    /**
     * This method set value of Bag's Student
     *
     * @param gameBoard type {@link SerializableGameBoard}: SerializableGameBoard object
     */
    public void updateBag(SerializableGameBoard gameBoard) {
        textBagStudent.setText("red: " + gameBoard.getStudentsBag().get(PawnType.RED) + "--" +
                "yellow: " + gameBoard.getStudentsBag().get(PawnType.YELLOW) + "--" +
                "blue: " + gameBoard.getStudentsBag().get(PawnType.BLUE) + "--" +
                "pink: " + gameBoard.getStudentsBag().get(PawnType.PINK) + "--" +
                "green: " + gameBoard.getStudentsBag().get(PawnType.GREEN)
        );
    }

    /**
     * This method is called to open Assistant scene
     */
    public void chooseAssistant() {
        //check if action is allowed
        if (getGui().getAssistantController() == null && getGui().getPhase().equals(Phase.ASSISTANT)) {
            Stage stage = new Stage();
            stage.setScene(getGui().createScene("/fxml/Assistant.fxml"));
            //Add the function that will be called when the scene is closed
            stage.setOnCloseRequest(windowEvent -> {
                getGui().setAssistantController(null);
            });
            stage.sizeToScene();
            stage.show();
        }
    }

    /**
     * This method is called to open Other scene
     */
    public void otherPlayer() {
        if (getGui().getOtherController() == null) {
            Stage stage = new Stage();

            stage.setScene(getGui().createScene("/fxml/Other.fxml"));
            //Add the function that will be called when the scene is closed
            stage.setOnCloseRequest(windowEvent -> {
                getGui().setOtherController(null);
            });
            stage.sizeToScene();
            stage.show();
        }

    }


}
