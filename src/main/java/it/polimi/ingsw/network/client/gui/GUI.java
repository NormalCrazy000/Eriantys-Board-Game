package it.polimi.ingsw.network.client.gui;

import it.polimi.ingsw.model.CharacterCard;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.network.client.ClientSocket;
import it.polimi.ingsw.network.client.gui.controllerGUI.AssistantController;
import it.polimi.ingsw.network.client.gui.controllerGUI.Character.ControllerCharacter;
import it.polimi.ingsw.network.client.gui.controllerGUI.Controller;
import it.polimi.ingsw.network.client.gui.controllerGUI.OtherPlayerController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.Socket;

/**
 * This class represents is used to manage the GUI
 */
public class GUI extends Application {
    private ClientSocket clientSocket;
    private Socket socket;
    private boolean isAckArrived;
    private boolean isNackArrived;
    private int numberOfPlayer;
    private String nickname;
    private Stage stage;
    private Controller controller;
    Player player;
    private String messageFromServer;
    private boolean isActionDone;
    private int motherMovement;
    private int additionalMovement;
    private AssistantController assistantController;
    private OtherPlayerController otherController;
    private ControllerCharacter characterController;
    private Phase phase;
    private boolean canPlay;
    private boolean canPlayCharacter;
    private boolean error;
    private String winner;
    private CharacterCard cardPlayed;

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * This method is called when GUI start and it set GUI setup
     *
     * @param stage type{@link Stage}: initial stage
     */
    @Override
    public void start(Stage stage) {
        Parent root;
        FXMLLoader loader;
        isAckArrived = false;
        isNackArrived = false;
        canPlay = true;
        canPlayCharacter = true;
        error = false;
        try {
            loader = new FXMLLoader(getClass().getResource("/fxml/Connection.fxml"));
            root = loader.load();
            Controller newController = loader.getController();
            newController.setGui(this);
            controller = newController;
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        Scene sc = new Scene(root);
        this.stage = stage;
        stage.setScene(sc);
        stage.sizeToScene();
        stage.setOnCloseRequest(windowEvent -> {
            System.exit(0);
        });
        stage.show();
    }

    public void setup() {

    }

    /**
     * This method set socket
     *
     * @param socket type {@link Socket}: socket object
     */
    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    /**
     * This method returns socket object
     *
     * @return type {@link Socket}: socket object
     */
    public Socket getSocket() {
        return socket;
    }

    /**
     * This method set Client socket
     *
     * @param clientSocket type {@link ClientSocket}: Client socket object
     */
    public void setClientSocket(ClientSocket clientSocket) {
        this.clientSocket = clientSocket;
    }

    /**
     * This method returns Client socket
     *
     * @return type {@link ClientSocket}: Client socket object
     */
    public ClientSocket getClientSocket() {
        return clientSocket;
    }

    /**
     * This method is used to handle the reception of the ack: True if ACK is arrived, else false
     *
     * @param value type bool: value
     */
    public void setIsAckArrived(boolean value) {
        isAckArrived = value;
    }

    /* public void setGamePhase(Phase gamePhase) {
         this.gamePhase=gamePhase;
     }
     public Phase getGamePhase(){
         return gamePhase;
     }
 */


    /**
     * This method is used to set Player's nickname on GUI
     *
     * @param nickname type {@link String}: Player's nickname
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * This method returns Player's nickname
     *
     * @return type {@link String}: Player's nickname
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * This method returns true if ACk is arrived, else false
     *
     * @return type bool: value
     */
    public boolean isAckArrived() {
        return isAckArrived;
    }


    /**
     * This method returns Scene's controller
     *
     * @return type {@link Controller}: Controller object
     */
    public Controller getController() {
        return controller;
    }

    /**
     * This method sets Scene's controller
     *
     * @param controller type {@link Controller}
     */
    public void setController(Controller controller) {
        this.controller = controller;
    }

    /**
     * This method is used to switch Game's scene
     *
     * @param newPath type {@link String}: scene's path
     */
    public void switchScene(String newPath) {
        stage.setScene(createScene(newPath));
        stage.setOnCloseRequest(windowEvent -> {
            System.exit(0);
        });
        stage.sizeToScene();
        stage.show();
    }

    /**
     * This method is used to create scene by fxml path
     *
     * @param newPath type {@link String}: fxml scene path
     * @return type {@link Scene}: scene object
     */
    public Scene createScene(String newPath) {
        Parent root;
        FXMLLoader loader;
        try {
            loader = new FXMLLoader(getClass().getResource(newPath));
            ///Pay Attention
            root = loader.load();
            //Set controller and GUI
            if (loader.getController() instanceof AssistantController) {
                assistantController = loader.getController();
                assistantController.setup(this);
            } else if (loader.getController() instanceof OtherPlayerController) {
                otherController = loader.getController();
                otherController.setup(this);
            } else if (loader.getController() instanceof ControllerCharacter) {
                characterController = (ControllerCharacter) loader.getController();
                characterController.setup(this);
            } else {
                controller = loader.getController();
                controller.setup(this);

            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return new Scene(root);
    }

    /**
     * This method returns message that it arrived from server
     *
     * @return type {@link String}: server message
     */
    public String getMessageFromServer() {
        return messageFromServer;
    }

    /**
     * This method is used to set server message
     *
     * @param messageFromServer type {@link String}: server message
     */
    public void setMessageFromServer(String messageFromServer) {
        this.messageFromServer = messageFromServer;
    }


    /**
     * This method is used to check if actionPhase is played
     *
     * @return type bool: true if actionPhase is played, else false
     */
    public boolean isActionDone() {
        return isActionDone;
    }

    /**
     * This method is used to manage actionPhase
     *
     * @param actionDone type bool:  true if actionPhase is played, else false
     */
    public void setActionDone(boolean actionDone) {
        isActionDone = actionDone;
    }

    /**
     * This method returns mother's movement in relation to Assistantcard played
     *
     * @return type int: mother's movement
     */
    public int getMotherMovement() {
        return motherMovement;
    }

    /**
     * This method is used to set up mother's movement in relation to Assistantcard played
     *
     * @param motherMovement type int: mother's movement
     */
    public void setMotherMovement(int motherMovement) {
        this.motherMovement = motherMovement;
    }

    /**
     * This method returns game's phase
     *
     * @return type {@link Phase}: game's phase
     */
    public Phase getPhase() {
        return phase;
    }

    /**
     * This method is used to set up game's phase
     *
     * @param phase type {@link Phase}: game's phase
     */
    public void setPhase(Phase phase) {
        this.phase = phase;
    }

    /**
     * This method returns controller associated to the scene that manages the display of other players
     *
     * @return type {@link OtherPlayerController}: OtherPlayerController object
     */
    public OtherPlayerController getOtherController() {
        return otherController;
    }

    /**
     * This method set controller associated to the scene that manages the display of other players
     *
     * @param otherController type {@link OtherPlayerController}: OtherPlayerController object
     */
    public void setOtherController(OtherPlayerController otherController) {
        this.otherController = otherController;
    }

    /**
     * This method set controller associated to the scene that manages the display of Player's assistantCard
     *
     * @param assistantController type {@link AssistantController}: AssistantController object
     */
    public void setAssistantController(AssistantController assistantController) {
        this.assistantController = assistantController;
    }

    /**
     * This method returns controller associated to the scene that manages the display of Player's assistantCard
     *
     * @return type {@link AssistantController}: AssistantController object
     */
    public AssistantController getAssistantController() {
        return assistantController;
    }

    /**
     * This method checks if game's action is permitted
     *
     * @return type bool: true if action is permitted, else false
     */
    public boolean isCanPlay() {
        return canPlay;
    }

    /**
     * This method is used to manage game's action
     *
     * @param canPlay type bool: true if action is permitted, else false
     */
    public void setCanPlay(boolean canPlay) {
        this.canPlay = canPlay;
    }

    /**
     * This method checks if game's action made a mistake
     *
     * @return type bool: true if game's action made a mistake, else false
     */
    public boolean isError() {
        return error;
    }

    /**
     * This method is used to manage game's action mistake
     *
     * @param error type bool: true if game's action made a mistake, else false
     */
    public void setError(boolean error) {
        this.error = error;
    }

    /**
     * This method returns Game's winner
     *
     * @return type {@link String}: winner nickname
     */
    public String getWinner() {
        return winner;
    }

    /**
     * this method is used to set game's winner
     *
     * @param winner type {@link String}: winner nickname
     */
    public void setWinner(String winner) {
        this.winner = winner;
    }

    /**
     * This method is used to check if characterCard can be played
     *
     * @return type bool: true if card can be played, else false
     */
    public boolean isCanPlayCharacter() {
        return canPlayCharacter;
    }

    /**
     * This method is used to manage card played
     *
     * @param canPlayCharacter type bool: true if card can be played, else false
     */
    public void setCanPlayCharacter(boolean canPlayCharacter) {
        this.canPlayCharacter = canPlayCharacter;
    }

    /**
     * This method is used to get Mother's additional movement
     *
     * @return type int: Mother's additional movement
     */
    public int getAdditionalMovement() {
        return additionalMovement;
    }

    /**
     * This method is used to set up Mother's additional movement
     *
     * @param additionalMovement type int: Mother's additional movement
     */
    public void setAdditionalMovement(int additionalMovement) {
        this.additionalMovement = additionalMovement;
    }

    /**
     * This method returns CharacterCard played
     *
     * @return type {@link CharacterCard}: CharacterCard played
     */
    public CharacterCard getCardPlayed() {
        return cardPlayed;
    }

    /**
     * This method sets CharacterCard played
     *
     * @param cardPlayed type {@link CharacterCard}: CharacterCard played
     */
    public void setCardPlayed(CharacterCard cardPlayed) {
        this.cardPlayed = cardPlayed;
    }
}
