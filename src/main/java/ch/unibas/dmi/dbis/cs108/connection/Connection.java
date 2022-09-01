package ch.unibas.dmi.dbis.cs108.connection;

import ch.unibas.dmi.dbis.cs108.Lobby.Lobby;
import ch.unibas.dmi.dbis.cs108.protocol.Protocol;
import ch.unibas.dmi.dbis.cs108.server.Server;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

import java.net.Socket;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import static ch.unibas.dmi.dbis.cs108.util.Util.removeIllegalCharacters;
import static ch.unibas.dmi.dbis.cs108.util.Util.splitCommandParameter;
import static ch.unibas.dmi.dbis.cs108.util.Util.splitParameterAtFirstDelim;
import static ch.unibas.dmi.dbis.cs108.util.Util.splitParameterAtFirstN;

public class Connection implements Runnable {

    private static final Logger logger = LogManager.getLogger(Connection.class);

    private static final String DEFAULT_NAME = "[New Player]";
    private Server server;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private String playerName = DEFAULT_NAME;
    private ArrayList<Character> illegalCharacters = new ArrayList<>();
    private Lobby lobby;
    private int playerId;

    public Connection(Server server, Socket socket) {
        this.server = server;
        this.socket = socket;
        new Thread(this).start();
        try {
            //throws exception when the client don't communicate with the server for 30 sec
            this.socket.setSoTimeout(3000);
        } catch (SocketException se) {
            //do nothing
        }
        illegalCharacters.add('|');

        Configurator.setLevel(LogManager.getLogger(Connection.class).getName(), Level.ERROR);
    }

