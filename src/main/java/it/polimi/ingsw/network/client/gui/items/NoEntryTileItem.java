package it.polimi.ingsw.network.client.gui.items;

import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Ellipse;

/**
 * This class represents NoEntryTile item.
 * This item cannot move from its position
 */
public class NoEntryTileItem extends Ellipse {
    private GridPane grid;
    private int row;
    private int column;
    private AnchorPane anchorFather;

    /**
     * This method creates the graphical item
     *
     * @param grid         type {@link GridPane}: gridPane that contains student
     * @param column       type int: col into gridPane
     * @param row          type int: row into gridPane
     * @param anchorFather type {@link AnchorPane}: this represents the anchorPane father of the scene.
     */
    public NoEntryTileItem(GridPane grid, int column, int row, AnchorPane anchorFather) {
        this.grid = grid;
        this.row = row;
        this.column = column;
        this.anchorFather = anchorFather;
        setRadiusX(15);
        setRadiusY(15);
        String pathImage = "/Assets/noentry.png";
        Image image = new Image(pathImage);
        this.setFill(new ImagePattern(image));
    }
}