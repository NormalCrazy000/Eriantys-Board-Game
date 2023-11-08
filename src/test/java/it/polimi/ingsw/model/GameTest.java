package it.polimi.ingsw.model;

import it.polimi.ingsw.model.characterCard.NoEntryCardCharacterCard;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * GameTest class testes {@link Game} class
 *
 * @author Maria Pia Marini
 * @see Game
 */

class GameTest {

    @ParameterizedTest
    @CsvSource(value = {"1","2"})
    @DisplayName("Game constructor test")
    public void testGame(int gameMode) {
        Game game = null;
        if(gameMode==1){
            game = new Game(GameMode.EASY,2);
            assertEquals(GameMode.EASY,game.getGameMode());
        }else{
            game = new Game(GameMode.EXPERT,2);
            assertEquals(GameMode.EXPERT,game.getGameMode());
            assertEquals(3,game.getCharacterDeck().getDeckList().length);
        }
        assertEquals(false,game.isEndGame());
        assertNull(game.getWinner());
        assertEquals(false,game.isLastTurn());
        assertEquals(0,game.getIndexCurrentPlayer());
        assertEquals(false,game.isNoTowerInfluence());
        assertNull(game.getColorDenied());
    }

    @Test
    @DisplayName("Game addNewPlayerIntoGame test")
    public void testAddNewPlayerIntoGame() {
        Game game = new Game(GameMode.EASY,2);
        Player player = new Player("nickname");
        game.addNewPlayerIntoGame(player);
        assertEquals(player,game.getPlayerByNickname("nickname"));

    }

    @Test
    @DisplayName("Game getPlayerByNickname test")
    public void testGetPlayerByNickname() {
        Game game = new Game(GameMode.EASY,2);
        Player player = new Player("nickname");
        game.addNewPlayerIntoGame(player);
        assertEquals(player,game.getPlayerByNickname("nickname"));
    }

    @Test
    @DisplayName("Game getPlayerByNickname test(null return)")
    public void testGetPlayerByNicknameNoPlayer() {
        Game game = new Game(GameMode.EASY,2);
        Player player = new Player("nickname1");
        game.addNewPlayerIntoGame(player);
        assertNull(game.getPlayerByNickname("nickname2"));
    }

    @Test
    @DisplayName("Game getPlayerByIndex test")
    public void testGetPlayerByIndex() {
        Game game = new Game(GameMode.EASY,2);
        Player player = new Player("nickname");
        game.addNewPlayerIntoGame(player);
        assertEquals(player,game.getPlayerByIndex(0));
    }

    @Test
    @DisplayName("Game getCurrentPlayer test")
    public void testGetCurrentPlayer() {
        Game game = new Game(GameMode.EASY,2);
        Player player = new Player("nickname");
        game.addNewPlayerIntoGame(player);
        Player player2 = new Player("nickname2");
        game.addNewPlayerIntoGame(player2);
        assertEquals(player,game.getCurrentPlayer());
    }

    @Test
    @DisplayName("Game nextPlayer test")
    public void testNextPlayer() {
        Game game = new Game(GameMode.EASY,2);
        Player player = new Player("nickname");
        game.addNewPlayerIntoGame(player);
        Player player2 = new Player("nickname2");
        game.addNewPlayerIntoGame(player2);
        game.nextPlayer();
        assertEquals(1,game.getIndexCurrentPlayer());
    }

    @Test
    @DisplayName("Game nextPlayer test(last index)")
    public void testNextPlayerLastIndex() {
        Game game = new Game(GameMode.EASY,2);
        Player player = new Player("nickname");
        game.addNewPlayerIntoGame(player);
        Player player2 = new Player("nickname2");
        game.addNewPlayerIntoGame(player2);
        game.nextPlayer();
        game.nextPlayer();
        assertEquals(0,game.getIndexCurrentPlayer());
    }

    @Test
    @DisplayName("Game getGameMode test")
    public void testGetGameMode() {
        Game game = new Game(GameMode.EASY,2);
        assertEquals(GameMode.EASY,game.getGameMode());
    }

    @Test
    @DisplayName("Game getCharacterDeck test")
    public void testGetCharacterDeck() {
        Game game = new Game(GameMode.EXPERT,2);
        assertEquals(3,game.getCharacterDeck().getDeckList().length);
    }

