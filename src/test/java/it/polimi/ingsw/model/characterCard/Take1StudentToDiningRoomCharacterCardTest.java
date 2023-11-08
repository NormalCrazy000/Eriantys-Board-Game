package it.polimi.ingsw.model.characterCard;

import it.polimi.ingsw.model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class Take1StudentToDiningRoomCharacterCardTest {

    Take1StudentToDiningRoomCharacterCard card;
    Game game;

    @BeforeEach
    public void setup() {
        game = new Game(GameMode.EXPERT, 2);
        Player player = new Player("nickname");
        game.addNewPlayerIntoGame(player);
        card = new Take1StudentToDiningRoomCharacterCard("11", "2", "Take1StudentToDiningRoom", "In setup, draw 4 Students and place them on this card.", "Take 1 Student from this card and place it in your Dining Room. Then, draw a new Student from the bag and place it on this card.",game);
        card.setGame(game);
    }

    @AfterEach
    public void reset() {
        card = null;
        game = null;
    }

    @Test
    @DisplayName("Take1StudentToDiningRoomCharacterCard action test")
    public void testAction() {

        Map<PawnType, Integer> studentsOnCardsOld = card.getStudentsCard();

        PawnType colorChoose = null;
        for (PawnType color : studentsOnCardsOld.keySet()) {
            if (studentsOnCardsOld.get(color) > 0) {
                colorChoose = color;
                break;
            }
        }

        System.out.println("->"+colorChoose);

        int num_of_stud = card.getStudentsCard().values().stream().mapToInt(Integer::intValue).sum();
        int prevDinigRoomValue = game.getPlayerByNickname("nickname").getPlayerboard().getAllStudentsOrderDiningRoom().get(colorChoose);


        card.setParameterToEffect(colorChoose);
        card.effect();

        assertEquals(game.getPlayerByNickname("nickname").getPlayerboard().getAllStudentsOrderDiningRoom().get(colorChoose), prevDinigRoomValue+1);
        assertEquals(num_of_stud,card.getStudentsCard().values().stream().mapToInt(Integer::intValue).sum());



    }

}