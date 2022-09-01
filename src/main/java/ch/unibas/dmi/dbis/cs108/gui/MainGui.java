package ch.unibas.dmi.dbis.cs108.gui;

import ch.unibas.dmi.dbis.cs108.client.Client;
import ch.unibas.dmi.dbis.cs108.game.client.ClientGameLoop;
import ch.unibas.dmi.dbis.cs108.game.client.ClientReceiver;
import ch.unibas.dmi.dbis.cs108.game.client.ClientSender;
import ch.unibas.dmi.dbis.cs108.game.client.Input;
import ch.unibas.dmi.dbis.cs108.timer.Timer;
import ch.unibas.dmi.dbis.cs108.util.Util;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Duration;


/**
 * The main gui for the client.
 */
public class MainGui extends Application implements MainGuiInterface {
    // https://stackoverflow.com/questions/33932309/how-to-access-a-javafx-stage-from-a-controller
    private static Stage primaryStage;
    private final int gameTime = 120;

    private Input input;
    private Client client;
    private GameGuiFX gameGui;
    private ChatGuiFX chatGui;
    private LobbyGuiFX lobbyGui;
    private TimerGuiFX timerGui;
    private RankGuiFX rankGui;
    private ClientReceiver clientReceiver;
    private Timeline gameLoop;
    private Timer timer = new Timer(gameTime);

    /**
     * Launching this method will not work on some platforms.
     * What you should do is to create a separate main class and launch the GUI class from there as is done in {@link Main}
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init() {
        //
    }

    @Override
    public void start(Stage stage) {
        setPrimaryStage(stage);

        //Platform.setImplicitExit(false);
        clientReceiver = new ClientReceiver();
        client = new Client(clientReceiver, this);
        lobbyGui = new LobbyGuiFX(client);
        timerGui = new TimerGuiFX(timer);
        timerGui.setVisible(false);
        rankGui = new RankGuiFX();
        rankGui.setVisible(false);
        gameGui = new GameGuiFX(1080, 720, client);

        if (getParameters().getRaw().size() >= 2) {
            String param = getParameters().getRaw().get(1);
            String ip = Util.splitParameterAtFirstN(param, ":", 2)[0];
            String port = Util.splitParameterAtFirstN(param, ":", 2)[1];
            if (getParameters().getRaw().size() == 3) {
                String name = getParameters().getRaw().get(2);
                chatGui = new ChatGuiFX(client, port, ip, name);
            } else {
                chatGui = new ChatGuiFX(client, port, ip, "");
            }
        } else {
            chatGui = new ChatGuiFX(client);
        }

        BorderPane root = new BorderPane();

        //Icon
        Image IconImg = new Image("/icons/icon.png");
        stage.setTitle("bubble.uni");
        stage.getIcons().add(IconImg);

        Image introImg = new Image("/intro.png");
        Canvas can = gameGui;
        gameGui.setVisible(true);
        GraphicsContext gc = can.getGraphicsContext2D();
        gc.drawImage(introImg, 0, 0, can.getWidth(), can.getHeight());


        root.setCenter(gameGui);
        root.setRight(chatGui);
        root.setLeft(lobbyGui);
        root.setTop(timerGui);
        root.setBottom(rankGui);

        Scene scene = new Scene(root, 1600, 1000);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() {
        client.closeClient();
        Platform.exit();
        System.exit(0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void startGame(){
        gameGui.setFocusTraversable(true);
        this.gameGui.displayGame(true);
        timerGui.setVisible(true);
        rankGui.setVisible(false);
        chatGui.setChangeNameButtonToDisable(true);
        setUpGameLoop();
    }

    /**
     * {@inheritDoc}
     * Stops the game loop with the timer and shows the rank gui.
     */
    @Override
    public void endGame() {
        gameLoop.stop();
        timer.setRunning(false);
        timerGui.setVisible(false);
        rankGui.setVisible(true);
        chatGui.setChangeNameButtonToDisable(false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void startTimer() {
        this.timer.setTimerGui(this.timerGui);
        this.timer.startTimer();
    }

    /**
     * Sets up the game loop and tries to reach 60 frames per second
     */
    private void setUpGameLoop() {
        input = new Input(this.gameGui);

        gameLoop = new Timeline();
        //makes the game loop run indefinitely
        gameLoop.setCycleCount(Timeline.INDEFINITE);
        gameLoop.getKeyFrames().add(new KeyFrame(
                Duration.seconds(0.033), // 30 FPS
                new ClientGameLoop(this.gameGui, input, this.clientReceiver, new ClientSender(this.client), client.getPlayerId())));

        System.out.println(client.getPlayerId());

        gameLoop.play();
    }

    /**
     * @return s the primary stage of the main gui.
     */
    public static Stage getPrimaryStage() {
        return MainGui.primaryStage;
    }

    /**
     * Sets the @param primary stage.
     */
    private static void setPrimaryStage(Stage stage) {
        MainGui.primaryStage = stage;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setTimer() {this.timer = new Timer(gameTime);}

    /**
     * {@inheritDoc}
     */
    @Override
    public RankGuiFX getRankGui() {
        return this.rankGui;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LobbyGuiInterface getLobbyGui() {
        return this.lobbyGui;
    }

}
