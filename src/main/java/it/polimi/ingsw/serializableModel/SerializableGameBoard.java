package it.polimi.ingsw.serializableModel;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.utils.Constants;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

/**
 * Serializable class that contains the information needed by the view.
 * Light copy of the {@link GameBoard}.
 */
public class SerializableGameBoard implements Serializable {

    private  ArrayList<IslandRegion> regions;
    private  CloudTile[] clouds;
    private  Map<PawnType,Integer> studentsBag;
    private int motherIndex;
    private int coins;
    private CharacterDeck characterDeck;
    private GameMode gameMode;


    public SerializableGameBoard(GameBoard gameBoard){
        this.regions = gameBoard.getRegions();
        this.clouds = gameBoard.getClouds();
        this.studentsBag = gameBoard.getStudentsBag();
        this.coins = gameBoard.getCoins();
        this.motherIndex = gameBoard.getIndexMother();
        this.characterDeck = gameBoard.getCharacterDeck();
        this.gameMode = gameBoard.getGameMode();
    }


    public ArrayList<IslandRegion> getRegions() {
        return regions;
    }

    public CloudTile[] getClouds() {
        return clouds;
    }

    public Map<PawnType, Integer> getStudentsBag() {
        return studentsBag;
    }

    public int getMotherIndex() {
        return motherIndex;
    }

    public int getCoins() {
        return coins;
    }

    public CharacterDeck getCharacterDeck() {
        return characterDeck;
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public void setMotherIndex(int motherIndex) {
        this.motherIndex = motherIndex;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public void setRegions(ArrayList<IslandRegion> regions) {
        this.regions = regions;
    }

    public void setClouds(CloudTile[] clouds) {
        this.clouds = clouds;
    }

    public void setStudentsBag(Map<PawnType, Integer> studentsBag) {
        this.studentsBag = studentsBag;
    }

    public void setCharacterDeck(CharacterDeck characterDeck) {
        this.characterDeck = characterDeck;
    }

    public void setGameMode(GameMode gameMode) {
        this.gameMode = gameMode;
    }

    public boolean isBagEmpty(){
        return studentsBag.values().stream().mapToInt(e-> e).sum() == 0;
    }

}
