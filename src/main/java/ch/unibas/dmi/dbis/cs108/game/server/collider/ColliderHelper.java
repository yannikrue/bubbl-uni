package ch.unibas.dmi.dbis.cs108.game.server.collider;

import javafx.geometry.Rectangle2D;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

public class ColliderHelper {

    // Idea from https://www.geeksforgeeks.org/check-if-any-point-overlaps-the-given-circle-and-rectangle/

    /**
     * Checks for the intersection between a rectangle and a circle (center, radius^2).
     *
     * @param rectangle2D   is the rectangle.
     * @param center        is the center of the circle.
     * @param radiusSquared is the radius squared.
     * @return true if they're intersecting, else it's false.
     */
    public static boolean intersect(Rectangle2D rectangle2D, Vector2D center, double radiusSquared) {

        // Find the nearest point on the rectangle to the center of the circle
        double Xn = Math.max(rectangle2D.getMinX(), Math.min(center.getX(), rectangle2D.getMaxX()));
        double Yn = Math.max(rectangle2D.getMinY(), Math.min(center.getY(), rectangle2D.getMaxY()));

        /**
         * Find the distance between the nearest point and the center of the circle.
         * The distance between 2 points, (x1, y1) & (x2, y2) in 2D Euclidean space is
         * ((x1-x2)^2 + (y1-y2)^2)^0.5
         */
        double Dx = Xn - center.getX();
        double Dy = Yn - center.getY();
        return (Dx * Dx + Dy * Dy) <= radiusSquared;
    }
}
