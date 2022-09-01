package ch.unibas.dmi.dbis.cs108.game.server;

import ch.unibas.dmi.dbis.cs108.Lobby.Lobby;
import ch.unibas.dmi.dbis.cs108.game.server.collider.ColliderException;
import ch.unibas.dmi.dbis.cs108.timer.Timer;
import ch.unibas.dmi.dbis.cs108.util.Util;
import javafx.geometry.Rectangle2D;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * The ServerGameLoop class is responsible for calculations
 * such as setting the player's position, checking for collisions etc.
 */
public class ServerGameLoop implements Runnable {
    private double canvasWidth;
    private double canvasHeight;
    private double blockWidth = 80;
    private double blockHeight = 50;
    private int time = 120;

    private Rectangle2D boundaries;
    private Random random = new Random();
    private GameWorld gameWorld;
    private Lobby lobby;
    private Timer timer;

    private long lastUpdate;
    private static final float SPEED = 300;

    private final ServerReceiverInterface receiver;
    private final ServerSenderInterface sender;

    private int playerCount;
    private int leftPlayers = 0;

    private boolean running = true;

    public ServerGameLoop(
            double canvasWidth,
            double canvasHeight,
            ServerSenderInterface sender,
            Lobby lobby) {
        this.receiver = lobby.getServerReceiver();
        this.sender = sender;
        this.playerCount = lobby.getNumberOfPlayers();
        this.canvasWidth = canvasWidth;
        this.canvasHeight = canvasHeight;
        this.lobby = lobby;
        this.lobby.setGameLoop(this);
        this.timer = new Timer(time);
        this.timer.setGameLoop(this);
        this.timer.startTimer();

        // These are the boundaries of the level itself, meaning the area outside of it
        // where the players and bubbles cannot pass through.
        boundaries = new Rectangle2D(-2, -2, canvasWidth + 2, canvasHeight + 2);

        var blocks = new Rectangle2D[]{
                new Rectangle2D(canvasWidth / 4 - blockWidth / 2, canvasHeight / 4 - blockHeight / 2, blockWidth, blockHeight),
                new Rectangle2D(3 * canvasWidth / 4 - blockWidth / 2, canvasHeight / 4 - blockHeight / 2, blockWidth, blockHeight),
                new Rectangle2D(canvasWidth / 4 - blockWidth / 2, 3 * canvasHeight / 4 - blockHeight / 2, blockWidth, blockHeight),
                new Rectangle2D(3 * canvasWidth / 4 - blockWidth / 2, 3 * canvasHeight / 4 - blockHeight / 2, blockWidth, blockHeight)
        };

        var players = new Player[playerCount];
        for (int i = 0; i < playerCount; i++) {
            players[i] = new Player(1, gameWorld, lobby.getPlayers().get(i));
            if (i == 0) {
                players[i].setPos(100, 100);
            } else if (i == 1) {
                players[i].setPos(100, canvasHeight - 100);
            } else if (i == 2) {
                players[i].setPos(canvasWidth - 100, 100);
            } else if (i == 3) {
                players[i].setPos(canvasWidth - 100, canvasHeight - 100);
            }
        }

        gameWorld = new GameWorld(players, blocks);
    }

