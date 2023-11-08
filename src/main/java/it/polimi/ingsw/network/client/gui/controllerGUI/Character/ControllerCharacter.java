package it.polimi.ingsw.network.client.gui.controllerGUI.Character;

import it.polimi.ingsw.network.client.gui.GUI;
/**
 * This abstract class is used to manage scene
 */
public abstract class ControllerCharacter {
    private GUI gui;

    /**
     * This method is called to add GUI object into controller
     * @param gui type {@link GUI}: GUI object
     */
    public void setGui(GUI gui) {
        this.gui = gui;
    }

    /**
     * This method returns GUI object
     * @return type {@link GUI}: GUI object
     */
    public GUI getGui() {
        return gui;
    }

    /**
     * This method is used to set up the scene.
     * @param gui type {@link GUI}: GUI object
     */
    public void setup(GUI gui) {
        setGui(gui);
    }
}
