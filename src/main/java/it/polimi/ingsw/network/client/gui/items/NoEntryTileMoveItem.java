package it.polimi.ingsw.network.client.gui.items;

import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

/**
 * This class represents NoEntryItem item.
 * This item can move from its position. It is used where item must be moved
 */
public class NoEntryTileMoveItem extends NoEntryTileItem {
    /**
     * This method creates the graphical item
     *
     * @param grid         type {@link GridPane}: gridPane that contains student
     * @param column       type int: col into gridPane
     * @param row          type int: row into gridPane
     * @param anchorFather type {@link AnchorPane}: this represents the anchorPane father of the scene.
     */
    public NoEntryTileMoveItem(GridPane grid, int column, int row, AnchorPane anchorFather) {
        super(grid, column, row, anchorFather);
    }
}
