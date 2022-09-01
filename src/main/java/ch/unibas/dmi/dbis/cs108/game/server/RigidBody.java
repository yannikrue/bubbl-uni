package ch.unibas.dmi.dbis.cs108.game.server;

import ch.unibas.dmi.dbis.cs108.game.server.collider.Collideable;
import ch.unibas.dmi.dbis.cs108.game.server.collider.ColliderException;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * The RigidBody contains various methods and fields that are required
 * by objects in the game world to be able to, for example, detect collisions
 * with other objects and move around on the canvas. This interface also possesses
 * the update method which is needed by these objects and is called every frame
 * in order to be able to update their current position and display the game state
 * correctly.
 */
public abstract class RigidBody {

    protected Vector2D pos  = Vector2D.ZERO;
    protected Vector2D velocity = Vector2D.ZERO;
    protected Collideable collideable;
    protected final GameWorldInterface gameWorld;
    private final ArrayList<Collideable> collisions = new ArrayList<>();

    public RigidBody(GameWorldInterface gameWorld) {
        this.gameWorld = gameWorld;
    }

    /**
     * Returns the collideable.
     */
    public Collideable getCollideable() {
        return collideable;
    }

    /**
     * Sets the current position as a 2D Vector.
     * @param x is the x-coordinate of the position to be set.
     * @param y is the y-coordinate of the position to be set.
     */
    public void setPos(double x, double y) {
        setPos(new Vector2D(x, y));
    }

    /**
     * Sets the current position as a 2D Vector.
     * @param pos is the position to be set (which is also a 2D Vector).
     */
    public void setPos(Vector2D pos) {
        this.pos = pos;
    }

    /**
     * Gets the x-coordinate of the current position.
     */
    public double getPosX() {
        return pos.getX();
    }

    /**
     * Gets the y-coordinate of the current position.
     */
    public double getPosY() {
        return pos.getY();
    }

    /**
     * Gets the current position.
     */
    public Vector2D getPos() {
        return pos;
    }

    /**
     * Sets the velocity.
     * @param x is the x component of the velocity vector.
     * @param y is the y component of the velocity vector.
     */
    public void setVelocity(double x, double y) {
        setVelocity(new Vector2D(x, y));
    }

    /**
     * Sets the velocity.
     * @param velocity is the velocity vector.
     */
    public void setVelocity(Vector2D velocity) {
        this.velocity = velocity;
    }

    /**
     * Updates the position after every frame.
     * Uses the delta time (dt) to normalize the movement,
     * so clients with 60 fps move with the same speed
     * as clients with 30 fps.
     */
    public void update(float dt) {
        pos = pos.add(velocity.scalarMultiply(dt));
        onUpdate();
        updateCollisions();
    }

    /**
     * Invokes {@link #onCollision(Collideable)} for every collision.
     */
    protected void updateCollisions() {
        for (Collideable collision : collisions) {
            onCollision(collision);
        }
        collisions.clear();
    }

    /**
     * Takes a set of collideables and checks for collisions with this collideable.
     */
    public void checkForCollisions(HashSet<Collideable> collideables) throws ColliderException {
        for (Collideable otherCollideables : collideables) {
            if (otherCollideables != collideable && otherCollideables.intersects(collideable)){
                collisions.add(otherCollideables);
            }
        }
    }

    /**
     * Gets called when this collideable collides with another collideable.
     */
    public abstract void onCollision(Collideable collideable);

    /**
     * Gets called every frame.
     */
    public abstract void onUpdate();
}