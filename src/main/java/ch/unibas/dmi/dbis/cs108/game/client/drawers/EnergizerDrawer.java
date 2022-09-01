package ch.unibas.dmi.dbis.cs108.game.client.drawers;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

/**
 * The EnergizerDrawer is responsible for drawing the
 * energizers onto the canvas.
 */
public class EnergizerDrawer {
    private final GraphicsContext gc;
    private static final double RADIUS = 5;

    public EnergizerDrawer(GraphicsContext gc) {
        this.gc = gc;
    }

    /**
     * Draws an energizer at the position of the given
     * 2D Vector @param position by drawing and filling a circle.
     */
    public void draw(Vector2D position) {
        gc.setFill(Color.rgb(13, 247, 45));
        gc.fillOval(position.getX() - RADIUS, position.getY() - RADIUS, RADIUS * 2, RADIUS * 2);
    }
}
