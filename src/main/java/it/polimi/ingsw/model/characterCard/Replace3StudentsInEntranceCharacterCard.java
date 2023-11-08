package it.polimi.ingsw.model.characterCard;

import it.polimi.ingsw.model.CharacterCard;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.PawnType;

import java.io.Serializable;
import java.util.Map;


/**
 * Replace3StudentsInEntranceCharacterCard class
 * This class implements effect of card number 7:
 * In setup, draw 6 Students and place them on this card.
 * You may take up to 3 Students from this card and replace them with the same number of Students from your Entrance.
 *
 * @author Christian Lisi
 */
public class Replace3StudentsInEntranceCharacterCard extends CharacterCard implements Serializable {
    String nickname;
    Map<PawnType, Integer> studentsCard;
    Map<PawnType, Integer> studentsFromPlayerBoard;
    Map<PawnType, Integer> studentsFromCard;

    /**
     * Constructor method that sets information of card and associates object Game to card
     *
     * @param ID                type {@link String}: card's ID
     * @param cost              type {@link String}: card's cost
     * @param effectType        type {@link String}: card's type
     * @param setup             type {@link String}: card's type
     * @param effectDescription type {@link String}: card's effect
     * @param game              type {@link Game}: object Game that represents entire game
     */
    public Replace3StudentsInEntranceCharacterCard(String ID, String cost, String effectType, String setup, String effectDescription, Game game) {
        setID(ID);
        setCost(Integer.parseInt(cost));
        setEffectType(effectType);
        setSetup(setup);
        setEffectDescription(effectDescription);
        setGame(game);
        //Add students on card
        studentsCard = getGame().getGameBoard().getStudentsFromBag(6);
    }

    /**
     * This method sets parameter that will be used to active effect
     *
     * @param studentsFromCard        type {@link Map} with keys {@link PawnType} and values {@link Integer}: entrance's students that will be added to the player's entrance
     * @param studentsFromPlayerBoard type {@link Map} with keys {@link PawnType} and values {@link Integer}: entrance's students that will be added on card
     */
    public void setParameterToEffect(Map<PawnType, Integer> studentsFromPlayerBoard, Map<PawnType, Integer> studentsFromCard, String nickname) {
        this.studentsFromPlayerBoard = studentsFromPlayerBoard;
        this.studentsFromCard = studentsFromCard;
        this.nickname = nickname;
    }

    /**
     * This method actives card's effect
     */
    @Override
    public void effect() {

        getGame().getPlayerByNickname(nickname).getPlayerboard().removeStudentFromEntrance(studentsFromPlayerBoard);
        getGame().getPlayerByNickname(nickname).getPlayerboard().addStudentsToEntrance(studentsFromCard);
        removeStudentsCard(studentsFromCard);
        addStudentsCard(studentsFromPlayerBoard);

        // situation of professor has probably changed
        getGame().assignProfessor();
    }

    /**
     * This method removes students on card
     *
     * @param students type {@link Map} with keys {@link PawnType} and values {@link Integer}:students to remove
     */
    private void removeStudentsCard(Map<PawnType, Integer> students) {
        students.forEach((key, value) -> studentsCard.put(key, studentsCard.get(key) - value));
    }

    /**
     * This method adds students on card
     *
     * @param students type {@link Map} with keys {@link PawnType} and values {@link Integer}:students to add
     */
    private void addStudentsCard(Map<PawnType, Integer> students) {
        students.forEach((key, value) -> studentsCard.put(key, studentsCard.get(key) + value));
    }

    /**
     * This method returns students on card
     *
     * @return type {@link Map} with keys {@link PawnType} and values {@link Integer}: card's students
     */
    public Map<PawnType, Integer> getStudentsCard() {
        return studentsCard;
    }

    /**
     * This helper methods count number of students into map
     * @param map type {@link Map} with keys {@link PawnType} and values {@link Integer}: students map
     * @return type int: number of students
     */

}
