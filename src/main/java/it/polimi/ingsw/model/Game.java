package it.polimi.ingsw.model;

import it.polimi.ingsw.model.characterCard.NoEntryCardCharacterCard;

import java.io.Serializable;
import java.util.*;

/**
 * This class represents the entire game.
 * noTowerInfluence variable is used to manage the effect of the characters(in particular {@link it.polimi.ingsw.model.characterCard.NoTowerInInfluenceCharacterCard}
 * colorDenied variable is used to manage the effect of the characters(in particular {@link it.polimi.ingsw.model.characterCard.ColorNoInfluenceCharacterCard}
 * These variables are used in case the game mode is expert and left at zero otherwise, so you can manage both modes
 *
 * @author Gabriele Giannotto
 */
public class Game implements Serializable {

    private final GameBoard gameBoard;
    private ArrayList<Player> players;
    private int indexCurrentPlayer;
    private final GameMode gameMode;
    private CharacterDeck characterDeck;
    private boolean endGame;
    private boolean lastTurn;
    private Player winner;
    //These variables are used to implement character cards
    private boolean noTowerInfluence;
    private PawnType colorDenied;


    public Game(GameMode gameMode, int numberOfPlayers) {
        ///Variable to endGame
        endGame = false;
        winner = null;
        lastTurn = false;
        //Setup game
        players = new ArrayList<>();
        indexCurrentPlayer = 0;
        this.gameMode = gameMode;
        gameBoard = new GameBoard(numberOfPlayers);
        gameBoard.setGameMode(gameMode);

        if (gameMode == GameMode.EXPERT) {
            characterDeck = new CharacterDeck(this);
            gameBoard.setCharacterDeck(characterDeck);
        }

        //These variables are used to implement character cards
        noTowerInfluence = false;
        colorDenied = null;
    }

    /**
     * This method add player into a Game Object
     *
     * @param player type {@link Player}:player which will added to the game
     */
    public void addNewPlayerIntoGame(Player player) {
        players.add(player);
    }

    /**
     * This method returns the player of the game according to the input nickname
     *
     * @param nickname (type String): input nickname
     * @return type {@link Player}: player into the game if search is successful, else null
     */
    public Player getPlayerByNickname(String nickname) {
        //Check player into a Game
        for (Player p : players) {
            if (p.getNickname().equals(nickname)) {
                return p;
            }
        }
        //Return null because input nickname player isn't associated any players in Game
        return null;
    }

    /**
     * This method returns the player of the game according to the input index
     *
     * @param index (type int): linked to the index
     * @return type {@link Player}: player into the game associated
     */
    public Player getPlayerByIndex(int index) {
        return players.get(index);
    }

    /**
     * This method returns number of Players
     *
     * @return (type int): number of players
     */


    /**
     * This method returns the current player who is playing the turn
     *
     * @return type {@link Player}:current player
     */
    public Player getCurrentPlayer() {
        return players.get(indexCurrentPlayer);
    }

    /**
     * This method sets the player who will play in the next round
     */
    public void nextPlayer() {
        if (indexCurrentPlayer == players.size() - 1) {
            indexCurrentPlayer = 0;
        } else {
            indexCurrentPlayer++;
        }
    }

    /**
     * This method return GameBord object
     *
     * @return type {@link GameBoard}: gameBoard object
     */
    public GameMode getGameMode() {
        return gameMode;
    }


    /**
     * This method return CharacterDeck object
     *
     * @return type {@link CharacterDeck}: characterDeck object
     */
    public CharacterDeck getCharacterDeck() {
        return gameBoard.getCharacterDeck();
    }

