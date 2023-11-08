package it.polimi.ingsw.network.client.cli.TurnPhases;

import it.polimi.ingsw.network.client.cli.CLI;
import it.polimi.ingsw.utils.Constants;

/**
 * This class notify the player that he won the game
 */
public class WinnerPhase extends Phase{
    @Override
    public void makeAction(CLI cli) {
        System.out.println(Constants.ANSI_GREEN+ "\n\n CONGRATULATION. YOU WIN!\n\n ");
        System.exit(0);
    }
}
