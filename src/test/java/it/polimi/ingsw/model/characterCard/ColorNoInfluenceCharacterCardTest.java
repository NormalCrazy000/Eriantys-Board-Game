package it.polimi.ingsw.model.characterCard;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameMode;
import it.polimi.ingsw.model.PawnType;
import it.polimi.ingsw.model.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.*;

class ColorNoInfluenceCharacterCardTest {
    ColorNoInfluenceCharacterCard card;
    Game game;

    @BeforeEach
    public void setup() {
        game = new Game(GameMode.EXPERT, 2);
        Player player = new Player("nickname");
        game.addNewPlayerIntoGame(player);
        card = new ColorNoInfluenceCharacterCard("9", "3", "ColorNoInfluence", "Choose a color of Student: during the influence calculation this turn, that color adds no influence.");
        card.setGame(game);
    }

    @AfterEach
    public void reset() {
        card = null;
        game = null;

    }

    @ParameterizedTest
    @EnumSource(PawnType.class)
    @DisplayName("ColorNoInfluenceCharacterCardController action test")
    public void testAction(PawnType color) {
        card.setParameterToEffect(color);
        card.effect();
        assertEquals(color, game.getColorDenied());
    }

    @ParameterizedTest
    @EnumSource(PawnType.class)
    @DisplayName("ColorNoInfluenceCharacterCardController resetEffect test")
    public void testResetEffect(PawnType color) {
        card.setParameterToEffect(color);
        card.effect();
        card.resetEffect("nickname");
        assertNull(game.getColorDenied());
    }

}