    /**
     * This method is used to check tower influence into region.
     * It adds/changes towers in case the influence has changed.
     * Influence is calculated based on professors and number of students over the region
     *
     * @param region type {@link IslandRegion}:region to be controlled
     */
    public void checkTowerInfluence(IslandRegion region) {
        //check if in the region there are bans due to the character card 5
        if (region.getNoEntry() == 0) {
            //This Map is used to represent the players and students association of which they have professor
            Map<Integer, Integer> towerCheck = new HashMap<>();
            for (int i = 0; i < players.size(); i++) {
                towerCheck.put(i, 0);
            }
            for (PawnType color : PawnType.values()) {
                //Check if color is denied due to the character card 9
                if (colorDenied == null || !colorDenied.equals(color)) {
                    int numberColorStudentOnRegion = region.getAllStudentsOrder().get(color);
                    for (int i = 0; i < players.size(); i++) {
                        //Check if player has professor with same color
                        if (players.get(i).getPlayerboard().checkColorProfessor(color)) {
                            int plusInfluence;
                            //Add plusInfluence if there is an additional influence caused by character number 8
                            plusInfluence = players.get(i).getAdditionalInfluence();
                            //Add influence if there is no prohibition of influence of the tower (chart 10) and if the tower is of the same color as the player
                            if (!noTowerInfluence && region.getColorTower() != null && region.getColorTower().equals(players.get(i).getPlayerboard().getTowerColor())) {
                                plusInfluence = plusInfluence + region.numberOfTowers();
                            }
                            //Check to see if the player's new influence is greater than it was due to other professors
                            if (towerCheck.get(i) < numberColorStudentOnRegion + plusInfluence) {
                                System.out.println("DEBUG: check region influence 1");
                                towerCheck.put(i, numberColorStudentOnRegion + plusInfluence);
                            }
                        }
                    }
                }
            }
            //Check if towerCheck hasn't duplicates maxValue because otherwise no one conquers the region
            if (noMaxValueDuplicated(towerCheck)) {
                System.out.println("DEBUG: check region influence 2");

                //remove old towers on region and add them to their owner
                int numberOfTowersOld = region.numberOfTowers();
                TowerColor colorTowerOld = region.getColorTower();
                region.removeAllTower();
                for (Player player : players) {
                    if (player.getPlayerboard().getTowerColor().equals(colorTowerOld)) {
                        player.getPlayerboard().addTower(numberOfTowersOld);
                    }
                }
                //Add new towers to the region and remove them to their owner.
                //The new towers are equal in number to the old towers but
                //if the number of the old towers was zero, the new towers will be equal in number to 1
                int winnerInfluencePlayer = keyOfMaxValue(towerCheck);
                //Number of tower to be added on region
                int numberOfTowersWinner = Math.min(players.get(winnerInfluencePlayer).getPlayerboard().getNumberOfTower(), Math.max(1, numberOfTowersOld));
                region.addTower(numberOfTowersWinner);
                region.setTower(players.get(winnerInfluencePlayer).getPlayerboard().getTowerColor());
                players.get(winnerInfluencePlayer).getPlayerboard().removeTower(numberOfTowersWinner);
                System.out.println(region.numberOfTowers());
            }
        } else {
            //Remove one bans on region and add it on CharacterCard 5
            region.removeNoEntry();
            NoEntryCardCharacterCard characterCard = (NoEntryCardCharacterCard) gameBoard.getCharacterDeck().getCardByID("5");
            characterCard.addNoEntry();
        }
    }

    /**
     * This method return player that played Character card number 2
     *
     * @return type {@link Player}: return player that played card or null if the character has not been played
     */
    private Player getPlayerThatPlayedCharacterCard2() {
        for (Player player : players) {
            if (player.isControlProfessor()) return player;
        }
        return null;
    }

