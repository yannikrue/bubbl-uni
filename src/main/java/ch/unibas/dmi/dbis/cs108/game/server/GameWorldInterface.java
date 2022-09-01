package ch.unibas.dmi.dbis.cs108.game.server;

import ch.unibas.dmi.dbis.cs108.game.server.collider.Collideable;

import java.util.HashSet;

/**
 * The GameWorld is the game state, which is updated every frame.
 * These updates include actions such as
 * adding and removing bubbles and energizers.
 */
public interface GameWorldInterface {
    /**
     * Adds a bubble to the Hashset of bubbles and adds a
     * collider for this bubble.
     */
    void addBubble(Bubble bubble);

    /**
     * Marks the bubble colliding with a player
     * by adding it to the markedBubbles Hashset,
     * so it can later be removed.
     */
    void removeBubble(Bubble bubble);

    /**
     * Marks the bubble colliding with the boundaries or blocks
     * by adding it to the markedBubbles Hashset,
     * so it can later be removed.
     */
    void removeBorderBubble(Bubble bubble);

    /**
     * Gets the bubbles.
     */
    HashSet<Bubble> getBubbles();

    /**
     * Adds an energizer to the energizers Hashset and adds a
     * collider for this energizer.
     */
    void addEnergizer(Energizer energizer);

    /**
     * Marks the energizer by adding it to the markedEnergizers Hashset,
     * so it can later be removed.
     */
    void removeEnergizer(Energizer energizer);

    /**
     * Gets the energizers.
     */
    HashSet<Energizer> getEnergizer();

    /**
     * Gets the colliders.
     */
    HashSet<Collideable> getColliders();

    /**
     * Gets the player ID's.
     */
    Player getPlayer(int id);

    /**
     * Gets the players.
     */
    Player[] getPlayers();

    /**
     * Removes all marked bubbles and energizers and their corresponding colliders.
     */
    void removeMarked();

}
