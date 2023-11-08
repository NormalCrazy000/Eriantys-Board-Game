package it.polimi.ingsw.network.client.cli;

import it.polimi.ingsw.model.CharacterCard;
import it.polimi.ingsw.network.client.ClientSocket;
import it.polimi.ingsw.network.client.cli.TurnPhases.ConnectionToServerPhase;
import it.polimi.ingsw.network.client.cli.TurnPhases.Phase;
import it.polimi.ingsw.network.client.cli.componentPrinter.EnemyPlayerBoardPrinter;
import it.polimi.ingsw.network.client.cli.componentPrinter.GameBoardPrinter;
import it.polimi.ingsw.network.client.cli.componentPrinter.PlayerBoardPrinter;
import it.polimi.ingsw.serializableModel.SerializableGameBoard;
import it.polimi.ingsw.serializableModel.SerializablePlayerBoard;
import it.polimi.ingsw.utils.Constants;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;

/**
 * Class responsible to coordinate the client execution if the cli interface is
 * selected
 */
public class CLI implements Runnable {

    private ClientSocket clientSocket;
    private Socket socket;
    private boolean isAckArrived;
    private boolean moveStudentsDone;
    private boolean moveMotherNature;
    private boolean getStudFromCloudDone;
    private boolean isActionDone;
    private int numberOfPlayer;
    private Phase gamePhase;
    private String nickname;
    private int motherMovement;
    private boolean characterCardPlayed;

    private CharacterCard cardPlayed;
    private int additionalMovement;

   //Player player;

    /**
     * This constructor is used to initialize CLI's variables
     */
    public CLI() {
        isAckArrived = false;
        isActionDone = false;
        characterCardPlayed = false;
        gamePhase = new ConnectionToServerPhase();
        new Thread(this).start();
    }

    /**
     * This method is used to print Game's tile
     */
    public void printTitle() {
        System.out.println(Constants.ERIANTYS);
        System.out.println(Constants.AUTHORS);
    }

    /**
     * Method clearScreen flushes terminal's screen.
     */
    public static void clearScreen() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else
                Runtime.getRuntime().exec("clear");
        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    /**
     * calls the methods doAction of the current game phase and pass itself to that
     */
    @Override
    public void run() {
        gamePhase.makeAction(this);
    }

    /**
     * Method setup called when a client instance has started. It asks player's nickname and tries to establish a
     * connection to the remote server through the socket interface. If the connection is active, displays a message on
     * the CLI.
     */
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
    /**
     * This method is used to set up game's phase
     *
     * @param gamePhase type {@link Phase}: game's phase
     */
    public void setGamePhase(Phase gamePhase) {
        this.gamePhase=gamePhase;
    }
    /**
     * This method returns game's phase
     *
     * @return type {@link Phase}: game's phase
     */
    public Phase getGamePhase(){
        return gamePhase;
    }


    /**
     * This method prints the {@link it.polimi.ingsw.model.GameBoard} using the {@link GameBoardPrinter} class
     */
    public void printGameBoard(){
        SerializableGameBoard gameBoard = clientSocket.getView().getGameBoard();
        GameBoardPrinter printer = new GameBoardPrinter();
        printer.printGameBoard(gameBoard);
    }
    /**
     * This method prints the {@link it.polimi.ingsw.model.PlayerBoard} using the {@link PlayerBoardPrinter} class
     */
    public void printPlayerBoard(){
        SerializablePlayerBoard playerBoard = clientSocket.getView().getMyPlayerBoard();
        PlayerBoardPrinter printer = new PlayerBoardPrinter();
        printer.print(playerBoard);
    }

    /**
     * This method prints the enemy player board using the {@link PlayerBoardPrinter} class
     */
    public void printEnemyPlayerBoard(){
        HashMap<String, SerializablePlayerBoard> enemyPlayerBoards = clientSocket.getView().getEnemyPlayerBoards();
        EnemyPlayerBoardPrinter printer = new EnemyPlayerBoardPrinter();
        printer.print(enemyPlayerBoards);
    }

    /**
     * This method is used to set Player's nickname on GUI
     *
     * @param nickname type {@link String}: Player's nickname
     */
    public void setNickname(String nickname){
        this.nickname=nickname;
    }
    /**
     * This method returns Player's nickname
     *
     * @return type {@link String}: Player's nickname
     */
    public String getNickname(){
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
     * This method is used to set up Mother's additional movement
     * @param additionalMovement type int: Mother's additional movement
     */
    public void setAdditionalMovement(int additionalMovement) {
        this.additionalMovement = additionalMovement;
    }
    /**
     * This method is used to get Mother's additional movement
     * @return type int: Mother's additional movement
     */
    public int getAdditionalMovement() {
        return additionalMovement;
    }
    /**
     * This method returns CharacterCard played
     * @return type {@link CharacterCard}: CharacterCard played
     */
    public CharacterCard getCardPlayed() {
        return cardPlayed;
    }

    /**
     * This method check if move student phase is played
     * @return type bool: true if move student phase is played, else false
     */
    public boolean isMoveStudentsDone() {
        return moveStudentsDone;
    }
    /**
     * This method check if move mother phase is played
     * @return type bool: true if move mother phase is played, else false
     */
    public boolean isMoveMotherNature() {
        return moveMotherNature;
    }
    /**
     * This method check if get student from cloud phase is played
     * @return type bool: true if get student from cloud phase is played, else false
     */
    public boolean isGetStudFromCloudDone() {
        return getStudFromCloudDone;
    }

    /**
     * This method manage move student phase
     * @param moveStudentsDone: true if move student phase is played, else false
     */
    public void setMoveStudentsDone(boolean moveStudentsDone) {
        this.moveStudentsDone = moveStudentsDone;
    }
    /**
     * This method sets CharacterCard played
     * @param cardPlayed type {@link CharacterCard}: CharacterCard played
     */
    public void setCardPlayed(CharacterCard cardPlayed) {
        this.cardPlayed = cardPlayed;
    }
    /**
     * This method manage move mother phase
     * @param moveMotherNature: true if move mother phase is played, else false
     */
    public void setMoveMotherNature(boolean moveMotherNature) {
        this.moveMotherNature = moveMotherNature;
    }
    /**
     * This method manage get student from cloud phase
     * @param getStudFromCloudDone: true if move mother phase is played, else false
     */
    public void setGetStudFromCloudDone(boolean getStudFromCloudDone) {
        this.getStudFromCloudDone = getStudFromCloudDone;
    }
    /**
     * This method manage character card phase
     * @param characterCardPlayed: true if character card phase is played, else false
     */
    public void setCharacterCardPlayed(boolean characterCardPlayed) {
        this.characterCardPlayed = characterCardPlayed;
    }
    /**
     * This method check if character card phase is played
     * @return type bool: true if character card phase is played, else false
     */
    public boolean isCharacterCardPlayed() {
        return characterCardPlayed;
    }
}
