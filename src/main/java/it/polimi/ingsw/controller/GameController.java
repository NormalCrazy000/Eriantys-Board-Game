package it.polimi.ingsw.controller;


import it.polimi.ingsw.model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * This class represents gameController that manage game
 * @author Christian Lisi
 */
public class GameController {

    private int playerNumbers;
    private Game game;
    String currentPlayer;
    ArrayList<TowerColor> availableColor = new ArrayList<>();
    ArrayList<MageType> availableMageTypes = new ArrayList<>();
    ArrayList<Integer> cardSelectedByPlayers = new ArrayList<>();

    /**
     * This constructor is used to set up gameController
     */
    public GameController() {
        playerNumbers = 0;
        currentPlayer = null;
        game = null;
        availableColor.add(TowerColor.WHITE);
        availableColor.add(TowerColor.BLACK);
        availableColor.add(TowerColor.GREY);

        availableMageTypes.add(MageType.ELDER_MAGICIAN);
        availableMageTypes.add(MageType.ORIENTAL_MAGICIAN);
        availableMageTypes.add(MageType.KING_MAGICIAN);
        availableMageTypes.add(MageType.WITCH_MAGICIAN);
    }

    /**
     * This method is used to create game
     *
     * @param playerNumbers type int:  number of players
     * @param gamemode      type {@link GameMode}: game mode
     */
    public void createGame(int playerNumbers, GameMode gamemode) {
        game = new Game(gamemode, playerNumbers);
        this.playerNumbers = playerNumbers;
    }

    /**
     * This method is used to register player
     *
     * @param nickname type {@link String}: the nickname of the player
     */
    public void registration(String nickname) {
        game.addNewPlayerIntoGame(new Player(nickname));
    }

    /**
     * This method is used to set up playerboards
     */
    public void setupPlayerBoard() {
        int numberOfTower, numberOfStudents;

        if (game.getPlayers().size() == 2) {
            numberOfTower = 8;
            numberOfStudents = 7;
        } else {
            numberOfTower = 6;
            numberOfStudents = 9;
        }

        if (game.getGameMode() == GameMode.EXPERT) {
            for (Player player : game.getPlayers()) {
                player.getPlayerboard().setNumberOfTower(numberOfTower);
                player.getPlayerboard().addStudentsToEntrance(game.getGameBoard().getStudentsFromBag(numberOfStudents));
                player.getPlayerboard().addCoins(1);
                game.getGameBoard().removeCoins(1);
                //setup tower color
                player.getPlayerboard().setTowerColor(availableColor.get(0));
                availableColor.remove(0);
                //setup mage type
                player.selectAssistantDeckType(availableMageTypes.get(0));
                availableMageTypes.remove(0);
            }
        } else {
            for (Player player : game.getPlayers()) {
                player.getPlayerboard().setNumberOfTower(numberOfTower);
                player.getPlayerboard().addStudentsToEntrance(game.getGameBoard().getStudentsFromBag(numberOfStudents));
                //setup tower color
                player.getPlayerboard().setTowerColor(availableColor.get(0));
                availableColor.remove(0);
                //setup mage type
                player.selectAssistantDeckType(availableMageTypes.get(0));
                availableMageTypes.remove(0);
            }
        }

    }

    /**
     * This method is used to populate cloud tile
     */
    public void populateCloudTile() {

        if (game.getGameBoard().checkNumberOfStudentsOnBag() == 0) {
            game.setLastTurn(true);
        }

        for (int i = 0; i < game.getGameBoard().checkNumberOfClouds(); i++) {
            game.getGameBoard().getCloud(i).addStudents(game.getGameBoard().getStudentsFromBag(game.getGameBoard().checkNumberOfClouds() + 1));
        }
    }

    /**
     * This method is used to select assistantCard on player's deck
     *
     * @param nickname type {@link String}: player's nickname
     * @param card     type {@link AssistantCard}: card selected
     */
    public void selectAssistantCard(String nickname, AssistantCard card) {
        game.getPlayerByNickname(nickname).selectCardFromAssistantDeck(card.getValue());
        cardSelectedByPlayers.add(card.getValue());

        if (game.getPlayerByNickname(nickname).getAssistantDeck().getAvailableDeckList().size() == 0) {
            setLastTurn();
        }
    }

    /**
     * This method is used to order players turn
     */
    public void orderTurnPlayer() {
        System.out.println("> Change turn order");
        ArrayList<Player> newTurnOrder = new ArrayList<>();
        Map<Player, Integer> playerCardAssociation = new HashMap<>();

        for (Player player : game.getPlayers()) {
            playerCardAssociation.put(player, player.getCardPlayed().getValue());
        }

        playerCardAssociation
                .entrySet()
                .stream()
                .sorted(Map.Entry.<Player, Integer>comparingByValue())
                .forEach(e ->
                        newTurnOrder.add(e.getKey()));

        newTurnOrder.forEach(e -> System.out.println(e.getNickname()));

        game.setPlayers(newTurnOrder);
    }

