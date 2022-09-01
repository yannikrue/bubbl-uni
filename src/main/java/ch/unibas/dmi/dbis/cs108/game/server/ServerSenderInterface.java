package ch.unibas.dmi.dbis.cs108.game.server;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import java.util.ArrayList;
import java.util.Map;

/**
 * The ServerSender is responsible for sending information, for example
 * the player and bubble positions to the clients so the game state can be
 * displayed correctly for each player.
 */
public interface ServerSenderInterface {

    /**
     * Sends the player positions to the client.
     */
    void sendPlayerPositions(ArrayList<Vector2D> positions);

    /**
     * Sends the player sizes to the client.
     */
    void sendPlayerSizes(ArrayList<Float> sizes);

    /**
     * Sends the bubble positions to the client.
     */
    void sendBubblePositions(ArrayList<Vector2D> positions);

    /**
     * Sends the bubble ids to the client
     */
    void sendBubbleIds(ArrayList<Integer>  ids);

    /**
     * Sends the energizer positions to the client.
     */
    void sendEnergizerPositions(ArrayList<Vector2D> positions);

    /**
     * Sends the player angles to the client.
     */
    void sendPlayerAngles(ArrayList<Float> angles);

    /**
     * Ends the game.
     */
    void endGame(String lobbyName);

    /**
     * Sends the ranking to the client.
     */
    void sendRanking(Map<String, Double> ranking, String time);
}
