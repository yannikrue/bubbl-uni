package ch.unibas.dmi.dbis.cs108.mockups;

import ch.unibas.dmi.dbis.cs108.game.client.ClientReceiverInterface;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import java.util.ArrayList;

public class ClientReceiverMockup implements ClientReceiverInterface {
    @Override
    public ArrayList<String> getPlayerNames() {
        return null;
    }

    @Override
    public ArrayList<Vector2D> getPlayerPositions() {
        return null;
    }

    @Override
    public ArrayList<Float> getPlayerSizes() {
        return null;
    }

    @Override
    public ArrayList<Float> getPlayerAngles() {
        return null;
    }

    @Override
    public ArrayList<Vector2D> getBubblePositions() {
        return null;
    }

    @Override
    public ArrayList<Integer> getBubbleIds() {
        return null;
    }

    @Override
    public ArrayList<Vector2D> getEnergizerPositions() {
        return null;
    }

    @Override
    public ArrayList<String> getRanking() { return null; }

    @Override
    public void setPlayerNames(ArrayList<String> splitParameter) {

    }

    @Override
    public void setRanking(ArrayList<String> ranking) {

    }

    @Override
    public void setPlayerPositions(ArrayList<Vector2D> positions) {

    }

    @Override
    public void setPlayerSizes(ArrayList<Float> sizes) {

    }

    @Override
    public void setPlayerAngles(ArrayList<Float> angles) {

    }

    @Override
    public void setBubblePositions(ArrayList<Vector2D> bubblePositions) {

    }

    @Override
    public void setBubbleIds(ArrayList<Integer> bubbleIds) {

    }

    @Override
    public void setEnergizerPositions(ArrayList<Vector2D> energizerPositions) {

    }
}
