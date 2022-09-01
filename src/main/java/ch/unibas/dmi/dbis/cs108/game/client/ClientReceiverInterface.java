package ch.unibas.dmi.dbis.cs108.game.client;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import java.util.ArrayList;

/**
 * The ClientReceiver is responsible for receiving the
 * properties of all non-static objects in the game, such
 * as the player's sizes and positions, the bubble's positions
 * and the energizer's positions.
 */
public interface ClientReceiverInterface {

    /**
     * Gets the player names.
     */
    ArrayList<String> getPlayerNames();

    /**
     * Gets the player positions.
     */
    ArrayList<Vector2D> getPlayerPositions();

    /**
     * Gets the player sizes.
     */
    ArrayList<Float> getPlayerSizes();

    /**
     * Gets the player rotations.
     */
    ArrayList<Float> getPlayerAngles();

    /**
     * Gets the bubble positions.
     */
    ArrayList<Vector2D> getBubblePositions();

    /**
     * Gets the bubble ids.
     */
    ArrayList<Integer> getBubbleIds();

    /**
     * Gets the energizer positions.
     */
    ArrayList<Vector2D> getEnergizerPositions();

    /**
     * Gets the ranking.
     */
    ArrayList<String> getRanking();

    /**
     * Sets the player's names.
     */
    void setPlayerNames(ArrayList<String> splitParameter);

    /**
     * Sets the player's positions.
     */
    void setPlayerPositions(ArrayList<Vector2D> positions);

    /**
     * Sets the player's sizes.
     */
    void setPlayerSizes(ArrayList<Float> sizes);

    /**
     * Sets the player's angles.
     */
    void setPlayerAngles(ArrayList<Float> angles);

    /**
     * Sets the bubble's positions.
     */
    void setBubblePositions(ArrayList<Vector2D> bubblePositions);

    /**
     * Sets the bubble's ids.
     */
    void setBubbleIds(ArrayList<Integer> bubbleIds);

    /**
     * Sets the energizer's positions.
     */
    void setEnergizerPositions(ArrayList<Vector2D> energizerPositions);

    /**
     * Sets the ranking.
     */
    void setRanking(ArrayList<String> ranking);
}