    @Test
    @DisplayName("Game checkTowerInfluence test(NoEntry Tile")
    public void testCheckTowerInfluenceNoEntry() {
        Game game;
        game = new Game(GameMode.EXPERT,2);

        CharacterCard card = new NoEntryCardCharacterCard("5","2","NoEntryCard","In setup, put the 4 No Entry Tiles on this card.","Place a No Entry on an island of your choice. The first time Mother Nature ends her movement there, put the No Entry tile back onto this card DO NOT calculate influence on that Island, or place any Towers.");
        card.setGame(game);


        game.getCharacterDeck().getDeckList()[0] = card;
        game.getGameBoard().getRegion(0).addNoEntry();
        int numberOfTileOnCardOld= ((NoEntryCardCharacterCard)game.getCharacterDeck().getCardByID("5")).getNoEntry();
        game.checkTowerInfluence(game.getGameBoard().getRegion(0));
        assertEquals(0,game.getGameBoard().getRegion(0).getNoEntry());
        assertEquals(numberOfTileOnCardOld+1,((NoEntryCardCharacterCard)game.getCharacterDeck().getCardByID("5")).getNoEntry());

    }

    @Test
    @DisplayName("Game checkTowerInfluence test(ColorDenied")
    public void testCheckTowerInfluenceColorDenied() {
        //Setup
        Game game = new Game(GameMode.EXPERT,2);
        setStudentOnIslandType1(game.getGameBoard().getRegions().get(0));
        Player player1 = new Player("nickname1");
        Player player2 = new Player("nickname2");
        game.addNewPlayerIntoGame(player1);
        game.addNewPlayerIntoGame(player2);
        player1.getPlayerboard().addProfessor(PawnType.RED);
        player2.getPlayerboard().addProfessor(PawnType.YELLOW);
        player1.getPlayerboard().setTowerColor(TowerColor.WHITE);
        player2.getPlayerboard().setTowerColor(TowerColor.BLACK);
        player1.getPlayerboard().addTower(8);
        player2.getPlayerboard().addTower(8);
        int oldTowerPlayer1 = player1.getPlayerboard().getNumberOfTower();
        int oldTowerPlayer2 = player2.getPlayerboard().getNumberOfTower();
        //PlayCharacterCard
        game.setColorDenied(PawnType.YELLOW);
        //Assign tower
        game.checkTowerInfluence(game.getGameBoard().getRegion(0));
        //Check condition
        assertEquals(oldTowerPlayer1-1,player1.getPlayerboard().getNumberOfTower());
        assertEquals(TowerColor.WHITE,game.getGameBoard().getRegion(0).getColorTower());

        assertEquals(oldTowerPlayer2,player2.getPlayerboard().getNumberOfTower());

    }

    @Test
    @DisplayName("Game checkTowerInfluence test(Additional influence")
    public void testCheckTowerInfluenceAdditionalInfluence() {
        //Setup
        Game game = new Game(GameMode.EXPERT,2);
        setStudentOnIslandType1(game.getGameBoard().getRegions().get(0));
        Player player1 = new Player("nickname1");
        Player player2 = new Player("nickname2");
        game.addNewPlayerIntoGame(player1);
        game.addNewPlayerIntoGame(player2);
        player1.getPlayerboard().addProfessor(PawnType.RED);
        player2.getPlayerboard().addProfessor(PawnType.YELLOW);
        player1.getPlayerboard().setTowerColor(TowerColor.WHITE);
        player2.getPlayerboard().setTowerColor(TowerColor.BLACK);
        player1.getPlayerboard().addTower(8);
        player2.getPlayerboard().addTower(8);
        int oldTowerPlayer1 = player1.getPlayerboard().getNumberOfTower();
        int oldTowerPlayer2 = player2.getPlayerboard().getNumberOfTower();
        //PlayCharacterCard
        player1.setAdditionalInfluence(2);
        //Assign tower
        game.checkTowerInfluence(game.getGameBoard().getRegion(0));
        //Check condition
        assertEquals(oldTowerPlayer1-1,player1.getPlayerboard().getNumberOfTower());
        assertEquals(TowerColor.WHITE,game.getGameBoard().getRegion(0).getColorTower());

        assertEquals(oldTowerPlayer2,player2.getPlayerboard().getNumberOfTower());

    }