    /**
     * This method assigns professor to player.
     * Count the number of students of a color and assign the professor to the one with the most.
     * In case of a tie, no professors are assigned unless character card 2 has been played.
     */
    public void assignProfessor() {
        //For each color check number of students
        for (PawnType color : PawnType.values()) {
            Map<Integer, Integer> colorTurn = new HashMap<>();
            //Add students into colorTurn map(index player,student number)
            for (int i = 0; i < players.size(); i++) {
                colorTurn.put(i, players.get(i).getPlayerboard().getAllStudentsOrderDiningRoom().get(color));
            }
            //Check if colorTurnOrder hasn't duplicates max value
            if (noMaxValueDuplicated(colorTurn)) {
                //Remove professor from old player
                for (Player player : players) {
                    player.getPlayerboard().removeProfessor(color);
//                    System.out.println(player.getPlayerboard().getAllProfessors());
                }
                //Add professor to winner influence player
                players.get(keyOfMaxValue(colorTurn)).getPlayerboard().addProfessor(color);
            } else {
                //Take max number of students in colorTurnOrder
                int maxValue = colorTurn.get(keyOfMaxValue(colorTurn));
                for (Map.Entry<Integer, Integer> entry : colorTurn.entrySet()) {
                    //checks if character card 2 has been played.
                    // If the player who played it has students equal to the maxvalue, he assigns the professor to him.
                    if (getPlayerThatPlayedCharacterCard2() != null && players.get(entry.getKey()).equals(getPlayerThatPlayedCharacterCard2()) && entry.getValue() == maxValue) {
                        for (Player player : players) {
                            player.getPlayerboard().removeProfessor(color);
                        }
                        getPlayerThatPlayedCharacterCard2().getPlayerboard().addProfessor(color);
                    }
                }
            }
        }
    }


    /**
     * This method returns key associated of max value into map
     *
     * @param map type {@link Map} that contains for key {@link PawnType} and for value {@link Integer}: map to check
     * @return type int: key associated of max value into map
     */
    private int keyOfMaxValue(Map<Integer, Integer> map) {
        return Collections.max(map.entrySet(), Comparator.comparingInt(Map.Entry::getValue)).getKey();
    }

    /**
     * This method check if into map there are duplicated max values
     *
     * @param map type {@link Map} that contains for key {@link PawnType} and for value {@link Integer}: map to check
     * @return type bool: true if there aren't duplicated, else false
     */
    private boolean noMaxValueDuplicated(Map<Integer, Integer> map) {
        return (Collections.frequency(
                map.values(),
                Collections.max(map.entrySet(), Comparator.comparingInt(Map.Entry::getValue)).getValue()
        ) == 1);
    }

    /**
     * This method returns key associated of min value into map
     *
     * @param map type {@link Map} that contains for key {@link PawnType} and for value {@link Integer}: map to check
     * @return type int: key associated of min value into map
     */
    private int keyOfMinValue(Map<Integer, Integer> map) {
        return Collections.min(map.entrySet(), Comparator.comparingInt(Map.Entry::getValue)).getKey();
    }

    /**
     * This method check if into map there are duplicated min values
     *
     * @param map type {@link Map} that contains for key {@link PawnType} and for value {@link Integer}: map to check
     * @return type bool: true if there aren't duplicated, else false
     */
    private boolean noMinValueDuplicated(Map<Integer, Integer> map) {
        return (Collections.frequency(
                map.values(),
                Collections.min(map.entrySet(), Comparator.comparingInt(Map.Entry::getValue)).getValue()
        ) == 1);
    }

    /**
     * This method is used to check if some player hasn't towers.
     *
     * @return (type bool): return true if some player hasn't towers, else false
     */
    private boolean checkEndTower() {
        for (Player player : players) {
            if (player.getPlayerboard().getNumberOfTower() == 0) return true;
        }
        return false;
    }

    /**
     * This method is used to check if regions are 3.
     *
     * @return (type bool): return true if regions are 3, else false
     */
    private boolean checkEndRegion() {
        return gameBoard.checkNumberOfRegions() == 3;
    }

    /**
     * This method is used to check if bag is empty.
     *
     * @return (type bool): return true if bag is empty, else false
     */
    private boolean checkEndStudent() {
        return gameBoard.checkNumberOfStudentsOnBag() == 0;
    }

    /**
     * This method is used to check if some player hasn't AssistantController card.
     *
     * @return (type bool): return true if some player hasn't AssistantController card, else false
     */
    private boolean checkEndAssistant() {
        for (Player player : players) {
            if (player.isAssistantDeckEmpty()) return true;
        }
        return false;
    }

