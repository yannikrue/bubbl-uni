package ch.unibas.dmi.dbis.cs108.game.client;

import ch.unibas.dmi.dbis.cs108.game.server.ServerReceiver;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

public class DebugClientSender implements ClientSenderInterface{
    private final ServerReceiver serverReceiver;
    private final int playerId;

    public DebugClientSender(int playerId, ServerReceiver serverReceiver){
        this.playerId = playerId;
        this.serverReceiver = serverReceiver;
    }

    @Override
    public void sendMoveDirection(Vector2D direction) {
        serverReceiver.insertMoveDirection(playerId, direction);
    }

    @Override
    public void sendBubbleCreation(Vector2D direction) {
        serverReceiver.insertBubbleCreation(playerId, direction);
    }

    @Override
    public void sendPlayerAngle(float angle){serverReceiver.insertPlayerAngle(playerId, angle);
    }
}