    /**
     * This method is used to move student to dining room
     *
     * @param stud Map with key {@link PawnType} and value {@link Integer}: students to move
     */
    public void moveStudentToDiningRoom(HashMap<PawnType, Integer> stud) {
        game.getCurrentPlayer().getPlayerboard().addStudentToDiningRoomAndRemoveStudentsFromEntrance(stud, game.getGameMode(), game.getGameBoard());
        //Check if professors must be reassigned
        game.assignProfessor();
    }

    /**
     * This method is used to move student to island room
     *
     * @param islandIndex type int: island's index
     * @param stud        Map with key {@link PawnType} and value {@link Integer}: students to move
     */
    public void moveStudentToIsland(int islandIndex, HashMap<PawnType, Integer> stud) {
        game.getGameBoard().getRegions().get(islandIndex).addStudents(stud);
        game.getCurrentPlayer().getPlayerboard().removeStudentFromEntrance(stud);

    }

    /**
     * This method is used to move mother nature
     *
     * @param movement type int: mother nature's movement
     */
    public void moveMotherNature(int movement) {
        game.getGameBoard().moveMotherNature(movement);
        game.checkTowerInfluence(game.getGameBoard().getRegion(game.getGameBoard().getIndexMother()));
        game.getGameBoard().checkIslandAggregation();
    }

    /**
     * This method is used to get students from cloud
     *
     * @param cloudIndex type int: cloud's index
     */
    public void getStudFromCloud(int cloudIndex) {
        int checkNoStud = 0;
        // check if clouds have all the students required for moving
        for (CloudTile cloud : game.getGameBoard().getClouds()) {
            if (cloud.getAllStudents().values().stream().mapToInt(Integer::intValue).sum() < game.getGameBoard().getClouds().length + 1) {
                checkNoStud++;
            }
        }
        //if ok , do the move
        if (checkNoStud < getGame().getGameBoard().getClouds().length) {
            game.getCurrentPlayer().getPlayerboard().addStudentsToEntrance(
                    game.getGameBoard().getCloud(cloudIndex).removeStudentOnCloud()
            );
        } else {
            //if not, perform the move, but you don't get stud from cloud (Empty map)
            HashMap<PawnType, Integer> stud = new HashMap<>();
            for (PawnType color : PawnType.values()) {
                stud.put(color, 0);
            }
            game.getCurrentPlayer().getPlayerboard().addStudentsToEntrance(
                    stud
            );
        }
    }

    /**
     * This method returns assistantCards selected
     *
     * @return type {@link ArrayList<Integer>}: assistantCards selected
     */
    public ArrayList<Integer> getCardSelectedByPlayers() {
        return cardSelectedByPlayers;
    }

    /**
     * This method sets current Player on Game
     *
     * @param currentPlayer type {@link String}: current player on Game
     */
    public void setCurrentPlayer(String currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    /**
     * This method returns current Player on Game
     *
     * @return type {@link String}: current player on Game
     */
    public String getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * This method is used to play character card selected
     *
     * @param card     type {@link CharacterCard}: characterCard selected
     * @param nickname type {@link String}:player's nickname
     */
    public void playCharacterCard(CharacterCard card, String nickname) {
        card.effect();
        card.getGame().getPlayerByNickname(nickname).setCharacterCardPlayed(card);
        //Spend the cost of the character and update its future cost
        card.getGame().getPlayerByNickname(nickname).getPlayerboard().removeCoins(card.getCost());
        card.getGame().getGameBoard().addCoins(card.getCost() - 1);
        card.getGame().getCharacterDeck().getCardByID(card.getID()).addMoneyCost();

    }

    /**
     * This method is used to reset characterCard effect
     *
     * @param card     type {@link CharacterCard}: characterCard selected
     * @param nickname type {@link String}:player's nickname
     */
    public void resetCharacterCardEffect(CharacterCard card, String nickname) {
        card.resetEffect(game.getPlayerByNickname(nickname).getNickname());
    }

    /**
     * This method is used to manage last turn
     */
    public void setLastTurn() {
        if (game.checkLastTurn()) {
            game.setLastTurn(true);
        }
    }

    /**
     * This method returns number of players
     *
     * @return type int: number of players
     */
    public int getPlayerNumbers() {
        return playerNumbers;
    }

    /**
     * This method returns Game object
     *
     * @return type {@link Game}: Game object
     */
    public Game getGame() {
        return game;
    }

}
