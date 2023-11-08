package it.polimi.ingsw.model.characterCard;

import it.polimi.ingsw.helper.HelperMap;
import it.polimi.ingsw.model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class Replace3StudentsInEntranceCharacterCardTest {
    Replace3StudentsInEntranceCharacterCard card;
    Game game;

    @BeforeEach
    public void setup() {
        game = new Game(GameMode.EXPERT, 2);
        Player player = new Player("nickname");
        game.addNewPlayerIntoGame(player);
        card = new Replace3StudentsInEntranceCharacterCard("7", "1", "Replace3StudentsInEntrance", "In setup, draw 6 Students and place them on this card.", "You may take up to 3 Students from this card and replace them with the same number of Students from your Entrance.",game);
        card.setGame(game);
    }

    @AfterEach
    public void reset() {
        card = null;
        game = null;
    }

    @Test
    @DisplayName("Replace3StudentsInEntranceCharacterCardTest action test")
    public void testAction() {

        HelperMap.mapStudentGenerator(game.getPlayerByNickname("nickname").getPlayerboard().getAllStudentsEntrance(),1, 2, 0, 0, 0);

        HashMap<PawnType,Integer> studFromEntrance = new HashMap<>();
        studFromEntrance.put(PawnType.RED,1);
        studFromEntrance.put(PawnType.GREEN,2);

        HelperMap.mapStudentGenerator(card.studentsCard,2,1,1,1,1);

        HashMap<PawnType,Integer> studFromCard = new HashMap<>();
        studFromCard.put(PawnType.BLUE,1);
        studFromCard.put(PawnType.PINK,1);
        studFromCard.put(PawnType.YELLOW,1);


        card.setParameterToEffect(studFromEntrance,studFromCard,"nickname");
        card.effect();


        assertEquals(0,game.getPlayerByNickname("nickname").getPlayerboard().getAllStudentsEntrance().get(PawnType.RED));
        assertEquals(0,game.getPlayerByNickname("nickname").getPlayerboard().getAllStudentsEntrance().get(PawnType.GREEN));
        assertEquals(1,game.getPlayerByNickname("nickname").getPlayerboard().getAllStudentsEntrance().get(PawnType.BLUE));
        assertEquals(1,game.getPlayerByNickname("nickname").getPlayerboard().getAllStudentsEntrance().get(PawnType.PINK));
        assertEquals(1,game.getPlayerByNickname("nickname").getPlayerboard().getAllStudentsEntrance().get(PawnType.YELLOW));


        assertEquals(3,card.getStudentsCard().get(PawnType.RED));
        assertEquals(3,card.getStudentsCard().get(PawnType.GREEN));
        assertEquals(0,card.getStudentsCard().get(PawnType.BLUE));
        assertEquals(0,card.getStudentsCard().get(PawnType.PINK));
        assertEquals(0,card.getStudentsCard().get(PawnType.YELLOW));


    }
}