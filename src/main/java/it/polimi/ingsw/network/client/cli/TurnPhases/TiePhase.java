package it.polimi.ingsw.network.client.cli.TurnPhases;

import it.polimi.ingsw.network.client.cli.CLI;
import it.polimi.ingsw.utils.Constants;

public class TiePhase extends Phase{

    @Override
    public void makeAction(CLI cli) {
        System.out.println(Constants.ANSI_GREEN+ "\n\n GAME ENDED. IT WAS A TIE!\n\n ");
        System.exit(0);
    }
}