    @Test
    @DisplayName("Game checkTowerInfluence test(NoTower influence")
    public void testCheckTowerInfluenceNoTowerInfluence() {
        //Setup
        Game game = new Game(GameMode.EXPERT,2);
        setStudentOnIslandType1(game.getGameBoard().getRegions().get(0));
        Player player1 = new Player("nickname1");
        Player player2 = new Player("nickname2");
        game.addNewPlayerIntoGame(player1);
        game.addNewPlayerIntoGame(player2);
        player1.getPlayerboard().addProfessor(PawnType.RED);
        player2.getPlayerboard().addProfessor(PawnType.YELLOW);
        player1.getPlayerboard().setTowerColor(TowerColor.WHITE);
        player2.getPlayerboard().setTowerColor(TowerColor.BLACK);
        player1.getPlayerboard().addTower(8);
        player2.getPlayerboard().addTower(8);

        //Set player1's tower on region
        game.getGameBoard().getRegions().get(0).addTower(2);
        game.getGameBoard().getRegions().get(0).setTower(TowerColor.WHITE);
        player1.getPlayerboard().removeTower(2);

        int oldTowerPlayer1 = player1.getPlayerboard().getNumberOfTower();
        int oldTowerPlayer2 = player2.getPlayerboard().getNumberOfTower();
        //PlayCharacterCard
        game.setNoTowerInfluence(true);
        //Assign tower
        game.checkTowerInfluence(game.getGameBoard().getRegion(0));
        //Check condition
        assertEquals(oldTowerPlayer1+2,player1.getPlayerboard().getNumberOfTower());
        assertEquals(TowerColor.BLACK,game.getGameBoard().getRegion(0).getColorTower());

        assertEquals(oldTowerPlayer2-2,player2.getPlayerboard().getNumberOfTower());

    }


    @Test
    @DisplayName("Game checkTowerInfluence test(Tie)")
    public void testCheckTowerInfluenceTie() {
        //Setup
        Game game = new Game(GameMode.EXPERT,2);
        setStudentOnIslandType1(game.getGameBoard().getRegions().get(0));
        Player player1 = new Player("nickname1");
        Player player2 = new Player("nickname2");
        game.addNewPlayerIntoGame(player1);
        game.addNewPlayerIntoGame(player2);
        player1.getPlayerboard().addProfessor(PawnType.RED);
        player2.getPlayerboard().addProfessor(PawnType.YELLOW);
        player1.getPlayerboard().setTowerColor(TowerColor.WHITE);
        player2.getPlayerboard().setTowerColor(TowerColor.BLACK);
        player1.getPlayerboard().addTower(8);
        player2.getPlayerboard().addTower(8);

        //Set player1's tower on region
        game.getGameBoard().getRegions().get(0).addTower(1);
        game.getGameBoard().getRegions().get(0).setTower(TowerColor.WHITE);
        player1.getPlayerboard().removeTower(2);

        int oldTowerPlayer1 = player1.getPlayerboard().getNumberOfTower();
        int oldTowerPlayer2 = player2.getPlayerboard().getNumberOfTower();
        //Assign tower
        game.checkTowerInfluence(game.getGameBoard().getRegion(0));
        //Check condition
        assertEquals(oldTowerPlayer1,player1.getPlayerboard().getNumberOfTower());
        assertEquals(TowerColor.WHITE,game.getGameBoard().getRegion(0).getColorTower());

        assertEquals(oldTowerPlayer2,player2.getPlayerboard().getNumberOfTower());

    }


    @Test
    @DisplayName("Game assignProfessor test")
    public void testAssignProfessor() {
        //Setup
        Game game = new Game(GameMode.EXPERT,2);
        Player player1 = new Player("nickname1");
        Player player2 = new Player("nickname2");
        Map<PawnType,Integer> studentPlayer1 = new HashMap<>();
        Map<PawnType,Integer> studentPlayer2 = new HashMap<>();
        studentPlayer1.put(PawnType.RED,1);
        studentPlayer2.put(PawnType.RED,2);
        player1.getPlayerboard().addStudentToDiningRoom(studentPlayer1,GameMode.EXPERT,game.getGameBoard());
        player2.getPlayerboard().addStudentToDiningRoom(studentPlayer2,GameMode.EXPERT,game.getGameBoard());
        game.addNewPlayerIntoGame(player1);
        game.addNewPlayerIntoGame(player2);
        game.assignProfessor();
        //Check condition
        assertEquals(false,player1.getPlayerboard().getProfessors().get(PawnType.RED));
        assertEquals(true,player2.getPlayerboard().getProfessors().get(PawnType.RED));

    }

