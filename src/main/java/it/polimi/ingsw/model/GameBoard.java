package it.polimi.ingsw.model;


import java.io.Serializable;
import java.util.*;

/**
 * GameBoard Class
 *
 * @author Gabriele Giannotto
 */
public class GameBoard implements Serializable {

    private final ArrayList<IslandRegion> regions;
    private final CloudTile[] clouds;
    private final Map<PawnType, Integer> studentsBag;
    private int motherIndex;
    private int coins;
    private GameMode gameMode;

    private CharacterDeck characterDeck;

    /**
     * This constructor set up game board for all gamemode(expert/easy mode and two/three/four players)
     * Setup: one student on each regions except mother's region and the one in front
     *
     * @param numberPlayer (int type): number of players.It is used to set number of clouds
     */
    public GameBoard(int numberPlayer) {
        regions = new ArrayList<>();
        //Setup students bag.
        // 24 for each color because 10 students will be added on gameBord
        studentsBag = new HashMap<>();

        for (PawnType c : PawnType.values()) {
            studentsBag.put(c, 24);
        }
        //Setup number coins
        coins = 20;
        //Create regions(12)
        for (int i = 0; i < 12; i++) {
            regions.add(new IslandRegion());
        }

        //Create cloud in accordance with numberPlayer
        clouds = new CloudTile[numberPlayer];
        for (int i = 0; i < clouds.length; i++) {
            clouds[i] = new CloudTile();
        }
        //Create random mother position on gameBoard
        Random ran = new Random();

        int indexMotherRandom = ran.nextInt(12);

        //Set mother flag on region designated
        regions.get(indexMotherRandom).setMother(true);
        motherIndex = indexMotherRandom;
        //Create list that contains students to be added( 10 students: 2 for each color)
        ArrayList<PawnType> studentSetup = new ArrayList<>();
        for (PawnType c : PawnType.values()) {
            studentSetup.add(c);
            studentSetup.add(c);
        }
        //Randomized list
        Collections.shuffle(studentSetup);
        //Add studentsSetup on regions
        int indexRegions = 0;

        while (indexRegions < 12) {
            //Check if region(indexRegions) isn't in front of mother's region or isn't mother regions
            if (Math.abs(indexRegions - motherIndex) != 6 && indexRegions != motherIndex) {
                //Create map to add student on region and set with 0 value for each color
                Map<PawnType, Integer> mapToAddStudentsSetup = new HashMap<>();
                for (PawnType c : PawnType.values()) {
                    mapToAddStudentsSetup.put(c, 0);
                }
                //Add student on map(first value of random color list )
                mapToAddStudentsSetup.put(studentSetup.get(0), 1);
                //Add students on region
                regions.get(indexRegions).addStudents(mapToAddStudentsSetup);
                //Remove student random color list(remove first value)
                studentSetup.remove(0);
            }
            indexRegions++;
        }
    }

    /**
     * It checks if region is near(left near) a region with same tower and if it is aggregate regions
     */
    public void checkIslandAggregation() {
        for (int i = 0; i < regions.size(); i++) {
            //Check if checkRegion is not null and if the near left region has same tower
            if (!(regions.get(i).getColorTower() == null) && nearTowerRegion(i)) {
                //Exception if number of region is 3 and regions could still aggregate
                if (checkNumberOfRegions() < 4) /*throw new MinRegionAggregationException();*/ break;
                //Aggregate regions
                aggregateRegions(i, nearIndexRegion(i));
                //To reset control
                i--;
            }
        }
    }

    /**
     * This method removes region from group's regions
     *
     * @param indexRemove (int type): the value of region's index
     */
    private void removeRegion(int indexRemove) {
        regions.remove(indexRemove);
    }

