package it.polimi.ingsw.network.client.cli.TurnPhases;

import it.polimi.ingsw.network.client.cli.CLI;
import it.polimi.ingsw.utils.Constants;

/**
 * This class that notify the player that he lost the game
 */
public class LosePhase extends Phase{
    @Override
    public void makeAction(CLI cli) {

        System.out.println(Constants.ANSI_RED + " \n\nYOU LOST!\n\n");
        System.exit(0);
    }
}
