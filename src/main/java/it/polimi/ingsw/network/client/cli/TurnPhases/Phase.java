package it.polimi.ingsw.network.client.cli.TurnPhases;

import it.polimi.ingsw.network.client.cli.CLI;

import java.io.Serializable;

/**
 * class that defined a phase in the cli
 */
public abstract class Phase {

    /**
     * method that defines the action that the phase can handle and execute that
     * @param cli is client's cli
     */
    public abstract void makeAction(CLI cli);
}

