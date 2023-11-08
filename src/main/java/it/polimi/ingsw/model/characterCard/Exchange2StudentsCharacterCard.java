package it.polimi.ingsw.model.characterCard;

import it.polimi.ingsw.model.CharacterCard;
import it.polimi.ingsw.model.PawnType;

import java.io.Serializable;
import java.util.Map;


/**
 * Exchange2StudentsCharacterCard class
 * This class implements effect of card number 1:
 * You may exchange up to 2 Students between your Entrance and your Dining Room.
 *
 * @author Christian Lisi
 */
public class Exchange2StudentsCharacterCard extends CharacterCard implements Serializable {
    Map<PawnType, Integer> studentsDiningRoom;
    Map<PawnType, Integer> studentsEntrance;

    /**
     * Constructor method that sets information of card and associates object Game to card
     *
     * @param ID                type {@link String}: card's ID
     * @param cost              type {@link String}: card's cost
     * @param effectType        type {@link String}: card's type
     * @param effectDescription type {@link String}: card's effect
     */
    public Exchange2StudentsCharacterCard(String ID, String cost, String effectType, String effectDescription) {
        setID(ID);
        setCost(Integer.parseInt(cost));
        setEffectType(effectType);
        setSetup("");
        setEffectDescription(effectDescription);
    }

    /**
     * This method sets parameter that will be used to active effect
     *
     * @param studentsDiningRoom type {@link Map} with keys {@link PawnType} and values {@link Integer} : diningRoom's students to be removed
     * @param studentsEntrance   type {@link Map} with keys {@link PawnType} and values {@link Integer}: entrance's students to be removed
     */
    public void setParameterToEffect(Map<PawnType, Integer> studentsDiningRoom, Map<PawnType, Integer> studentsEntrance) {
        this.studentsDiningRoom = studentsDiningRoom;
        this.studentsEntrance = studentsEntrance;
    }

    /**
     * This method actives card's effect
     */
    @Override
    public void effect() {
        //Replace students
        getGame().getCurrentPlayer().getPlayerboard().replaceStudentDiningRoomAndEntrance(studentsDiningRoom, studentsEntrance, getGame().getGameMode(), getGame().getGameBoard());
        getGame().assignProfessor();
    }

    /**
     * This helper methods count number of students into map
     * @param map type type {@link Map} with keys {@link PawnType} and values {@link Integer}: students map
     * @return type int: number of students
     */

}
