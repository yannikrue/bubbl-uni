package ch.unibas.dmi.dbis.cs108.game;

import ch.unibas.dmi.dbis.cs108.game.client.ClientGameLoop;
import ch.unibas.dmi.dbis.cs108.game.client.DebugClientReceiver;
import ch.unibas.dmi.dbis.cs108.game.client.DebugClientSender;
import ch.unibas.dmi.dbis.cs108.game.client.Input;
import ch.unibas.dmi.dbis.cs108.game.server.ServerReceiver;
import ch.unibas.dmi.dbis.cs108.game.server.DebugServerSender;
import ch.unibas.dmi.dbis.cs108.game.server.ServerGameLoop;
import ch.unibas.dmi.dbis.cs108.gui.Main;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.stage.Stage;
import javafx.util.Duration;

public class DebugGUI extends Application {

    /**
     * Launching this method will not work on some platforms.
     * What you should do is to create a separate main class and launch the GUI class from there as is done in {@link Main}
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        var group = new Group();
        Canvas canvas = new Canvas(1080, 720);

        group.getChildren().add(canvas);
        Scene scene = new Scene(group);
        primaryStage.setScene(scene);
        Input input = new Input(canvas);
        setUpGameLoop(canvas, input);

        primaryStage.show();
    }


    /**
     * Sets up the game loop and tries to reach 60 frames per second
     */
    private void setUpGameLoop(Canvas canvas, Input input) {
        var debugClientReceiver = new DebugClientReceiver();
        var debugServerReceiver = new ServerReceiver(2);
        var debugClientSender = new DebugClientSender(0, debugServerReceiver);
        var debugServerSender = new DebugServerSender(debugClientReceiver);

        var serverLoop = new ServerGameLoop(canvas.getWidth(), canvas.getHeight(), 2, debugServerReceiver, debugServerSender);
        Thread thread = new Thread(serverLoop);
        thread.start();

        Timeline gameLoop = new Timeline();
        //makes the game loop run indefinitely
        gameLoop.setCycleCount(Timeline.INDEFINITE);
        gameLoop.getKeyFrames().add(new KeyFrame(
                Duration.seconds(0.017), // 60 FPS
                new ClientGameLoop(canvas, input, debugClientReceiver, debugClientSender, 0)));

        gameLoop.play();
    }

}
