package ch.unibas.dmi.dbis.cs108.game.client;

import ch.unibas.dmi.dbis.cs108.client.Client;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

/**
 * {@inheritDoc}
 */
public class ClientSender implements ClientSenderInterface{
    private final int playerId;
    private final Client client;

    public ClientSender(Client client){
        this.playerId = client.getPlayerId();
        this.client = client;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendMoveDirection(Vector2D direction) {
        client.askServerToMoveTo(direction);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendBubbleCreation(Vector2D direction) {
        client.askToCreateBubble(direction);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendPlayerAngle(float angle){
        client.askServerToUpdateAngle(angle);
    }
}
