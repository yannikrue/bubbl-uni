package ch.unibas.dmi.dbis.cs108.game.server;

import ch.unibas.dmi.dbis.cs108.connection.Connection;
import ch.unibas.dmi.dbis.cs108.game.server.collider.CircleCollider;
import ch.unibas.dmi.dbis.cs108.game.server.collider.Collideable;
import ch.unibas.dmi.dbis.cs108.game.server.collider.ColliderType;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

/**
 * The Player contains all the information that is required
 * for the player to be displayed in the game and contains physical
 * calculations (for the movement and collisions with other objects).
 */
public class Player extends RigidBody {
    private float angle;
    private float scaleFactor;
    private static final double SVG_RADIUS = 5.6;
    private double mass;
    private double maxMass;
    private boolean collidesWithObstacle = false;
    private Connection player;

    public Player(double mass, GameWorldInterface gameWorld, Connection player) {
        super(gameWorld);
        this.mass = mass;
        this.maxMass = mass;
        this.scaleFactor = 10;
        this.collideable = new CircleCollider(ColliderType.PLAYER, this.pos, this.getRadius());
        this.player = player;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(float dt) {
        pos = pos.add(velocity.scalarMultiply(dt * 1 / Math.cbrt(mass)));
        onUpdate();
        updateCollisions();
    }

    /**
     * Gets the player's collider, which is a @see CircleCollider.
     */
    public CircleCollider getCollider() {
        return (CircleCollider) collideable;
    }

    /**
     * Gets the player's mass.
     */
    public double getMass() {
        return mass;
    }

    /**
     * Gets the player's max mass.
     */
    public Double getMaxMass() {
        return this.maxMass;
    }

    /**
     * Sets the player's mass.
     */
    public void setMass(double mass) {
        if (mass > this.maxMass) {
            this.maxMass = mass;
        }
        this.mass = mass;
    }

    /**
     * Sets the player's max mass.
     */
    public void setMaxMass(double mass) {
        this.maxMass = mass;
    }

    /**
     * Gets the player's radius.
     */
    public double getRadius() {
        return this.scaleFactor * SVG_RADIUS;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setVelocity(Vector2D velocity) {
        if (!collidesWithObstacle) {
            super.setVelocity(velocity);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onUpdate() {
        getCollider().setCircleCenter(pos);
        collidesWithObstacle = false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCollision(Collideable otherCollideable) {
        if (otherCollideable.getColliderType() == ColliderType.BUBBLE) {
            if (getMass() > 0.25) {
                setMass(getMass() - 0.15f);
                setScale(getScale() - 0.3f);
            }
        } else if (otherCollideable.getColliderType() == ColliderType.PLAYER ||
                otherCollideable.getColliderType() == ColliderType.BLOCK) {
            var otherPlayerDirection = this.pos.subtract(otherCollideable.getCenter());
            setVelocity(otherPlayerDirection);
            collidesWithObstacle = true;
        } else if (otherCollideable.getColliderType() == ColliderType.ENERGIZER) {
            setMass(getMass() + 0.1f);
            setScale(getScale() + 0.2f);
        }
    }

    /**
     * Sets the player rotation.
     * 0° is up and 180 ° is down.
     *
     * @param angle in degrees
     */
    public void setPlayerRotation(float angle) {
        this.angle = angle;
    }

    /**
     * Sets the player's scale.
     */
    public void setScale(float scale) {
        this.scaleFactor = scale;
        getCollider().setRadius(getRadius());
    }

    /**
     * Gets the player's scale.
     */
    public float getScale() {
        return this.scaleFactor;
    }

    /**
     * Gets the player's angle.
     */
    public float getAngle() {
        return this.angle;
    }

    /**
     * Gets the player.
     */
    public Connection getPlayer() {return this.player;}
}
