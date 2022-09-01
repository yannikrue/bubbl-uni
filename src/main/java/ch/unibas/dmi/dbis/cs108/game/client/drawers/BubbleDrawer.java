package ch.unibas.dmi.dbis.cs108.game.client.drawers;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

/**
 * The BubbleDrawer is responsible for drawing the
 * bubbles onto the canvas.
 */
public class BubbleDrawer {
    private final GraphicsContext gc;
    private static final int BUBBLE_SIZE = 10;

    public BubbleDrawer(GraphicsContext gc) {
        this.gc = gc;
    }

    /**
     * Draws a bubble at the position of the given
     * 2D Vector @param position by drawing and filling a circle.
     */
    public void draw(Vector2D position, Color color) {
        gc.setFill(color);
        gc.fillOval(position.getX() - BUBBLE_SIZE / 2f, position.getY() - BUBBLE_SIZE / 2f, BUBBLE_SIZE, BUBBLE_SIZE);
    }
}
