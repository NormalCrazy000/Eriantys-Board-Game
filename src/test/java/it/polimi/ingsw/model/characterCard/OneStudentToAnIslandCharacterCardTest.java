package it.polimi.ingsw.model.characterCard;

import it.polimi.ingsw.helper.HelperMap;
import it.polimi.ingsw.model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class OneStudentToAnIslandCharacterCardTest {
    OneStudentToAnIslandCharacterCard card;
    Game game;

    @BeforeEach
    public void setup(){
        game= new Game(GameMode.EXPERT,2);
        card = new OneStudentToAnIslandCharacterCard("1","1","OneStudentToAnIsland","In setup, draw 4 Students and place them on this card","Take 1 Student from this card and place it on an island of yor choice. Then, draw a new Student from the Bag and place it on his card.",game);
        card.setGame(game);
    }

    @AfterEach
    public void reset(){
        card = null;
        game = null;
    }

    @Test
    @DisplayName("OneStudentToAnIslandCharacterCard action test")
    public void testAction(){


        game.getGameBoard().getRegion(0).addStudents(game.getGameBoard().getStudentsFromBag(5));
        Map<PawnType,Integer> studentsOnRegionsOld = game.getGameBoard().getRegion(0).getAllStudentsOrder();


        Map<PawnType,Integer> studentsOnCardsOld = new HashMap<>();
        HelperMap.mapStudentResetWithZeroValue(studentsOnCardsOld);
        for (PawnType color : card.getStudentsCard().keySet()){
            studentsOnCardsOld.put(color,card.getStudentsCard().get(color));
        }

        PawnType colorChoose = null;
        for (PawnType color : studentsOnCardsOld.keySet()){
            if(studentsOnCardsOld.get(color) > 0){
                colorChoose = color;
                break;
            }
        }


        card.setParameterToEffect(0,colorChoose);

        card.effect();


        card.getStudentsCard().get(colorChoose);


        assertEquals(studentsOnRegionsOld.get(colorChoose) + 1 , game.getGameBoard().getRegion(0).getAllStudentsOrder().get(colorChoose));

    }

    @Test
    public void testAction_WithStudBagEmpty(){

        game.getGameBoard().getRegion(0).addStudents(game.getGameBoard().getStudentsFromBag(5));
        Map<PawnType,Integer> studentsOnRegionsOld = game.getGameBoard().getRegion(0).getAllStudentsOrder();


        game.getGameBoard().getStudentsFromBag(game.getGameBoard().checkNumberOfStudentsOnBag());



        Map<PawnType,Integer> studentsOnCardsOld = new HashMap<>();
        HelperMap.mapStudentResetWithZeroValue(studentsOnCardsOld);
        for (PawnType color : card.getStudentsCard().keySet()){
            studentsOnCardsOld.put(color,card.getStudentsCard().get(color));
        }

        PawnType colorChoose = null;
        for (PawnType color : studentsOnCardsOld.keySet()){
            if(studentsOnCardsOld.get(color) > 0){
                colorChoose = color;
                break;
            }
        }


        card.setParameterToEffect(0,colorChoose);

        card.effect();


        card.getStudentsCard().get(colorChoose);


        assertEquals(studentsOnRegionsOld.get(colorChoose) + 1 , game.getGameBoard().getRegion(0).getAllStudentsOrder().get(colorChoose));

        assertTrue(game.isLastTurn());

    }






}