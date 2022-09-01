package ch.unibas.dmi.dbis.cs108.client;

import static ch.unibas.dmi.dbis.cs108.util.Util.splitCommandParameter;
import static ch.unibas.dmi.dbis.cs108.util.Util.splitParameter;

import ch.unibas.dmi.dbis.cs108.game.client.ClientReceiver;
import ch.unibas.dmi.dbis.cs108.game.client.ClientReceiverInterface;
import ch.unibas.dmi.dbis.cs108.gui.ChatGuiInterface;
import ch.unibas.dmi.dbis.cs108.gui.GameGuiInterface;
import ch.unibas.dmi.dbis.cs108.gui.LobbyGuiInterface;
import ch.unibas.dmi.dbis.cs108.gui.MainGuiInterface;
import ch.unibas.dmi.dbis.cs108.gui.MainGui;
import ch.unibas.dmi.dbis.cs108.protocol.Protocol;
import ch.unibas.dmi.dbis.cs108.rank.RankEntry;
import ch.unibas.dmi.dbis.cs108.util.Util;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

import javax.swing.JOptionPane;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

/**
 * The Client class manages the user-interactions with the Server class. It uses a number of gui-classes for graphical
 * representations like the ChatGuiFX class.
 */
public class Client implements Runnable {

    private static final Logger logger = LogManager.getLogger(Client.class);

    private int portNumber = 57532;
    private String host = "localhost";
    private String playerName = "Andrew";
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private ChatGuiInterface chatGui;
    private MainGuiInterface mainGui;
    private LobbyGuiInterface lobbyGui;
    private GameGuiInterface gameGui;
    private boolean isLoggedIn = false;
    private boolean hasJoinedGame = false;
    private ClientReceiverInterface clientReceiver;
    private int playerId;


    public Client(ClientReceiverInterface clientReceiver, MainGui mainGui) {
        this.clientReceiver = clientReceiver;
        this.mainGui = mainGui;
        Configurator.setLevel(LogManager.getLogger(Client.class).getName(), Level.ERROR);
    }

