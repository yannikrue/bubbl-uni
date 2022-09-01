package ch.unibas.dmi.dbis.cs108.game;

import static org.mockito.Mockito.*;

import ch.unibas.dmi.dbis.cs108.game.server.GameWorldInterface;
import ch.unibas.dmi.dbis.cs108.game.server.collider.Collideable;
import ch.unibas.dmi.dbis.cs108.game.server.collider.ColliderException;
import ch.unibas.dmi.dbis.cs108.mockups.RigidBodyMockup;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.junit.Assert;
import org.junit.Test;
import java.util.HashSet;

/**
 * The rigidbody is the core of all non-static objects in the game, such as
 * the player, bubbles and energizers. Checking whether the rigidbody updates
 * and whether certain methods are called is important for the game to work properly
 * and enforce its game rules. Therefore, testing this component is relevant.
 */
public class RigidBodyTest {

    @Test
    public void testPositionUpdate() {
        // arrange
        RigidBodyMockup rigidBodyUnderTest = getRigidBodyMockup();

        // act
        rigidBodyUnderTest.setPos(0, 0);
        rigidBodyUnderTest.setVelocity(1, 1);
        rigidBodyUnderTest.update(1);

        // assert
        Assert.assertEquals(new Vector2D(1, 1), rigidBodyUnderTest.getPos());
    }

    @Test
    public void testOnUpdateIsCalled() {
        // arrange
        RigidBodyMockup rigidBodyUnderTest = getRigidBodyMockup();

        // act
        rigidBodyUnderTest.update(1);

        // assert
        Assert.assertEquals(1, rigidBodyUnderTest.getOnUpdateCallCount());
    }


    @Test
    public void testOnCollisionIsCalled() throws ColliderException {
        // arrange
        RigidBodyMockup rigidBodyUnderTest = getRigidBodyMockup();
        var collideable = mock(Collideable.class);
        var otherCollideable = mock(Collideable.class);
        when(otherCollideable.intersects(collideable)).thenReturn(true);
        rigidBodyUnderTest.setCollideable(collideable);

        // act
        rigidBodyUnderTest.checkForCollisions(new HashSet<>() {{
            add(otherCollideable);
        }});
        rigidBodyUnderTest.update(1);

        // assert
        Assert.assertTrue(rigidBodyUnderTest.getOnCollisionCalled().contains(otherCollideable));
    }

    private RigidBodyMockup getRigidBodyMockup() {
        var gameWorld = mock(GameWorldInterface.class);
        return new RigidBodyMockup(gameWorld);
    }
}
