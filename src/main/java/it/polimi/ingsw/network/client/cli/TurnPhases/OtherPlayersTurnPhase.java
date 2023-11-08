package it.polimi.ingsw.network.client.cli.TurnPhases;

import it.polimi.ingsw.network.client.cli.CLI;
import it.polimi.ingsw.utils.Constants;

/**
 * This class puts a player in wait until the other players have terminated their action
 */
public class OtherPlayersTurnPhase extends Phase{


    @Override
    public void makeAction(CLI cli) {

        System.out.println(Constants.ANSI_YELLOW + "Waiting for other player to finish their turn!" +Constants.ANSI_RESET);

        synchronized (this) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

//        cli.setGamePhase(new SelectAssistantCardPhase());
//        new Thread(cli).start();

    }
}
