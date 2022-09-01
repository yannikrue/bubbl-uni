package ch.unibas.dmi.dbis.cs108.game.client;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import java.util.ArrayList;


public class DebugClientReceiver implements ClientReceiverInterface {
    private ArrayList<String> playerNames = new ArrayList<String>(){
        {
            add("Player1");
            add("Player2");
            add("Player3");
            add("Player4");
        }
    };
    private ArrayList<Vector2D> playerPositions = new ArrayList<>();
    private ArrayList<Float> playerSizes = new ArrayList<>();
    private ArrayList<Float> playerAngles = new ArrayList<>();
    private ArrayList<Vector2D> bubblePositions = new ArrayList<>();
    private ArrayList<Integer> bubbleIds = new ArrayList<>();
    private ArrayList<Vector2D> energizerPositions = new ArrayList<>();
    private ArrayList<String> ranking = new ArrayList<>();
    public static boolean gameHasEnded = false;

    public void setPlayerPositions(ArrayList<Vector2D> playerPositions) {
        this.playerPositions = playerPositions;
    }

    public void setPlayerSizes(ArrayList<Float> playerSizes) {
        this.playerSizes = playerSizes;
    }

    public void setPlayerAngles(ArrayList<Float> angles) {

    }

    public void sendPlayerRotations(ArrayList<Float> playerAngles) {
        this.playerAngles = playerAngles;
    }

    public void setBubblePositions(ArrayList<Vector2D> bubblePositions) {
        this.bubblePositions = bubblePositions;
    }

    public void setBubbleIds(ArrayList<Integer> bubbleIds) {
        this.bubbleIds = bubbleIds;
    }

    public void setEnergizerPositions(ArrayList<Vector2D> energizerPositions) {
        this.energizerPositions = energizerPositions;
    }

    public void setPlayerNames(ArrayList<String> playerNames) {
        this.playerNames = playerNames;
    }

    public void setRanking(ArrayList<String> ranking) {
        gameHasEnded = true;
        this.ranking = ranking; }


    @Override
    public ArrayList<String> getPlayerNames() {
        return playerNames;
    }

    @Override
    public ArrayList<Vector2D> getPlayerPositions() {
        return playerPositions;
    }

    @Override
    public ArrayList<Float> getPlayerSizes() {
        return playerSizes;
    }

    @Override
    public ArrayList<Float> getPlayerAngles() {
        return playerAngles;
    }

    @Override
    public ArrayList<Vector2D> getBubblePositions() {
        return bubblePositions;
    }

    @Override
    public ArrayList<Integer> getBubbleIds() {
        return bubbleIds;
    }

    @Override
    public ArrayList<Vector2D> getEnergizerPositions() {
        return energizerPositions;
    }

    @Override
    public ArrayList<String> getRanking() { return ranking; }
}
