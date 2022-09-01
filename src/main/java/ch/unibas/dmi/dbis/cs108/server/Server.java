package ch.unibas.dmi.dbis.cs108.server;

import static ch.unibas.dmi.dbis.cs108.util.Util.isValidName;

import ch.unibas.dmi.dbis.cs108.Lobby.Lobby;
import ch.unibas.dmi.dbis.cs108.connection.Connection;
import ch.unibas.dmi.dbis.cs108.game.server.ServerGameLoop;
import ch.unibas.dmi.dbis.cs108.game.server.ServerSender;

import ch.unibas.dmi.dbis.cs108.protocol.Protocol;
import ch.unibas.dmi.dbis.cs108.rank.HighScore;
import ch.unibas.dmi.dbis.cs108.rank.RankEntry;
import ch.unibas.dmi.dbis.cs108.util.Util;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

/**
 * The main server functionality.
 */
public class Server implements Runnable {

    private static final Logger logger = LogManager.getLogger(Server.class);

    private static int PORT_NUMBER;

    private final ArrayList<Lobby> lobbies = new ArrayList<>();
    private final ArrayList<Connection> connections = new ArrayList<>();
    private ServerSocket serverSocket;
    private ArrayList<String> invalidNames = new ArrayList<>();

    public Server(String port) {
        PORT_NUMBER = Integer.parseInt(port);
        invalidNames.add("");       // the empty string is not a valid name
        invalidNames.add("All");    // "All" cannot be used as name as it is used in the chat to specify the recipient(s)
        invalidNames.add(Connection.getDefaultName());  // the default player name is not a valid player name

        Configurator.setLevel(LogManager.getLogger(Server.class).getName(), Level.ERROR);
        HighScore.readList();
    }

    public static void main(String[] args) {
        String port = "57532";
        if (args.length == 2) {
            if (args[1].chars().allMatch( Character::isDigit ) && Integer.parseInt(args[1]) > 1023) {
                port = args[1];
            } else {
                logMessage("Invalid port, used default port");
            }
        }
        Server server = new Server(port);
        server.startServer();
        logMessage("Server listen on port: " + port);
        logMessage("Server is running ... ");
    }

    /**
     * Prints the message with a date and time stamp.
     */
    public static void logMessage(String message) {
        Date dateTime = new Date();
        SimpleDateFormat dateTmeFormat = new SimpleDateFormat("yyyy-MM-dd, HH:mm:ss");
        String dateTimeStamp = dateTmeFormat.format(dateTime);
        System.out.println(dateTimeStamp + ": " + message);
    }

    /**
     * Calls the method run of this Server to be run in a new Thread.
     */
    public void startServer() {
        new Thread(this).start();
    }

    /**
     * This Server listens at the specified PORT_NUMBER of connection-requests by Clients. Each accepted
     * connection-request is run in a new Connection object.
     */
    public void run() {
        try {
            serverSocket = new ServerSocket(PORT_NUMBER);   // listens to the specified port for clients
            while (true) {
                Socket socket = serverSocket.accept();
                //serverGui.logMessage("Server is starting a new connection.");
                logMessage("Server is starting a new connection.");
                new Connection(this, socket);
            }
        } catch (IOException ioe) {
            logger.info("An error occurred while listening to port \" + PORT_NUMBER");
        } finally {
            try {
                if (!serverSocket.isClosed()) {
                    serverSocket.close();
                }
            } catch (Exception e) {
                // do nothing
            }
        }

    }

    /**
     * Stops the Server.
     */
    public void stopServer() {
        if (serverSocket != null && !serverSocket.isClosed()) {
            try {
                serverSocket.close();
            } catch (Exception e) {
                logger.error("The program was not able to close the server connection.");
                //serverGui.logMessage("The program was not able to close the server connection.");
            }
        }
    }

    /**
     * Adds a @param newConnection if the @param newPlayerName is available and confirms by returning true.
     * Otherwise, if @param playerName is not available, returns false.
     */
    public boolean addConnection(Connection newConnection, String newPlayerName) {
        if (!isValidName(newPlayerName, invalidNames)) {
            return false;
        }
        boolean added = false;
        boolean available;
        synchronized (connections) {
            available = isAvailable(newPlayerName);
            if (available) {
                connections.add(newConnection);
                added = true;
            }
            return added;
        }
    }

