package ch.unibas.dmi.dbis.cs108.game.client.drawers;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

/**
 * The PlayerDrawer is responsible for drawing the
 * players onto the canvas.
 */
public class PlayerDrawer {
    private final GraphicsContext gc;
    private static final double SVG_RADIUS = 5.6;

    public PlayerDrawer(GraphicsContext gc) {
        this.gc = gc;
    }

    /**
     * Draw a player
     *
     * @param position    the position of the player
     * @param angle       angle in degrees 0° is up and 180 ° is down.
     * @param scaleFactor the scale factor of the player
     * @param playerId    the playerId
     */
    public void draw(Vector2D position, double angle, double scaleFactor, int playerId, Color color) {
        gc.save();

        gc.setFill(color);
        gc.setStroke(color.darker());
        gc.setLineWidth(0.9);

        Affine affine = new Affine();

        affine.append(new Translate(position.getX(), position.getY()));
        affine.append(new Rotate(angle));
        affine.appendScale(scaleFactor, scaleFactor);

        gc.setTransform(affine);
        gc.beginPath();

        // This SVG is the player
        gc.appendSVGPath("M1.831,-4.946l-1.588,-3.176c-0.046,-0.093 -0.141,-0.151 -0.244,-0.151c-0.104,0 -0.198,0.058 -0.244,0.151l-1.589,3.177c-2.008,0.745 -3.44,2.68 -3.44,4.946c-0,2.911 2.363,5.275 5.274,5.275c2.911,-0 5.274,-2.364 5.274,-5.275c0,-2.267 -1.433,-4.202 -3.443,-4.947Z");
        gc.closePath();
        gc.fill();
        gc.stroke();
        gc.restore();
        gc.setStroke(Color.TRANSPARENT);
    }

    /**
     * Draws the player's name on the player.
     */
    public void drawPlayerName(String name, double size, double xPos, double yPos) {
        gc.setFill(Color.rgb(255, 255, 255, 0.90));
        int length = name.length();
        if (length < 12) {
            gc.setFont(Font.font("monospaced", 2 * size));
        } else {
            gc.setFont(Font.font("monospaced", 3 * size / 2));
        }
        double scaleXPos = Math.min(4 * size, size * length * 9 / 14);
        gc.fillText(name, xPos - scaleXPos, yPos + (size / 2), 8 * size);
    }
}
