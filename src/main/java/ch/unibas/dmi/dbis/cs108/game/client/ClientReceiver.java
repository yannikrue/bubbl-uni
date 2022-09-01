package ch.unibas.dmi.dbis.cs108.game.client;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import java.util.ArrayList;

/**
 * {@inheritDoc}
 */
public class ClientReceiver implements ClientReceiverInterface {
    private ArrayList<String> playerNames = new ArrayList<>();
    private ArrayList<Vector2D> playerPositions = new ArrayList<>();
    private ArrayList<Float> playerSizes = new ArrayList<>();
    private ArrayList<Float> playerRotations = new ArrayList<>();
    private ArrayList<Vector2D> bubblePositions = new ArrayList<>();
    private ArrayList<Integer> bubbleIds = new ArrayList<>();
    private ArrayList<Vector2D> energizerPositions = new ArrayList<>();
    private ArrayList<String> ranking = new ArrayList<>();
    public static boolean gameHasEnded = false;

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPlayerPositions(ArrayList<Vector2D> playerPositions) {
        this.playerPositions = playerPositions;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPlayerSizes(ArrayList<Float> playerSizes) {
        this.playerSizes = playerSizes;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPlayerAngles(ArrayList<Float> playerAngles) {
        this.playerRotations = playerAngles;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setBubblePositions(ArrayList<Vector2D> bubblePositions) {
        this.bubblePositions = bubblePositions;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setBubbleIds(ArrayList<Integer> bubbleIds) {
        this.bubbleIds = bubbleIds;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setEnergizerPositions(ArrayList<Vector2D> energizerPositions) {
        this.energizerPositions = energizerPositions;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPlayerNames(ArrayList<String> playerNames) {
        this.playerNames = playerNames;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setRanking(ArrayList<String> ranking) {
        gameHasEnded = true;
        this.ranking = ranking; }

    /**
     * {@inheritDoc}
     */
    @Override
    public ArrayList<String> getPlayerNames() {
        return playerNames;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ArrayList<Vector2D> getPlayerPositions() {
        return playerPositions;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ArrayList<Float> getPlayerSizes() {
        return playerSizes;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ArrayList<Float> getPlayerAngles() {
        return playerRotations;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ArrayList<Vector2D> getBubblePositions() {
        return bubblePositions;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ArrayList<Integer> getBubbleIds() {
        return bubbleIds;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ArrayList<Vector2D> getEnergizerPositions() {
        return energizerPositions;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ArrayList<String> getRanking() { return ranking; }
}