    /**
     * Adds a new Lobby to lobbies if the @param lobbyName is available and confirms with @return true.
     * The player @param playerName adds to the player list in the lobby.
     * Otherwise, if @param lobbyName is not available, @return false.
     */
    public boolean addLobby(String lobbyName, Connection connection) {
        boolean added = false;
        synchronized (lobbies) {
            if (isLobbyAvailable(lobbyName)) {
                Lobby lobby = new Lobby(lobbyName, connection);
                lobbies.add(lobby);
                connection.setLobby(lobby);
                added = true;
            }
            return added;
        }
    }

    /**
     * The player @param playerName joins the lobby @param lobbyName.
     */
    public void joinLobby(String lobbyName, Connection connection) {
        synchronized (lobbies) {
            for (Lobby lobby : lobbies) {
                if (lobby.getLobbyName().equals(lobbyName)) {
                    lobby.getPlayers().add(connection);
                    connection.setLobby(lobby);
                }
                if (lobby.getPlayers().size() == 4) {
                    lobby.setGameState("FULL");
                }
            }
        }
    }

    /**
     * Change the player name of an existing player.
     */
    public boolean changePlayerNameOfConnection(String oldPlayerName, String newPlayerName) {
        if (!isValidName(newPlayerName, invalidNames)) {
            return false;
        } else if (oldPlayerName.equals(newPlayerName)) {
            return true;
        }
        boolean available;
        synchronized (connections) {
            available = isAvailable(newPlayerName);
            if (available) {
                for (Connection connection : connections) {
                    if (connection.getPlayerName().equals(oldPlayerName)) {
                        connection.setPlayerName(newPlayerName);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Checks is the provided @param playerName is available.
     */
    private boolean isAvailable(String playerName) {
        synchronized (connections) {
            for (Connection connection : connections) {
                if (connection.getPlayerName().equals(playerName)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Checks is the provided @param lobbyName is available.
     */
    private boolean isLobbyAvailable(String lobbyName) {
        synchronized (lobbies) {
            for (Lobby lobby : lobbies) {
                if (lobby.getLobbyName().equals(lobbyName) || lobby.getLobbyName().equals("DEFAULT")) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Suggests an available name based on @param name.
     */
    public String suggestName(String name) {
        boolean available = false;
        int i = 0;
        while (!available) {
            i++;
            available = isAvailable(name + i);
        }
        return name + i;
    }

    /**
     * Removes the @param removePlayerName from the list of connections to make it available again.
     */
    public boolean removeConnection(String removePlayerName) {
        boolean removed = removePlayerName.equals(Connection.getDefaultName());
        if (!removed) {
            synchronized (connections) {
                for (Connection connection : connections) {
                    if (connection.getPlayerName().equals(removePlayerName)) {
                        connections.remove(connection);
                        logger.info("Player: " + removePlayerName + " has been removed from connections.");
                        updatePlayerNames();
                        connection.sendToClient(Protocol.QUIT_CONFIRMED);
                        removed = true;
                        break;
                    }
                }
            }
        }
        return removed;
    }

    /**
     * Removes the @param connection from the list of connections to make it available again.
     */
    public boolean removeConnection(Connection removeConnection) {
        boolean removed = false;
        String removePlayerName = removeConnection.getPlayerName();
        if (removePlayerName.equals(Connection.getDefaultName())) {
            removed = true;
        }
        if (!removed) {
            synchronized (connections) {
                for (Connection connection : connections) {
                    if (connection.getPlayerName().equals(removePlayerName)) {
                        connections.remove(connection);
                        logger.info("Player: " + removePlayerName + " has been removed from connections.");
                        updatePlayerNames();
                        removed = true;
                        break;
                    }
                }
            }
        }
        removeConnection.sendToClient(Protocol.QUIT_CONFIRMED);
        return removed;
    }

    /**
     * Removes the player @param playerName from the @param lobbyName.
     */
    public void removePlayerFromLobby(String lobbyName, Connection connection) {
        synchronized (lobbies) {
            for (Lobby lobby : lobbies) {
                if (lobby.getLobbyName().equals(lobbyName)) {
                    lobby.getPlayers().remove(connection);
                    if (lobby.getGameState().equals("FULL")) {
                        lobby.setGameState("OPEN");
                    }
                }
            }
        }
    }

    /**
     * Removes the player @param connection from the game.
     */
    public void removePlayerFromGame(Connection connection) {
        synchronized (lobbies) {
            for (Lobby lobby : lobbies) {
                if (lobby == connection.getLobby()) {
                    lobby.getGameLoop().leftPlayer(connection.getPlayerId());
                    connection.sendToClient(Protocol.END_GAME + "IN GAME");
                }
            }
        }
    }

    /**
     * Broadcasts the @param message to all connected Clients.
     */
    public void broadcastMessage(String message) {
        // maybe check whether String begins with CHAT.
        synchronized (connections) {
            for (Connection connection : connections) {
                connection.sendToClient(message);
            }
        }
    }

    /**
     * Whisper the @param message to @param fromPlayerName and to @param toPlayerName.
     */
    public void whisperMessage(String fromPlayerName, String toPlayerName, String message) {
        synchronized (connections) {
            for (Connection connection : connections) {
                if (connection.getPlayerName().equals(fromPlayerName) || connection.getPlayerName().equals(toPlayerName)) {
                    connection.sendToClient(message);
                }
            }
        }
    }

    /**
     * Broadcast the @param message to all Clients in the @param lobbyName.
     */
    public void broadcastLobbyMessage(String lobbyName, String message) {
        synchronized (lobbies) {
            synchronized (connections) {
                for (Lobby lobby : lobbies) {
                    if (lobby.getLobbyName().equals(lobbyName)) {
                        for (Connection player : lobby.getPlayers()) {
                            for (Connection connection : connections) {
                                if (connection.getPlayerName().equals(player.getPlayerName())) {
                                    connection.sendToClient(message);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Sends the command GRANT_PLAYER_NAMES to all connected Clients to update the playerNames.
     */
    public void updatePlayerNames() {
        String playerNames = grandPlayerNames();
        synchronized (connections) {
            for (Connection connection : connections) {
                connection.sendToClient(Protocol.GRANT_PLAYER_NAMES + playerNames);
            }
        }
    }

    /**
     * Sends the command GRANT_LOBBY_LIST to all connected Clients to update the lobbyNames.
     */
    public void updateLobbyNames() {
        String lobbyNames = grandLobbyNames();
        synchronized (connections) {
            for (Connection connection : connections) {
                connection.sendToClient(Protocol.GRANT_LOBBY_LIST + lobbyNames);
            }
        }
    }

    /**
     * Sends the command GRANT_NAMES_IN_LOBBY to all Client in this lobby to update the player list.
     */
    public void updateNamesInLobby(String lobbyName) {
        String names = grandNamesInLobby(lobbyName);
        synchronized (lobbies) {
            synchronized (connections) {
                for (Lobby lobby : lobbies) {
                    if (lobby.getLobbyName().equals(lobbyName)) {
                        for (Connection player : lobby.getPlayers()) {
                            for (Connection connection : connections) {
                                if (connection.getPlayerName().equals(player.getPlayerName())) {
                                    connection.sendToClient(Protocol.GRANT_NAMES_IN_LOBBY + names);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Checks every Connection to grand all @return playerNames.
     */
    public String grandPlayerNames() {
        StringBuilder sb = new StringBuilder();
        synchronized (connections) {
            for (Connection connection : connections) {
                sb.append("|").append(connection.getPlayerName());
            }
        }
        return sb.toString();
    }

    /**
     * Checks every Lobby to grand all @return lobbyNames.
     */
    public String grandLobbyNames() {
        StringBuilder sb = new StringBuilder();
        synchronized (lobbies) {
            for (Lobby lobby : lobbies) {
                sb.append("|").append(lobby.getLobbyName()).append("/").append(lobby.getGameState());
            }
        }
        return sb.toString();
    }

    /**
     * Checks @param lobbyName and to grand all @return playerNames in this lobby.
     */
    private String grandNamesInLobby(String lobbyName) {
        StringBuilder sb = new StringBuilder();
        synchronized (lobbies) {
            for (Lobby lobby : lobbies) {
                if (lobby.getLobbyName().equals(lobbyName)) {
                    for (Connection player : lobby.getPlayers()) {
                        sb.append("|").append(player.getPlayerName());
                    }
                }
            }
        }
        return sb.toString();
    }

    /**
     * Sets up a new game for the @param lobbyName.
     */
    public void initializeGame(String lobbyName) {
        synchronized(lobbies) {
            for (Lobby lobby : lobbies) {
                if (lobby.getLobbyName().equals(lobbyName)) {
                    lobby.setGameState("IN GAME");
                    for (Connection player : lobby.getPlayers()) {
                        // setup game
                        player.setPlayerId(lobby.getFreeId());
                        player.sendToClient(Protocol.SEND_PLAYER_ID + player.getPlayerId());
                        player.sendToClient(Protocol.NEW_GAME);
                    }

                    var serverLoop = new ServerGameLoop(
                            1080,
                            720,
                            new ServerSender(this, lobbyName),
                            lobby);
                    Thread thread = new Thread(serverLoop);
                    thread.start();
                    lobby.setGameThread(thread);
                }
            }
        }
    }

    /**
     * Ends the game in the @param lobbyName and send confirmation to the client.
     */
    public void endGame(String lobbyName) {
        synchronized (lobbies) {
            for (Lobby lobby : lobbies) {
                if (lobby.getLobbyName().equals(lobbyName)) {
                    for (Connection player : lobby.getPlayers()) {
                        player.sendToClient(Protocol.END_GAME);
                    }
                    lobby.setGameState("FINISHED");
                }
            }
        }
    }

    /**
     * Validate the player's request in the specified direction.
     */
    public void movePlayerCharacter(Connection player, String direction) {
        var directionVector = Util.parseVector2D(direction);
        player.getLobby().getServerReceiver().insertMoveDirection(player.getPlayerId(), directionVector);
    }

    /**
     * Updates the angle @param parameter of the player @param connection.
     */
    public void updatePlayerAngle(Connection connection, String parameter){
        var angle = Float.parseFloat(parameter);
        connection.getLobby().getServerReceiver().insertPlayerAngle(connection.getPlayerId(), angle);
    }

    /**
     * Creates new bubble with direction @param parameter from the player @param connection.
     */
    public void createBubble(Connection connection, String parameter) {
        var directionVector = Util.parseVector2D(parameter);
        connection.getLobby().getServerReceiver().insertBubbleCreation(connection.getPlayerId(), directionVector);
    }

    /**
     * Sends all clients in lobby @param lobbyName the current player positions @param positions.
     */
    public void sendPlayerPositions(String lobbyName, ArrayList<Vector2D> positions){
        var parameter = Util.vectorListToString(positions);

        synchronized (lobbies) {
            for (Lobby lobby : lobbies) {
                if (lobby.getLobbyName().equals(lobbyName)) {
                    for (Connection player : lobby.getPlayers()) {
                        player.sendToClient(Protocol.SEND_PLAYER_POSITIONS + parameter);
                    }
                }
            }
        }
    }
    /**
     * Sends all clients in lobby @param lobbyName the current player sizes @param sizes.
     */
    public void sendPlayerSizes(String lobbyName, ArrayList<Float> sizes){
        var parameter = Util.floatListToString(sizes);

        synchronized (lobbies) {
            for (Lobby lobby : lobbies) {
                if (lobby.getLobbyName().equals(lobbyName)) {
                    for (Connection player : lobby.getPlayers()) {
                        player.sendToClient(Protocol.SEND_PLAYER_SIZES + parameter);
                    }
                }
            }
        }
    }

    /**
     * Sends all clients in lobby @param lobbyName the current player angles @param angles.
     */
    public void sendPlayerAngles(String lobbyName, ArrayList<Float> angles) {
        var parameter = Util.floatListToString(angles);

        synchronized (lobbies) {
            for (Lobby lobby : lobbies) {
                if (lobby.getLobbyName().equals(lobbyName)){
                    for (Connection player : lobby.getPlayers()){
                        player.sendToClient(Protocol.SEND_PLAYER_ANGLES + parameter);
                    }
                }
            }
        }
    }

    /**
     * Sends all clients in lobby @param lobbyName the current bubble positions @param positions.
     */
    public void sendBubblePositions(String lobbyName, ArrayList<Vector2D> positions){
        var parameter = Util.vectorListToString(positions);

        synchronized (lobbies) {
            for (Lobby lobby : lobbies) {
                if (lobby.getLobbyName().equals(lobbyName)) {
                    for (Connection player : lobby.getPlayers()) {
                        player.sendToClient(Protocol.SEND_BUBBLE_POSITION + parameter);
                    }
                }
            }
        }
    }

    /**
     * Sends all clients in lobby @param lobbyName the current bubble ids @param ids.
     */
    public void sendBubbleIds(String lobbyName, ArrayList<Integer> ids) {
        StringBuilder sb = new StringBuilder();

        for (Integer i : ids) {
            sb.append("|").append(i);
        }
        String parameter = sb.toString();

        synchronized (lobbies) {
            for (Lobby lobby : lobbies) {
                if (lobby.getLobbyName().equals(lobbyName)) {
                    for (Connection player : lobby.getPlayers()) {
                        player.sendToClient(Protocol.SEND_BUBBLE_ID + parameter);
                    }
                }
            }
        }
    }

    /**
     * Sends all clients in lobby @param lobbyName the current energizer positions @param positions.
     */
    public void sendEnergizerPositions(String lobbyName, ArrayList<Vector2D> positions){
        var parameter = Util.vectorListToString(positions);

        synchronized (lobbies) {
            for (Lobby lobby : lobbies) {
                if (lobby.getLobbyName().equals(lobbyName)) {
                    for (Connection player : lobby.getPlayers()) {
                        player.sendToClient(Protocol.SEND_ENERGIZER_POSITIONS + parameter);
                    }
                }
            }
        }
    }

    /**
     * Sends the ranking (using the command SEND_RANKING) to the Clients of the lobby after the game has ended.
     * The ranking consists of a string concatenating the player name, its size, and the remaining time in the lobby,
     * separated by the delimiter "|", for each player in the lobby.
     * The methods also send the parameters of the winner to the HighScore to be verified if it can be added.
     */
    public void sendRanking(String lobbyName, Map<String, Double> ranking, String time) {
        StringBuilder sb = new StringBuilder();
        ArrayList<String> names = new ArrayList<>(ranking.keySet());
        ArrayList<Double> sizes = new ArrayList<>(ranking.values());

        for (int i = 0; i < names.size(); i++) {
            sizes.set(i, Math.round(sizes.get(i) * 100d) / 100d);
            sb.append("|").append(names.get(i)).append("|").append(sizes.get(i)).append("|").append(time);
            if (i == 0) {
                logger.debug(names.get(i) + ", " + sizes.get(i) + ", " + time);
                HighScore.add(new RankEntry(names.get(i), sizes.get(i), time));
                sendHighScore();
                HighScore.writeList();
            }
        }

        var parameter = sb.toString();

        synchronized (lobbies) {
            for (Lobby lobby : lobbies) {
                if (lobby.getLobbyName().equals(lobbyName)) {
                    for (Connection player : lobby.getPlayers()) {
                        player.sendToClient(Protocol.SEND_RANKING + parameter);
                    }
                }
            }
        }
    }

    /**
     * Send the (updated) high score to the clients.
     */
    public void sendHighScore() {
        String highScore = HighScore.getHighScoreAsString();
        synchronized (connections) {
            for (Connection connection : connections) {
                connection.sendToClient(Protocol.SEND_HIGH_SCORE + highScore);
            }
        }


    }

    /**
     * Returns true if the @param playerName is contained in connections and false otherwise.
     */
    public boolean containsPlayerName(String playerName) {
        synchronized(connections) {
            for (Connection connection : connections) {
                if (connection.getPlayerName().equals(playerName)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns the list of connections. This is used for testing purposes.
     */
    public ArrayList<Connection> getConnections() {
        return connections;
    }

    /**
     * Returns the list of lobbies. This is used for testing purposes.
     */
    public ArrayList<Lobby> getLobbies() {
        return lobbies;
    }
}