    /**
     * While the Connection between a Server and a specific Client is running with a BufferedReader in and a PrintWriter
     * out to communicate
     * After 5 sec without reading a command client at readLine() it throws a timeout exception because
     * of the timeout the client sets a flag (isPingSent) and sends a PONG and waits for an answer to check the connection.
     * Because of the flag the 2nd timeout means a disconnection.
     * If the command NAME is received from a Client, the Server verifies the player name received from the Client is
     * unique; if it is, it is , this new Connection is added to the list of connections managed by the server and
     * ACCEPTED is send to the corresponding Client; otherwise REJECTED is send to the corresponding Client.
     * If CHANGE_NAME + newPlayerName is received, the Server validates and, if valid, update the player name to
     * newPlayerName.
     * If SET_UP_LOBBY + lobbyName is received from a client, then the server sets up a new lobby
     * If QUIT is received, the run-method is exited.
     * If BROADCAST + message is received from a valid player name, then the message is broadcast to all Clients.
     * If BROADCAST_LOBBY + lobbyName + message is received, then the message is broadcast to all clients in this lobby.
     * If WHISPER + | + fromPlayerName + | + toPlayerName + | + message is received from the Client, the Server is
     * asked to only transmit the message to the two players with name fromPlayerName and toPlayerName.
     * If REQUEST_PLAYER_NAME is received, the Server is asked to send all player names that are currently connected.
     * If JOIN_LOBBY + lobbyName is received from a client, then the server adds this client to the lobby.
     * If LEAVE_LOBBY + lobbyName is received from a client, then the server removes this client from the lobby.
     * If REQUEST_NEW_GAME is received from a client, the server is asked to prepare and start a new game for all
     * players that are in the same lobby as the player who sent the request.
     * If REQUEST_END_GAME + lobbyName is received from a client, then the server ends the game in this lobby.
     * If MOVE + direction is received, the server is asked to verify and (if valid) approve the move for the player.
     * If CREATE_BUBBLE is received, the server is asked to create a new bubble in the specified direction.
     * If ANGLE is received, the server is asked to update the player angle.
     * If PONG is received, the server response with the PONG_ANSWER
     */
    public void run() {
        boolean keepRunning = true;
        boolean validPlayerName = false;
        boolean isPingSent = false;

        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(),"UTF-8"), true);

            sendToClient(Protocol.SUBMIT);
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException ie) {
                // do nothing
            }
            while(keepRunning) {
                try {
                    String input = in.readLine();
                    isPingSent = false;
                    if (input == null) {
                        keepRunning = false;
                    } else if (input.length() > 3) {
                        String[] splitInputArray = splitCommandParameter(input);
                        String command = splitInputArray[0];
                        String parameter = splitInputArray[1];
                        logger.info("Connection " + playerName + " received command " + command);
                        logger.info("Connection " + playerName + " received parameter " + parameter);
                        System.out.println();
                        switch (command) {
                            case Protocol.NAME:
                                String submittedPlayerName = removeIllegalCharacters(parameter, illegalCharacters);
                                boolean added = server.addConnection(this, submittedPlayerName);
                                if (added) {
                                    validPlayerName = true;
                                    playerName = submittedPlayerName;
                                    sendToClient(Protocol.ACCEPTED + playerName);
                                    String message = Protocol.CHAT + playerName + " joined the game.";
                                    server.broadcastMessage(message);
                                    server.updateLobbyNames();
                                    server.updatePlayerNames();
                                    server.sendHighScore();
                                } else {
                                    String suggestedPlayerName = server.suggestName(submittedPlayerName);
                                    sendToClient(Protocol.REJECTED + suggestedPlayerName);
                                }
                                break;
                            case Protocol.CHANGE_NAME:
                                String oldPlayerName = playerName;
                                String lobbyName = splitParameterAtFirstDelim(parameter)[0];
                                String newPlayerName = removeIllegalCharacters(splitParameterAtFirstDelim(parameter)[1], illegalCharacters);
                                boolean changed = server.changePlayerNameOfConnection(oldPlayerName, newPlayerName);
                                while(!changed) {
                                    newPlayerName = server.suggestName(newPlayerName);
                                    changed = server.changePlayerNameOfConnection(oldPlayerName, newPlayerName);
                                }
                                sendToClient(Protocol.ACCEPTED + playerName);
                                String message = Protocol.CHAT + oldPlayerName + " changed name to " + newPlayerName;
                                server.broadcastMessage(message);
                                if (!lobbyName.equals("DEFAULT")) {
                                    server.updateNamesInLobby(lobbyName);
                                }
                                server.updateLobbyNames();
                                server.updatePlayerNames();
                                break;
                            case Protocol.SET_UP_LOBBY:
                                lobbyName = removeIllegalCharacters(parameter, illegalCharacters);
                                boolean addedLobby = server.addLobby(lobbyName, this);
                                while(!addedLobby) {
                                    lobbyName = server.suggestName(lobbyName);
                                    addedLobby = server.addLobby(lobbyName, this);
                                }
                                sendToClient(Protocol.NEW_LOBBY + lobbyName);
                                message = Protocol.CHAT + playerName + " set up Lobby " + lobbyName;
                                server.broadcastMessage(message);
                                server.updateLobbyNames();
                                server.updateNamesInLobby(lobbyName);
                                server.updatePlayerNames();
                                break;
                            case Protocol.QUIT:
                                message = Protocol.CHAT + playerName + " quit the game.";
                                server.broadcastMessage(message);
                                server.removeConnection(this);
                                break;
                            case Protocol.BROADCAST:
                                if (validPlayerName) {
                                    message = Protocol.CHAT + parameter;
                                    server.broadcastMessage(message);
                                } else {
                                    logger.error("BROADCAST: Player name invalid");
                                }
                                break;
                            case Protocol.BROADCAST_LOBBY:
                                lobbyName = splitParameterAtFirstDelim(parameter)[0];
                                message = Protocol.CHAT + splitParameterAtFirstDelim(parameter)[1];
                                server.broadcastLobbyMessage(lobbyName, message);
                                break;
                            case Protocol.WHISPER:
                                String[] strArray = splitParameterAtFirstN(parameter, 3);
                                String fromPlayerName = strArray[0];
                                String toPlayerName = strArray[1];
                                message = Protocol.CHAT + strArray[2];
                                server.whisperMessage(fromPlayerName, toPlayerName, message);
                                break;
                            case Protocol.REQUEST_PLAYER_NAMES:
                                server.updateLobbyNames();
                                break;
                            case Protocol.JOIN_LOBBY:
                                lobbyName = parameter;
                                server.joinLobby(lobbyName, this);
                                sendToClient(Protocol.NEW_LOBBY + lobbyName);
                                message = Protocol.CHAT + playerName + " joined Lobby " + lobbyName;
                                server.broadcastMessage(message);
                                server.updateLobbyNames();
                                server.updateNamesInLobby(lobbyName);
                                server.updatePlayerNames();
                                break;
                            case Protocol.LEAVE_LOBBY:
                                lobbyName = parameter;
                                server.removePlayerFromLobby(lobbyName, this);
                                sendToClient(Protocol.NEW_LOBBY + "DEFAULT");
                                message = Protocol.CHAT + playerName + " left Lobby " + lobbyName;
                                server.broadcastMessage(message);
                                server.updateLobbyNames();
                                if (this.lobby.getGameState().equals("IN GAME")) {
                                    server.removePlayerFromGame(this);
                                } else {
                                    server.updateNamesInLobby(lobbyName);
                                }
                                server.updatePlayerNames();
                                break;
                            case Protocol.REQUEST_NEW_GAME:
                                server.initializeGame(parameter);
                                server.updateLobbyNames();
                                break;
                            case Protocol.REQUEST_END_GAME:
                                server.endGame(parameter);
                                break;
                            case Protocol.MOVE:
                                // TO DO ask server to move in the specified direction
                                logger.debug("Parameter: " + parameter);
                                server.movePlayerCharacter(this, parameter);
                                break;
                            case Protocol.CREATE_BUBBLE:
                                server.createBubble(this, parameter);
                                break;
                            case Protocol.ANGLE:
                                server.updatePlayerAngle(this, parameter);
                                break;
                            case Protocol.PING_ANSWER:
                                //does nothing because its just the answer from the client
                                break;
                            case Protocol.PONG:
                                sendToClient(Protocol.PONG_ANSWER);
                                break;
                            default:
                                logger.error("Connection " + playerName + " received parameter " + parameter + " for undefined command");
                        }
                    }
                } catch (SocketTimeoutException ste) {
                    if (isPingSent) {
                        logger.info("No answer to PING received.");
                        String message = Protocol.PING + playerName + " lost connection";
                        server.broadcastMessage(message);
                        server.removePlayerFromLobby(lobby.getLobbyName(), this);
                        if (this.lobby.getGameState().equals("IN GAME")) {
                            server.removePlayerFromGame(this);
                        } else {
                            server.updateNamesInLobby(lobby.getLobbyName());
                        }
                        keepRunning = false;
                    } else {
                        sendToClient(Protocol.PING);
                        isPingSent = true;
                    }

                } catch (IOException ioe) {
                    //do nothing
                }
            }
        } catch (IOException ioe) {
            if (keepRunning) {  // player did not quit intentionally
                logger.info("An error occurred when connecting to a new client or communicating with that client");
            }
        } finally {
            quitConnection();
            logger.info("Finally.");
        }


    }

    /**
     * Removes the disconnected player name from the list of player names managed by the Server.
     */
    private void quitConnection() {
        logger.info("Connection ended for " + playerName);
        if (!playerName.equals(DEFAULT_NAME)) {
            server.removeConnection(playerName);
        }
        try {
            socket.close();
        } catch(IOException ioe) {
            // do nothing
        }
    }

    /**
     * Sends a Strong @param str to this Client.
     */
    public void sendToClient(String str) {
        out.println(str); // just print wont work!
        server.logMessage(str + " has been sent to " + playerName);
    }

    /**
     * Returns this player name.
     */
    public String getPlayerName() {
        return this.playerName;
    }

    /**
     * Change the player name to @param player name
     */
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    /**
     * Returns the player id
     */
    public int getPlayerId() {
        return this.playerId;
    }

    /**
     * Sets the player id to the @param id
     */
    public void setPlayerId(int id) {
        this.playerId = id;
    }

    /**
     * Returns the lobby
     * @return
     */
    public Lobby getLobby(){
        return this.lobby;
    }

    /**
     * Sets the lobby of the player to @param lobby
     */
    public void setLobby(Lobby lobby){
        this.lobby = lobby;
    }

    /**
     * Returns the default player name: DEFAULT_NAME.
     */
    public static String getDefaultName() {
        return DEFAULT_NAME;
    }

    /**
     *  Returns True if the @param playerName is equal to the DEFAULT_NAME and False otherwise.
     */
    public boolean isDefaultName(String playerName) {
        return playerName.equals(Connection.DEFAULT_NAME);
    }

}
