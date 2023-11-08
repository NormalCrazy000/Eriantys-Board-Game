package it.polimi.ingsw.model.characterCard;

import it.polimi.ingsw.model.CharacterCard;
import it.polimi.ingsw.model.PawnType;
import it.polimi.ingsw.model.Player;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


/**
 * Remove3StudentsFromDiningRoomCharacterCard class
 * This class implements effect of card number 12:
 * Choose a type of Student: every player (including yourself) must return 3 Students of that type from their Dining Room to the bag.
 * If any player has fewer than 3 Students of that type, return as many Students as they have.
 *
 * @author Christian Lisi
 */
public class Remove3StudentsFromDiningRoomCharacterCard extends CharacterCard implements Serializable {
    PawnType studColor;
    Map<Player, Integer> studentsForPlayer;

    /**
     * Constructor method that sets information of card and associates object Game to card
     *
     * @param ID                type {@link String}: card's ID
     * @param cost              type {@link String}: card's cost
     * @param effectType        type {@link String}: card's type
     * @param setup             type {@link String}: card's type
     * @param effectDescription type {@link String}: card's effect
     */
    public Remove3StudentsFromDiningRoomCharacterCard(String ID, String cost, String effectType, String setup, String effectDescription) {
        setID(ID);
        setCost(Integer.parseInt(cost));
        setEffectType(effectType);
        setSetup(setup);
        setEffectDescription(effectDescription);
    }


    /**
     * This method is used to get the color chosen by the player who played the card
     *
     * @return studColor  type {@link PawnType}
     */
    public PawnType getStudColor() {
        return studColor;
    }

    /**
     * This method sets parameter that will be used to active effect
     *
     * @param studColor type {@link PawnType} is the color chosen by the player who played the card
     */
    public void setParameterToEffect(PawnType studColor) {
        this.studColor = studColor;
    }

    /**
     * This method actives card's effect
     */
    public void effect() {


        for (Player player : getGame().getPlayers()) {

            int x = player.getPlayerboard().getAllStudentsOrderDiningRoom().get(studColor);

            if (x >= 3) {
                HashMap<PawnType, Integer> stud = new HashMap<>();
                stud.put(getStudColor(), 3);

                player.getPlayerboard().removeStudentsFromDiningRoom(stud);
                getGame().getGameBoard().addPlayerIntoBag(stud);
            } else {
                HashMap<PawnType, Integer> stud = new HashMap<>();
                stud.put(getStudColor(), x);

                player.getPlayerboard().removeStudentsFromDiningRoom(stud);
                getGame().getGameBoard().addPlayerIntoBag(stud);

            }
        }


        //Call assignProfessor because the influence can be changed
        getGame().assignProfessor();
    }
}