    /**
     * Creates a new Socket with a BufferedReader in and a PrintWriter out to connect to and communicate with the
     * Server:
     * After 5 sec without reading a command at readLine() it throws a SocketTimeoutException.
     * After the timeout the client sets a flag (isPingSent) and sends a PONG then waits for an answer to check the connection.
     * Because of the flag the 2nd timeout means a disconnection.
     * If the Client receives SUBMIT from the Server, it sends the player name using the command NAME.
     * If the Client receives ACCEPTED from the Server it does nothing so far.
     * If the Client receives REJECTED from the Server, the user has the choice of selecting the player name provided
     * by the Server or re-entering a different player name.
     * If the Client receives QUIT_CONFIRMED, the run-loop is left.
     * If the Client receives CHAT + message from the Server, then the message is displayed in the corresponding
     * ClientGui.
     * If the Client receives GRANT_PLAYER_NAMES + | + playerName1 + | + playerName2 + ... , the the list of player name
     * is updated in the drop-down for the chat used to send a message to all people who joined the server, all players
     * in the lobby, or a specific player.
     * If the Client receives GRANT_LOBBY_LIST + lobbyNames from the Server, then the lobby list in the lobby gui
     * gets updated.
     * If the Client receives GRANT_NAMES_IN_LOBBY + playerNames from the Server, then the player list in the lobby
     * gui gets updated.
     * If the Client receives NEW_LOBBY + lobbyName from the Server, then the lobby name field gets updated.
     * If the Client receives NEW_GAME from the Server, it displays a new game gui.
     * If the Client receives END_GAME, close the game gui.
     * If the Client receives SEND_PLAYER_ID, then the player id of this client is updated.
     * If the Client receives SEND_PLAYER_POSITIONS, then the player positions in the client gui are updated.
     * If the Client receives SEND_PLAYER_SIZES, then the player sizes in the client gui are updated.
     * If the Client receives SEND_PLAYER_ANGLES, the the player angles in the client gui are updated.
     * If the Client receives SEND_BUBBLE_POSITION, then the position of the bubbles in the client gui is updated.
     * If the Client receives SEND_ENERGIZER_POSITIONS, then the position of the energizers in the client gui is updated.
     * If the Client receives SEND_RANKING + | + playerName1 + | size1 + | + time + | +playerName2 + | + size2 + | + time + ....,
     * then the table with the ranking is updated.
     * If the Client receives SEND_HIGH_SCORE + | + playerName1 + | size1 + | + time + | +playerName2 + | + size2 + | + time + ....,
     * then the data in the high score table are updated.
     * If the Client receives GRANT_MOVE, the Client asks the game gui to execute the approved move.
     * If the Client receives PING from the Server it response with PING_ANSWER.
     */
    public void run() {
        try {
            socket = new Socket(host, portNumber);
            socket.setSoTimeout(30000); //throws exception when the client don't communicate with the server for 5 sec
            in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);  // automatically flushes input

            boolean keepRunning = true;
            boolean isPongSent = false;
            while (keepRunning) {
                try {
                    String input = in.readLine();
                    logger.info("Client input: " + input);
                    isPongSent = false;
                    //logger.info("Client " + playerName + " received input: " + input);
                    if (input == null) {
                        keepRunning = false;
                    } else if (input.length() > 3) {    // the commands in the protocol have length 4
                        String[] splitInputArray = splitCommandParameter(input);
                        String command = splitInputArray[0];
                        String parameter = splitInputArray[1];
                        logger.info("Client " + playerName + " received command " + command);
                        logger.info("Client " + playerName + " received parameter " + parameter);
                        System.out.println();
                        switch (command) {
                            case Protocol.SUBMIT:
                                out.println(Protocol.NAME + playerName);
                                break;
                            case Protocol.ACCEPTED:
                                playerName = parameter;
                                isLoggedIn = true;
                                break;
                            case Protocol.REJECTED:
                                // Note: if this dialog remains open for longer than the timeout, then the Client loses the connection.
                                int option = JOptionPane.showConfirmDialog(null, "The player name " + playerName +
                                                " is already taken. Do you want to use player name " + parameter + " instead?",
                                        "Player Name", JOptionPane.YES_NO_OPTION);
                                if (option == JOptionPane.YES_OPTION) {
                                    out.println(Protocol.NAME + parameter);
                                } else if (!isLoggedIn) {
                                    chatGui.logIn();
                                    out.println(Protocol.NAME + playerName);
                                } else {
                                    chatGui.changePlayerName();
                                }
                                break;
                            case Protocol.QUIT_CONFIRMED:
                                keepRunning = false;
                                break;
                            case Protocol.CHAT:
                                chatGui.displayMessage(parameter + "\n\n");
                                break;
                            case Protocol.GRANT_PLAYER_NAMES:
                                List<String> playerNames = splitParameter(parameter);
                                chatGui.setPlayerNames(playerNames);
                                lobbyGui.displayPlayerNames(parameter);
                                break;
                            case Protocol.GRANT_LOBBY_LIST:
                                while (lobbyGui == null) {
                                    // hack to avoid a NullPointerException as the lobbyGui seems not ready
                                }
                                lobbyGui.displayLobbyList(parameter);
                                break;
                            case Protocol.GRANT_NAMES_IN_LOBBY:
                                lobbyGui.displayPlayerList(parameter);
                                clientReceiver.setPlayerNames(splitParameter(parameter));
                                break;
                            case Protocol.NEW_LOBBY:
                                lobbyGui.displayLobby(parameter);
                                lobbyGui.setLobbyName(parameter);
                                break;
                            case Protocol.NEW_GAME:
                                mainGui.startGame();
                                mainGui.setTimer();
                                mainGui.startTimer();
                                hasJoinedGame = true;
                                break;
                            case Protocol.END_GAME:
                                gameGui.setVisible(false);
                                mainGui.endGame();
                                ClientReceiver.gameHasEnded = false;
                                if (!parameter.equals("IN GAME")) {
                                    lobbyGui.leaveLobby();
                                }
                                break;
                            case Protocol.SEND_PLAYER_ID:
                                playerId = Integer.parseInt(parameter);
                                break;
                            case Protocol.SEND_PLAYER_POSITIONS:
                                var positions = Util.parseVectorList(parameter);
                                clientReceiver.setPlayerPositions(positions);
                                break;
                            case Protocol.SEND_PLAYER_SIZES:
                                var sizes = Util.parseFloatList(parameter);
                                clientReceiver.setPlayerSizes(sizes);
                                break;
                            case Protocol.SEND_PLAYER_ANGLES:
                                var angles = Util.parseFloatList(parameter);
                                clientReceiver.setPlayerAngles(angles);
                                break;
                            case Protocol.SEND_BUBBLE_POSITION:
                                var bubblePositions = Util.parseVectorList(parameter);
                                clientReceiver.setBubblePositions(bubblePositions);
                                break;
                            case Protocol.SEND_BUBBLE_ID:
                                var bubbleIdsStr = Util.splitParameter(parameter);
                                ArrayList<Integer> bubbleIds = new ArrayList<>();
                                for (int i = 0; i < bubbleIdsStr.size(); i++) {
                                    bubbleIds.add(Integer.parseInt(bubbleIdsStr.get(i)));
                                }
                                clientReceiver.setBubbleIds(bubbleIds);
                                break;
                            case Protocol.SEND_ENERGIZER_POSITIONS:
                                var energizerPositions = Util.parseVectorList(parameter);
                                clientReceiver.setEnergizerPositions(energizerPositions);
                                break;
                            case Protocol.SEND_RANKING:
                                List<RankEntry> ranking = Util.splitParameterIntoRankEntry(parameter);
                                mainGui.getRankGui().setRanking(ranking);
                                break;
                            case Protocol.SEND_HIGH_SCORE:
                                List<RankEntry> highScore = Util.splitParameterIntoRankEntry(parameter);
                                mainGui.getLobbyGui().getHighScoreDialog().updateHighScoreList(highScore);
                                break;
                            case Protocol.PING:
                                out.println(Protocol.PING_ANSWER);
                                break;
                            case Protocol.PONG_ANSWER:
                                // does nothing because its just the answer from the server
                                break;
                            default:
                                logger.error("Client " + playerName + " received parameter " + parameter + " for undefined command");
                        }
                    }
                } catch (SocketException se) {
                    JOptionPane.showMessageDialog(null, "Time-out: Connection to Server lost.");
                    System.exit(0);
                } catch (SocketTimeoutException ste) {
                    if (isPongSent) {
                        keepRunning = false;
                        JOptionPane.showMessageDialog(null, "Connection to Server lost.");
                    } else {
                        out.println(Protocol.PONG);
                        isPongSent = true;
                    }
                }
            }
        } catch (ConnectException ce) {
            JOptionPane.showMessageDialog(null, "Server is NOT running.");
            logger.error("A connection exception occurred: " + ce.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Connection to Server lost.");
        } finally {
            try {
                if (socket != null) {
                    socket.close();
                }
            } catch (Exception e) {
                // Do nothing
            }

        }
    }