    /**
     * It moves students and towers from region(startRegion) to region(endRegion)
     *
     * @param endRegion   (int type): the region destination index
     * @param startRegion (int type): the region start index
     */
    private void aggregateRegions(int endRegion, int startRegion) {
        regions.get(endRegion).addStudents(regions.get(startRegion).getAllStudentsOrder());
        //Check if startRegion has mother flag
        if (regions.get(startRegion).isMother()) {
            regions.get(endRegion).setMother(true);
            regions.get(startRegion).setMother(false);
        }

        //Add tower startRegion on endRegion
        regions.get(endRegion).addTower(regions.get(startRegion).numberOfTowers());
        for (int i = 0; i < regions.get(startRegion).getNoEntry(); i++) {
            regions.get(endRegion).addNoEntry();
        }
        //Remove startRegion
        removeRegion(startRegion);
        int i = 0;
        for (IslandRegion region : getRegions()) {
            if (region.isMother()) {
                motherIndex = i;
            }
            i++;
        }

    }

    /**
     * It checks the tower of the left near region
     *
     * @param indexCheckRegion (int type): index of the region to check
     * @return (bool type):Returns:true if region is the same, else false. If near region tower is null return false
     */
    private boolean nearTowerRegion(int indexCheckRegion) {
        //index: regionIndex of left near region
        int index = nearIndexRegion(indexCheckRegion);
        return regions.get(index).getColorTower() != null && regions.get(index).getColorTower().equals(regions.get(indexCheckRegion).getColorTower());
    }

    /**
     * It returns the index of the left near region
     *
     * @param indexCheckRegion (int type): index of the region to check
     * @return (int type): index of left region
     */
    private int nearIndexRegion(int indexCheckRegion) {
        int indexL;
        //Check if index is not first index
        if ((indexCheckRegion > 0 && indexCheckRegion < regions.size() - 1) || (indexCheckRegion == regions.size() - 1)) {
            indexL = indexCheckRegion - 1;
        } else {
            indexL = regions.size() - 1;
        }
        return indexL;
    }

    /**
     * It returns number of region(it is variable because regions can be aggregated
     *
     * @return (int type): number of regions
     */
    public int checkNumberOfRegions() {
        return regions.size();
    }

    /**
     * It "draws" students and removes them from bag
     *
     * @param numberOfStudents (int type): number of students that need to be drawn
     * @return type {@link Map} with keys {@link PawnType} and values {@link Integer}: "draw" students
     */
    public Map<PawnType, Integer> getStudentsFromBag(int numberOfStudents) {
        //Check number of students into bag. If the number of students required is greater than it only draws those available
        if (numberOfStudents > checkNumberOfStudentsOnBag()) {
            numberOfStudents = checkNumberOfStudentsOnBag();
        }
        //Create map to insert required students and setup it
        Map<PawnType, Integer> chosenStudents = new HashMap<>();
        for (PawnType c : PawnType.values()) {
            chosenStudents.put(c, 0);
        }
        //Add random student into chosenStudents
        int index = 0;
        while (index < numberOfStudents) {
            ArrayList<PawnType> color = randomList();
            //Check if color in fist position isn't into bagStudent
            while (studentsBag.get(color.get(0)) == 0) {
                //remove first elements because color isn't into bag
                color.remove(0);
            }
            //Add student on map that represents request student
            chosenStudents.put(color.get(0), chosenStudents.get(color.get(0)) + 1);
            //Remove student into bag
            studentsBag.put(color.get(0), studentsBag.get(color.get(0)) - 1);
            index++;
        }
        return chosenStudents;
    }

    /**
     * It returns randomize Colors List(it uses PawnType class)
     *
     * @return (PawnType type): casual Color List(5 elements)
     */
    private ArrayList<PawnType> randomList() {
        //Create list with all colors and randomize it.
        ArrayList<PawnType> colorList = new ArrayList<>();
        Collections.addAll(colorList, PawnType.values());
        Collections.shuffle(colorList);
        return colorList;
    }

    /**
     * Check bag's students
     *
     * @return (bool type): true if bag is empty,else false
     */
    public boolean isStudentsBagEmpty() {
        return checkNumberOfStudentsOnBag() == 0;
    }

    /**
     * it returns number of students into bag
     *
     * @return (int type): number of students into bag
     */
    public int checkNumberOfStudentsOnBag() {
        //studentsBag.values().stream().mapToInt(e->e).sum();?????????
        return studentsBag.values().stream().mapToInt(e -> e).sum();
    }