    @Test
    @DisplayName("Game assignProfessor test(Tie)")
    public void testAssignProfessorTie() {
        //Setup
        Game game = new Game(GameMode.EXPERT,2);
        Player player1 = new Player("nickname1");
        Player player2 = new Player("nickname2");
        Map<PawnType,Integer> studentPlayer1 = new HashMap<>();
        Map<PawnType,Integer> studentPlayer2 = new HashMap<>();
        studentPlayer1.put(PawnType.RED,1);
        studentPlayer2.put(PawnType.RED,1);
        player1.getPlayerboard().addStudentToDiningRoom(studentPlayer1,GameMode.EXPERT,game.getGameBoard());
        player2.getPlayerboard().addStudentToDiningRoom(studentPlayer2,GameMode.EXPERT,game.getGameBoard());
        game.addNewPlayerIntoGame(player1);
        game.addNewPlayerIntoGame(player2);
        game.assignProfessor();
        //Check condition
        assertEquals(false,player1.getPlayerboard().getProfessors().get(PawnType.RED));
        assertEquals(false,player2.getPlayerboard().getProfessors().get(PawnType.RED));

    }

    @Test
    @DisplayName("Game assignProfessor test(ProfessorControl)")
    public void testAssignProfessorProfessorControl() {
        //Setup
        Game game = new Game(GameMode.EXPERT,2);
        Player player1 = new Player("nickname1");
        Player player2 = new Player("nickname2");
        Map<PawnType,Integer> studentPlayer1 = new HashMap<>();
        Map<PawnType,Integer> studentPlayer2 = new HashMap<>();
        studentPlayer1.put(PawnType.RED,1);
        studentPlayer2.put(PawnType.RED,1);
        player1.getPlayerboard().addStudentToDiningRoom(studentPlayer1,GameMode.EXPERT,game.getGameBoard());
        player2.getPlayerboard().addStudentToDiningRoom(studentPlayer2,GameMode.EXPERT,game.getGameBoard());
        //PlayCharacterCard
        player1.setControlProfessor(true);
        game.addNewPlayerIntoGame(player1);
        game.addNewPlayerIntoGame(player2);
        game.assignProfessor();
        //Check condition
        assertEquals(true,player1.getPlayerboard().getProfessors().get(PawnType.RED));
        assertEquals(false,player2.getPlayerboard().getProfessors().get(PawnType.RED));

    }

    @Test
    @DisplayName("Game checkEndGame test(NumberRegion)")
    public void testCheckEndGameNumberRegion() {
        //Setup
        Game game = new Game(GameMode.EXPERT,2);
        Player player1 = new Player("nickname1");
        Player player2 = new Player("nickname2");
        game.addNewPlayerIntoGame(player1);
        game.addNewPlayerIntoGame(player2);
        player1.getPlayerboard().setTowerColor(TowerColor.WHITE);
        player2.getPlayerboard().setTowerColor(TowerColor.BLACK);
        player1.getPlayerboard().addTower(8);
        player2.getPlayerboard().addTower(8);
        for(int i = 0; i<10;i++){
            game.getGameBoard().getRegions().get(i).setTower(TowerColor.BLACK);
            game.getGameBoard().getRegions().get(i).addTower(1);
        }
//        try {
            game.getGameBoard().checkIslandAggregation();
//        } catch (MinRegionAggregationException e) {
//            e.printStackTrace();
//        }
        assertEquals(true,game.checkEndGame());

    }

    @Test
    @DisplayName("Game checkEndGame test(Tower)")
    public void testCheckEndGameNumberTower() {
        //Setup
        Game game = new Game(GameMode.EXPERT,2);
        Player player1 = new Player("nickname1");
        Player player2 = new Player("nickname2");
        game.addNewPlayerIntoGame(player1);
        game.addNewPlayerIntoGame(player2);
        player1.getPlayerboard().setTowerColor(TowerColor.WHITE);
        player2.getPlayerboard().setTowerColor(TowerColor.BLACK);
        player1.getPlayerboard().addTower(0);
        player2.getPlayerboard().addTower(1);
        assertEquals(true,game.checkEndGame());

    }

    @Test
    @DisplayName("Game checkEndGame test(no TowerNumber)")
    public void testCheckEndGameNoNumberTower() {
        //Setup
        Game game = new Game(GameMode.EXPERT,2);
        Player player1 = new Player("nickname1");
        Player player2 = new Player("nickname2");
        game.addNewPlayerIntoGame(player1);
        game.addNewPlayerIntoGame(player2);
        player1.getPlayerboard().setTowerColor(TowerColor.WHITE);
        player2.getPlayerboard().setTowerColor(TowerColor.BLACK);
        player1.getPlayerboard().addTower(1);
        player2.getPlayerboard().addTower(1);
        assertEquals(false,game.checkEndGame());

    }