    /**
     * This method is used to check if the game must end.
     *
     * @return (type bool): return true if the game must end(number of regions equal to 3 or number of towers for at least one player is zero) , else false
     */
    public boolean checkEndGame() {
        return checkEndRegion() || checkEndTower();
    }

    /**
     * This method is used to check if it's the last round
     *
     * @return (type bool): return true if it's the last round, else false
     */
    public boolean checkLastTurn() {
        return checkEndStudent() || checkEndAssistant();
    }

    /**
     * This method controls the winner of the game. To do this, first use the number of towers(less value to win), then the number of professor(max value to win).
     * Otherwise, it ends in a tie
     */
    public void winner() {
        Map<Integer, Integer> towerCheck = new HashMap<>();
        Player checkWinner = null;
        for (int i = 0; i < players.size(); i++) {
            towerCheck.put(i, players.get(i).getPlayerboard().getNumberOfTower());
        }
        //if there are no duplicates min value then there is a player with minimum number of towers
        if (noMinValueDuplicated(towerCheck)) {
            checkWinner = players.get(keyOfMinValue(towerCheck));

        } else {
            Map<Integer, Integer> professorCheck = new HashMap<>();
            for (int i = 0; i < players.size(); i++) {
                professorCheck.put(i, players.get(i).getPlayerboard().checkNumberProfessors());
            }
            //if there are no duplicates max value then there is a player with maximum number of professors
            if (noMaxValueDuplicated(professorCheck)) {
                checkWinner = players.get(keyOfMaxValue(professorCheck));
            }
        }
        //Set null in case of a tie
        winner = checkWinner;
    }

    /**
     * This method is used to return gameBoard
     *
     * @return type {@link GameBoard}: gameBoard
     */
    public GameBoard getGameBoard() {
        return gameBoard;
    }

    /**
     * This method is used to return endGame
     *
     * @return (type bool): endGame
     */
    public boolean isEndGame() {
        return endGame;
    }

    /**
     * This method set endgame
     *
     * @param endGame (type bool): value to set
     */
    public void setEndGame(boolean endGame) {
        this.endGame = endGame;
    }

    /**
     * This method is used to return lastTurn
     *
     * @return (type bool): lastTurn
     */
    public boolean isLastTurn() {
        return lastTurn;
    }

    /**
     * This method set lastTurn
     *
     * @param lastTurn (type bool): value to set
     */
    public void setLastTurn(boolean lastTurn) {
        this.lastTurn = lastTurn;
    }

    /**
     * This method is used to return noTowerInfluence
     *
     * @return (type bool): noTowerInfluence
     */
    public boolean isNoTowerInfluence() {
        return noTowerInfluence;
    }

    /**
     * This method set noTowerInfluence
     *
     * @param noTowerInfluence (type bool): value to set
     */
    public void setNoTowerInfluence(boolean noTowerInfluence) {
        this.noTowerInfluence = noTowerInfluence;
    }

    /**
     * This method is used to return colorDenied
     *
     * @return type {@link PawnType}: colorDenied
     */
    public PawnType getColorDenied() {
        return colorDenied;
    }

    /**
     * This method is used to set colorDenied
     *
     * @param colorDenied type {@link PawnType}: colorDenied
     */
    public void setColorDenied(PawnType colorDenied) {
        this.colorDenied = colorDenied;
    }


    /**
     * This method returns player
     *
     * @return type {@link ArrayList<Player>}: players on Game
     */
    public ArrayList<Player> getPlayers() {
        return players;
    }

    /**
     * This method returns current player index
     *
     * @return type int: current player index
     */
    public int getIndexCurrentPlayer() {
        return indexCurrentPlayer;
    }

    /**
     * This method is used to set players on Game
     *
     * @param players type {@link ArrayList<Player>}: players
     */
    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    /**
     * This method is used to return winner player
     *
     * @return type {@link Player}: winner player
     */
    public Player getWinner() {
        return winner;
    }

}