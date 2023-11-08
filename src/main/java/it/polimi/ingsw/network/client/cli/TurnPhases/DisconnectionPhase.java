package it.polimi.ingsw.network.client.cli.TurnPhases;

import it.polimi.ingsw.network.client.cli.CLI;
import it.polimi.ingsw.utils.Constants;

/**
 * This class handles the Phase where the player is notified that someone has disconnected from teh server
 */
public class DisconnectionPhase extends Phase{
    @Override
    public void makeAction(CLI cli) {

        System.out.println(Constants.ANSI_RED + "The game is ended due to an early disconnection " +
                "of a player. Please restart the application "+ Constants.ANSI_RESET);
        System.exit(0);
    }
}
