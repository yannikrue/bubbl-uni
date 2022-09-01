package ch.unibas.dmi.dbis.cs108.game;

import ch.unibas.dmi.dbis.cs108.game.server.collider.CircleCollider;
import ch.unibas.dmi.dbis.cs108.game.server.collider.ColliderException;
import ch.unibas.dmi.dbis.cs108.game.server.collider.ColliderType;
import ch.unibas.dmi.dbis.cs108.game.server.collider.RectangleCollider;
import javafx.geometry.Rectangle2D;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.junit.Assert;
import org.junit.Test;

import org.junit.runner.RunWith;


/**
 * The colliders are a core component of the game because they are required
 * for the game rules to be enforced during the match. They are needed for
 * checking whether two objects that can move around are colliding with other objects.
 * These objects can be two players, a player and a bubble or energizer or a player and a block.
 *
 * The colliders can be circles or rectangles, however, there are never two
 * rectangles that collide during a match. It should also never be possible for
 * the same collider to intersect with itself.
 */

@RunWith(JUnitParamsRunner.class)
public class ColliderTest {

    @Test
    @Parameters({
            "0.0, 0.0, 1.0, 1.5, 0.0, 1.0",
            "0.0, 0.0, 1.0, 2.5, 0.0, 2.0",
            "0.0, 0.0, 1.0, 0.0, 0.0, 1.0" // two circles at the exact same position with the exact same size
    })
    public void testTwoIntersectingCircleColliders(double circle1x, double circle1y, double circle1radius, double circle2x, double circle2y, double circle2radius) throws ColliderException {
        // arrange
        CircleCollider circleCollider1 = new CircleCollider(ColliderType.PLAYER, new Vector2D(circle1x, circle1y), circle1radius);
        CircleCollider circleCollider2 = new CircleCollider(ColliderType.PLAYER, new Vector2D(circle2x, circle2y), circle2radius);

        // act
        boolean isIntersecting = circleCollider1.intersects(circleCollider2);

        // assert
        Assert.assertTrue(isIntersecting);
    }

    @Test
    @Parameters({
            "0.0, 0.0, 1.0, 3.0, 0.0, 1.0",
            "0.0, 0.0, 1.0, 2.1, 0.0, 1.0",
            "0.0, 0.0, 0.7, 1.0, 1.0, 0.3",
            "0.0, 0.0, 0.6, 0.8, 0.8, 0.1",
            "0.0, 0.0, 1.0, 2.0, 0.0, 1.0", // two circles "touching" at exactly x = 1 are not intersecting
    })
    public void testTwoNonIntersectingCircleColliders(double circle1x, double circle1y, double circle1radius, double circle2x, double circle2y, double circle2radius) throws ColliderException {
        // arrange
        CircleCollider circleCollider1 = new CircleCollider(ColliderType.PLAYER, new Vector2D(circle1x, circle1y), circle1radius);
        CircleCollider circleCollider2 = new CircleCollider(ColliderType.PLAYER, new Vector2D(circle2x, circle2y), circle2radius);

        // act
        boolean isIntersecting = circleCollider1.intersects(circleCollider2);

        // assert
        Assert.assertFalse(isIntersecting);
    }

    @Test
    public void testLessOrEqualsZeroRadius() {
        // assert
        Assert.assertThrows(IllegalArgumentException.class, () -> new CircleCollider(ColliderType.PLAYER, new Vector2D(0, 0), 0.0));
        Assert.assertThrows(IllegalArgumentException.class, () -> new CircleCollider(ColliderType.PLAYER, new Vector2D(0, 0), -1.0));
    }

    @Test
    @Parameters({
            "1.0, 0.0, 1.0, 0.0, 0.0, 2.0, 2.0",
            "1.0, 1.0, 1.0, 0.5, 0.5, 1.0, 1.0" // overlapping completely
    })
    public void testIntersectingCircleColliderRectangleCollider(double circle1x, double circle1y, double circle1radius, double rectangleMinX, double rectangleMinY, double rectangleWidth, double rectangleHeight) throws ColliderException {
        // arrange
        RectangleCollider rectangleCollider = new RectangleCollider(ColliderType.BLOCK, new Rectangle2D(rectangleMinX, rectangleMinY, rectangleWidth, rectangleHeight));
        CircleCollider circleCollider = new CircleCollider(ColliderType.PLAYER, new Vector2D(circle1x, circle1y), circle1radius);

        // act

        boolean isIntersecting = circleCollider.intersects(rectangleCollider);

        // assert
        Assert.assertTrue(isIntersecting);
    }

    @Test
    @Parameters({
            "0.0, 0.0, 1.0, 1.0, 1.0, 1.0, 1.0",
            "0.0, 0.0, 0.9, 1.0, 1.0, 1.0, 1.0", // a circle and rectangle intersecting almost intersecting
            "1.5, 1.5, 1.0, 0.0, 0.0, 0.5, 0.5", // a circle and rectangle "touching" at exactly x = 1 are not intersecting
    })
    public void testNonIntersectingCircleColliderRectangleCollider(double circle1x, double circle1y, double circle1radius, double rectangleMinX, double rectangleMinY, double rectangleWidth, double rectangleHeight) throws ColliderException {
        // arrange
        RectangleCollider rectangleCollider = new RectangleCollider(ColliderType.BLOCK, new Rectangle2D(rectangleMinX, rectangleMinY, rectangleWidth, rectangleHeight));
        CircleCollider circleCollider = new CircleCollider(ColliderType.PLAYER, new Vector2D(circle1x, circle1y), circle1radius);

        // act
        boolean isIntersecting = circleCollider.intersects(rectangleCollider);

        // assert
        Assert.assertFalse(isIntersecting);
    }

    @Test
    public void testTwoIntersectingRectangles() {
        // arrange
        RectangleCollider rectangleCollider1 = new RectangleCollider(ColliderType.BLOCK,new Rectangle2D(0,0,1,1));
        Rectangle2D rectangle = new Rectangle2D(0.5,0.5,1,1);

        // assert
        Assert.assertThrows(UnsupportedOperationException.class, () -> rectangleCollider1.intersects(rectangle));
    }

    @Test
    public void testSameRectangleColliderIntersectingWithItself(){
        // arrange
        RectangleCollider rectangleCollider = new RectangleCollider(ColliderType.BLOCK,new Rectangle2D(0,0,1,1));

        // assert
        Assert.assertThrows(ColliderException.class, () -> rectangleCollider.intersects(rectangleCollider));
    }

    @Test
    public void testSameCircleColliderIntersectingWithItself(){
        // arrange
        CircleCollider circleCollider = new CircleCollider(ColliderType.PLAYER, new Vector2D(1,1), 1);

        // assert
        Assert.assertThrows(ColliderException.class, () -> circleCollider.intersects(circleCollider));
    }
}
