package ch.unibas.dmi.dbis.cs108.game.server;

import ch.unibas.dmi.dbis.cs108.game.server.collider.Collideable;
import ch.unibas.dmi.dbis.cs108.game.server.collider.ColliderType;
import ch.unibas.dmi.dbis.cs108.game.server.collider.RectangleCollider;
import javafx.geometry.Rectangle2D;

import java.util.HashSet;

/**
 * {@inheritDoc}
 */
public class GameWorld implements GameWorldInterface {
    private final HashSet<Bubble> bubbles = new HashSet<>();
    private final HashSet<Energizer> energizers = new HashSet<>();
    private final HashSet<Collideable> colliders = new HashSet<>();
    private final Player[] players;
    private final Rectangle2D[] blocks;

    private final HashSet<Bubble> markedBubbles = new HashSet<>();
    private final HashSet<Energizer> markedEnergizers = new HashSet<>();
    private double inGameMass;
    private double maxMass = 4;

    public GameWorld(Player[] players, Rectangle2D[] blocks) {
        this.players = players;
        this.blocks = blocks;
        for (Rectangle2D block : blocks) {
            colliders.add(new RectangleCollider(ColliderType.BLOCK, block));
        }

        for (Player player : players) {
            colliders.add(player.getCollider());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addBubble(Bubble bubble){
        bubbles.add(bubble);
        colliders.add(bubble.getCollideable());
    }

    /**
     * {@inheritDoc}
     * Gives the player who sent the bubble a mass and size reward.
     */
    @Override
    public void removeBubble(Bubble bubble){
        bubble.getPlayer().setMass(bubble.getPlayer().getMass() + 0.25f);
        bubble.getPlayer().setScale(bubble.getPlayer().getScale() + 0.5f);
        markedBubbles.add(bubble);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeBorderBubble(Bubble bubble){
        markedBubbles.add(bubble);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HashSet<Bubble> getBubbles(){
        return bubbles;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addEnergizer(Energizer energizer){
        energizers.add(energizer);
        colliders.add(energizer.getCollideable());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeEnergizer(Energizer energizer){
        markedEnergizers.add(energizer);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HashSet<Energizer> getEnergizer(){
        return energizers;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HashSet<Collideable> getColliders(){
        return colliders;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Player getPlayer(int id){
        return players[id];
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Player[] getPlayers(){
        return players;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeMarked(){
        for (Bubble markedBubble : markedBubbles) {
            bubbles.remove(markedBubble);
            colliders.remove(markedBubble.getCollideable());
        }

        for (Energizer markedEnergizer : markedEnergizers) {
            energizers.remove(markedEnergizer);
            colliders.remove(markedEnergizer.getCollideable());
        }
    }

    /**
     * Calculates the total mass of the players and the energizers.
     */
    public double calculateInGameMass() {
        inGameMass = 0;
        int playerCount = players.length;
        for (int i = 0; i < playerCount; i++) {
            var player = getPlayer(i);
            inGameMass += player.getMass();
        }
        for (Energizer energizer : getEnergizer()) {
            inGameMass += 0.1;
        }
        return inGameMass;
    }

    /**
     *Checks if a player has reached the minimal mass.
     */
    public boolean playerHasMinimumMass() {
        int playerCount = players.length;
        boolean minimumMass = false;
        for (int i = 0; i < playerCount; i++) {
            var player = getPlayer(i);
            if (player.getMass() <= 0.25) {
                minimumMass = true;
            }
        }
        return minimumMass;
    }

    /**
     *Checks if a player has reached the maximal mass.
     */
    public boolean playerHasMaximumMass() {
        int playerCount = players.length;
        boolean maximumMass = false;
        for (int i = 0; i < playerCount; i++) {
            var player = getPlayer(i);
            if (player.getMass() >= maxMass) {
                maximumMass = true;
            }
        }
        return maximumMass;
    }
}
