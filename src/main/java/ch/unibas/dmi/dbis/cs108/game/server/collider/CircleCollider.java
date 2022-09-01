package ch.unibas.dmi.dbis.cs108.game.server.collider;

import javafx.geometry.Rectangle2D;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

public class CircleCollider implements Collideable {
    private final ColliderType colliderType;
    private Vector2D circleCenter;
    private double radius;
    private double radiusSquared;

    public CircleCollider(ColliderType colliderType, Vector2D circleCenter, double radius) {
        this.colliderType = colliderType;
        this.circleCenter = circleCenter;
        this.setRadius(radius);
        this.radiusSquared = radius * radius;
    }



    /**
     * {@inheritDoc}
     */
    @Override
    public ColliderType getColliderType() {
        return colliderType;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Vector2D getCenter() {
        return circleCenter;
    }

    /**
     * Sets the circle center to the position of the
     * 2D Vector @param circleCenter.
     */
    public void setCircleCenter(Vector2D circleCenter) {
        this.circleCenter = circleCenter;
    }

    /**
     * Gets the radius of the circle.
     */
    public double getRadius() {
        return radius;
    }

    /**
     * Sets the radius of a circle.
     */
    public void setRadius(double radius) {
        if (radius <= 0){
            throw new IllegalArgumentException("Radius needs to be greater than 0.");
        }
        this.radius = radius;
        this.radiusSquared = radius * radius;
    }


    /**
     * {@inheritDoc}
     * In this case, the other object is a circle.
     */
    @Override
    public boolean intersects(Rectangle2D rectangle2D) {
        return ColliderHelper.intersect(rectangle2D, circleCenter, radiusSquared);
    }


    /**
     * {@inheritDoc}
     * In this case, the other object is a circle.
     */
    @Override
    public boolean intersects(Vector2D center, double radius) {
        var distance = Vector2D.distance(center, circleCenter);
        return distance < radius + this.radius;
    }


    /**
     * {@inheritDoc}
     * In this case, the other object is another collideable.
     */
    @Override
    public boolean intersects(Collideable collideable) throws ColliderException {
        if(collideable == this){
            throw new ColliderException("Cannot collide with itself.", this);
        }
        if (collideable instanceof CircleCollider) {
            var otherCircleCollider = (CircleCollider) collideable;
            return this.intersects(otherCircleCollider.getCenter(), otherCircleCollider.getRadius());
        } else if (collideable instanceof RectangleCollider) {
            var otherRectangleCollider = (RectangleCollider) collideable;
            return this.intersects(otherRectangleCollider.getRectangle2D());
        }
        return false;
    }


}

