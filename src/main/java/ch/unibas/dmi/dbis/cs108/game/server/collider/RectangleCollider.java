package ch.unibas.dmi.dbis.cs108.game.server.collider;

import javafx.geometry.Rectangle2D;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

public class RectangleCollider implements Collideable {
    private final ColliderType colliderType;
    private final Rectangle2D rectangle2D;

    public RectangleCollider(ColliderType colliderType, Rectangle2D rectangle2D) {
        this.colliderType = colliderType;
        this.rectangle2D = rectangle2D;
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
        return new Vector2D((rectangle2D.getMaxX() - rectangle2D.getMinX()) / 2 + rectangle2D.getMinX(),
                (rectangle2D.getMaxY() - rectangle2D.getMaxY()) / 2 + rectangle2D.getMinY());
    }

    public Rectangle2D getRectangle2D() {
        return rectangle2D;
    }


    /**
     * {@inheritDoc}
     *  In this case, the other object is a rectangle.
     *  However, this case never occurs in the game and is
     *  therefore not implemented.
     */
    @Override
    public boolean intersects(Rectangle2D rectangle2D) {
        throw new UnsupportedOperationException();
    }


    /**
     * {@inheritDoc}
     *  In this case, the other object is a circle.
     */
    @Override
    public boolean intersects(Vector2D center, double radius) {
        return ColliderHelper.intersect(rectangle2D, center, radius * radius);
    }


    /**
     * {@inheritDoc}
     *  In this case, the other object is another collideable.
     */
    @Override
    public boolean intersects(Collideable collideable) throws ColliderException {
        if(collideable == this){
            throw new ColliderException("Cannot collide with itself.", this);
        }
        if (collideable instanceof CircleCollider) {
            var otherCircleCollider = (CircleCollider) collideable;
            return this.intersects(otherCircleCollider.getCenter(), otherCircleCollider.getRadius());
        }
        return false;
    }
}