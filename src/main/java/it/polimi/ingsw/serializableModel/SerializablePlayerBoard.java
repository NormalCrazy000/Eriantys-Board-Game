package it.polimi.ingsw.serializableModel;

import it.polimi.ingsw.model.AssistantCard;
import it.polimi.ingsw.model.PawnType;
import it.polimi.ingsw.model.PlayerBoard;
import it.polimi.ingsw.model.TowerColor;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Serializable class that contains the information needed by the view.
 * Light copy of {@link it.polimi.ingsw.model.PlayerBoard}.
 */
public class SerializablePlayerBoard implements Serializable {

    private final Map<PawnType,Integer> entrance;
    private final Map<PawnType,Integer> diningRoom;
    private final Map<PawnType,Boolean> professors;
    private boolean characterCardProfessors;
    private int towers;
    private TowerColor towerColor;
    private int coins;

    private AssistantCard assistantCard;

    public SerializablePlayerBoard(PlayerBoard playerBoard){

        this.entrance = playerBoard.getAllStudentsEntrance();
        this.diningRoom = playerBoard.getAllStudentsOrderDiningRoom();
        this.professors = playerBoard.getProfessors();
        this.towers = playerBoard.getNumberOfTower();
        this.towerColor = playerBoard.getTowerColor();
        this.coins = playerBoard.getCoins();
        this.characterCardProfessors = playerBoard.isCharacterCardProfessors();
        this.assistantCard = null;
    }


    public Map<PawnType, Integer> getEntrance() {
        return entrance;
    }

    public Map<PawnType, Integer> getDiningRoom() {
        return diningRoom;
    }

    public Map<PawnType, Boolean> getProfessors() {
        return professors;
    }

    public boolean isCharacterCardProfessors() {
        return characterCardProfessors;
    }

    public int getTowers() {
        return towers;
    }

    public TowerColor getTowerColor() {
        return towerColor;
    }

    public int getCoins() {
        return coins;
    }

    public AssistantCard getAssistantCard() {
        return assistantCard;
    }

    public void setTowers(int towers) {
        this.towers = towers;
    }

    public void setTowerColor(TowerColor towerColor) {
        this.towerColor = towerColor;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public void setAssistantCard(AssistantCard assistantCard) {
        this.assistantCard = assistantCard;
    }
}
