package ch.unibas.dmi.dbis.cs108.game.client;

import ch.unibas.dmi.dbis.cs108.client.Client;
import ch.unibas.dmi.dbis.cs108.game.client.drawers.BlockDrawer;
import ch.unibas.dmi.dbis.cs108.game.client.drawers.BubbleDrawer;
import ch.unibas.dmi.dbis.cs108.game.client.drawers.EnergizerDrawer;
import ch.unibas.dmi.dbis.cs108.game.client.drawers.PlayerDrawer;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;


/**
 * The ClientGameLoop class is responsible for sending each players input information
 * and draws the current frame of the game for each player by updating the canvas with
 * the current information that was sent by the server.
 */
public class ClientGameLoop implements EventHandler<ActionEvent> {

    private static final Logger logger = LogManager.getLogger(ClientGameLoop.class);

    private GraphicsContext gc;
    private Input input;
    private double canvasWidth;
    private double canvasHeight;
    private double blockWidth = 80;
    private double blockHeight = 50;

    private long lastBubbleSpawn;

    private final ClientReceiverInterface receiver;
    private final ClientSenderInterface sender;

    private final Rectangle2D[] blocks;

    private final PlayerDrawer playerDrawer;
    private final BubbleDrawer bubbleDrawer;
    private final EnergizerDrawer energizerDrawer;
    private final BlockDrawer blockDrawer;

    private Color[] colors = {Color.rgb(255, 137, 50), Color.rgb(102, 0, 204),
            Color.rgb(153, 204, 255), Color.rgb(255, 51, 51)};

    private int playerId;
    private Vector2D playerPosition = Vector2D.ZERO;

    public ClientGameLoop(Canvas canvas, Input input, ClientReceiverInterface receiver, ClientSenderInterface sender, int playerId) {
        Configurator.setLevel(LogManager.getLogger(ClientGameLoop.class).getName(), Level.ERROR);
        this.gc = canvas.getGraphicsContext2D();
        this.input = input;
        this.receiver = receiver;
        this.sender = sender;
        this.playerId = playerId;
        canvasWidth = canvas.getWidth();
        canvasHeight = canvas.getHeight();

        playerDrawer = new PlayerDrawer(gc);
        bubbleDrawer = new BubbleDrawer(gc);
        energizerDrawer = new EnergizerDrawer(gc);
        blockDrawer = new BlockDrawer(gc);

        blocks = new Rectangle2D[]{
                new Rectangle2D(canvasWidth / 4 - blockWidth / 2, canvasHeight / 4 - blockHeight / 2, blockWidth, blockHeight),
                new Rectangle2D(3 * canvasWidth / 4 - blockWidth / 2, canvasHeight / 4 - blockHeight / 2, blockWidth, blockHeight),
                new Rectangle2D(canvasWidth / 4 - blockWidth / 2, 3 * canvasHeight / 4 - blockHeight / 2, blockWidth, blockHeight),
                new Rectangle2D(3 * canvasWidth / 4 - blockWidth / 2, 3 * canvasHeight / 4 - blockHeight / 2, blockWidth, blockHeight)
        };
    }

    /**
     * //TODO: add comment
     */
    @Override
    public void handle(ActionEvent event) {
        clearCanvas();
        updateCanvas();
        SendInputs();
    }

    /**
     * Clears the canvas by filling out the entire canvas with one specific color.
     */
    private void clearCanvas() {
        gc.setFill(Color.rgb(236, 233, 220));
        gc.fillRect(0, 0, canvasWidth, canvasHeight);
    }

