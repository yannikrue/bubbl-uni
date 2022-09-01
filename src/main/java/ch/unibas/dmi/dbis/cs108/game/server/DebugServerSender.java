package ch.unibas.dmi.dbis.cs108.game.server;

import ch.unibas.dmi.dbis.cs108.game.client.DebugClientReceiver;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import java.util.ArrayList;
import java.util.Map;

public class DebugServerSender implements ServerSenderInterface{
    private final DebugClientReceiver clientReceiver;

    public DebugServerSender(DebugClientReceiver clientReceiver){
        this.clientReceiver = clientReceiver;
    }

    @Override
    public void sendPlayerPositions(ArrayList<Vector2D> positions) {
        clientReceiver.setPlayerPositions(positions);
    }

    @Override
    public void sendPlayerSizes(ArrayList<Float> sizes) {
        clientReceiver.setPlayerSizes(sizes);
    }

    @Override
    public void sendPlayerAngles(ArrayList<Float> angles) {
        clientReceiver.sendPlayerRotations(angles);
    }

    @Override
    public void endGame(String lobbyName) {

    }

    @Override
    public void sendRanking(Map<String, Double> ranking, String time) {

    }

    @Override
    public void sendBubblePositions(ArrayList<Vector2D> positions) {
        clientReceiver.setBubblePositions(positions);
    }

    @Override
    public void sendBubbleIds(ArrayList<Integer> ids) {
        clientReceiver.setBubbleIds(ids);
    }

    @Override
    public void sendEnergizerPositions(ArrayList<Vector2D> positions) {
        clientReceiver.setEnergizerPositions(positions);
    }
}
