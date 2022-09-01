package ch.unibas.dmi.dbis.cs108.game.server;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import java.util.ArrayList;

/**
 * {@inheritDoc}
 */
public class ServerReceiver implements ServerReceiverInterface {
    private final ArrayList<Vector2D> playerMoveDirections = new ArrayList<>();
    private final ArrayList<Vector2D> bubbleDirections = new ArrayList<>();
    private final ArrayList<Float> playerRotations = new ArrayList<>();

    public ServerReceiver(int playerCount) {
        for (int i = 0; i < playerCount; i++) {
            playerMoveDirections.add(Vector2D.ZERO);
            bubbleDirections.add(null);
            playerRotations.add(0f);
        }
    }

    public void insertMoveDirection(int playerId, Vector2D direction) {
        playerMoveDirections.set(playerId, direction);
    }

    public void insertBubbleCreation(int playerId, Vector2D direction) {
        bubbleDirections.set(playerId, direction);
    }

    public void insertPlayerAngle(int playerId, float angle) {
        playerRotations.set(playerId, angle);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Vector2D receiveMoveDirection(int playerId) {
        return playerMoveDirections.get(playerId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Vector2D receiveBubbleCreation(int playerId) {
        var bubbleDirection = bubbleDirections.get(playerId);
        bubbleDirections.set(playerId, null);
        return bubbleDirection;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public float receivePlayerAngle(int playerId) {
        return playerRotations.get(playerId);
    }

}
