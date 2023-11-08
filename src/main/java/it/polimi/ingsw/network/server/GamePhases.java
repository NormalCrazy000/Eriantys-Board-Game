package it.polimi.ingsw.network.server;

import java.io.Serializable;

/**
 * enum that represent all the possible phases in which can be the game server side
 */
public enum GamePhases implements Serializable {
    CONFIGURATION,
    INITIALIZATION,
    GAME,
    ENDGAME
}
