package ch.unibas.dmi.dbis.cs108.game.server;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

/**
 * The ServerReceiver is responsible for receiving the inputs
 * made by the players, such as the players movement direction
 * and mouse position and bubble creations.
 *
 */
public interface ServerReceiverInterface {

    /**
     * Receives the move direction of a player.
     */
    Vector2D receiveMoveDirection(int playerId);

    /**
     * Receives bubble creations.
     */
    Vector2D receiveBubbleCreation(int playerId);

    /**
     * Receives the player angle.
     */
    float receivePlayerAngle(int playerId);
}