    /**
     * Sends the inputs that are made by the player as a
     * 2D Vector {@param direction}.
     */
    private void SendInputs() {
        var mousePos = new Vector2D(input.getMousePosX(), input.getMousePosY());
        /*
         * Checks if the mouse is pressed and if the last bubble spawned longer
         * than 0.5 seconds ago. If this is the case, it spawns a new bubble.
         */
        if (input.isMousePressed()) {
            if (System.currentTimeMillis() - lastBubbleSpawn > 500) {
                var bubbleDirection = calculatePlayerToMouseVector(mousePos, playerPosition).normalize();
                lastBubbleSpawn = System.currentTimeMillis();
                sender.sendBubbleCreation(bubbleDirection);
            }
        }

        /*
         * Checks if any keys (W, A, S, D) are pressed and adds 1 to the corresponding direction.
         */
        double vx = 0;
        double vy = 0;

        if (input.isKeyPressed(KeyCode.D) && playerPosition.getX() < canvasWidth) {
            vx += 1;
        }
        if (input.isKeyPressed(KeyCode.W) && playerPosition.getY() > 0) {
            vy -= 1;
        }
        if (input.isKeyPressed(KeyCode.S) && playerPosition.getY() < canvasHeight) {
            vy += 1;
        }
        if (input.isKeyPressed(KeyCode.A) && playerPosition.getX() > 0) {
            vx -= 1;
        }

        /*
         * Creates a 2D Vector which represents the direction of the player movement
         * and normalizes this vector to prevent the player from moving faster
         * in diagonal directions (meaning if the player presses two keys at the same time,
         * for example S and D).
         */
        var direction = new Vector2D(vx, vy);
        if (direction.getNormSq() > 0) {
            direction = direction.normalize();
        }

        /*
         * Creates a 2D Vector which represents the direction of the player to the current
         * mouse position. This is needed for the calculation of the player rotation angle.
         */
        Vector2D mouseDirection = calculatePlayerToMouseVector(mousePos, playerPosition);
        //The 2D Vector up is used for calculating the rotation of the player
        Vector2D up = new Vector2D(0, -1);
        double dot = up.dotProduct(mouseDirection);
        var angle = Math.toDegrees(Math.acos(dot));

        /*
         * If the x-coordinate of the mouse is negative, then the angle
         *  needs to be mirrored on the y-axis.
         *  This is why the angle is set to 360 minus the calculated angle in this case.
         */
        if (mouseDirection.getX() < 0) {
            angle = 360 - angle;
        }

        sender.sendPlayerAngle((float) angle);
        sender.sendMoveDirection(direction);
    }

    /**
     * Updates the canvas by drawing the objects (meaning blocks, energizers,
     * bubbles and players) on the canvas. This method is called every frame.
     */
    private void updateCanvas() {
        var playerPositions = receiver.getPlayerPositions();
        var playerSizes = receiver.getPlayerSizes();
        var playerAngles = receiver.getPlayerAngles();
        var playerNames = receiver.getPlayerNames();
        var bubbleIds = receiver.getBubbleIds();

        if (playerPositions.size() != playerSizes.size()) {
            logger.error("playerPositions.size() != playerSizes.size() occurred");
            return;
        }
        for (Rectangle2D block : blocks) {
            blockDrawer.draw(block);
        }

        for (var energizerPos : receiver.getEnergizerPositions()) {
            energizerDrawer.draw(energizerPos);
        }

        int n = 0;
        for (var bubblePos : receiver.getBubblePositions()) {
            bubbleDrawer.draw(bubblePos, colors[bubbleIds.get(n)]);
            n++;
        }


        for (int i = 0; i < playerPositions.size(); i++) {
            var pos = playerPositions.get(i);
            var size = playerSizes.get(i);
            var angle = playerAngles.get(i);
            var name = playerNames.get(i);

            if (i == playerId) {
                playerPosition = pos;
            }

            playerDrawer.draw(pos, angle, size, i, colors[i]);
            playerDrawer.drawPlayerName(name, size, pos.getX(), pos.getY());
        }
    }

    /**
     * Calculates the direction from the player's position to the current position of the mouse.
     */
    public Vector2D calculatePlayerToMouseVector(Vector2D mousePos, Vector2D playerPos) {
        var direction = mousePos.subtract(playerPos);

        if (direction.getNormSq() > 0) {
            return direction.normalize();
        } else {
            return Vector2D.ZERO;
        }
    }
}


