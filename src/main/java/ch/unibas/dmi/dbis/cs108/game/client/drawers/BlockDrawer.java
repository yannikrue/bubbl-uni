package ch.unibas.dmi.dbis.cs108.game.client.drawers;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * The BlockDrawer is responsible for drawing the
 * black blocks onto the canvas.
 */
public class BlockDrawer {
    private final GraphicsContext gc;

    public BlockDrawer(GraphicsContext gc) {
        this.gc = gc;
    }

    /**
     * Draws a block by drawing and filling a 2D Rectangle.
     */
    public void draw(Rectangle2D rect) {
        gc.save();
        gc.setFill(Color.rgb(12, 9, 15));
        double strokeThickness = 5;
        gc.fillRoundRect(rect.getMinX(), rect.getMinY(), rect.getWidth(), rect.getHeight(), 10, 10);
        gc.setFill(Color.rgb(25, 23, 26));
        gc.fillRoundRect(rect.getMinX() + strokeThickness, rect.getMinY() + strokeThickness, rect.getWidth() - 2 * strokeThickness, rect.getHeight() - 2 * strokeThickness, 6, 6);
        gc.stroke();
        gc.restore();
    }
}