    /**
     * Constructor for debug
     */
    public ServerGameLoop(double width,
                          double height,
                          int n,
                          ServerReceiver debugServerReceiver,
                          DebugServerSender debugServerSender) {
        this.canvasWidth = width;
        this.canvasHeight = height;
        this.playerCount = n;
        this.receiver = debugServerReceiver;
        this.sender = debugServerSender;

        boundaries = new Rectangle2D(-2, -2, canvasWidth + 2, canvasHeight + 2);

        var blocks = new Rectangle2D[]{
                new Rectangle2D(canvasWidth / 4 - blockWidth / 2, canvasHeight / 4 - blockHeight / 2, blockWidth, blockHeight),
                new Rectangle2D(3 * canvasWidth / 4 - blockWidth / 2, canvasHeight / 4 - blockHeight / 2, blockWidth, blockHeight),
                new Rectangle2D(canvasWidth / 4 - blockWidth / 2, 3 * canvasHeight / 4 - blockHeight / 2, blockWidth, blockHeight),
                new Rectangle2D(3 * canvasWidth / 4 - blockWidth / 2, 3 * canvasHeight / 4 - blockHeight / 2, blockWidth, blockHeight)
        };

        var players = new Player[playerCount];
        for (int i = 0; i < playerCount; i++) {
            players[i] = new Player(1, gameWorld, null);
            if (i == 0) {
                players[i].setPos(100, 100);
            } else if (i == 1) {
                players[i].setPos(100, canvasHeight - 100);
            } else if (i == 2) {
                players[i].setPos(canvasWidth - 100, 100);
            } else if (i == 3) {
                players[i].setPos(canvasWidth - 100, canvasHeight - 100);
            }
        }

        gameWorld = new GameWorld(players, blocks);
    }

