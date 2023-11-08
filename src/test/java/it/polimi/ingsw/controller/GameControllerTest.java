package it.polimi.ingsw.controller;

import it.polimi.ingsw.helper.HelperMap;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.characterCard.Add2ToMotherNatureMovementCharacterCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class GameControllerTest {

    GameController controller;


    @BeforeEach
    public void setup(){
        controller=new GameController();
        controller.createGame(2,GameMode.EXPERT);
        controller.registration("first");
        controller.registration("second");
    }



    @ParameterizedTest
    @CsvSource(value = {"2:1","2:2","3:1","3:2"},delimiter = ':')
    @DisplayName("GameController CreateGame test")
    public void testCreateGame(int playerNumbers, int gameMode){

        //check value of playerNumbers
        if(gameMode == 1){
            controller.createGame(playerNumbers, GameMode.EASY);
            assertEquals(GameMode.EASY,controller.getGame().getGameMode());
        }else{
            controller.createGame(playerNumbers, GameMode.EXPERT);
            assertEquals(GameMode.EXPERT,controller.getGame().getGameMode());
        }
        assertEquals(playerNumbers,controller.getPlayerNumbers());
    }



    @Test
    public void testRegistration(){

        controller.createGame(2,GameMode.EASY);

        controller.registration("first");
        assertEquals(1, controller.getGame().getPlayers().size());

        controller.registration("second");
        assertEquals(2, controller.getGame().getPlayers().size());
    }




    @Test
    public void testSetupPlayerBoardPlayer3AndEasy(){
        GameController controllerEasy=new GameController();
        controllerEasy.createGame(3,GameMode.EASY);
        controllerEasy.registration("first");
        controllerEasy.registration("second");
        controllerEasy.registration("third");

        controllerEasy.setupPlayerBoard();

        assertEquals(6,controllerEasy.getGame().getPlayerByNickname("first").getPlayerboard().getNumberOfTower());
        assertEquals(6,controllerEasy.getGame().getPlayerByNickname("second").getPlayerboard().getNumberOfTower());

        assertEquals(9,controllerEasy.getGame().getPlayerByNickname("first").getPlayerboard().getAllStudentsEntrance().values().stream().mapToInt(Integer::intValue).sum());
        assertEquals(9,controllerEasy.getGame().getPlayerByNickname("first").getPlayerboard().getAllStudentsEntrance().values().stream().mapToInt(Integer::intValue).sum());

        assertEquals(0,controllerEasy.getGame().getPlayerByNickname("first").getPlayerboard().getCoins());
        assertEquals(0,controllerEasy.getGame().getPlayerByNickname("second").getPlayerboard().getCoins());

        assertEquals(TowerColor.WHITE, controllerEasy.getGame().getPlayerByNickname("first").getPlayerboard().getTowerColor());
        assertEquals(TowerColor.BLACK, controllerEasy.getGame().getPlayerByNickname("second").getPlayerboard().getTowerColor());

        assertNull( controllerEasy.getGame().getGameBoard().getCharacterDeck());

    }
    @Test
    public void testSetupPlayerBoard(){

        controller.setupPlayerBoard();

        assertEquals(8,controller.getGame().getPlayerByNickname("first").getPlayerboard().getNumberOfTower());
        assertEquals(8,controller.getGame().getPlayerByNickname("second").getPlayerboard().getNumberOfTower());

        assertEquals(7,controller.getGame().getPlayerByNickname("first").getPlayerboard().getAllStudentsEntrance().values().stream().mapToInt(Integer::intValue).sum());
        assertEquals(7,controller.getGame().getPlayerByNickname("first").getPlayerboard().getAllStudentsEntrance().values().stream().mapToInt(Integer::intValue).sum());

        assertEquals(1,controller.getGame().getPlayerByNickname("first").getPlayerboard().getCoins());
        assertEquals(1,controller.getGame().getPlayerByNickname("second").getPlayerboard().getCoins());

        assertEquals(TowerColor.WHITE, controller.getGame().getPlayerByNickname("first").getPlayerboard().getTowerColor());
        assertEquals(TowerColor.BLACK, controller.getGame().getPlayerByNickname("second").getPlayerboard().getTowerColor());

        assertNotEquals(null, controller.getGame().getGameBoard().getCharacterDeck());

    }

    @Test
    public void testPopulateCloudTile() {

        HelperMap.mapStudentResetWithZeroValue(controller.getGame().getGameBoard().getCloud(0).getAllStudents());

        assertEquals(0,controller.getGame().getGameBoard().getCloud(0).getAllStudents().values().stream().mapToInt(Integer::intValue).sum());


        controller.populateCloudTile();


        assertEquals(3,controller.getGame().getGameBoard().getCloud(0).getAllStudents().values().stream().mapToInt(Integer::intValue).sum());

    }

    @Test
    public  void  testSelectAssistantCard() {
        controller.setupPlayerBoard();
        assertNull(controller.getGame().getPlayerByNickname("first").getCharacterCardPlayed());
        controller.selectAssistantCard("first",controller.getGame().getPlayerByNickname("first").getAssistantDeck().getDeckList().get(0));

        assertEquals(controller.getGame().getPlayerByNickname("first").getAssistantDeck().getDeckList().get(0),
                controller.getGame().getPlayerByNickname("first").getCardPlayed());

        assertEquals(controller.getGame().getPlayerByNickname("first").getCardPlayed().getValue(), controller.getCardSelectedByPlayers().get(0));

    }

    @Test
    public  void  testSelectAssistantCardLastTurn() {
        controller.setupPlayerBoard();
        assertNull(controller.getGame().getPlayerByNickname("first").getCharacterCardPlayed());
        for (int i = 0; i < 10; i++) {
            controller.selectAssistantCard("first",controller.getGame().getPlayerByNickname("first").getAssistantDeck().getDeckList().get(i));

        }
        assertEquals(controller.getGame().getPlayerByNickname("first").getAssistantDeck().getDeckList().get(9),
                controller.getGame().getPlayerByNickname("first").getCardPlayed());

        assertEquals(controller.getGame().getPlayerByNickname("first").getCardPlayed().getValue(), controller.getCardSelectedByPlayers().get(9));
        assertTrue(controller.getGame().isLastTurn());
    }

    @Test
    public void testOrderTurnPlayer(){

        controller.setupPlayerBoard();
        controller.selectAssistantCard("first",controller.getGame().getPlayerByNickname("first").getAssistantDeck().getDeckList().get(2));
        controller.selectAssistantCard("second",controller.getGame().getPlayerByNickname("second").getAssistantDeck().getDeckList().get(0));

        assertEquals("first",controller.getGame().getPlayers().get(0).getNickname());
        assertEquals("second",controller.getGame().getPlayers().get(1).getNickname());

        controller.orderTurnPlayer();

        assertEquals("second",controller.getGame().getPlayers().get(0).getNickname());
        assertEquals("first",controller.getGame().getPlayers().get(1).getNickname());

    }



    @Test
    public void testMoveStudentToDiningRoom() {

        HelperMap.mapStudentGenerator(controller.getGame().getPlayerByNickname("first").getPlayerboard().getAllStudentsEntrance(),
                1,0,0,0,0);

        HashMap<PawnType,Integer> studToMove = new HashMap<>();
        studToMove.put(PawnType.RED,1);

        assertEquals(0, controller.getGame().getPlayerByNickname("first").getPlayerboard().getAllStudentsOrderDiningRoom().get(PawnType.RED));

        controller.moveStudentToDiningRoom(studToMove);

        assertEquals(0, controller.getGame().getPlayerByNickname("first").getPlayerboard().getAllStudentsEntrance().get(PawnType.RED));
        assertEquals(1, controller.getGame().getPlayerByNickname("first").getPlayerboard().getAllStudentsOrderDiningRoom().get(PawnType.RED));

    }


    @Test
    public void testMoveStudentToDiningRoomMoveErrorException() {

        boolean exception = false;

        HelperMap.mapStudentGenerator(controller.getGame().getPlayerByNickname("first").getPlayerboard().getAllStudentsEntrance(),
                1,0,0,0,0);

        HashMap<PawnType,Integer> studInDiningRoom = new HashMap<>();
        studInDiningRoom.put(PawnType.RED,10);

        controller.getGame().getPlayerByNickname("first").getPlayerboard().addStudentToDiningRoom(studInDiningRoom,controller.getGame().getGameMode(),controller.getGame().getGameBoard());


        controller.getGame().getPlayerByNickname("first").getPlayerboard().getAllStudentsOrderDiningRoom().forEach((K,V)-> System.out.println(K.name()+" : "+V));


        HashMap<PawnType,Integer> studToMove = new HashMap<>();
        studToMove.put(PawnType.RED,1);

        assertEquals(10, controller.getGame().getPlayerByNickname("first").getPlayerboard().getAllStudentsOrderDiningRoom().get(PawnType.RED));


        controller.moveStudentToDiningRoom(studToMove);

    }

    @Test
    public void testMoveStudentToIsland(){


        HelperMap.mapStudentGenerator(controller.getGame().getPlayerByNickname("first").getPlayerboard().getAllStudentsEntrance(),
                1,0,0,0,0);

        HashMap<PawnType,Integer> studToMove = new HashMap<>();
        studToMove.put(PawnType.RED,1);

        int x  = controller.getGame().getGameBoard().getRegions().get(0).getAllStudentsOrder().values().stream().mapToInt(Integer::intValue).sum();

        controller.moveStudentToIsland(0,studToMove);

        assertEquals(0, controller.getGame().getPlayerByNickname("first").getPlayerboard().getAllStudentsEntrance().get(PawnType.RED));
        assertEquals(x+1, controller.getGame().getGameBoard().getRegions().get(0).getAllStudentsOrder().values().stream().mapToInt(Integer::intValue).sum());

    }


    @Test
    public void testMoveMotherNature() {

        controller.setupPlayerBoard();
        int islandIndex;

        if(controller.getGame().getGameBoard().getIndexMother() == 11){
            islandIndex=0;
        }else {
            islandIndex = controller.getGame().getGameBoard().getIndexMother()+1;
        }

        HashMap<PawnType,Integer> stud = new HashMap<>();
        HelperMap.mapStudentResetWithZeroValue(stud);

        controller.getGame().getGameBoard().getRegions().get(islandIndex).addStudents(stud);
        controller.getGame().getPlayerByNickname("second").getPlayerboard().addStudentToDiningRoom(stud,controller.getGame().getGameMode(), controller.getGame().getGameBoard());

        stud.put(PawnType.RED,1);

        controller.getGame().getPlayerByNickname("first").getPlayerboard().addStudentToDiningRoom(stud,controller.getGame().getGameMode(), controller.getGame().getGameBoard());

        controller.getGame().assignProfessor();


        HelperMap.mapStudentResetWithZeroValue(controller.getGame().getGameBoard().getRegion(islandIndex).getAllStudentsOrder());

        controller.getGame().getGameBoard().getRegion(islandIndex).getAllStudentsOrder().forEach((k, v)-> System.out.println(k.name()+" : "+v));

        HashMap<PawnType,Integer> studToMove = new HashMap<>();
        studToMove.put(PawnType.RED,1);

        controller.moveStudentToIsland(islandIndex,studToMove);

        controller.moveMotherNature(1);

        assertEquals(TowerColor.WHITE,controller.getGame().getGameBoard().getRegions().get(islandIndex).getColorTower());
    }

//    @Test
//    public void testMoveMotherNatureLastTurn() {
//
//        controller.setupPlayerBoard();
//        int islandIndex;
//
//        if(controller.getGame().getGameBoard().getIndexMother() == 11){
//            islandIndex=0;
//        }else {
//            islandIndex = controller.getGame().getGameBoard().getIndexMother()+1;
//        }
//
//        HashMap<PawnType,Integer> stud = new HashMap<>();
//        HelperMap.mapStudentResetWithZeroValue(stud);
//
//        controller.getGame().getGameBoard().getRegions().get(islandIndex).addStudents(stud);
//        controller.getGame().getPlayerByNickname("second").getPlayerboard().addStudentToDiningRoom(stud,controller.getGame().getGameMode(), controller.getGame().getGameBoard());
//
//        stud.put(PawnType.RED,1);
//
//        controller.getGame().getPlayerByNickname("first").getPlayerboard().addStudentToDiningRoom(stud,controller.getGame().getGameMode(), controller.getGame().getGameBoard());
//        //remove tower
//        controller.getGame().getPlayerByNickname("first").getPlayerboard().setNumberOfTower(1);
//        controller.getGame().assignProfessor();
//
//
//        HelperMap.mapStudentResetWithZeroValue(controller.getGame().getGameBoard().getRegion(islandIndex).getAllStudentsOrder());
//
//        HashMap<PawnType,Integer> studToMove = new HashMap<>();
//        studToMove.put(PawnType.RED,1);
//
//        controller.moveStudentToIsland(islandIndex,studToMove);
//
//        controller.moveMotherNature(1);
//
//        assertEquals(TowerColor.WHITE,controller.getGame().getGameBoard().getRegions().get(islandIndex).getColorTower());
////        assertTrue(controller.getGame().isLastTurn());
//
//    }


    @Test
    public void testGetStudFromCloud(){

        controller.setupPlayerBoard();
        controller.populateCloudTile();
        assertEquals(3,controller.getGame().getGameBoard().getCloud(0).getAllStudents().values().stream().mapToInt(Integer::intValue).sum());

        controller.getStudFromCloud(0);

        assertEquals(0, controller.getGame().getGameBoard().getCloud(0).getAllStudents().values().stream().mapToInt(Integer::intValue).sum());
        assertEquals(10, controller.getGame().getPlayerByNickname("first").getPlayerboard().getAllStudentsEntrance().values().stream().mapToInt(Integer::intValue).sum());

    }

/*
    @Test
    public void testEmptySelectedAssistantCardArray (){

        controller.emptySelectedAssistantCardArray();
        assertEquals(0, controller.getCardSelectedByPlayers().size());

    }

 */

    @Test
    public void testSetCurrentPlayer() {
        controller.setCurrentPlayer("second");
        assertEquals("second",controller.getCurrentPlayer());
    }

    @Test
    public void testGetCurrentPlayer() {
        controller.setCurrentPlayer("first");
        assertEquals("first",controller.getCurrentPlayer());


        controller.setCurrentPlayer("second");
        assertEquals("second",controller.getCurrentPlayer());
    }

    @Test
    public void testPlayCharacterCard () {

        controller.setupPlayerBoard();
        Add2ToMotherNatureMovementCharacterCard card = new Add2ToMotherNatureMovementCharacterCard("4","1","Add2ToMotherNatureMovement","You may move Mother Nature up to 2 additional Island than is indicated by the Assistant card you've played.");
        controller.getGame().getCharacterDeck().getDeckList()[0] = card;
        card.setGame(controller.getGame());

        int cost = card.getCost();

        controller.setCurrentPlayer("first");
        controller.getGame().getPlayerByNickname("first").getPlayerboard().addCoins(6);
        controller.playCharacterCard(controller.getGame().getCharacterDeck().getDeckList()[0],"first");

        assertEquals(7 - cost , controller.getGame().getPlayerByNickname("first").getPlayerboard().getCoins());
        assertEquals(cost + 1 , card.getCost());

    }

    @Test
    public void testResetCharacterCardEffect (){
        controller.setupPlayerBoard();
        Add2ToMotherNatureMovementCharacterCard card = new Add2ToMotherNatureMovementCharacterCard("4","1","Add2ToMotherNatureMovement","You may move Mother Nature up to 2 additional Island than is indicated by the Assistant card you've played.");
        controller.getGame().getCharacterDeck().getDeckList()[0] = card;
        card.setGame(controller.getGame());

        controller.setCurrentPlayer("first");
        controller.getGame().getPlayerByNickname("first").getPlayerboard().addCoins(6);
        controller.playCharacterCard(card,"first");

        assertEquals(controller.getGame().getCharacterDeck().getDeckList()[0],controller.getGame().getPlayerByNickname("first").getCharacterCardPlayed());


        controller.resetCharacterCardEffect(controller.getGame().getCharacterDeck().getDeckList()[0],"first");

        assertEquals(null,controller.getGame().getPlayerByNickname("first").getCharacterCardPlayed());

    }

    @Test
    public void testSetLastTurn(){
        controller.setupPlayerBoard();
        for(int i =0; i< 10;i++){
            controller.getGame().getPlayerByNickname("first").getAssistantDeck().getCard((i+1));
        }
        assertFalse(controller.getGame().isLastTurn());
        controller.setLastTurn();
        assertTrue(controller.getGame().isLastTurn());
    }



    @Test
    public void testPopulateCloudTile_WhenNoStudentsInBag(){
        controller.setupPlayerBoard();

        controller.getGame().getGameBoard().getStudentsFromBag(
               controller.getGame().getGameBoard().checkNumberOfStudentsOnBag()
        );

        for(CloudTile cloud : controller.getGame().getGameBoard().getClouds()){
            cloud.removeStudentOnCloud();
        }
        controller.populateCloudTile();
        assertTrue(controller.getGame().isLastTurn());

    }


    @Test
    public void testPopulateCloudTile_WhenNotEnoughStudentsInBag(){
        controller.setupPlayerBoard();
        controller.setCurrentPlayer("first");


        controller.getGame().getGameBoard().getStudentsFromBag(
                controller.getGame().getGameBoard().checkNumberOfStudentsOnBag()-1
        );

        for(CloudTile cloud : controller.getGame().getGameBoard().getClouds()){
            cloud.removeStudentOnCloud();
        }
        controller.populateCloudTile();

        HashMap<PawnType,Integer> oldEntrance = new HashMap<>();

        for(PawnType color : PawnType.values()){
            oldEntrance.put(color,controller.getGame().getPlayerByNickname("first").getPlayerboard().getAllStudentsEntrance().get(color));
        }


        controller.getStudFromCloud(0);

        for(PawnType color : PawnType.values()){
            assertEquals(oldEntrance.get(color), controller.getGame().getPlayerByNickname("first").getPlayerboard().getAllStudentsEntrance().get(color));
        }

    }


}