    @Test
    @DisplayName("Game checkLastTurn test(StudentNumber)")
    public void testCheckLastTurnStudentNumber() {
        //Setup
        Game game = new Game(GameMode.EXPERT,2);
        game.getGameBoard().getStudentsFromBag(game.getGameBoard().checkNumberOfStudentsOnBag());
        assertEquals(true,game.checkLastTurn());

    }

    @Test
    @DisplayName("Game checkLastTurn test(AssistantNumber)")
    public void testCheckLastTurnAssistantNumber() {
        //Setup
        Game game = new Game(GameMode.EXPERT,2);
        Player player1 = new Player("nickname1");
        Player player2 = new Player("nickname2");
        game.addNewPlayerIntoGame(player1);
        game.addNewPlayerIntoGame(player2);
        player1.selectAssistantDeckType(MageType.ORIENTAL_MAGICIAN);
        player2.selectAssistantDeckType(MageType.ELDER_MAGICIAN);
        for(int i = 0;i<10;i++){
            player1.selectCardFromAssistantDeck(i+1);
        }

        assertEquals(true,game.checkLastTurn());

    }

    @Test
    @DisplayName("Game checkLastTurn test(No AssistantNumber)")
    public void testCheckLastTurnNoAssistantNumber() {
        //Setup
        Game game = new Game(GameMode.EXPERT,2);
        Player player1 = new Player("nickname1");
        Player player2 = new Player("nickname2");
        game.addNewPlayerIntoGame(player1);
        game.addNewPlayerIntoGame(player2);
        player1.selectAssistantDeckType(MageType.ORIENTAL_MAGICIAN);
        player2.selectAssistantDeckType(MageType.ELDER_MAGICIAN);
        assertEquals(false,game.checkLastTurn());

    }

    @Test
    @DisplayName("Game winner test(tower)")
    public void testWinnerWithTower() {
        //Setup
        Game game = new Game(GameMode.EXPERT,2);
        Player player1 = new Player("nickname1");
        Player player2 = new Player("nickname2");
        game.addNewPlayerIntoGame(player1);
        game.addNewPlayerIntoGame(player2);
        player1.getPlayerboard().setTowerColor(TowerColor.WHITE);
        player2.getPlayerboard().setTowerColor(TowerColor.BLACK);
        player1.getPlayerboard().addTower(0);
        player2.getPlayerboard().addTower(1);
        game.winner();
        assertEquals(player1,game.getWinner());

    }

    @Test
    @DisplayName("Game winner test(professor)")
    public void testWinnerWithProfessor() {
        //Setup
        Game game = new Game(GameMode.EXPERT,2);
        Player player1 = new Player("nickname1");
        Player player2 = new Player("nickname2");
        game.addNewPlayerIntoGame(player1);
        game.addNewPlayerIntoGame(player2);
        player1.getPlayerboard().setTowerColor(TowerColor.WHITE);
        player2.getPlayerboard().setTowerColor(TowerColor.BLACK);
        player1.getPlayerboard().addTower(0);
        player2.getPlayerboard().addTower(0);
        player1.getPlayerboard().addProfessor(PawnType.RED);
        player1.getPlayerboard().addProfessor(PawnType.YELLOW);
        player2.getPlayerboard().addProfessor(PawnType.GREEN);
        game.winner();
        assertEquals(player1,game.getWinner());

    }

    @Test
    @DisplayName("Game winner test(Tie)")
    public void testWinnerWithTiw() {
        //Setup
        Game game = new Game(GameMode.EXPERT,2);
        Player player1 = new Player("nickname1");
        Player player2 = new Player("nickname2");
        game.addNewPlayerIntoGame(player1);
        game.addNewPlayerIntoGame(player2);
        player1.getPlayerboard().setTowerColor(TowerColor.WHITE);
        player2.getPlayerboard().setTowerColor(TowerColor.BLACK);
        player1.getPlayerboard().addTower(0);
        player2.getPlayerboard().addTower(0);
        player1.getPlayerboard().addProfessor(PawnType.RED);
        player2.getPlayerboard().addProfessor(PawnType.YELLOW);
        game.winner();
        assertNull(game.getWinner());

    }


    private void setStudentOnIslandType1(IslandRegion region){
        Map<PawnType,Integer> students = new HashMap();
        if(region.getAllStudentsOrder().get(PawnType.RED)==1){
            students.put(PawnType.YELLOW,2);
        }else if(region.getAllStudentsOrder().get(PawnType.YELLOW)==1){
            students.put(PawnType.RED,1);
            students.put(PawnType.YELLOW,1);
        }else{
            students.put(PawnType.RED,1);
            students.put(PawnType.YELLOW,2);
        }
        region.addStudents(students);
    }
}