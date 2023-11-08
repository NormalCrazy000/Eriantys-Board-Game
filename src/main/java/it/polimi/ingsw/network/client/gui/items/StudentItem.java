package it.polimi.ingsw.network.client.gui.items;

import it.polimi.ingsw.model.PawnType;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Ellipse;

/**
 * This class represents Student item.
 * This item cannot move from its position
 */
public class StudentItem extends Ellipse {
    private PawnType color;
    private GridPane grid;
    private int row;
    private int column;
    private AnchorPane anchorFather;

    /**
     * This method creates the graphical item
     *
     * @param color        type {@link PawnType}: student's color
     * @param grid         type {@link GridPane}: gridPane that contains student
     * @param column       type int: col into gridPane
     * @param row          type int: row into gridPane
     * @param anchorFather type {@link AnchorPane}: this represents the anchorPane father of the scene.
     */
    public StudentItem(PawnType color, GridPane grid, int column, int row, AnchorPane anchorFather) {
        this.color = color;
        this.grid = grid;
        this.row = row;
        this.column = column;
        this.anchorFather = anchorFather;
        setRadiusX(15);
        setRadiusY(15);
        String pathImage = "";
        switch (color) {
            case YELLOW:
                pathImage = "/Assets/yellowstud.png";
                break;
            case RED:
                pathImage = "/Assets/redstud.png";
                break;
            case GREEN:
                pathImage = "/Assets/greenstud.png";
                break;
            case BLUE:
                pathImage = "/Assets/bluestud.png";
                break;
            case PINK:
                pathImage = "/Assets/purplestud.png";
                break;
        }
        Image image = new Image(pathImage);
        this.setFill(new ImagePattern(image));
    }

    /**
     * This method is used to return grid Pane
     *
     * @return type {@link GridPane}: gridPane obejct
     */
    public GridPane getGrid() {
        return grid;
    }

    /**
     * This method returns student's color
     *
     * @return type {@link PawnType}: professor's color
     */
    public PawnType getColor() {
        return color;
    }

    /**
     * This method returns anchorfather of the scene
     *
     * @return type {@link AnchorPane}: AnchorPane object
     */
    public AnchorPane getFather() {
        return anchorFather;
    }

    /**
     * This method returns row
     *
     * @return type int: row
     */
    public int getRow() {
        return row;
    }

    /**
     * This method returns col
     *
     * @return type int: col
     */
    public int getColumn() {
        return column;
    }
}
