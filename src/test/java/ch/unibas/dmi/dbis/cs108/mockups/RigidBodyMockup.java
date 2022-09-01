package ch.unibas.dmi.dbis.cs108.mockups;

import ch.unibas.dmi.dbis.cs108.game.server.GameWorldInterface;
import ch.unibas.dmi.dbis.cs108.game.server.RigidBody;
import ch.unibas.dmi.dbis.cs108.game.server.collider.Collideable;

import java.util.ArrayList;

public class RigidBodyMockup extends RigidBody {
    private int onUpdateCallCount;
    private ArrayList<Collideable> onCollisionCalled = new ArrayList<>();

    public RigidBodyMockup(GameWorldInterface gameWorld) {
        super(gameWorld);
    }

    @Override
    public void onCollision(Collideable collideable) {
        onCollisionCalled.add(collideable);
    }

    @Override
    public void onUpdate() {
        onUpdateCallCount++;
    }

    public int getOnUpdateCallCount() {
        return onUpdateCallCount;
    }

    public ArrayList<Collideable> getOnCollisionCalled() {
        return onCollisionCalled;
    }

    public void setCollideable(Collideable collideable) {
        this.collideable = collideable;
    }
}
