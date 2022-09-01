package ch.unibas.dmi.dbis.cs108.game.client;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

/**
 * The ClientSender sends information regarding
 * the player's move direction and newly created
 * bubbles to the server.
 */
public interface ClientSenderInterface {

    /**
     * Sends the move direction of the player as a 2D Vector
     * to the server.
     */
    void sendMoveDirection(Vector2D direction);

    /**
     * Sends the direction of a newly created bubble
     * to the server.
     */
    void sendBubbleCreation(Vector2D direction);

    /**
     * Sends the player angle to the server.
     */
    void sendPlayerAngle(float angle);
}