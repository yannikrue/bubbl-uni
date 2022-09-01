package ch.unibas.dmi.dbis.cs108.game.server;

import ch.unibas.dmi.dbis.cs108.game.server.collider.CircleCollider;
import ch.unibas.dmi.dbis.cs108.game.server.collider.Collideable;
import ch.unibas.dmi.dbis.cs108.game.server.collider.ColliderType;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;


/**
 * The energizers are green circles, which the players can pick up to grow.
 * They never change their position, however they do have colliders, which is why
 * they extend the RigidBody.
 */
public class Energizer extends RigidBody {
    private static final double RADIUS = 5.5;

    public Energizer(GameWorldInterface gameWorld) {
        super(gameWorld);
        this.collideable = new CircleCollider(ColliderType.ENERGIZER, this.pos, RADIUS);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPos(Vector2D pos) {
        ((CircleCollider) this.collideable).setCircleCenter(pos);
        super.setPos(pos);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCollision(Collideable collideable) {
        if (collideable.getColliderType() == ColliderType.PLAYER
                || collideable.getColliderType() == ColliderType.BLOCK) {
            gameWorld.removeEnergizer(this);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onUpdate() {
    }
}


