package ch.unibas.dmi.dbis.cs108.Lobby;

import ch.unibas.dmi.dbis.cs108.connection.Connection;
import ch.unibas.dmi.dbis.cs108.game.server.ServerGameLoop;
import ch.unibas.dmi.dbis.cs108.game.server.ServerReceiver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Players have to join a lobby to start a game.
 */
public class Lobby {

    private String lobbyName;
    private ArrayList<Connection> players = new ArrayList<>();
    private ArrayList<Integer> freeIds = new ArrayList<>(Arrays.asList(0, 1, 2, 3));
    private String gameState = "OPEN";
    private ServerReceiver serverReceiver;
    private Thread gameThread;
    private ServerGameLoop gameLoop;

    public Lobby(String lobbyName, Connection connection) {
        this.lobbyName = lobbyName;
        this.players.add(connection);
        connection.setLobby(this);
    }

    /**
     * Returns a list of all players in this lobby.
     */
    public ArrayList<Connection> getPlayers() {
        return players;
    }

    /**
     * Returns size the number of players in the lobby.
     */
    public int getNumberOfPlayers() {
        return players.size();
    }

    /**
     * Returns lobbyName the name of the lobby.
     */
    public String getLobbyName() {
        return lobbyName;
    }

    /**
     * Returns gameState the current state of the lobby.
     */
    public String getGameState() {return gameState;}

    /**
     * Returns the server receiver of this lobby.
     */
    public ServerReceiver getServerReceiver(){
        return this.serverReceiver;
    }

    /**
     * Gets a free id from the id list.
     */
    public int getFreeId() {
        int id = freeIds.get(0);
        freeIds.remove(0);
        return id;
    }

    /**
     * Returns the game thread.
     */
    public Thread getGameThread() {
        return gameThread;
    }

    /**
     * Sets the gameState with the @param gameState.
     */
    public void setGameState(String gameState) {
        if(gameState.equals("IN GAME")) {
            this.serverReceiver = new ServerReceiver(this.players.size());
        }

        this.gameState = gameState;
    }

    /**
     * Sets the game thread to this @param gameThread.
     */
    public void setGameThread(Thread gameThread) {
        this.gameThread = gameThread;
    }

    /**
     * Sets the game loop to this @param gameLoop.
     */
    public void setGameLoop(ServerGameLoop gameLoop) {
        this.gameLoop = gameLoop;
    }

    /**
     * Returns the game loop.
     */
    public ServerGameLoop getGameLoop() {
        return this.gameLoop;
    }
}
