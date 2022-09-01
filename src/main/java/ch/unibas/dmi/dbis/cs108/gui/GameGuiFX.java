package ch.unibas.dmi.dbis.cs108.gui;

import ch.unibas.dmi.dbis.cs108.client.Client;
import javafx.scene.canvas.Canvas;
import javafx.stage.Stage;

public class GameGuiFX extends Canvas implements GameGuiInterface {

    private Client client;
    private Stage stage;

    public GameGuiFX(double width, double height, Client client) {
        setWidth(width);
        setHeight(height);
        //https://stackoverflow.com/questions/24126845/javafx-canvas-not-picking-up-key-events
//        addEventFilter(MouseEvent.ANY, (e) -> this.requestFocus());
        setVisible(false);
        this.client = client;
        client.setGameGui(this);
        try {
            stage = MainGui.getPrimaryStage();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets the game visible. This is used when starting the game.
     */
    public void displayGame(boolean bool) {
        setVisible(bool);
    }


}