    /**
     * Creates a new thread which runs this client.
     */
    public void startClient() {
        new Thread(this).start();
    }

    /**
     * Closes this client, sending the command QUIT to the server to inform it that the client disconnected.
     */
    public void closeClient() {
        try {
            if (socket != null) {
                leaveLobby(lobbyGui.getLobbyName());
                out.println(Protocol.QUIT);
                logger.info("Client has been closed.");
            }
        } catch (Exception e) {
            logger.error("An exception has occurred when closing the client.");
            logger.error(e.getMessage());
        }
        System.exit(0);
    }

    /**
     * Sends the @param message to the Server and asks it to BROADCAST it to all clients.
     */
    public void sendMessageToAll(String message) {
        String str = Protocol.BROADCAST + getPlayerName() + ": " + message;
        out.println(str);
    }

    /**
     * Sends the @param message to the Server and asks it to BROADCAST it to all clients in the
     * lobby @param lobbyName
     */
    public void sendMessageToLobby(String lobbyName, String message) {
        String str = Protocol.BROADCAST_LOBBY + "|" + lobbyName + "|" + getPlayerName() + "[lobby]: " + message;
        out.println(str);
    }

    /**
     * Send the @param message to the server to be sent only to @param fromPlayerName and @param toPlayerName.
     */
    public void sendMessage(String fromPlayerName, String toPlayerName, String message) {
        String str = Protocol.WHISPER + "|" + fromPlayerName + "|" + toPlayerName + "|" + getPlayerName()
                + "[whisper]: " + message;
        logger.debug(str);
        out.println(str);
    }

