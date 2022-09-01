package ch.unibas.dmi.dbis.cs108.game.server.collider;

public class ColliderException extends Exception{
    private final Collideable collideable;

    public ColliderException(String message, Collideable collideable) {
        super(message);
        this.collideable = collideable;
    }

    public Collideable getCollideable() {
        return collideable;
    }
}
