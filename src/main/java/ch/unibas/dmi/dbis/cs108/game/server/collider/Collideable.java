package ch.unibas.dmi.dbis.cs108.game.server.collider;

import javafx.geometry.Rectangle2D;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

public interface Collideable {

    /**
     * Gets the collider type of an object.
     */
    ColliderType getColliderType();

    /**
     * Gets the center of a circle.
     */
    Vector2D getCenter();

    /**
     * Checks for the intersection between a rectangle and another object.
     * Returns true if they are intersecting and false, if they aren't intersecting.
     */
    boolean intersects(Rectangle2D rectangle2D);

    /**
     * Checks for the intersection between a circle and another object.
     * Returns true if they are intersecting and false, if they aren't intersecting.
     */
    boolean intersects(Vector2D center, double radius);

    /**
     * Checks for the intersection between a collideable and another object.
     * Returns true if they are intersecting and false, if they aren't intersecting.
     */
    boolean intersects(Collideable collideable) throws ColliderException;
}