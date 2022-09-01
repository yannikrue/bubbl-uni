package ch.unibas.dmi.dbis.cs108.game.server;

import ch.unibas.dmi.dbis.cs108.server.Server;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import java.util.ArrayList;
import java.util.Map;

/**
 * {@inheritDoc}
 */
public class ServerSender implements ServerSenderInterface {
    private final Server server;
    private final String lobbyName;

    public ServerSender(Server server, String lobbyName) {
        this.server = server;
        this.lobbyName = lobbyName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendPlayerPositions(ArrayList<Vector2D> positions) {
        server.sendPlayerPositions(lobbyName, positions);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendPlayerSizes(ArrayList<Float> sizes) {
        server.sendPlayerSizes(lobbyName, sizes);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendPlayerAngles(ArrayList<Float> angles) { server.sendPlayerAngles(lobbyName, angles); }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendBubblePositions(ArrayList<Vector2D> positions) {
        server.sendBubblePositions(lobbyName, positions);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendBubbleIds(ArrayList<Integer> ids) {
        server.sendBubbleIds(lobbyName, ids);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendEnergizerPositions(ArrayList<Vector2D> positions) {
        server.sendEnergizerPositions(lobbyName, positions);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void endGame(String lobbyName) {
        server.endGame(lobbyName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendRanking(Map<String, Double> ranking, String time) {
        server.sendRanking(lobbyName, ranking, time);
    }
}
