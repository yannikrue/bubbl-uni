package ch.unibas.dmi.dbis.cs108.game.server;

import ch.unibas.dmi.dbis.cs108.game.server.collider.CircleCollider;
import ch.unibas.dmi.dbis.cs108.game.server.collider.Collideable;
import ch.unibas.dmi.dbis.cs108.game.server.collider.ColliderType;

/**
 * The bubble class contains all the information that is required
 * for the bubble to be able to be displayed in the game and collide with
 * other objects in the game world.
 */
public class Bubble extends RigidBody {
    private Player player;
    private static final int BUBBLE_SIZE = 10;

    public Bubble(GameWorldInterface gameWorld, Player player) {
        super(gameWorld);
        this.player = player;
        this.collideable = new CircleCollider(ColliderType.BUBBLE, this.pos, BUBBLE_SIZE/2.0);
    }

    /**
     * Gets the collider for the bubble.
     */
    private CircleCollider getCollider() {
        return (CircleCollider)collideable;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onUpdate() {
        getCollider().setCircleCenter(this.pos);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCollision(Collideable collideable) {
        if(collideable.getColliderType() == ColliderType.BLOCK) {
            gameWorld.removeBorderBubble(this);
        } else if(collideable.getColliderType() == ColliderType.PLAYER){
            gameWorld.removeBubble(this);
        }
    }

    /**
     * Gets the player for the bubble.
     */
    public Player getPlayer() {return this.player;}
}
