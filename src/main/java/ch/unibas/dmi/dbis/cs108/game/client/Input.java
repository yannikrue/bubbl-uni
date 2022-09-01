package ch.unibas.dmi.dbis.cs108.game.client;

import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import java.util.HashSet;

/**
 * The Input class is responsible for the inputs in the game,
 * such as the input for the movement direction of the player
 * and the mouse position that is needed for the player to be able
 * to point to the mouse position.
 */
public class Input {
    private final HashSet<KeyCode> pressedKeys = new HashSet<>();

    private boolean mousePressed;
    private double mouseX;
    private double mouseY;

    public Input(Canvas canvas) {
        canvas.addEventFilter(MouseEvent.ANY, (e) -> canvas.requestFocus());

        canvas.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            System.out.println(event.getCode());
            pressedKeys.add(event.getCode());
        });

        canvas.addEventHandler(KeyEvent.KEY_RELEASED, event ->
                pressedKeys.remove(event.getCode()));

        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
            mouseX = event.getX();
            mouseY = event.getY();
            mousePressed = true;
        });

        canvas.addEventHandler(MouseEvent.MOUSE_RELEASED, event -> {
            mouseX = event.getX();
            mouseY = event.getY();
            mousePressed = false;
        });

        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, event -> {
            mouseX = event.getX();
            mouseY = event.getY();
        });

        canvas.addEventHandler(MouseEvent.MOUSE_MOVED, event -> {
            mouseX = event.getX();
            mouseY = event.getY();
        });
    }

    /**
     * isMousePressed() returns true if the left mouse button is being clicked.
     */
    public boolean isMousePressed() {
        return mousePressed;
    }

    /**
     * Gets the x-coordinate of the current mouse position.
     */
    public double getMousePosX() {
        return mouseX;
    }

    /**
     * Gets the y-coordinate of the current mouse position.
     */
    public double getMousePosY() {
        return mouseY;
    }

    /**
     * isKeyPressed returns the keyCodes which are in.
     * pressedKeys, which may be none, one or several pressed buttons.
     */
    public boolean isKeyPressed(KeyCode keyCode) {
        return pressedKeys.contains(keyCode);
    }
}