    /**
     * It returns region
     *
     * @param index (int type): region index
     * @return {@link IslandRegion type}: region
     */
    public IslandRegion getRegion(int index) {
        return regions.get(index);
    }

    /**
     * It returns number of clouds
     *
     * @return (int type): number of clouds
     */
    public int checkNumberOfClouds() {
        return clouds.length;
    }

    /**
     * It returns cloud
     *
     * @param index (int type): cloud index
     * @return {@link CloudTile type}: cloud
     */
    public CloudTile getCloud(int index) {
        return clouds[index];
    }

    /**
     * It moves mother between regions.
     *
     * @param movement (int type) mother nature's movement on game board
     */
    public void moveMotherNature(int movement) {

        System.out.println("Qyesti sono i moviemnti" + movement);
        //Set new mother index
        int newIndexMother = motherIndex + movement;
        //Check if motherIndex + movement is bigger than number of regions
        while (newIndexMother >= checkNumberOfRegions()) {
            //fix the index of the new position
            newIndexMother = newIndexMother - checkNumberOfRegions();
        }
        System.out.println("Qyesti è il nuovo indice" + newIndexMother);
        System.out.println("Qyesti è il vecchio indice" + motherIndex);

        //Remove mother on old region
        regions.get(motherIndex).setMother(false);
        //Add mother on new region
        regions.get(newIndexMother).setMother(true);
        //Set index mother
        motherIndex = newIndexMother;
    }

    /**
     * It returns Mother's index
     *
     * @return (type int): Mother's index
     */
    public int getIndexMother() {
        return motherIndex;
    }

    /**
     * This method adds students into a bag
     *
     * @param students Map<PawnType,Integer> : students to be added
     */
    public void addPlayerIntoBag(Map<PawnType, Integer> students) {
        for (Map.Entry<PawnType, Integer> entry : students.entrySet()) {
            studentsBag.put(entry.getKey(), studentsBag.get(entry.getKey()) + entry.getValue());
        }
    }

    /**
     * This method adds coins into board
     *
     * @param numberOfCoins (type int): number of coins to add
     */
    public void addCoins(int numberOfCoins) {
        coins = coins + numberOfCoins;
    }

    /**
     * This method removes coins into board
     *
     * @param numberOfCoins (type int): number of coins to remove
     */
    public void removeCoins(int numberOfCoins) {
        coins = coins - numberOfCoins;
    }

    /**
     * This method returns number of coins on board
     *
     * @return (type int): number of coins
     */
    public int getCoins() {
        return coins;
    }

    /**
     * This method is used to return character deck
     *
     * @return type {@link CharacterDeck}: character deck
     */
    public CharacterDeck getCharacterDeck() {
        return characterDeck;
    }

    /**
     * This method is used to set up character deck
     *
     * @param characterDeck type {@link CharacterDeck}: character deck
     */
    public void setCharacterDeck(CharacterDeck characterDeck) {
        this.characterDeck = characterDeck;
    }

    /**
     * This method is used to set up game mode
     *
     * @param gameMode type {@link GameMode}: game mode
     */
    public void setGameMode(GameMode gameMode) {
        this.gameMode = gameMode;
    }

    /**
     * This method returns game mode
     *
     * @return type {@link GameMode}: game mode
     */
    public GameMode getGameMode() {
        return gameMode;
    }

    /**
     * This method returns islands
     *
     * @return type {@link ArrayList<IslandRegion>}: islands
     */
    public ArrayList<IslandRegion> getRegions() {
        return regions;
    }

    /**
     * This method returns cloud
     *
     * @return type array with value {@link CloudTile}: clouds
     */
    public CloudTile[] getClouds() {
        return clouds;
    }

    /**
     * This method returns students on bag
     *
     * @return type Map with key {@link PawnType} and value {@link Integer}: students on bag
     */
    public Map<PawnType, Integer> getStudentsBag() {
        return studentsBag;
    }
}




