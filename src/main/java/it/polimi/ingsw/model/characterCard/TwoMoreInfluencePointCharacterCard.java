package it.polimi.ingsw.model.characterCard;

import it.polimi.ingsw.model.CharacterCard;
import it.polimi.ingsw.model.Player;

import java.io.Serializable;


/**
 * TwoMoreInfluencePointCharacterCard class
 * This class implements effect of card number 2:
 * During the influence calculation this turn, you count as having 2 more influence
 *
 * @author Christian Lisi
 */
public class TwoMoreInfluencePointCharacterCard extends CharacterCard implements Serializable {

    String nickname;

    /**
     * Constructor method that sets information of card and associates object Game to card
     *
     * @param ID                type {@link String}: card's ID
     * @param cost              type {@link String}: card's cost
     * @param effectType        type {@link String}: card's type
     * @param effectDescription type {@link String}: card's effect
     */
    public TwoMoreInfluencePointCharacterCard(String ID, String cost, String effectType, String effectDescription) {
        setID(ID);
        setCost(Integer.parseInt(cost));
        setEffectType(effectType);
        setSetup("");
        setEffectDescription(effectDescription);
    }

    /**
     * This method is used to set up parameter to effect card
     * @param nickname type {@link String}: player's nickname
     */
    public void setParameterToEffect(String nickname) {
        this.nickname = nickname;
    }


    /**
     * This method resets card's effect.
     * It must be called at the end turn
     */
    @Override
    public void resetEffect(String nickname) {
        getGame().getPlayerByNickname(nickname).setAdditionalInfluence(0);
        super.resetEffect(nickname);
    }

    /**
     * This method actives card's effect
     */
    @Override
    public void effect() {
        getGame().getCurrentPlayer().setAdditionalInfluence(2);

        for (Player player : getGame().getPlayers()) {
            System.out.println(player.getNickname() + " : " + player.getAdditionalInfluence());
        }

    }
}