    /**
     * Asks the server to change the player name to @param newPlayerName.
     */
    public void askServerToChangePlayerName(String newPlayerName, String lobbyName) {
        String str = Protocol.CHANGE_NAME + "|" + lobbyName + "|" + newPlayerName;
        out.println(str);
    }

    /**
     * Asks the server to set up new lobby with the name @param newLobbyName.
     */
    public void askServerToSetUpLobby(String newLobbyName) {
        String str = Protocol.SET_UP_LOBBY + newLobbyName;
        out.println(str);
    }

    /**
     * Asks the server to join the lobby with the name @param lobbyName.
     */
    public void askServerToJoinLobby(String lobbyName) {
        String str = Protocol.JOIN_LOBBY + lobbyName;
        out.println(str);
    }

    /**
     * Asks the server to create a new game.
     */
    public void askServerToCreateNewGame(String lobbyName) {
        String str = Protocol.REQUEST_NEW_GAME + lobbyName;
        out.println(str);
    }

    //TODO: check if the following code can be removed
    /**
     * Asks the server to end the game.
     */
    public void askServerToEndGame(String lobbyName) {
        String str = Protocol.REQUEST_END_GAME + lobbyName;
        out.println(str);
    }

    /**
     * Asks the server to move in the direction specified by @param direction.
     */
    public void askServerToMoveTo(Vector2D direction) {
        String str = String.format("%s%.2f,%.2f", Protocol.MOVE, direction.getX(), direction.getY());
        out.println(str);
    }

    /**
     * Asks the server to create a bubble in the direction specified by @param direction.
     */
    public void askToCreateBubble(Vector2D direction) {
        String str = String.format("%s%.2f,%.2f", Protocol.CREATE_BUBBLE, direction.getX(), direction.getY());
        out.println(str);
    }

    /**
     * Asks the server to change the player's angle.
     */
    public void askServerToUpdateAngle(float angle) {
        String str = String.format("%s%.2f", Protocol.ANGLE, angle);
        out.println(str);
    }

    /**
     * Asks the server to leave the lobby with the name @param lobbyName.
     */
    public void leaveLobby(String lobbyName) {
        gameGui.setVisible(false);
        String str = Protocol.LEAVE_LOBBY + lobbyName;
        out.println(str);
    }

    /**
     * Gets the ClientGui that is paired with this client.
     */
    public ChatGuiInterface getChatGui() {
        return chatGui;
    }

    /**
     * Gets the lobbyGui.
     */
    public LobbyGuiInterface getLobbyGui() {
        return lobbyGui;
    }


    /**
     * Pairs this Client with the @param clientGui.
     */
    public void setChatGui(ChatGuiInterface chatGui) {
        this.chatGui = chatGui;
    }

    /**
     * Sets the lobby gui with @param lobbyGui.
     */
    public void setLobbyGui(LobbyGuiInterface lobbyGui) {
        this.lobbyGui = lobbyGui;
    }

    /**
     * Sets the game gui with @param gameGui.
     */
    public void setGameGui(GameGuiInterface gameGui) {
        this.gameGui = gameGui;
    }

    /**
     * Sets the port number of this Client to @param portNumber.
     */
    public void setPortNumber(int portNumber) {
        this.portNumber = portNumber;
    }

    /**
     * Sets the player name of this Client to @param playerName.
     */
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    /**
     * Gets the player name of this client.
     */
    public String getPlayerName() {
        return this.playerName;
    }

    /**
     * Sets the host (IP address) of this client to @param host.
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * Gets the player ID.
     */
    public int getPlayerId() {
        return playerId;
    }

    /**
     * Returns the client receiver.
     */
    public ClientReceiverInterface getClientReceiver() {
        return this.clientReceiver;
    }

    /**
     * Sets the main gui to @param mainGui. This is used for testing purposes.
     */
    public void setMainGui(MainGuiInterface mainGui) {
        this.mainGui = mainGui;
    }

}