    @Override
    public void run() {
        while (!gameWorld.playerHasMaximumMass() && running) {
            try {
                var dt = (System.currentTimeMillis() - lastUpdate) / 1000f;
                lastUpdate = System.currentTimeMillis();

                updatePhysics();
                spawnNewEnergizers();

                updatePlayers(dt);
                updateBubbles(dt);
                updateEnergizers(dt);

                sendDataToClient();

                gameWorld.removeMarked();

                if (!(time > 0)) {
                    running = false;
                }

                var waitTime = (int) Math.max(1000 / 30f - (System.currentTimeMillis() - lastUpdate), 0);
                lastUpdate = System.currentTimeMillis();

                Thread.sleep(waitTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        sender.sendRanking(makeRanking(gameWorld.playerHasMaximumMass()), timer.getTimeString());
        timer.setRunning(false);
        sender.endGame(lobby.getLobbyName());
    }

    /**
     * Loops through all the players, energizers and bubbles
     * and checks for collisions.
     */
    private void updatePhysics() throws ColliderException {
        for (Bubble bubble : gameWorld.getBubbles()) {
            bubble.checkForCollisions(gameWorld.getColliders());
        }

        for (Energizer energizer : gameWorld.getEnergizer()) {
            energizer.checkForCollisions(gameWorld.getColliders());
        }

        for (Player player : gameWorld.getPlayers()) {
            player.checkForCollisions(gameWorld.getColliders());
        }
    }

    /**
     * Updates the velocity, rotation and mass of every player according to
     * their inputs.
     * @param dt is delta time
     */
    private void updatePlayers(float dt) {
        for (int i = 0; i < playerCount; i++) {
            var moveDirection = receiver.receiveMoveDirection(i);
            var bubbleDirection = receiver.receiveBubbleCreation(i);
            var angle = receiver.receivePlayerAngle(i);

            var player = gameWorld.getPlayer(i);

            if (bubbleDirection != null) {
                if (player.getMass() > 0.25) {
                    var bubbleVelocity = bubbleDirection.scalarMultiply(650);
                    var bubble = new Bubble(gameWorld, player);

                    /*
                     * The spawn location of the bubble is at the tip of the player's arrow, this is why
                     * mouseDirection.getX or .getY is added to the player's position.
                     * This is then multiplied with the bubbleOffsetFactor, so the position
                     * will change depending on the player's current size.
                     */
                    double bubbleOffsetFactor = player.getRadius() * 1.4;
                    bubble.setPos(player.getPosX() + bubbleDirection.getX() * bubbleOffsetFactor, player.getPosY() + bubbleDirection.getY() * bubbleOffsetFactor);
                    bubble.setVelocity(bubbleVelocity);
                    gameWorld.addBubble(bubble);
                    player.setMass(player.getMass() - 0.1f);
                    player.setScale(player.getScale() - 0.2f);
                }
            }

            var playerVelocity = moveDirection;
            if (playerVelocity.getNormSq() > 0) {
                playerVelocity = playerVelocity.normalize().scalarMultiply(SPEED);
            }

            player.setPlayerRotation(angle);
            player.setVelocity(playerVelocity);
            player.update(dt);
        }
    }

    /**
     * Creates new energizers if there are less than 20 currently
     * in the level and the total mass (players mass + energizers mass)
     * is lower than 4 or if a player has reached the minimal mass.
     */
    private void spawnNewEnergizers() {
        if (gameWorld.calculateInGameMass() < 4 || gameWorld.playerHasMinimumMass()) {
            if (gameWorld.getEnergizer().size() < 20) {
                var energizer = new Energizer(gameWorld);
                energizer.setPos(random.nextFloat() * (float) canvasWidth, random.nextFloat() * (float) canvasHeight);
                gameWorld.addEnergizer(energizer);
            }
        }
    }

    /**
     * Updates all bubbles after delta time has passed.
     * Removes bubbles when they collide with the boundaries.
     */
    private void updateBubbles(float dt) {
        for (Bubble bubble : gameWorld.getBubbles()) {
            if (boundaries.contains(bubble.getPosX(), bubble.getPosY())) {
                bubble.update(dt);
            } else {
                gameWorld.removeBorderBubble(bubble);
            }
        }
    }

    /**
     * Updates all energizers after delta time has passed.
     */
    private void updateEnergizers(float dt) {
        for (Energizer energizer : gameWorld.getEnergizer()) {
            energizer.update(dt);
        }
    }

    /**
     * Updates the time.
     */
    public void updateTime(int time) {
        this.time = time;
    }

    /**
     * Remove player from game (only visually).
     */
    public void leftPlayer(int id) {
        gameWorld.getPlayer(id).setMass(0);
        gameWorld.getPlayer(id).setMaxMass(0);
        gameWorld.getPlayer(id).setPos(0, 0);
        leftPlayers++;
        if (playerCount - leftPlayers <= 1) {
            running = false;
        }
    }

    /**
     *Creates a ranking based on the mass of the players.
     */
    private Map<String, Double> makeRanking(boolean maxMass) {
        Map<String, Double> list = new HashMap<>();
        for (Player player : gameWorld.getPlayers()) {
            if (maxMass) {
                list.put(player.getPlayer().getPlayerName(), player.getMass());
            } else {
                timer.setTime(0);
                list.put(player.getPlayer().getPlayerName(), player.getMaxMass());
            }
        }
        var ranking = Util.sortByValue(list, false);
        return ranking;
    }

    /**
     * Sends data regarding the player's position, player's size,
     * bubbles' positions and energizers' positions to the client.
     */
    private void sendDataToClient() {
        ArrayList<Vector2D> playerPositions = new ArrayList<>();
        ArrayList<Float> playerSizes = new ArrayList<>();
        ArrayList<Float> playerAngles = new ArrayList<>();

        for (Player player : gameWorld.getPlayers()) {
            playerPositions.add(player.getPos());
            playerSizes.add(player.getScale());
            playerAngles.add(player.getAngle());
        }

        sender.sendPlayerPositions(playerPositions);
        sender.sendPlayerSizes(playerSizes);
        sender.sendPlayerAngles(playerAngles);

        ArrayList<Vector2D> bubblePositions = new ArrayList<>();
        ArrayList<Integer> bubbleIds = new ArrayList<>();
        for (Bubble bubble : gameWorld.getBubbles()) {
            Vector2D pos = bubble.getPos();
            int id = bubble.getPlayer().getPlayer().getPlayerId();
            bubblePositions.add(pos);
            bubbleIds.add(id);
        }
        sender.sendBubblePositions(bubblePositions);
        sender.sendBubbleIds(bubbleIds);

        ArrayList<Vector2D> energizerPositions = new ArrayList<>();
        for (Energizer energizer : gameWorld.getEnergizer()) {
            Vector2D pos = energizer.getPos();
            energizerPositions.add(pos);
        }
        sender.sendEnergizerPositions(energizerPositions);
    }

    /**
     * Sets the game time to @param time
     */
    public void setTime(int time) {
        this.time = time;
    }
}
